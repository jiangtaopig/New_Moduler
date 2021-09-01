package com.example.lib.consumerAndProducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TestConsumerAndProducer {
    public static void main(String[] args) {
        test2();
    }

    private static void test1() {
        Object lock = new Object();
        List<String> list = new ArrayList<>();

        new Thread(new Consumer(lock, list), "消费者线程1 >>> ").start();
        new Thread(new Consumer(lock, list), "消费者线程2 >>> ").start();
        new Thread(new Producer(lock, list), "生产者线程1 ---").start();
        new Thread(new Producer(lock, list), "生产者线程2 ---").start();
    }

    private static void test2() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition notFull = reentrantLock.newCondition();
        Condition notEmpty = reentrantLock.newCondition();

        List<String> list = new ArrayList<>();
        new Thread(new Consumer2(reentrantLock, list, notFull, notEmpty), "消费者线程1 >>> ").start();
        new Thread(new Producer2(reentrantLock, list, notFull, notEmpty), "生产者线程1 >>> ").start();
        new Thread(new Consumer2(reentrantLock, list, notFull, notEmpty), "消费者线程2 >>> ").start();
        new Thread(new Producer2(reentrantLock, list, notFull, notEmpty), "生产者线程2 >>> ").start();

    }
}
