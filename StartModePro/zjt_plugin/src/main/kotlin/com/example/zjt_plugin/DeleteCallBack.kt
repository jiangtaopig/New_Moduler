package com.example.zjt_plugin

/**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
 */
interface DeleteCallBack {
    fun delete(className: String, classBytes: ByteArray)
}