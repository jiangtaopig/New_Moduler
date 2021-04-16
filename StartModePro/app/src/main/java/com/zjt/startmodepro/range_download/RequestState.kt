package com.bilibili.bilibililive.virtuallive.downloader

/**
 * 标识一个请求的下载状态
 * 目前的状态没有为多chunk做准备。
 * 暂时不支持一个文件多chunk
 */
enum class RequestState(val code: Int) {
    /**
     * 创建任务时最初始的状态
     */
    NEW(0),

    /**
     * 任务已经初始化完成；下载尚未开始
     */
    READY(1),
    /**
     * 下载中;如果持久化层取出的数据为此状态；可以尝试续传
     */
    DOWNLOADING(2),

    /**
     * 下载暂停
     */
    PAUSED(3),

    /**
     * 下载失败;如果文件可以恢复下载；那么和PAUSE的处理状态相似；如果
     * 文件不可恢复;则状态转到TERMINATED
     */
    FAILED(4),

    /**
     * 下载完成;可能需要做一些检查；最终状态到TERMINATED
     */
    DOWNLOADED(5),

    /**
     * 任务结束；可能是正常结束；也可能是非正常结束
     */
    TERMINATED(6)
}