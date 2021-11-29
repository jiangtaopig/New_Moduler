package com.example.lib.multithread;

public class DoSth {

    private String name;
    private String doSth;

    public DoSth(String name, String doSth) {
        this.name = name;
        this.doSth = doSth;
    }

    @Override
    public String toString() {
        return "DoSth{" +
                "name='" + name + '\'' +
                ", doSth='" + doSth + '\'' +
                '}';
    }
}
