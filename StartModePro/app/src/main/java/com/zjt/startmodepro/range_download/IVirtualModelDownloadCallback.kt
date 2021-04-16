package com.bilibili.bilibililive.virtuallive.downloader

/**
 * live2d模型下载对应的回调
 */
interface IVirtualModelDownloadCallback {
   /**
    * 开始下载
    */
   fun onStart(virtualId: String, url: String)

   /**
    * 加载失败
    */
   fun onFail(virtualId: String, url: String, errorCode: FailReason)

   /**
    * 下载且解压成功
    */
   fun onSuccess(virtualId: String, url: String)

   enum class FailReason {
      // 下载失败
      DOWNLOAD_FAILED,
      // 解压失败
      EXTRACT_FAILED
   }
}