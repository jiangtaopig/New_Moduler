package com.zjt.startmodepro

class TestKotlin {

    private var a: String? = null
    private lateinit var b :String

    fun f1(string: String?) {
        string?.length
        a?.length
    }

    // 伴生类就相当于静态内部类, 使用 @JvmStatic 注解就表示该属性或方法就是外部类的静态方法和属性
    companion object {
        val n = 1

        @JvmStatic
        val mm = 2

        @JvmStatic
        fun m1() {

        }
    }

}