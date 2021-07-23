package com.zjt.startmodepro.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class PigPlugin implements Plugin<Project>{

    @Override
    void apply(Project target) {
        AppExtension appExtension = target.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new PigTransform(target))
    }
}