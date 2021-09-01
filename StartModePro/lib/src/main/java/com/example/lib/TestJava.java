package com.example.lib;

import java.net.URI;
import java.util.Calendar;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/8 10:09 上午
 * @Description : TestJava
 */

public class TestJava {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        int[] b = new int[10];
//        System.arraycopy(a, 0, b, 0, b.length);

        String url = "rtmpsrt://live-push";
        URI uri = URI.create(url);

        String host = uri.getHost();
        String scheme = uri.getScheme();
        int am = 1;

        url.subSequence(0, 2);

        ReentrantLock reentrantLock = new ReentrantLock(true);
        reentrantLock.lock();
        try {
            reentrantLock.lockInterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Semaphore semaphore = new Semaphore(3);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




    private static void makeString(int a , String b){
        String c = a + b;
    }
}
