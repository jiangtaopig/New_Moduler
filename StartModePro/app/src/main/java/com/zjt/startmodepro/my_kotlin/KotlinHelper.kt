package com.zjt.startmodepro.my_kotlin

/**
 * 类似与Java中的单例
 */
object KotlinHelper {

    fun isPositiveNum(x :Int) = x > 0


    // 扩展最基本类的对象，Any 类似于 Java中的 Object，即是一切类的基类
    inline fun <reified T> Any.isInstanceOf(): Boolean = this is T
}