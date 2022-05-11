package com.example.lib.pattern.strategy;

import com.example.lib.pattern.Strategy;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/13 11:28 上午
 * @Description : SubstractOperation
 */


public class SubtractStrategy implements Strategy {
    @Override
    public int opt(int num1, int num2) {
        return num1 - num2;
    }
}
