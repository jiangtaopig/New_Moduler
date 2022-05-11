package com.example.lib.pattern.strategy2;

public class SubtractStrategy2 extends AbstractStrategy {

    private static final SubtractStrategy2 instance = new SubtractStrategy2();

    private SubtractStrategy2 () {
        register();
    }

    public static SubtractStrategy2 getInstance() {
        return instance;
    }

    @Override
    public int opt(int num1, int num2) {
        return num1 - num2;
    }
}
