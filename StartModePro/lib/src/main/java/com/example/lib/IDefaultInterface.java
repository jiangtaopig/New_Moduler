package com.example.lib;

public interface IDefaultInterface {
    // 接口支持默认的实现
    default void sayHi() {
        System.out.println("i am default sayHi");
    }

    void sayGoodBye();
}
