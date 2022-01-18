package com.example.lib;

import java.lang.reflect.Field;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/5/17 7:46 下午
 * @Description : TestThreadLocal
 */


public class TestThreadLocal {


    /**
     * main 中运行代码的结果如下, 第一个和第3个结果 可能是其他的地方在主线程中设置了值
     * 弱引用 key:java.lang.ThreadLocal@7f31245a    值:java.lang.ref.SoftReference@6d6f6e28
     * 弱引用 key:null    值:我是在主线程中设置的值
     * 弱引用 key:java.lang.ThreadLocal@135fbaa4    值:java.lang.ref.SoftReference@45ee12a7
     *
     * 把 threadLocal = null去掉，再运行的话，会发现，key不会为空，因为有强引用的存在，是不会被回收的，但是若断开强引用则是很快可以回收的
     * @param args
     */
    public static void main(String[] args) {

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("我是在主线程中设置的值");

        threadLocal = null; // 断开 ThreadLocal 的强引用
        System.gc(); // 主动垃圾回收

        Thread curThread = Thread.currentThread();
        Class<? extends Thread> clz = curThread.getClass();
        Field field = null;
        try {
            field = clz.getDeclaredField("threadLocals");
            field.setAccessible(true);
            Object threadLocalMap = field.get(curThread);

            Class<?> tlmClass = threadLocalMap.getClass();
            Field tableField = tlmClass.getDeclaredField("table");
            tableField.setAccessible(true);
            Object[] arr = (Object[]) tableField.get(threadLocalMap);

            for (Object o : arr) {
                if (o == null) continue;
                Class<?> entryClass = o.getClass();
                Field valueField = entryClass.getDeclaredField("value");
                Field referenceField = entryClass.getSuperclass().getSuperclass().getDeclaredField("referent");
                valueField.setAccessible(true);
                referenceField.setAccessible(true);
                System.out.println(String.format("弱引用key:%s    值:%s", referenceField.get(o), valueField.get(o)) + " , thread >> " +Thread.currentThread().getName());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
