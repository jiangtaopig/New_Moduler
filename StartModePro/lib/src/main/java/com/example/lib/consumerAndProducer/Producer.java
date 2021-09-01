package com.example.lib.consumerAndProducer;

import java.util.List;
import java.util.Random;

public class Producer implements Runnable{

    private final Object lock;
    private List<String> data;

    public Producer(Object lock, List<String> data) {
        this.lock = lock;
        this.data = data;
    }

    private void put() {
        synchronized (lock) {
//            System.out.println(Thread.currentThread().getName() + "去生产");
            try {
                while (data.size() == 1) {
                    lock.wait();
                }
                int random = new Random().nextInt(100);
                System.out.println(Thread.currentThread().getName() + " 生产了数据 " + random);
                data.add(String.valueOf(random));
                lock.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        int i = 0;
        // 由于有2个消费者，只有1个生产者线程，所以这里面循环次数得是消费者循环次数的2倍
        while (i < 4) {
            i++;
            put();
        }
    }
}
