package com.zjt.startmodepro;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class TestHandlerThread {

    private HandlerThread bgHandlerThread = new HandlerThread("back_thread >>");
    private Handler bgHandler;
    private Handler uiHandler;
    private Queue<String> queue = new LinkedList<>();
    private Object lock = new Object();
//    int cnt = 0;

    private Runnable task = () -> {
//            synchronized (lock) {
            poolMsg();
//            Log.e("zjt-1", "-----task----");
    };

    public TestHandlerThread() {
        bgHandlerThread.start();
        bgHandler = new Handler(bgHandlerThread.getLooper());
        uiHandler = new Handler();
    }

    public void addMsg(String msg) {
        bgHandler.post(() -> {
//            synchronized (lock) {
                Log.e("zjt-1", "--addMsg   msg ="+msg+", queue size = "+queue.size());
                queue.add(msg);
                bgHandler.post(task);
                Log.e("zjt-1", "--addMsg   end ");
//            }
        });
    }

    private void poolMsg() {
        Log.e("zjt-1", " *** poolMsg   queue =  " +queue);
        while (!queue.isEmpty()) {
            String data = queue.peek();
            if (!TextUtils.isEmpty(data)) {
                Log.e("zjt-1", " *** poolMsg   data =  " +data);
                queue.poll();
            }
        }
    }

}
