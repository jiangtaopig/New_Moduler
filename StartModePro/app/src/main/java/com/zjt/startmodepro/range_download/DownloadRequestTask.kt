package com.bilibili.bilibililive.virtuallive.downloader

import android.util.Log
import android.webkit.MimeTypeMap
import com.zjt.startmodepro.http.SSLSocketClient
import com.zjt.startmodepro.range_download.DownloadRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.net.MalformedURLException
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException
import kotlin.math.min


/**
 * @author Raine
 * 实际负责下载的任务; 这个任务负责将任务从其他的状态转移到下载
 */
class DownloadRequestTask(private val task: DownloadRequest, private val _listener: IRequestDownloadCallback) : Runnable {

    private val okHttpClient = getOkHttpClient()

    private val stateMachine = hashMapOf<RequestState, () -> Unit>()
    private val mInnerListeners = mutableListOf<IRequestDownloadCallback>()
    private val proxyListener = object : IRequestDownloadCallback {
        override fun onDownloadStarted(reqId: Int) {
            val copiedListener = synchronized(this) { mInnerListeners.toList() }
            copiedListener.forEach {
                it.onDownloadStarted(reqId)
            }
        }

        override fun onDownloadPause(reqId: Int) {
            val copiedListener = synchronized(this) { mInnerListeners.toList() }
            copiedListener.forEach {
                it.onDownloadPause(reqId)
            }
        }

        override fun onDownloadProgress(reqId: Int, percent: Double, downloadedByteLength: Long) {
            val copiedListener = synchronized(this) { mInnerListeners.toList() }
            copiedListener.forEach {
                it.onDownloadProgress(reqId, percent, downloadedByteLength)
            }
        }

        override fun onDownloadFailed(reqId: Int) {
            val copiedListener = synchronized(this) { mInnerListeners.toList() }
            copiedListener.forEach {
                it.onDownloadFailed(reqId)
            }
        }

        override fun onDownloaded(reqId: Int) {
            val copiedListener = synchronized(this) { mInnerListeners.toList() }
            copiedListener.forEach {
                it.onDownloaded(reqId)
            }
        }

        override fun onTerminated(reqId: Int) {
            val copiedListener = synchronized(this) { mInnerListeners.toList() }
            copiedListener.forEach {
                it.onTerminated(reqId)
            }
        }
    }
    private var mFailCount = 0

    override fun run() {
        Log.e("down_load", "callback start run")
        stateMachine[RequestState.NEW] = this::handleStateNew
        stateMachine[RequestState.READY] = this::handleStateReady
        stateMachine[RequestState.DOWNLOADING] = this::handleStateDownloading
        stateMachine[RequestState.PAUSED] = this::handleStateReady
        stateMachine[RequestState.DOWNLOADED] = this::handleStateDownloaded
        stateMachine[RequestState.FAILED] = this::handleStateFailed
        stateMachine[RequestState.TERMINATED] = this::handleStateTerminated
        gotoState(task.state)
    }


    /**
     * 外部调用resume方法的时候调用；其他时候不要调用
     */
    fun addCallback(callback: IRequestDownloadCallback) {
        synchronized(this) {
            mInnerListeners.add(callback)
        }
    }

    private fun log(msg: String) {
        Log.e("down_load", "msg = $msg")
    }

    private fun getOkHttpClient(): OkHttpClient {
        // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
        val trustAllCert: X509TrustManager = object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
        // 目的是为了解决 SSL 问题，但是在 oppo 上面验证后发现没什么用
        return OkHttpClient.Builder().sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), trustAllCert).build()
    }

    private fun handleStateNew() {
        /**
         *  从远端获取任务的基本信息
         *  1.内容长度
         *  2.确保连接可行
         */
        log("handleStateNew")
        Observable.just(task.url)
                .map {
                    try {
                        val url = URL(task.url)
                        val request = Request.Builder()
                                .head()
                                .url(url)
                                .build()
                        val response = okHttpClient.newCall(request).execute()
                        task.size = response.header("Content-Length")?.toLong() ?: 0L
                        task.extension = MimeTypeMap.getFileExtensionFromUrl(task.url)
                        response.close()
                        // 初始化完成；进入下一个状态
                        log("handleStateNew query meta info successfully goto state ready")
                        task.state = RequestState.READY

                        var file = File(task.localPath)
                        if (!file.exists()) {
                            file.mkdirs()
                        }
                        // 创建临时文件；没有下载完成的文件以.btmp结尾，下载完成后会重命名为 task.localPath 的名字
                        File("${task.localPath}.btmp").createNewFile()
                        // 删除file文件,否则下次下载成功后无法重命名零时文件为我们想要的文件
                        file.delete()
                        gotoState(task.state)
                    } catch (ex: MalformedURLException) {
                        // URL有问题；无法继续；状态设置为终结；
                        task.state = RequestState.TERMINATED
                        gotoState(task.state)
                    } catch (ex: IOException) {
                        proxyListener.onDownloadFailed(task.id)
                        task.state = RequestState.FAILED
                        log("$task failed")
                    } catch (ex: NumberFormatException) {
                        task.size = 0
                        task.state = RequestState.READY
                        gotoState(task.state)
                        log("$task failed because of wrong Content-Length field")
                    } catch (ex: Throwable) {
                        ex.printStackTrace()
                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    private fun handleStateReady() {
        /**
         * 对于size为0的内容；多半是动态生成的内容；因此删除上一次的下载
         */
        log("$task handleState Ready")
        if (task.size == 0L) {
            log("$task may be dynamic content")
            File("${task.localPath}.btmp").apply {
                if (exists()) {
                    delete()
                    createNewFile()
                }
            }
        }
        startDownloadInternal()
    }

    private fun handleStateDownloading() {
        log("already in downloading state; do nothing")
    }

    private fun handleStateFailed() {
        mFailCount++
        if (mFailCount >= 3) {
            // 失败超过三次；不再重试；下载失败；删除文件；并且清理数据库的记录
            File("${task.localPath}.btmp").apply {
                if (exists()) {
                    delete()
                }
            }
            task.state = RequestState.TERMINATED
            gotoState(task.state)
        } else {
            task.state = RequestState.NEW
            gotoState(RequestState.NEW)
        }
    }

    private fun handleStateDownloaded() {
        /**
         * 下载完成；不会再开始下载；尝试对文件进行重命名;如果有
         */
        File("${task.localPath}.btmp").apply {
            if (exists()) {
                renameTo(File("${task.localPath}"))
            }
        }
        proxyListener.onDownloaded(task.id)
        log("$task download file successfully. won't re-download")
    }

    private fun handleStateTerminated() {
        /**
         * 不管因为啥原因；任务终结啦
         */
        if (File("${task.localPath}").exists()) {
            log("$task successfully terminate")
        } else {
            log("$task lamentably  terminate retrycount: $mFailCount")
        }
        proxyListener.onTerminated(task.id)
    }


    private fun gotoState(state: RequestState) {
        stateMachine[state]?.invoke()
    }

    /**
     * 注意此方法目前还是在子线程中调用的，因为是在上面的 Observable 上面切换到子线程的
     */
    private fun startDownloadInternal() {
        log("startDownloadInternal start")
        val tmpFile = File("${task.localPath}.btmp")
        if (tmpFile.exists()) {
            proxyListener.onDownloadStarted(task.id)
            // TODO 这里的逻辑需要处理下，因为是断点续传，所以downloadedByteCount要每次到保存到本地文件，所以下次可以直接基于之前已下载的部分接着下载
            task.downloadedByteCount = tmpFile.length()
//            task.downloadedByteCount = min(task.downloadedByteCount, tmpFile.length())
            val downloadedBytes = task.downloadedByteCount
            val buf = ByteArray(BUFFER_SIZE) { 0 }
            val randomAccessFile = RandomAccessFile(tmpFile, "rw")
            randomAccessFile.seek(task.downloadedByteCount)
            log("startDownloadInternal open file for read")

            try {
                val url = URL(task.url)

                /**
                 * 发起Range请求
                 * !! todo Raine 需要提前判断是否可以发起range请求；
                 */
                val request = Request.Builder()
                        .get()
                        .url(url)
                        .header("Range", "bytes=$downloadedBytes-")
                        .build()
                val response = okHttpClient.newCall(request).execute()
                val statusCode = response.code()
                response.use {
                    Log.e("down_load", "thread name = " + Thread.currentThread().name)
                    if (statusCode == 416) {
                        // 请求了错误的范围
                        log("range request wrong!! not valid range...")
                        /**
                         * 认为下载失败；不会对外通知；直接重试
                         */
                        task.state = RequestState.FAILED
                        task.downloadedByteCount -= 10000
                        if (task.downloadedByteCount < 0) {
                            task.downloadedByteCount = 0
                        }
                        gotoState(RequestState.FAILED)
                    } else {
                        val inputStream = response.body()?.byteStream() ?: return
                        log("$task stream open successfully")
                        var len = 0
                        try {
                            if (!task.willPause) {
                                log("$task task will pause")
                                task.state = RequestState.DOWNLOADING
//                                   localStoreService?.updateDownloadRequest(task)
                                task.willPause = false
                            } else {

                                task.state = RequestState.PAUSED
                                proxyListener.onDownloadPause(task.id)
//                                   localStoreService?.updateDownloadRequest(task)
                                return
                            }
                            // todo 响应中断
                            while (inputStream.read(buf).also { len = it } > 0) {
                                if (task.state != RequestState.DOWNLOADING) {
                                    return
                                }
                                if (task.willPause) {
                                    task.state = RequestState.PAUSED
//                                       localStoreService?.updateDownloadRequest(task)
                                    proxyListener.onDownloadPause(task.id)
                                    return
                                }
                                randomAccessFile.write(buf, 0, len)
                                task.downloadedByteCount += len
                                if (task.size != 0L) {
                                    proxyListener.onDownloadProgress(task.id, task.downloadedByteCount.toDouble() / task.size, task.downloadedByteCount)
//                                       localStoreService?.updateDownloadRequest(task)
                                    if (task.downloadedByteCount == task.size) {
                                        task.state = RequestState.DOWNLOADED
                                        gotoState(task.state)
                                        proxyListener.onDownloaded(task.id)
                                        return
                                    }
                                } else {
                                    // 获取的数据为0；下载失败;
                                    proxyListener.onDownloadFailed(task.id)
                                    task.state = RequestState.FAILED
                                    break
//                                   gotoState(task.state)
                                }
                            }
                        } catch (ex: InterruptedException) {
                            /**
                             * 线程被中断；下载失败
                             */
                            proxyListener.onDownloadFailed(task.id)
                            task.state = RequestState.FAILED
//                           gotoState(task.state)
                        }
                    }
                }
            } catch (ex: MalformedURLException) {
                log("odd exception. it shouldn't stay in this state. program has error!")
            } catch (ex: IOException) {
                task.state = RequestState.FAILED
                proxyListener.onDownloadFailed(task.id)
                log("broken downloading becasue of io exception")
            } catch (ex: Throwable) {
                task.state = RequestState.FAILED
                proxyListener.onDownloadFailed(task.id)
                log("can't download ...")
                throw ex
            } finally {
                randomAccessFile.close()
            }
        }
    }

    fun pause (){
        task.state = RequestState.PAUSED
    }

    init {
        addCallback(_listener)
    }

    companion object {
        private const val TAG = "DownloadRequestTask"
        private const val BUFFER_SIZE = 8192
    }
}

