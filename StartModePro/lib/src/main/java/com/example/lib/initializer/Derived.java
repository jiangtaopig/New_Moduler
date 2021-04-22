package com.example.lib.initializer;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/19 11:37 上午
 * @Description : Derived
 */


public class Derived extends Base {
    private static String staticV = "我是子类的静态变量";
    private String normalV = "我是子类的普通变量";

    static {
        System.out.println("子类的 静态变量 staticV == "+staticV);
        System.out.println("子类的静态代码块");
    }

    {
        System.out.println("子类的 普通变量 normalV == "+normalV);
        System.out.println("子类的普通代码块");
    }

    public Derived() {
        System.out.println("子类的构造函数");
    }
}
