package com.zjt.startmodepro.range_download;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/3/28 9:35 上午
 * @Description : DownloadListener
 */



public interface DownloadListener {


    /**
     * 通知当前的下载进度
     * @param progress
     */
    void onProgress(int progress);

    /**
     * 通知下载成功
     */
    void onSuccess();

    /**
     * 通知下载失败
     */
    void onFailed();

    /**
     * 通知下载暂停
     */
    void onPaused();

    /**
     * 通知下载取消事件
     */
    void onCanceled();

}

