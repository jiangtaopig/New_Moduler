package com.zjt.startmodepro.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static MyExceptionHandler mInstance;

    private MyExceptionHandler(){

    }

    public static MyExceptionHandler getInstance() {
        if (mInstance == null) {
            synchronized (MyExceptionHandler.class) {
                if (mInstance == null)
                    mInstance = new MyExceptionHandler();
            }
        }
        return mInstance;
    }

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public void init() {
        // 系统默认的 exception handler
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull @NotNull Thread t, @NonNull @NotNull Throwable e) {
        // 不能在主线程，不然还是崩溃的
        if (t.getName().equals("thread_zhu") && e instanceof ArithmeticException) {
            Log.e("xzzjjtt", "--- ArithmeticException ---");
        } else {
            Log.e("xzzjjtt", "--- 其他异常 ---");
            mDefaultHandler.uncaughtException(t, e);
        }
    }
}
