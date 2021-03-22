package com.zjt.startmodepro.my_kotlin

import android.util.Log

class MyKotlinManager(val name: String) { // 主构造器

    companion object {
        val TAG = "MyKotlinManager"
    }

    var list  = ArrayList<Int>()
    var  map = HashMap<Int, String>()

    var age: Int = 0
    lateinit var listener: OnSuccessListener

    constructor(name: String, age: Int) : this(name) { // 次构造器
        this.age = age
    }

    fun showTops() {
        Log.e("zjt", "name = $name, age = $age")
    }

    fun setOnSuccessListener(listener: OnSuccessListener) {
        this.listener = listener
    }


    fun doInterface() {
        if (listener != null)
            listener.onSuccess("xxx zjt xxx")
    }

    private fun sth(x: Int, y: Int, option: (Int, Int) -> Int): Int {
        return option(x, y)
    }

    private fun sum(a: Int, b: Int): Int {
        return a + b;
    }

    // 调用 sth 的方法1
    fun ff(): Int {
        return sth(2, 3, ::sum)
    }

    // 调用 sth 的方法2
    fun ff2(){
        // sth 的最后一个入参是 函数类型，则可以移到参数列表的括号外面
        sth(3, 5) { x, y -> x + y }
    }

    fun sth2(opt : (Int , Int) -> Int) :Int{
        // opt(3, 7) 等价于 opt.invoke(3, 7)
        return opt.invoke(3, 7)
    }

    // 调用 sth2 的方法
    fun ff3() : Int{
        // 如果函数的入参只有一个且是函数类型，那么连入参的括号也可省略
        return sth2 {x, y -> x*y}
    }


    // 函数类型的赋值方式1 ： 利用 lambda 表达式来赋值
    val ss :(Int, Int) -> Int = {x :Int, y:Int -> x+y}
    // ss 的函数类型可省略 即 val ss = {x :Int, y:Int -> x+y}

    // 函数类型的变量赋值方式2 ： 直接使用在一个函数面前加上 ：：
    val ss2 : (Int, Int) -> Int = ::sum

    fun fff(){
        val s = ss(2, 3)
        Log.e("fff", "s = $s")
    }


    fun testWhen(){
        var a = -10
        when {
           isPositiveNumber(a) -> {
               Log.e(TAG, "a is positive , a = $a")
           }
            isChinese("zjt") -> {
                Log.e(TAG, "is chinese !!!")
            }
        }
    }

    private fun isPositiveNumber(x : Int) : Boolean = x > 0
    private fun isChinese(name : String)  = !name.isNullOrBlank()

    fun <T> doPrintln(content : T) {
        when (content) {
            is Int -> Log.e("zjt", "整型数字为 $content")
            is String -> Log.e("zjt", "字符串为 $content")
            else -> Log.e("zjt", "既不是整型也不是字符串类型 $content")
        }
    }



}