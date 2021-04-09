package com.zjt.startmodepro.refactor

import android.util.Log

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/8 7:54 下午

 * @Description : CameraPushClient

 */


class CameraPushClient() : AbstractLiveRoomClient(Constants.LifeCycle.CAMERA_TYPE) {


    override fun setLivePush(livePush: LivePush) {
        Log.e("xxx", " ---- setLivePush -----")
        livePush.let {
//            this.getAVContext().resourcePath = config.getResourcePath(activity)
//            val videoSession = this.createVideoSession()
//            videoSession.setSceneSource(config.getDefaultSceneSource(this.getAVContext()))
//            videoSession.videoSessionID = LiveConstants.MAIN_SOURCE
//            videoSession.isEncoder(true)
//            this.putVideoSession(videoSession)
//            this.initRenderPipeline()
//            initVideoQuality(this, false)
//            this.getAVContext().addImageSource(
//                    LiveConstants.MAIN_SOURCE,
//                    BitmapFactory.decodeStream(config.getSecondSourcePngStream()),
//                    LIVE_PRIVATE_MODE_SOURCE_NAME,
//                    SceneSource.Item.FIT_OUTER
        }
    }

    override fun startPush() {
        super.startPush()
        Log.e("xxx", "--- start push ---")
    }

    override fun onStart() {
        super.onStart()
    }

}