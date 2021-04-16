package com.zjt.startmodepro.range_download

import androidx.annotation.Keep
import com.bilibili.bilibililive.virtuallive.downloader.RequestState

/**
 * 抽象代表一个下载请求
 */
@Keep
data class DownloadRequest(
        /**
         * id唯一标识一次下载
         */
        var id: Int = 0,

        /**
         * 任务名称;不同的任务可能有相同的名称
         */
        var name: String= "",

        /**
         * 文件大小
         */
        var size: Long = 0,

        var extension: String = "",
        /**
         * 下载资源地址
         */
        var url: String = "",

        /**
         * 保存地址
         */
        var localPath: String = "",
        /**
         * 已经下载了的byte数量；以此为准；而非真实的文件长度
         */
        var downloadedByteCount: Long = 0L,
        /**
         * 该文件下载状态
         */
        @Volatile
        var state:RequestState = RequestState.NEW,
        /**
         * 对于尚未进入到downloading状态的任务的指示性标志
         */
        @Volatile
        var willPause: Boolean = false
) {
        fun adapt2DownloadRequestInfo(): DownloadRequestInfo {
               return DownloadRequestInfo(
                       id,
                       name,
                       size,
                       url,
                       localPath,
                       downloadedByteCount,
                       state
               )
        }
}

/**
 * 和DownloadRequest基本一样；只是下面的字段都不可改写
 */
data class DownloadRequestInfo(
        val id: Int = 0,
        val name: String = "",
        val size: Long = 0,
        val url: String = "",
        val localPath: String = "",
        val downloadedByteCount: Long = 0L,
        val state: RequestState = RequestState.NEW
) {
        fun adapt2DownloadRequest(): DownloadRequest {
                return DownloadRequest(
                       id,
                       name,
                       size,
                        "",
                       url,
                       localPath,
                        downloadedByteCount,
                       state
                        )
        }
}

