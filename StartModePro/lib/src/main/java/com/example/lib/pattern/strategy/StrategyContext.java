package com.example.lib.pattern.strategy;

import com.example.lib.pattern.Strategy;

/**
 * 策略环境类，供策略的服务调用
 */
public class StrategyContext {
    public static Strategy getStrategy(String type) {

        switch (type) {
            case "+" :
                return new PlusStrategy();
            case "-":
                return new SubtractStrategy();
            case "/" :
                return new DivideStrategy();
            default:
                throw new IllegalArgumentException("type is out of + , - , /");
        }
    }
}


