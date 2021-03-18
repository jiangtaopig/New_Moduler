package com.zjt.startmodepro.my_kotlin

/**
 * 如果子类没有主构造函数，则必须在每一个二级构造函数中用 super 关键字初始化基类
 */
class Woman : Person {
    var age:Int? = 0

    constructor(name :String, sex :Int, age :Int) : super(name, sex){
        this.age = age
    }

}