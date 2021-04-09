package com.zjt.startmodepro.refactor

import android.app.Activity

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/8 7:37 下午

 * @Description : IPushService

 */


interface IPushService {
    // 要和之前定义的API名字保持一致
    fun initLivePush(activity: Activity);
    fun getLivePush(): LivePush?
    fun startPush()
    fun stopPush()
    fun stopPushAndGenerateHistory()
}