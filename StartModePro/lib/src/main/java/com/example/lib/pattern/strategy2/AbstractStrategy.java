package com.example.lib.pattern.strategy2;

import com.example.lib.pattern.Strategy;

public abstract class AbstractStrategy implements Strategy {
    public void register() {
        StrategyContext2.registerStrategy(getClass().getSimpleName(), this);
    }
}
