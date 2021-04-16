package com.example.lib.pattern;

import com.example.lib.pattern.strategy.BaseOperation;
import com.example.lib.pattern.strategy.DivideOperation;
import com.example.lib.pattern.strategy.OperationClient;
import com.example.lib.pattern.strategy.PlusOperation;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/13 11:20 上午
 * @Description : TestMain
 */


public class TestMain {
    public static void main(String[] args) {
        BaseOperation plusOperation = new PlusOperation();
        OperationClient client = new OperationClient();
        client.setOperation(plusOperation);
        int res = client.doOperation(3, 8);
        System.out.printf("res = " + res);

        BaseOperation divideOpt = new DivideOperation();
        client.setOperation(divideOpt);
        client.doOperation(2, 4);
    }

}
