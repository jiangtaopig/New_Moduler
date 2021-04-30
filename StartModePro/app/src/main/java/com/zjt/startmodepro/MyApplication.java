package com.zjt.startmodepro;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tencent.mmkv.MMKV;
import com.zjt.base.BaseApplication;
import com.zjt.startmodepro.lifecycle.ApplicationObserver;

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
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver());
        mContext = getApplicationContext();
        initARouter();

        String rootDir = MMKV.initialize(this);
        Log.e("mmkv", "rootDir = " + rootDir);
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
