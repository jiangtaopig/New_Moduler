package com.zjt.asm.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class MyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        System.out.println("========== xx this is a log just from asm plugin by kotlin language ==========")
        val appExtension = target.extensions.findByType(AppExtension::class.java)
        appExtension?.registerTransform(MyTransform())
    }
}