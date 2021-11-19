package com.example.lib.loadclass;

public class Initable3 {
    static  int staticNotFinal = 13;

    static {
        System.out.println("---- initializing Initable3 ----");
    }
}
