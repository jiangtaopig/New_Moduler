package com.example.lib.multithread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;


/**
 * 有12个数，有3个线程依次输出从1~12
 */
public class TestSemaphore {

    public static void main(String[] args) {


//        test1();
        test2();

    }

    /**
     * 3个线程依次输出从1~12的数字
     */
    private static void test1(){
        Semaphore s1 = new Semaphore(1);
        Semaphore s2 = new Semaphore(0);
        Semaphore s3 = new Semaphore(0);

        AtomicInteger atomicInteger = new AtomicInteger(1);

        MyThread myThread1 = new MyThread(s1, s2, "线程1：", atomicInteger);
        MyThread myThread2 = new MyThread(s2, s3, "线程2：", atomicInteger);
        MyThread myThread3 = new MyThread(s3, s1, "线程3：", atomicInteger);

        myThread1.start();
        myThread2.start();
        myThread3.start();
    }

    private static void test2(){
        int max_size = 9;
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Consumer<AtomicInteger> consumer = new Consumer<AtomicInteger>() {
            @Override
            public void accept(AtomicInteger integer) {
                while (integer.get() <= max_size) {
                    LockSupport.park();
                    System.out.println("thread name = " + Thread.currentThread().getName()
                            + ", integer = " + integer.getAndAdd(1));
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread threadA = new Thread("线程A："){
            @Override
            public void run() {
                consumer.accept(atomicInteger);
            }
        };

        Thread threadB = new Thread("线程B："){
            @Override
            public void run() {
                consumer.accept(atomicInteger);
            }
        };

        Thread threadC = new Thread("线程C："){
            @Override
            public void run() {
                consumer.accept(atomicInteger);
            }
        };

        threadA.start();
        threadB.start();
        threadC.start();

        while (atomicInteger.get() <= max_size){
//            System.out.println("data ---- " + atomicInteger.get());
            if (atomicInteger.get() % 3 == 1){
                LockSupport.unpark(threadA);
            } else if (atomicInteger.get() % 3 == 2){
                LockSupport.unpark(threadB);
            } else {
                LockSupport.unpark(threadC);
            }
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}


class MyThread extends Thread {
    private Semaphore cur;
    private Semaphore next;
    private AtomicInteger value;

    public MyThread(Semaphore s1, Semaphore s2, String threadName, AtomicInteger value) {
        super(threadName);
        this.cur = s1;
        this.next = s2;
        this.value = value;
    }

    @Override
    public void run() {
        try {
            while (value.get() <= 12) {
                System.out.println("thread name = " + Thread.currentThread().getName()
                        + ", value get = " + value.get());
                cur.acquire();
                System.out.println("thread name = " + Thread.currentThread().getName()
                        + ", value = " + value.getAndAdd(1));
                next.release();
                sleep(10); // 这一行很重要
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


