package com.zjt.asm.asmplugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class AsmTestPlugin implements Plugin<Project> {

    @Override
    void apply(Project target) {
        System.out.println("========== this is a log just from asm plugin ==========")
    }
}