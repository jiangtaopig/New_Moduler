package com.example.lib.multithread;

import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock {

    public static void main(String[] args) {

        ReentrantLock reentrantLock = new ReentrantLock();

        new MyThread1(reentrantLock).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Thread thread2 = new MyThread2(reentrantLock);
        thread2.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.interrupt();

//        Condition condition = reentrantLock.newCondition();
//        condition.await();
//        condition.signal();
    }

    static class MyThread1 extends Thread {
        private ReentrantLock lock;

        public MyThread1(ReentrantLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " start and sleep 3s");
            lock.lock();
            try {
                System.out.println("--- isLock == > "+lock.isLocked());
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println("Thread.currentThread().getName() exit");
        }
    }

    static class MyThread2 extends Thread {
        private ReentrantLock lock;

        public MyThread2(ReentrantLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            System.out.println("isLock == > "+lock.isLocked());
            System.out.println(Thread.currentThread().getName() + " start and try lock by lockInterruptibly");
            try {
                // reentrantlock 支持中断，而 synchronized 不支持
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("中断了");
            } finally {
                lock.unlock();
            }
            System.out.println("Thread.currentThread().getName() exit");
        }
    }
}
