package com.example.lib.consumerAndProducer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Producer2 implements Runnable{

    private final ReentrantLock lock;
    private Condition notEmpty;
    private Condition notFull;
    private List<String> data;

    public Producer2(ReentrantLock lock, List<String> data, Condition notFull, Condition notEmpty) {
        this.lock = lock;
        this.data = data;
        this.notEmpty = notEmpty;
        this.notFull = notFull;
    }

    private void put() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "去生产");
            while (data.size() == 1) {
                notFull.await();
            }
            int random = new Random().nextInt(100);
            System.out.println(Thread.currentThread().getName() + " 生产了数据 " + random);
            data.add(String.valueOf(random));
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        int i = 0;
        // 由于有2个消费者，只有1个生产者线程，所以这里面循环次数得是消费者循环次数的2倍
        while (i < 2) {
            i++;
            put();
        }
    }
}
