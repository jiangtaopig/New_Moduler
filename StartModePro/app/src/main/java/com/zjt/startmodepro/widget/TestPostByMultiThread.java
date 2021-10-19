package com.zjt.startmodepro.widget;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TestPostByMultiThread {

    private HandlerThread backHandlerThread = new HandlerThread("back_thread");
    private Handler backHandler;
    private LinkedHashMap<Integer, String> map;

    public TestPostByMultiThread() {
        backHandlerThread.start();
        backHandler = new Handler(backHandlerThread.getLooper());
        map = new LinkedHashMap<>();
        map.put(1, "a");
        map.put(2, "b");
        map.put(3, "c");
    }

    public void addTask(int key, String value) {
        Log.e("zjt", "----------addTask thread = "+Thread.currentThread().getName());
        // 通过 handler 的post将任务放入 MessageQueue后可以解决 下面多线程的ConcurrentModificationException
        // 或者使用 ConcurrentHashMap，或者加锁
//        backHandler.post(() -> {
            map.put(key, value);
            traverseMap();
//        });
    }

    private void traverseMap() {
        Set<Map.Entry<Integer, String>> entrySet = map.entrySet();
        for (Map.Entry<Integer, String> map : entrySet) {
            Log.e("zjt", Thread.currentThread().getName() + " , key = " + map.getKey() + " , value = " + map.getValue());
        }
        Log.e("zjt", "----------end------------");
    }

}
