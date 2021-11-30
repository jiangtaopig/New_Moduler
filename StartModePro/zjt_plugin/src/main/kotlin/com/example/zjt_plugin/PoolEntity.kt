package com.example.zjt_plugin

/**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
 */
class PoolEntity(
    val code: Int,
    val owner: String,
    val name: String,
    val desc: String,
    val methodName: String = "getTHREAD_POOL_SHARE"
) {

    fun replaceDesc(): String {
        val index = desc.lastIndexOf(")")
        return desc.substring(0, index + 1) + ClassName
    }

    companion object {
        //com.zjt.startmodepro.utils，在自己项目中实现替换的线程池
        const val ClassName = "Lcom/zjt/startmodepro/utils/TestIOThreadExecutor;"
        const val Owner = "com/zjt/startmodepro/utils/TestIOThreadExecutor"
    }

}