package com.zjt.startmodepro.refactor

import android.app.Activity
import com.zjt.startmodepro.refactor.impl.BgmServiceImpl

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/9 9:39 上午

 * @Description : AbstractLiveRoomClient

 */


abstract class AbstractLiveRoomClient(val type: String) : IPushService, IBgmService, ILifeCycleService {


    private lateinit var mPusher: LivePush

    private val mBmgService by lazy {
        BgmServiceImpl()
    }

    private var mLifeCycleService: ILifeCycleService? = null

    init {
        mLifeCycleService = LifeCycleFactory.getLifeCycle(type)
    }


    // ----------------------- push 相关 start ---------------------------------------------

    override fun initLivePush(activity: Activity) {
        mPusher = LivePush()
        setLivePush(mPusher)
    }

    abstract fun setLivePush(livePush: LivePush)

    override fun getLivePush(): LivePush? {
        return mPusher
    }

    override fun startPush() {
        // 相同的部分可以写在这里, 不同的方法 可以再定义一个抽象方法
    }

    override fun stopPush() {
        // 相同的部分可以写在这里, 不同的方法 可以再定义一个抽象方法
    }

    override fun stopPushAndGenerateHistory() {
        // 相同的部分可以写在这里, 不同的方法 可以再定义一个抽象方法
    }


    // -----------------------bgm 相关 start ---------------------------------------------

    override fun startPlayBgm(url: String) {
        mBmgService.startPlayBgm(url)
    }

    override fun stopPlayBgm() {
        mBmgService.stopPlayBgm()
    }

    // ----------------------- lifecycle 相关 start ---------------------------------------------

    override fun onStart() {
        mLifeCycleService?.onStart()
    }

    override fun onStop() {
        mLifeCycleService?.onStop()
    }

    override fun onDestroy() {
        mLifeCycleService?.onDestroy()
    }

}