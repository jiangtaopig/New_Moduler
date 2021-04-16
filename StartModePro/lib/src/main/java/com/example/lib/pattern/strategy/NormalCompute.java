package com.example.lib.pattern.strategy;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/13 11:22 上午
 * @Description : NormalCompute 普通的计算加减乘除
 * 但是后来要增加二进制 异或 等操作就得改写这个类，不符合开闭原则，所以使用策略模式来做
 */


public class NormalCompute {

    public static int binomialOperation(int num1, int num2, char ch) {
        switch (ch) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            case '/':
                if (num2 != 0) {
                    return num1 / num2;
                } else {
                    System.out.println("除数不能为0！");
                }
        }
        return num2;
    }
}
