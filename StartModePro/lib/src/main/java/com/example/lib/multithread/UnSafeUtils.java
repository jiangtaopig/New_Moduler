package com.example.lib.multithread;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2020/1/14
 */
public class UnSafeUtils {
    /**
     * 反射获取Unsafe的方法
     * 获取了以后就可以愉快的使用CAS啦
     *
     * @return
     */
    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
