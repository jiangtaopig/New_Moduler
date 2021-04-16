package com.zjt.startmodepro.refactor.impl

import android.util.Log
import com.zjt.startmodepro.refactor.IRoomPushClientService

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/9 11:45 上午

 * @Description : LifeCycleServiceImpl

 */


abstract class AbstractLifeCycleService : IRoomPushClientService.ILifeCycleService {
    override fun onStart() {
        Log.e("xxx", "--- onStart ---")
    }

    override fun onStop() {
        Log.e("xxx", "--- onStop ---")
    }

    override fun onDestroy() {
        Log.e("xxx", "--- onDestroy ---")
    }
}