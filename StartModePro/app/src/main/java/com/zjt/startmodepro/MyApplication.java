package com.zjt.startmodepro;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tencent.mmkv.MMKV;
import com.zjt.base.BaseApplication;
import com.zjt.startmodepro.lifecycle.ApplicationObserver;
import com.zjt.startmodepro.soloader.SoFileLoadManager;
import com.zjt.startmodepro.soloader.SoUtils;
import com.zjt.startmodepro.soloader.ToastUtil;
import com.zjt.startmodepro.utils.MyExceptionHandler;

import java.io.File;
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
        Log.e("ReInstance__", "MyApplication onCreate");
        MyExceptionHandler.getInstance().init();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ApplicationObserver());
        mContext = getApplicationContext();
        initARouter();

        String rootDir = MMKV.initialize(this);
        Log.e("mmkv", "rootDir = " + rootDir);
        load();
        dynamicSo();
    }

    private void load() {
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

    private void dynamicSo() {
        String soFrom = SoUtils.getSoSourcePath();
        if (!new File(soFrom).exists()) {
            ToastUtil.toast(this, "哈哈，本地so文件不存在，" + soFrom);
        }
        SoFileLoadManager.loadSoFile(this, soFrom);
    }

}
