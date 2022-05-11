package com.example.lib.pattern.strategy2;

import com.example.lib.pattern.Strategy;

public class StrategyService2 {

    public static void main(String[] args) {
        Strategy strategy = StrategyContext2.getStrategy(PlusStrategy2.getInstance().getClass().getSimpleName());
        int plus = strategy.opt(2, 4);
        System.out.println("plus = " + plus);

        Strategy subtractStrategy = StrategyContext2.getStrategy(SubtractStrategy2.getInstance().getClass().getSimpleName());
        int subtract = subtractStrategy.opt(4, 1);
        System.out.println("subtract = " + subtract);
    }

}
