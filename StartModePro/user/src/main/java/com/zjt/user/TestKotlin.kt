package com.zjt.user

class TestKotlin {

    private val bean1 = MyBean("zjt", 23)
    private val bean2 = MyBean("zjt", 23)

    // kotlin == 相当于java 的 equal ， === 相当于 java 的引用是否相等，即是否是同一个对象

    fun testEqual() :Boolean{
        return bean1 == bean2
    }

    fun testSameReference() :Boolean {
        return bean1 === bean2
    }
}