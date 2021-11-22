package com.example.zjt_plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class ZRouterPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        println("----- zrouter plugin start------------")
        AppExtension appExtension = target.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new ZRouterTransform())

    }
}