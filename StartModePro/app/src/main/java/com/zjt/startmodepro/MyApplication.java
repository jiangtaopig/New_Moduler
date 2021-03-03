package com.zjt.startmodepro;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zjt.startmodepro.lifecycle.ApplicationObserver;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/1
 */
class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver());

        initARouter();
    }

    private void initARouter() {
        Log.e("time----", "MyApplication initARouter time --- "+System.currentTimeMillis());
        if(isDebug()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
        Log.e("time----", "MyApplication initARouter time --- "+System.currentTimeMillis());
    }

    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }
}
