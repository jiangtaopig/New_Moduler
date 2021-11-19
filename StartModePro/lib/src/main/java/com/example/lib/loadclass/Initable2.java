package com.example.lib.loadclass;

public class Initable2 {
    static  int staticNotFinal = 12;

    static {
        System.out.println("---- initializing Initable2 ----");
    }
}
