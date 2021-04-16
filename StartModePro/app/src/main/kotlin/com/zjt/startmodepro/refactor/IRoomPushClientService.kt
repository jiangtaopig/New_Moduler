package com.zjt.startmodepro.refactor

import android.app.Activity

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/9 2:59 下午

 * @Description : IRoomPushClientService

 */


interface IRoomPushClientService {

    interface IPushService {
        // 要和之前定义的API名字保持一致
        fun initLivePush(activity: Activity);
        fun getLivePush(): LivePush?
        fun startPush()
        fun stopPush()
        fun stopPushAndGenerateHistory()
    }

    interface IBgmService {
        fun startPlayBgm(url: String)
        fun stopPlayBgm()
    }

    interface ILifeCycleService {
        fun onStart()
        fun onStop()
        fun onDestroy()
    }
}