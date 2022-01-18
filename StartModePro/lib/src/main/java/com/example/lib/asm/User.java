package com.example.lib.asm;

public class User {
    private String name;

    public void show() {
        System.out.println(name);
    }

    public int sub(int a, int b) {
        return a - b;
    }

    public void testDuration() throws InterruptedException {
        Thread.sleep(1000);
    }
}
