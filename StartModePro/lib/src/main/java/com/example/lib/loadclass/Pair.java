package com.example.lib.loadclass;

public class Pair<T> {
    private T value;

    public  Pair(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
