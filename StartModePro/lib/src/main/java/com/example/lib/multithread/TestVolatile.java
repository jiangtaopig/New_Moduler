package com.example.lib.multithread;

import java.util.concurrent.TimeUnit;

public class TestVolatile {
    static boolean stop = false;

    public static void main(String[] args) throws InterruptedException {

        new Thread("线程1") {
            @Override
            public void run() {
                while (!stop) {

                }
                System.out.println("线程停下来了");
            }
        }.start();

        TimeUnit.MILLISECONDS.sleep(200);
        stop = true;
        System.out.println("需要停下来 >>> " + stop);

    }
}
