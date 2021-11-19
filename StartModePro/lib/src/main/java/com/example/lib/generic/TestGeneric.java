package com.example.lib.generic;


import java.util.ArrayList;
import java.util.List;

public class TestGeneric {
    public static void main(String[] args) {

        FruitManager<Fruit> fruitFruitManager = new FruitManager<>(new Apple());
        fruitFruitManager.show();

        fruitFruitManager = new FruitManager<>(new Banana());
        fruitFruitManager.show();

//        Pair<? super Meat> food = new Pair<Meat>(new Beef());
//        Beef beef = (Beef) food.getValue();
//        beef.name();

        // super 能存不能取，super表示 只能是 Fruit 或者是 Fruit 的父类
        // new Plate 的尖括号只能是 Fruit 或者是 Fruit 的父类
        Plate<? super Fruit> food = new Plate<Fruit>(new Fruit());
        food.set(new Apple());
//        Apple apple = food.get();  // 报错


        List<? super Fruit> list = new ArrayList<Fruit>();
        list.add(new Apple());

        new Thread() {
            @Override
            public void run() {
                super.run();
            }
        };

    }
}
