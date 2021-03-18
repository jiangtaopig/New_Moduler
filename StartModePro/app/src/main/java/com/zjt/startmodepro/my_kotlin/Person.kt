package com.zjt.startmodepro.my_kotlin

import android.util.Log

open class Person(var name :String, var sex :Int) {

    open fun showDetail(){
        Log.e("Person", "name = $name , sex = $sex")
    }
}
