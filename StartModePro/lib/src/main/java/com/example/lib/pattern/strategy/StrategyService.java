package com.example.lib.pattern.strategy;

import com.example.lib.pattern.Strategy;

/**
 * 优化后的策略服务比优化前的 OperationClient 类好了不少。
 *
 */
public class StrategyService {
    public static void main(String[] args) {
        Strategy operation = StrategyContext.getStrategy("+");
        int plus = operation.opt(1, 4);
        System.out.println(" plus = " + plus);
    }


}
