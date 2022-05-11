package com.example.lib.pattern;

import com.example.lib.pattern.strategy.DivideStrategy;
import com.example.lib.pattern.strategy.OperationClient;
import com.example.lib.pattern.strategy.PlusStrategy;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/13 11:20 上午
 * @Description : TestMain
 */


public class TestMain {
    int a = 1;

    public static void main(String[] args) {
        Strategy plusOperation = new PlusStrategy();
        OperationClient client = new OperationClient();
        client.setOperation(plusOperation);
        int res = client.doOperation(3, 8);
        System.out.printf("res = " + res);

        Strategy divideOpt = new DivideStrategy();
        client.setOperation(divideOpt);
        client.doOperation(2, 4);


//        int b = a ;
    }


    private static void f1() {
//        f2();
    }

    private void f2() {

    }

}
