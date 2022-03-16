package com.example.lib.multithread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 2个线程交替输出1A2B3C4D
 */
public class PrintCharAndNumber {
    volatile int a = 1;

    public static void main(String[] args) {
//        char[] num = new char[]{'1', '2', '3', '4'};
//        char[] str = new char[]{'A', 'B', 'C', 'D'};

        func1();
//        func2();
//        func3();
//        func4();
//        printCharBy3Threads();
//        printNum(6);
//        printCharBy3Threads2();
    }


    /**
     * 使用 synchronized 和 wait notify
     */
    private static void func1() {
        char[] num = new char[]{'1', '2', '3', '4'};
        char[] str = new char[]{'A', 'B', 'C', 'D'};
        Object lock = new Object();
        AtomicBoolean printChar = new AtomicBoolean(true);

        new Thread("T1") {
            @Override
            public void run() {
                System.out.println("T1 acquire lock ... ");
                synchronized (lock) {

                    for (char c : num) {
                        System.out.println("T1  num = " + c);
                        try {
                            while (printChar.get()) {
                                printChar.set(false);
                                System.out.println(getName() + " >>> " + c);
                                lock.notify();
                            }
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    lock.notify(); // 这行一定要加，不然会在所有数据遍历完成后T2线程还是wait状态
                }
            }
        }.start();

        // 防止有的虚拟机上不是先输出数字的
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread("T2") {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("T2 acquire lock ... ");
                    for (char c : str) {
                        System.out.println("T2  char = " + c);
                        try {
                            while (!printChar.get()) {
                                printChar.set(true);
                                System.out.println(getName() + " >>> " + c);
                                lock.notify();
                            }
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 这一行是可以去掉的，因为T2线程遍历完最后一个数据会通过notify唤醒T1线程
//                    lock.notify();
                }
            }
        }.start();

    }

    /**
     * 使用 ReentrantLock 加 Condition
     */
    private static void func2() {
        char[] num = new char[]{'1', '2', '3', '4'};
        char[] str = new char[]{'A', 'B', 'C', 'D'};
        ReentrantLock reentrantLock = new ReentrantLock(true);
        Condition printCharCondition = reentrantLock.newCondition();
        Condition printNumCondition = reentrantLock.newCondition();

        new Thread("T1") {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    for (char c : num) {
                        System.out.println(getName() + " >>> " + c);
                        printCharCondition.signal();
                        printNumCondition.await();
                    }
                    printCharCondition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        }.start();

        new Thread("T2") {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    for (char c : str) {
                        System.out.println(getName() + " >>> " + c);
                        printNumCondition.signal();
                        printCharCondition.await();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        }.start();
    }

    /**
     * 使用 Semaphore
     */
    private static void func3() {
        char[] num = new char[]{'1', '2', '3', '4'};
        char[] str = new char[]{'A', 'B', 'C', 'D'};
        Semaphore printNum = new Semaphore(1);
        Semaphore printChar = new Semaphore(0);

        new Thread("T1") {
            @Override
            public void run() {
                for (char c : num) {
                    try {
                        printNum.acquire();
                        System.out.println(getName() + " >>> " + c);
                        printChar.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread("T2") {
            @Override
            public void run() {
                for (char c : str) {
                    try {
                        printChar.acquire();
                        System.out.println(getName() + " >>> " + c);
                        printNum.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 使用自旋
     */
    private static void func4() {
        char[] num = new char[]{'1', '2', '3', '4'};
        char[] str = new char[]{'A', 'B', 'C', 'D'};

        AtomicBoolean printNum = new AtomicBoolean(true);
        new Thread("T1") {
            @Override
            public void run() {
                for (char c : num) {
                    while (!printNum.get()) {
                    }
                    System.out.println(getName() + " >>> " + c);
                    printNum.set(false);

                    /**
                     * 注意，上面的while循环的判断一定要加 !，否则后续的都可能无法输出了，如下写法就可能后续的无法输出
                     */

//                    while (printNum.get()) {
//                        System.out.println(getName() + " >>> " + c);
//                        printNum.set(false);
//                    }
                }
            }
        }.start();

        new Thread(("T2")) {
            @Override
            public void run() {
                for (char c : str) {
                    while (printNum.get()) {
                    }
                    System.out.println(getName() + " >>> " + c);
                    printNum.set(true);
//                    while (!printNum.get()) {
//                        System.out.println(getName() + " >>> " + c);
//                        printNum.set(true);
//                    }
                }
            }
        }.start();

    }


    /**
     * 3个线程循环 CYCLE_NUM 次，分别输出 A、B、C
     */
    private static void printCharBy3Threads() {
        Semaphore aSemaphore = new Semaphore(1);
        Semaphore bSemaphore = new Semaphore(0);
        Semaphore cSemaphore = new Semaphore(0);
        int CYCLE_NUM = 4;

        Thread threadA = new Thread("thread_1") {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < CYCLE_NUM; i++) {
                        aSemaphore.acquire();
                        System.out.println(getName() + " : >>> A");
                        bSemaphore.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread threadB = new Thread("thread_2") {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < CYCLE_NUM; i++) {
                        bSemaphore.acquire();
                        System.out.println(getName() + " : >>> B");
                        cSemaphore.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread threadC = new Thread("thread_3") {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < CYCLE_NUM; i++) {
                        cSemaphore.acquire();
                        System.out.println(getName() + " : >>> C");
                        aSemaphore.release();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        threadA.start();
        threadB.start();
        threadC.start();

    }

    private static void printCharBy3Threads2() {
        int CYCLE_NUM = 5;
        AtomicInteger value = new AtomicInteger(1);
        Semaphore aSemaphore = new Semaphore(1);
        Semaphore bSemaphore = new Semaphore(0);
        Semaphore cSemaphore = new Semaphore(0);

        Thread threadA = new Thread(new PrintCharThread(CYCLE_NUM, aSemaphore, bSemaphore, value));
        Thread threadB = new Thread(new PrintCharThread(CYCLE_NUM, bSemaphore, cSemaphore, value));
        Thread threadC = new Thread(new PrintCharThread(CYCLE_NUM, cSemaphore, aSemaphore, value));

        threadA.setName("thread_1");
        threadA.start();
        threadB.setName("thread_2");
        threadB.start();
        threadC.setName("thread_3");
        threadC.start();
    }

    /**
     * 有3个线程，分别输出1~12：
     * 线程1： 1，4，7，10
     * 线程2： 2，5，8，11
     * 线程3： 3，6，9，12
     */
    private static void printNum(int maxNum) {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition aCondition = reentrantLock.newCondition();
        Condition bCondition = reentrantLock.newCondition();
        Condition cCondition = reentrantLock.newCondition();
        AtomicInteger value = new AtomicInteger(1);

        Thread threadA = new Thread("thread_1") {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    while (value.get() <= maxNum) {
                        System.out.println(getName() + ", num = " + value.getAndAdd(1));
                        bCondition.signal();
                        aCondition.await();
                    }
                    bCondition.signal(); // 遍历结束后 唤醒还处于等待状态的 线程b
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        };

        Thread threadB = new Thread("thread_2") {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    while (value.get() <= maxNum) {
                        System.out.println(getName() + ", num = " + value.getAndAdd(1));
                        cCondition.signal();
                        bCondition.await();
                    }
                    cCondition.signal();// 遍历结束后 唤醒还处于等待状态的 线程c
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        };

        Thread threadC = new Thread("thread_3") {
            @Override
            public void run() {
                try {
                    reentrantLock.lock();
                    while (value.get() <= maxNum) {
                        System.out.println(getName() + ", num = " + value.getAndAdd(1));
                        aCondition.signal();
                        cCondition.await();
                    }
//                    aCondition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    reentrantLock.unlock();
                }
            }
        };

        try {
            threadA.start();
            Thread.sleep(10);
            threadB.start();
            Thread.sleep(10);
            threadC.start();
        } catch (InterruptedException e) {

        }

    }


    static class PrintCharThread implements Runnable {

        private int cycle_num;
        private Semaphore curSemaphore;
        private Semaphore nextSemaphore;
        private AtomicInteger value;
        private int INTERVAL = 3;

        public PrintCharThread(int cycle_num, Semaphore curSemaphore, Semaphore nextSemaphore, AtomicInteger value) {
            this.cycle_num = cycle_num;
            this.curSemaphore = curSemaphore;
            this.nextSemaphore = nextSemaphore;
            this.value = value;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < cycle_num; i++) {
                    curSemaphore.acquire();
                    if (value.get() % INTERVAL == 1) {
                        System.out.println(Thread.currentThread().getName() + " : >>> A");
                    } else if (value.get() % INTERVAL == 2) {
                        System.out.println(Thread.currentThread().getName() + " : >>> B");
                    } else {
                        System.out.println(Thread.currentThread().getName() + " : >>> C");
                    }
                    value.getAndAdd(1);
                    nextSemaphore.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
