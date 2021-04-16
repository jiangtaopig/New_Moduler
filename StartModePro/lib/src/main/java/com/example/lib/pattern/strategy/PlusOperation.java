package com.example.lib.pattern.strategy;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/13 11:26 上午
 * @Description : PlusOperation
 */


public class PlusOperation implements BaseOperation {
    @Override
    public int opt(int num1, int num2) {
        return num1 + num2;
    }
}
