package com.example.lib.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.Executors.newFixedThreadPool;

public class TestMain {
    public static void main(String[] args) {

        Object lock = new Object();
        TestThread testThread = new TestThread(lock);
        testThread.start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("start interrupt");
        testThread.interrupt();

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        DoSth aron1 = new DoSth("小王", "起床");
        DoSth aron2 = new DoSth("小王", "刷牙洗脸");
        DoSth aron3 = new DoSth("小王", "吃早饭");
        DoSth aron4 = new DoSth("小王", "坐地铁");
        DoSth aron5 = new DoSth("小王", "上楼");

        DoSth tony1 = new DoSth("小李", "起床");
        DoSth tony2 = new DoSth("小李", "刷牙洗脸");
        DoSth tony3 = new DoSth("小李", "吃早饭");
        DoSth tony4 = new DoSth("小李", "坐地铁");
        DoSth tony5 = new DoSth("小李", "上楼");

        List<DoSth> doSthList = new ArrayList<>(10);
        doSthList.add(aron1);
        doSthList.add(aron2);
        doSthList.add(aron3);
        doSthList.add(aron4);
        doSthList.add(aron5);

        doSthList.add(tony1);
        doSthList.add(tony2);
        doSthList.add(tony3);
        doSthList.add(tony4);
        doSthList.add(tony5);

//        for (DoSth doSth : doSthList) {
//            executorService.execute(() ->{
//                System.out.println(Thread.currentThread().getName() + " >>>> "+ doSth.toString());
//            });
//        }
        executorService.shutdown();

//        int a = 2/0;


        ExecutorService executorService1 = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executorService1.execute(() ->{
                System.out.println("newScheduledThreadPool >> "+Thread.currentThread().getName()+"， ii= "+ finalI);
                int b = 2/0;
            });
        }
        executorService1.shutdown();
    }


}

class TestThread extends Thread {

    private Object lock;

    public TestThread(Object lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        super.run();
        synchronized (lock) {
            System.out.println("enter ....");
            while (true) {
                if (isInterrupted())
                    break;
            }
            System.out.println("exit ....");
        }
    }
}
