package com.example.lib.loadclass;

public class Initable1 {

    // 编期静态常量
    static final int v1 = 12;

    //非编期静态常量
    static int v2 = 20;

    static {
        System.out.println("---- initializing Initable1 ----");
    }

    static void show() {
        System.out.println("Initable1 .... show ..");
    }
}
