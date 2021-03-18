package com.zjt.startmodepro.my_kotlin

import android.util.Log

class MyKotlinManager (val name :String){ // 主构造器

    var age: Int = 0

    constructor(name: String, age : Int) : this(name){ // 次构造器
        this.age = age
    }

    fun showTops(){
        Log.e("zjt", "name = $name, age = $age")
    }
}