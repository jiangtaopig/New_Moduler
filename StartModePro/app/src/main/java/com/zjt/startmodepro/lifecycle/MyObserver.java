package com.zjt.startmodepro.lifecycle;

import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/1
 */
class MyObserver implements LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private void create(){
        Log.e("zjt", "---- create-----");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void connect(){
        Log.e("zjt", "---- resume-----");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void pause(){
        Log.e("zjt", "---- pause-----");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void destroy(){
        Log.e("zjt", "---- destroy-----");
    }

}
