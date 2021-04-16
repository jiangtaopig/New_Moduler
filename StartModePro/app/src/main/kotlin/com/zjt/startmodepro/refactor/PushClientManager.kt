package com.zjt.startmodepro.refactor

import android.app.Activity

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/9 10:20 上午

 * @Description : PushClientManager

 */


class PushClientManager {

    var mCurPushClient: AbstractLiveRoomClient? = null
    var mCameraPushClient: AbstractLiveRoomClient? = null
    var mVoicePushClient: AbstractLiveRoomClient? = null


    fun startCameraPreview(activity: Activity) {
        if (mCameraPushClient == null) {
            mCameraPushClient = CameraPushClient().apply {
                initLivePush(activity)
            }
        } else {
            // 其他页面可能销毁了摄像头源；需要重新设置
//            mCameraPushClient?.getLivePush()?.getAVContext()?.addCameraSource(LiveConstants.MAIN_SOURCE, config.isCameraFront())
//            mCameraPushClient?.setCameraEffect()
        }
        mCurPushClient = mCameraPushClient
    }

    fun startVoicePreview(activity: Activity) {
        if (mVoicePushClient == null) {
            mVoicePushClient = VoicePushClient().apply {
                initLivePush(activity)
            }
        }
        mCurPushClient = mVoicePushClient
    }

    fun startPush() {
        destroyOtherPreview()
        mCurPushClient?.startPush()
    }

    private fun destroyOtherPreview() {
        when (mCurPushClient) {
            mCameraPushClient -> {
                mVoicePushClient?.onDestroy()
                mVoicePushClient = null
            }

            mVoicePushClient -> {
                mCameraPushClient?.onDestroy()
                mCameraPushClient = null
            }
        }
    }

    fun stopPush() {
        mCurPushClient?.stopPush()
    }

    fun stopPushAndGenerateHistory() = mCurPushClient?.stopPushAndGenerateHistory()

    fun getPush(): LivePush? = mCurPushClient?.getLivePush()

    fun startBgm(url: String) = mCurPushClient?.startPlayBgm(url)

    fun stopBgm() = mCameraPushClient?.stopPlayBgm()

    fun onStart() = mCurPushClient?.onStart()

    fun onStop() = mCurPushClient?.onStop()

    fun onDestroy() = mCurPushClient?.onDestroy()

}