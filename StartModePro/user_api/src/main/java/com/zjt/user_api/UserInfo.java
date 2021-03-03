package com.zjt.user_api;


/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/3
 */
public class UserInfo {
    private String name;
    private int age;

    public UserInfo(String name, int age){
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
