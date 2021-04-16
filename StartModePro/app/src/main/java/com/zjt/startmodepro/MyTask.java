package com.zjt.startmodepro;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/14 6:06 下午
 * @Description : MyTask
 */


public class MyTask {

    public OnDelayTaskListener onDelayTaskListener;

    public void setOnDelayTaskListener(OnDelayTaskListener onDelayTaskListener) {
        this.onDelayTaskListener = onDelayTaskListener;
    }

    public void delay() {
        Log.e("MyTask", "delay start");
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (onDelayTaskListener != null) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("MyTask", "delay end");
                    new Handler(Looper.getMainLooper())
                            .post(() -> {
                                onDelayTaskListener.doSth("-----我是大爷------");
                            });
                }
            }
        }.start();

    }

    public interface OnDelayTaskListener {
        void doSth(String value);
    }
}
