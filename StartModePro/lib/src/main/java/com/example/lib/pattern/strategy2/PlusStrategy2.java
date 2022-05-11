package com.example.lib.pattern.strategy2;

/**
 * 单例 加法策略
 */
public class PlusStrategy2 extends AbstractStrategy {

    private static final PlusStrategy2 instance = new PlusStrategy2();

    private PlusStrategy2() {
        register();
    }

    public static PlusStrategy2 getInstance() {
        return instance;
    }

    @Override
    public int opt(int num1, int num2) {
        return num1 + num2;
    }
}
