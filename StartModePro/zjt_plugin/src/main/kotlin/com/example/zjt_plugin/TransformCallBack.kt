package com.example.zjt_plugin

/**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
 */
interface TransformCallBack {
    fun process(className: String, classBytes: ByteArray?): ByteArray?
}