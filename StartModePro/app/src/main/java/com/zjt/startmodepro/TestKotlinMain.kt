package com.zjt.startmodepro

import android.util.Log

class TestKotlinMain {

    var address: String = ""
        get() {
            Log.e("zx", "get ")
            return field
        }
        set(value) {
            Log.e("zx", "set ")
            field = value
        }

    fun f() {
        TestKotlin.m1()
    }
}