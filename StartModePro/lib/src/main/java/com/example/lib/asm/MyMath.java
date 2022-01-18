package com.example.lib.asm;

public class MyMath {

    public int add(int a, int b) throws InterruptedException {
//        long start = System.currentTimeMillis();
        Thread.sleep(1000L);
//        long end = System.currentTimeMillis();
//        long cost = end - start;
//        System.out.println(cost);
        return a + b;
    }
}
