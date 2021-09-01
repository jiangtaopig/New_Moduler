package com.example.lib.consumerAndProducer;

import java.util.List;

public class Consumer implements Runnable{

    private final Object lock;
    private List<String> data;

    public Consumer(Object lock, List<String> data) {
        this.lock = lock;
        this.data = data;
    }


    // 为什么多生产消费者要使用 notifyAll() ?
    // 现在假设是有1个生产者A 和2个消费者 B 和 C，且数组只能存1个数据；
    // 消费者B 先拿到锁，然后发现数组是空的，然后调用wait 且释放锁，然后 C 拿到锁，同样发现数组为空，调用 wait后释放锁；
    // 生产者A 获取锁生产一个数据后，调用notify(需要等锁里面的代码块执行完才会去唤醒其他线程) ,由于for循环10次，然后发现数组为1满了，调用 wait 后释放锁，这时候才去唤醒锁
    // 假设 消费者 B 获取锁，消费了一个数据，然后调用 notify,再次循环时数组为空，释放锁且唤醒其他线程，假设唤醒了消费者 C，C发现数组为空调用 wait，此时 线程A/B/C调处于wait状态
    // 调用 notifyAll 就正常运行了-----注意，由于这里1个生产者2个消费者，所以生产者必须是消费者循环次数的2倍（ run 方法里面的循环次数），否者还是会发现假死现象

    private void get() {
        synchronized (lock) {
//            System.out.println(Thread.currentThread().getName() + "去消费");
            try {
                while (data.size() == 0) {
                    lock.wait();
//                    System.out.println(Thread.currentThread().getName() + "被唤醒");
                }
                String val = data.remove(0);
                System.out.println(Thread.currentThread().getName() + " 消费了数据  " +val);
                lock.notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
            get();
        }
    }
}
