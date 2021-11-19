package com.example.lib.generic;

/**
 * extends 不是继承的意思，它表示泛型的上界，即只能是 Fruit 本身或者是 Fruit 的子类
 * @param <T>
 */
public class FruitManager<T extends Fruit> {
    private T fruit;

    public FruitManager(T t) {
        fruit = t;
    }

    public void show() {
        fruit.name();
    }
}
