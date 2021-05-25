package com.example.lib.multithread;

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
