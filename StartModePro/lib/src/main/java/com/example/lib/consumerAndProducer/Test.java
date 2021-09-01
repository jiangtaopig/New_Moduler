package com.example.lib.consumerAndProducer;

public class Test {

    public static void main(String[] args) {
        Object lock = new Object();

        Thread waitThread = new Thread("waitThread") {
            @Override
            public void run() {
               synchronized (lock) {
                   System.out.println(Thread.currentThread().getName()+"  start");
                   try {
                       lock.wait();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   System.out.println(Thread.currentThread().getName()+"  end ");
               }
            }
        };

        Thread notifyThread = new Thread("notifyThread") {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println(Thread.currentThread().getName()+" notify  start");
                    lock.notify();
                    System.out.println(Thread.currentThread().getName()+"notify ----");
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+"notify end");
                }
            }
        };

        waitThread.start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        notifyThread.start();
    }
}
