package com.bilibili.bilibililive.virtuallive.downloader

/**
 * 所有回调都发生在子线程；回调实现者需要自己切换线程
 * @author Raine
 */
interface IRequestDownloadCallback {
    /**
     * 任务下载开始;reqId为任务Id
     */
    fun onDownloadStarted(reqId: Int) {}

    /**
     * 下载暂停
     */
    fun onDownloadPause(reqId: Int) {}

    /**
     * 下载进度回调
     */
    fun onDownloadProgress(reqId: Int, percent: Double, downloadedByteLength: Long) {}

    /**
     * 下载失败回调
     */
    fun onDownloadFailed(reqId: Int) {}

    /***
     * 下载成功回调
     */
    fun onDownloaded(reqId: Int) {}

    /**
     * 任务状态终结
     */
    fun onTerminated(reqId: Int) {}
}