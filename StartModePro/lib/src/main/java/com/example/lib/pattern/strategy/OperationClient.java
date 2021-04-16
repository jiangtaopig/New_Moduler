package com.example.lib.pattern.strategy;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/13 11:30 上午
 * @Description : OperationClient
 */


public class OperationClient {

    private BaseOperation operation;

    public void setOperation(BaseOperation operation) {
        this.operation = operation;
    }

    public int doOperation(int num1, int num2){
        return operation.opt(num1, num2);
    }
}
