package com.example.zjt_plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @Author ZhuJiangTao
 * @Since 2021/7/31
 */
class ThreadPoolPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        println("----- zjt threadpool plugin start------------")
        val appExtension = target.extensions.getByType(AppExtension::class.java)
        appExtension.registerTransform(ThreadPoolTransform())
    }
}