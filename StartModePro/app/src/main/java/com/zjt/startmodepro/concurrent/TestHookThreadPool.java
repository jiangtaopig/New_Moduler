package com.zjt.startmodepro.concurrent;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestHookThreadPool {

    public void testHook() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for(int i = 0; i < 5; i++) {
            int finalI = i;
            executorService.execute(() -> {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("hook_thread_pool", Thread.currentThread().getName() + " >>> " + finalI);
            });
        }

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                Log.e("hook_thread_pool", Thread.currentThread().getName() + " >>> test thread" );
            }
        };
        thread.setName("xxx--xxx");
        thread.start();

        ExecutorService service = new ThreadPoolExecutor(1, 3, 10_000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            service.execute(() -> {
                Log.e("hook_thread_pool", Thread.currentThread().getName() + " >>> test define thread_pool >>> " + finalI);
            });
        }
    }
}
