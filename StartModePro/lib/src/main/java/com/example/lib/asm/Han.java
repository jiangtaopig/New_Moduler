package com.example.lib.asm;

public class Han {
    public static void show(String name) {
        System.out.println(name);
    }

    public static void show2(String name) {
        System.out.println("show2 >>> " + name);
    }

    public int changeMethod() {
        int a = 2;
        int b = 3;
        return a + b;
    }
}
