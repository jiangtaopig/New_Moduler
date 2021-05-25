package com.example.lib.multithread;

import java.util.concurrent.locks.LockSupport;

public class TestLockSupportPark {

    public static void main(String[] args) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println("start ------ time = " + System.currentTimeMillis());
                LockSupport.park(); // 中断此线程后会立即返回，即不再阻塞该线程
                System.out.println("end -------- time = " + System.currentTimeMillis());
            }
        };

        thread.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始中断线程 time = " + System.currentTimeMillis());
        thread.interrupt();
    }
}
