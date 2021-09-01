package com.example.lib.consumerAndProducer;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Consumer2 implements Runnable {

    private final ReentrantLock lock;
    private Condition notEmpty;
    private Condition notFull;
    private List<String> data;

    public Consumer2(ReentrantLock lock, List<String> data, Condition notFull, Condition notEmpty) {
        this.lock = lock;
        this.data = data;
        this.notEmpty = notEmpty;
        this.notFull = notFull;
    }


    private void get() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "去消费");
            while (data.size() == 0) {
                notEmpty.await();
                System.out.println(Thread.currentThread().getName() + "被唤醒");
            }
            String val = data.remove(0);
            System.out.println(Thread.currentThread().getName() + " 消费了数据  " + val);
            notFull.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {
            get();
        }
    }
}
