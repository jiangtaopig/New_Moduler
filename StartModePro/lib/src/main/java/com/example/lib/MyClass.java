package com.example.lib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MyClass {
    private static final int COUNT_BITS = Integer.SIZE - 3;
    private static final int CAPACITY = (1 << COUNT_BITS) - 1;
    private static final int RUNNING = -1 << COUNT_BITS;
    private final static AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    private static int ctlOf(int rs, int wc) {
        return rs | wc;
    }

    private static int runStateOf(int c) {
        return c & ~CAPACITY; // 等于 c & 1110 0000 0000 0000 0000 0000 0000 0000 ,即高3位表示线程池的状态
    }


    public static void main(String[] args) throws InterruptedException {


        int a = -1;
        int b = a << 29;
        System.out.println("b = " + b); // -10 0000 0000 0000 0000 0000 0000 0000

        System.out.println("ctl = " + ctl.get());
        int state = runStateOf(ctl.get());
        System.out.println("state = " + state);

        // -536870912 的二进制表示
        // 原码： 0010 0000 0000 0000 0000 0000 0000 0000
        // 反码： 1101 1111 1111 1111 1111 1111 1111 1111
        // 补码： 1110 0000 0000 0000 0000 0000 0000 0000

//        testException("");
//        reSize(3);

        TestStatic ts1 = new TestStatic();
        ts1.printName();



        TestStatic ts2 = new TestStatic();
        ts2.printName();


//        ExecutorService executorService1 = new ThreadPoolExecutor(2, 3, 2_000,
//                TimeUnit.MILLISECONDS,
//                new LinkedBlockingDeque<>(3));
//
//        executorService1.execute(() -> System.out.println("11 name "+Thread.currentThread().getName()+" sssss"));
//
//        ExecutorService executorService2 = new ThreadPoolExecutor(2, 3, 2_000,
//                TimeUnit.MILLISECONDS,
//                new LinkedBlockingDeque<>(3));
//
//        executorService2.execute(() -> System.out.println("22 name "+Thread.currentThread().getName()+" sssss"));


        Object lock = new Object();
        new Thread("ttt1") {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println(" 获取锁了" + System.currentTimeMillis());
                    try {
                        Thread.sleep(3_000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("释放锁" + System.currentTimeMillis());
            }
        }.start();

        Thread t2 = new Thread("ttt2") {
            @Override
            public void run() {
                System.out.println("---- 去获取锁----" + System.currentTimeMillis());
                synchronized (lock) {

                    System.out.println("----获取到了锁----" + System.currentTimeMillis());
                }
            }
        };

        t2.start();

        TimeUnit.MILLISECONDS.sleep(300);

        t2.interrupt();


    }

    private static void testException(String val) {
        try {
            testThrow("cc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testThrows() throws NullPointerException, InterruptedException {
        Integer i = null;
        System.out.println(i + 1);
        Thread.sleep(333);
    }

    /**
     * 抛出非运行时异常，调用本函数的方法，必须捕获或抛出 IOException 异常
     *
     * @param filePath
     * @throws IOException
     */
    private static void testThrow(String filePath) throws IOException {
        if (filePath == null) {
            throw new IOException();//运行时异常不需要在方法上申明
        }
    }

    private static void readFromFile(String filePath) {
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void testList() {
        Student student = null;
        List<Student> studentList = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            student = new Student();
            student.name = "zjt" + "_" + i;
            student.age = i;
            studentList.add(student);
        }

        int size = studentList.size();
    }

    /**
     * 返回大于cap的 2 的幂次方
     *
     * @param cap
     * @return
     */
    private static int reSize(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : n + 1;
    }

    static class TestStatic {
        private static final AtomicInteger number = new AtomicInteger(1);
        private final String namePrefix;

        public TestStatic() {
            namePrefix = "test-" + number.getAndIncrement() + ">>";
        }

        public void printName() {
            System.out.println("namePrefix = " + namePrefix);
        }
    }

}