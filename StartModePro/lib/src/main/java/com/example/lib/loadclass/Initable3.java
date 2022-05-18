package com.example.lib.loadclass;

public class Initable3 {
    // 静态语句的执行顺序是由语句在Java类中出现的顺序所决定的
    static int staticNotFinal = getStaticVal();
    int normalVar = 1;

    // 静态代码块在类的初始化的时候执行
    // 静态语句块中只能访问到定义在静态语句块之前的变量，定义在他之后的变量，在前面的静态语句块中可以赋值，但是不能访问
    static {
        a = 3; // 静态变量 a 定义在静态代码块的后面，可以赋值
//        System.out.println("a = " +a); // 不能访问，编译报错
        System.out.println("---- initializing Initable3 static  ---- , " +
                "staticNotFinal = " + staticNotFinal);

    }

    static int a;

    // 普通代码块只在创建对象的实例的时候执行
    {
        System.out.println("---- initializing Initable3 not static  ---- normalVar = " + normalVar);
    }

    private static int getStaticVal() {
        System.out.println("静态赋值方法");
        return 12;
    }



}
