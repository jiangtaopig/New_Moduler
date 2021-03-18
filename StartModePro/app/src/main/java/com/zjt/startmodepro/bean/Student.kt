package com.zjt.startmodepro.bean

import android.util.Log

data class Student(val name :String, val age : Int) {


    fun showStudent(){
        Log.e("zjt", "name = $name, age = $$age")
    }
}