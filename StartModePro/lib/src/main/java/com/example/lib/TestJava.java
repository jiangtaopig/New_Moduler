package com.example.lib;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/8 10:09 ����
 * @Description : TestJava
 */

public class TestJava {
    private static long time ;
    public static void main(String[] args) throws InterruptedException {
        int[] a = {1, 2, 3, 4, 5};
        int[] b = new int[10];
//        System.arraycopy(a, 0, b, 0, b.length);

        CopyOnWriteArrayList<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
        copyOnWriteArrayList.add("1");

        Object[] objects = new Object[0];
        System.out.println("size = " + objects.length);

        objects = Arrays.copyOf(objects, 1);

        String[] strings = {"1", "2","3"};
        String[] destStrings = new String[4];

        System.arraycopy(strings, 0, destStrings, 0, 3);

        String url = "rtmpsrt://live-push";
        URI uri = URI.create(url);


//        Observer;
//        Observable

        String host = uri.getHost();
        String scheme = uri.getScheme();


        int am = 1;

        url.subSequence(0, 2);

        Map.Entry<Integer, TreeSet<String>> treeSetEntry;
        TreeMap<Integer, TreeSet<String>> treeSetTreeMap = new TreeMap<>();

        TreeSet<String> t1 = new TreeSet<>();
        t1.add("a");
        t1.add("b");

        treeSetTreeMap.put(1, t1);

        treeSetEntry = treeSetTreeMap.pollFirstEntry();
        String s = treeSetEntry.getValue().pollFirst();
        String s2 = treeSetEntry.getValue().pollFirst();
        System.out.println("s = " + s + ", s2 = " + s2 + " , treeSetEntry = " + treeSetEntry);


//        testSemaphore();

        LinkedBlockingDeque<Integer> blockingDeque = new LinkedBlockingDeque<>(3);
        blockingDeque.put(1);
        blockingDeque.add(2);

//        CompletableFuture<String> completableFuture = new CompletableFuture<>();
//        completableFuture.join();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new Student()::show);
        executorService.shutdown();

        String vv = "010";
        System.out.println("vv = "+Integer.parseInt(vv));


        IDefaultInterface defaultInterface = new DefaultImpl();
        defaultInterface.sayHi();
        defaultInterface.sayGoodBye();

        testTime();

    }

    /**
     * ���Է����ĺ�ʱ
     */
    private static void testTime() {
        time -= System.currentTimeMillis();
        System.out.println(" ... time = " + time);
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        time += System.currentTimeMillis();
        System.out.println("time = " + time);
    }

    private synchronized static void makeString(int a, String b) {
        String c = a + b;
    }

    private static void testSemaphore() {
        Semaphore semaphore = new Semaphore(1);

        new Thread("A") {
            @Override
            public void run() {
                try {
                    semaphore.acquire();
                    System.out.println("thread " + Thread.currentThread().getName() + " >>> start , time = " + System.currentTimeMillis());
                    Thread.sleep(2_000);
                    semaphore.release();
                    Thread.sleep(2_000);
                    System.out.println("thread " + Thread.currentThread().getName() + " >>> end !!! , time = " + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        new Thread("B") {
            @Override
            public void run() {
                try {
                    System.out.println("----------------------");
                    semaphore.acquire();
                    System.out.println("thread " + Thread.currentThread().getName() + " >>> start , time = " + System.currentTimeMillis());
                    Thread.sleep(2_000);
                    semaphore.release();
                    System.out.println("thread " + Thread.currentThread().getName() + " >>> end !!!, time = " + System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

//        new Thread("C") {
//            @Override
//            public void run() {
//                try {
//                    semaphore.acquire();
//                    System.out.println("thread "+Thread.currentThread().getName() + " >>> start , time = "+System.currentTimeMillis());
//                    Thread.sleep(2_000);
//                    semaphore.release();
//                    System.out.println("thread "+Thread.currentThread().getName() + " >>> end !!!, time = "+System.currentTimeMillis());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
    }
}
