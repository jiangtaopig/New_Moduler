package com.zjt.startmodepro.refactor

import com.zjt.startmodepro.refactor.impl.CameraLifeCycle
import com.zjt.startmodepro.refactor.impl.VoiceLifeCycle

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/9 12:02 下午

 * @Description : LifeCycleFactory

 */


class LifeCycleFactory {

    companion object {
        fun getLifeCycle(type: String): IRoomPushClientService.ILifeCycleService {
            return when (type) {
                Constants.LifeCycle.CAMERA_TYPE -> {
                    CameraLifeCycle()
                }

                Constants.LifeCycle.VOICE_TYPE -> {
                    VoiceLifeCycle()
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }


}