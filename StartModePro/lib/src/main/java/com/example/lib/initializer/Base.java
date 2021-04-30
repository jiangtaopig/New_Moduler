package com.example.lib.initializer;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/19 11:31 上午
 * @Description : Base
 */


public class Base {
    public static String staticVal = "我是父类的静态变量";
    protected String normalVal = "我是父类的普通变量";

    static {
        System.out.println("staticVal = "+staticVal);
        System.out.println("我是父类的静态代码块");
    }

    {
        System.out.println("normalVal = "+normalVal);
        System.out.println("我是父类的普通代码块");
    }

    public Base() {
        System.out.println("我是父类的构造函数");
    }
}
