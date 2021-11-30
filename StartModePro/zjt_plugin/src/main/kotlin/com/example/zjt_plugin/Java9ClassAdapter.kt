package com.example.zjt_plugin

import org.apache.commons.io.FileUtils
import java.io.File

/**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
 */

fun copyIfLegal(srcFile: File?, destFile: File) {
    if (srcFile?.name?.contains("module-info") != true) {
        try {
            srcFile?.apply {
                FileUtils.copyFile(srcFile, destFile)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        print("copyIfLegal module-info:" + srcFile.name)
    }
}
