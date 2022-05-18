package com.zjt.startmodepro;

import static android.content.pm.PackageManager.GET_META_DATA;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tencent.mmkv.MMKV;
import com.zjt.base.ApplicationDelegate;
import com.zjt.base.BaseApplication;
import com.zjt.startmodepro.lifecycle.ApplicationObserver;
import com.zjt.startmodepro.soloader.SoFileLoadManager;
import com.zjt.startmodepro.soloader.SoUtils;
import com.zjt.startmodepro.soloader.ToastUtil;
import com.zjt.startmodepro.utils.MyExceptionHandler;

import java.io.File;
import java.util.ArrayList;
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
    private static final String MODULE_META_KEY = "ApplicationDelegate";

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

        findApplicationDelegate(this);
    }


    public List<ApplicationDelegate> findApplicationDelegate(Context context) {
        List<ApplicationDelegate> delegates = new ArrayList<>();
        try {
            PackageManager pm = context.getPackageManager();
            String packageName = context.getPackageName();
            ApplicationInfo info = pm.getApplicationInfo(packageName, GET_META_DATA);
            if (info != null && info.metaData != null) {
                for (String key : info.metaData.keySet()) {
                    Object value = info.metaData.get(key);
                    if (MODULE_META_KEY.equals(value)) {
                        ApplicationDelegate delegate = initApplicationDelegate(key);
                        if (delegate != null) {
                            delegates.add(delegate);
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return delegates;
    }

    private ApplicationDelegate initApplicationDelegate(String className) {
        Class<?> clazz;
        Object instance = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("找不到" + className, e);
        }
        try {
            instance = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("不能获取 " + className + " 的实例", e);
        }
        if (!(instance instanceof ApplicationDelegate)) {
            throw new RuntimeException("不能获取 " + ApplicationDelegate.class.getName() + " 的实例 " + instance);
        }
        return (ApplicationDelegate) instance;
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
