package com.zjt.startmodepro.refactor.impl

import android.util.Log
import com.zjt.startmodepro.refactor.IBgmService

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/8 8:04 下午

 * @Description : BgmServiceImpl

 */


class BgmServiceImpl : IBgmService {
    override fun startPlayBgm(url: String) {
        Log.e("xxx", "开始播放 bgm")
    }

    override fun stopPlayBgm() {
        Log.e("xxx", "停止 bgm")
        // 可能要 销毁一些资源
    }
}