package com.example.lib.multithread;

import com.example.lib.SynchronousQueue;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestSynchronousQueue {

    private static SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

    public static void main(String[] args) throws InterruptedException {
//        testPut();

//        int NCPU = Runtime.getRuntime().availableProcessors();
//        System.out.println("NCPU = " + NCPU);

//        testOffer();
//        synchronousQueue.poll();
//        synchronousQueue.offer("11");

        ThreadPoolExecutor executors = new ThreadPoolExecutor(0, 5, 4_000, TimeUnit.MILLISECONDS,
                new SynchronousQueue(), new ZjtThreadFactory());

        new Thread("zhu_test"){
            @Override
            public void run() {
                System.out.println("pppppppppppppp");
                executors.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName()+" .... do it");
                        try {
                            Thread.sleep(10);
                            int b =  5 / 0;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }.start();




//
//        int activeCount = executors.getActiveCount();
//
//        Thread.sleep(2000);
//
//        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
//        map.size();
//
//        System.out.println("map size = " + map.size());
//
//        Thread.sleep(8000);
//        activeCount = executors.getActiveCount();
//
//        map = Thread.getAllStackTraces();
//        map.size();
//        System.out.println("after pool done map size = " + map.size());

    }

    private static void testPutAndTake() {
        new Thread(() -> {
            for (int i = 0; i < 4; i++) {
                System.out.println("take ---"+i);
                try {
                    System.out.println(">>> take  : " +synchronousQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() ->{
            for (int i = 0; i < 4; i++) {
                System.out.println("put ---"+i);
                String data = "xx_"+i;
                try {
                    System.out.println(">>> put : " +data);
                    synchronousQueue.put(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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


        System.out.println("testOffer --------");
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
