package com.zjt.startmodepro;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tencent.mmkv.MMKV;
import com.zjt.base.BaseApplication;
import com.zjt.startmodepro.lifecycle.ApplicationObserver;
import com.zjt.startmodepro.utils.MyExceptionHandler;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/1
 */
class MyApplication extends BaseApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MyExceptionHandler.getInstance().init();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver());
        mContext = getApplicationContext();
        initARouter();

        String rootDir = MMKV.initialize(this);
        Log.e("mmkv", "rootDir = " + rootDir);
        load();

    }

    private void load(){

        Task<String> stringTask = Task.callInBackground(new Callable<String>() {

            @Override
            public String call() throws Exception {
                return "12345";
            }
        });
        Task<Object> val = stringTask.cast();
    }

    private void initARouter() {
        Log.e("time----", "MyApplication initARouter time --- " + System.currentTimeMillis());
        if (isDebug()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
        Log.e("time----", "MyApplication initARouter time --- " + System.currentTimeMillis());
    }

    public static Context getContext() {
        return mContext;
    }

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
