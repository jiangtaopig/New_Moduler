package com.zjt.startmodepro.refactor

import android.graphics.BitmapFactory
import android.util.Log

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/9 10:55 上午

 * @Description : VoicePushClient

 */


class VoicePushClient : AbstractLiveRoomClient(Constants.LifeCycle.VOICE_TYPE){
    override fun setLivePush(livePush: LivePush) {
        Log.e("xxx", " ---- VoicePushClient  thread name = -----"+Thread.currentThread().name)
//        val videoSession = livePush.createVideoSession()
//        videoSession.setSceneSource(config.getDefaultSceneSource(livePush.getAVContext()))
//        videoSession.videoSessionID = LiveConstants.MAIN_SOURCE
//        videoSession.isEncoder(true)
//        livePush.putVideoSession(videoSession)
//        livePush.initRenderPipeline()
//
//        val bmp = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_blink_voice_default_bg)
//        setupImageSourceFromBitmap(livePush, bmp)
//        initQuality(livePush)
    }

    override fun startPush() {
        super.startPush()
        Log.e("xxx", "VoicePushClient startPush")
    }


}