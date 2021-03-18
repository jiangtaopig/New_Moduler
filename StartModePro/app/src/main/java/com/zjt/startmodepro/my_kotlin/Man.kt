package com.zjt.startmodepro.my_kotlin

import android.util.Log

/**
 * 子类有主构造函数，那么基类必须在主构造函数中立即初始化
 */
class Man(name : String, sex :Int, var age :Int) : Person(name, sex) {

    override fun showDetail() {
        super.showDetail()
        Log.e("Man", "age = $age")
    }
}