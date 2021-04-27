package com.zjt.startmodepro.bean

import android.util.Log

data class Student(var name :String, var age : Int) {

    var id = "0"

    fun showStudent(){
        Log.e("zjt", "name = $name, age = $$age")
    }

}