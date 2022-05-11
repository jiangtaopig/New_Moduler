package com.example.lib.pattern.strategy2;

import com.example.lib.pattern.Strategy;

import java.util.HashMap;

public class StrategyContext2 {

    private static HashMap<String, Strategy> map = new HashMap<>();

    public static void registerStrategy(String optType, Strategy strategy) {
        map.put(optType, strategy);
    }

    public static Strategy getStrategy(String optType) {
        return map.get(optType);
    }
}
