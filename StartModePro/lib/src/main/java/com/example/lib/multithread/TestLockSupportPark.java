package com.example.lib.multithread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class TestLockSupportPark {

    public static void main(String[] args) {
//        testInterrupt();
        testUnPark();
    }


    private static void testUnPark(){


        Thread thread1 = new Thread(){
            @Override
            public void run() {
                super.run();
                System.out.println("thread1  start and blocked.... time >>>> " + System.currentTimeMillis());
                LockSupport.park();
                System.out.println("thread1 is notify time >>>> " + System.currentTimeMillis());
            }
        };

        thread1.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(){
            @Override
            public void run() {
                super.run();

                System.out.println("thread 2 ---- start and unPark thread1----time >>>> " + System.currentTimeMillis());
                LockSupport.unpark(thread1);
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                int i = 0;
                while ( i < 10_000) {
                    i++;
                }
                System.out.println("thread2 end time >>>> " + System.currentTimeMillis() + "， i=" +i);
            }
        }.start();

    }


    private static void testInterrupt() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println("start ------ time = " + System.currentTimeMillis());
                LockSupport.park(); // 中断此线程后会立即返回，即不再阻塞该线程

                int i = 0;
                while ( i < 10_000) {
                    i++;
                }
                System.out.println("end -------- time = " + System.currentTimeMillis()   + " , i = " +i);
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
