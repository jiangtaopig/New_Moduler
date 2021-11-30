package com.example.zjt_plugin

import java.io.IOException

/**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
 */
interface AsmHelper {
    @Throws(IOException::class)
    fun modifyClass(srcClass: ByteArray, className :String): ByteArray
}