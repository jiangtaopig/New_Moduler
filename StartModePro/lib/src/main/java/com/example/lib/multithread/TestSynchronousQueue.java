package com.example.lib.multithread;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

public class TestSynchronousQueue {

    private static SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

    public static void main(String[] args) {
        testPut();

        int NCPU = Runtime.getRuntime().availableProcessors();
        System.out.println("NCPU = " + NCPU);

//        testOffer();
    }

    private static void testOffer() {

        System.out.println("testOffer , start time  >>> " + System.currentTimeMillis());

        new Thread() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 如果有线程在等待队列插入数据，那么此时 offer 为true，否则为false，不会阻塞当前线程
                boolean flag = synchronousQueue.offer("fff");
                System.out.println("testOffer flag = " + flag + " , time >>> " + System.currentTimeMillis());
            }
        }.start();


        try {
            // take 的时候如果发现队列中没有数据，那么就会阻塞当前线程，直到队列中有数据插入时才唤醒
            String value = synchronousQueue.take();
            System.out.println("testOffer  , take data >>> " + value + ", time = " + System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void testPut() {
        System.out.println("xxx  xxx " + " start, time = " + System.currentTimeMillis());
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    TimeUnit.SECONDS.sleep(1);
                    String take = synchronousQueue.take();
                    System.out.println("xxx xxx" + ", take = " + take);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        try {
            // put 操作会阻塞当前线程，直到有线程从队列中取数据才会唤醒
            synchronousQueue.put("2");
        } catch (InterruptedException e) {
            System.out.println("xxx  xxx " + "catch");
            e.printStackTrace();
        }

        System.out.println("xxx  xxx " + " end , time = " + System.currentTimeMillis());
    }

    private static void testPoll() {
        synchronousQueue.poll();
    }

}
