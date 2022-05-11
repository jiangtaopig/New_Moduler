package com.example.lib.pattern.strategy;

import com.example.lib.pattern.Strategy;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/13 11:26 上午
 * @Description : PlusOperation
 */


public class DivideStrategy implements Strategy {
    @Override
    public int opt(int num1, int num2) {
        if (num2 != 0)
            return num1 / num2;
        else
            throw new IllegalArgumentException("被除数为0");
    }
}
