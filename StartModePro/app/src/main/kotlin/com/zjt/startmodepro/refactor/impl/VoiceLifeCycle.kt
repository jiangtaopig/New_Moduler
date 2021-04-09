package com.zjt.startmodepro.refactor.impl

import android.util.Log

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/9 12:08 下午

 * @Description : CameraLifeCycle

 */


class VoiceLifeCycle : AbstractLifeCycleService() {
    override fun onStart() {
        super.onStart()
        Log.e("xxx", "--- VoiceLifeCycle onStart ---")
    }

    override fun onStop() {
        super.onStop()
        Log.e("xxx", "--- VoiceLifeCycle onStop ---")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("xxx", "--- VoiceLifeCycle onDestroy ---")
    }
}