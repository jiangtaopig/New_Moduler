package com.example.lib.multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestCondition {
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        Thread aThread = new Thread(new MThread(lock, condition));
        aThread.setName("[A]");

        Thread waitThread = new Thread(new WaitThread(lock, condition));
        waitThread.setName("[B]");

        Thread signalThread = new Thread(new SignalThread(lock, condition));
        signalThread.setName("[C]");

        Thread dThread = new Thread(new MThread(lock, condition));
        dThread.setName("[D]");

        aThread.start();

        Thread.sleep(100);

        waitThread.start();
        signalThread.start();
        dThread.start();
    }
}

class MThread implements Runnable{
    private final ReentrantLock lock;
    private final Condition condition ;

    public MThread(ReentrantLock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" 开始执行等待3s , time = " + System.currentTimeMillis());
            Thread.sleep(3_000);
            System.out.println(Thread.currentThread().getName()+" 执行结束, time = "+ System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

/**
 *  condition 的 await 的操作：
 *  1 .new Node(Thread.currentThread(), Node.CONDITION) 插入条件队列的尾部
 *  2. 如果等待队列中有其他线程结点，则唤醒后续的一个结点
 *  3. 阻塞当前线程
 *
 *  signal 操作：
 *  1. 把这个待唤醒的线程结点插入等待队列的尾部
 *  2. 唤醒这个结点的线程；分如下2种情况：
 *  如果等待队列里面没有没有其他的结点，那么该线程会获取锁然后执行逻辑；
 *  如果队列的前面还有其他的等待线程，那么要等等待队列前面的先执行完才轮到自己
 */
class WaitThread implements Runnable {
    private final ReentrantLock lock;
    private final Condition condition ;

    public WaitThread(ReentrantLock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" >>>>开始执行 await，等待被唤醒 , time = " + System.currentTimeMillis());
            condition.await();
            System.out.println(Thread.currentThread().getName()+" >>>>被唤醒 执行结束, time = "+ System.currentTimeMillis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}


class SignalThread implements Runnable {
    private final ReentrantLock lock;
    private final Condition condition ;

    public SignalThread(ReentrantLock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" --- 开始执行 signal，去唤醒其他线程 , time = " + System.currentTimeMillis());
            condition.signal();
            System.out.println(Thread.currentThread().getName()+" --- 执行结束, time = "+ System.currentTimeMillis());
        } finally {
            lock.unlock();
        }
    }
}
