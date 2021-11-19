package com.example.lib.loadclass;

import java.util.Random;


/**
 * 触发类的初始化的5种场景：
 * 1. new 一个类
 * 2. 读取或设置一个类的静态字段（static int a = 10）且该字段不是 final修饰的（因为 static final 字段在累的准备阶段已经赋值）；
 *    或者调用静态方法
 * 3. 使用反射的方法对类进行反射调用时，如果该类未初始化则进行初始化
 * 4. 当初始化一个类时，如果其父类还未初始化，则先初始化其父类
 * 5. 当Java虚拟机启动时，用户需要指定一个要执行的主类(包含main方法的类)，虚拟机会先初始化这个主类
 */
public class Main {
    public static Random rand = new Random(11);

    public static void main(String[] args) throws ClassNotFoundException {
        // 获取 class 对象
        Class init1 = Initable1.class;
        System.out.println("。。。。。。。After creating Initable1 ref");
        //不触发类初始化，因为 v1 变量是 static final 修饰的，在类的准备阶段已经赋值，所以不会触发类的初始化
        System.out.println(Initable1.v1);

        // 会触发类的初始化，属于读取类的非 final 静态字段
//        System.out.println(Initable1.v2);

        // 会触发类的初始化，属于调用类的静态方法
        Initable1.show();

        System.out.println("------------------------------");

        // Class.forName 会触发类的初始化操作
        Class init2 = Class.forName("com.example.lib.loadclass.Initable2");
        System.out.println("。。。。。。。After creating Initable2 ref");

    }
}
