package com.zjt.asm.plugin

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

class MyClassVisitor(classVisitor: ClassVisitor) :ClassVisitor(Opcodes.ASM5, classVisitor) {
    var className = ""
    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        if (name != null) {
            className = name
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
//        val classNose :ClassNode
        val methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
        // 在 MainActivity 的 onCreate 方法之前打印 log ,Log.e("TAG", "xxx")，具体得ASM 操作在 MyMethodVisitor 中实现
        System.out.println("========== MyClassVisitor ======= className = $className, name = $name")
        if (className == "com/zjt/startmodepro/MainActivity" && name == "onCreate") {
            System.out.println("========== 找到 MainActivity =======")
            return MyMethodVisitor(methodVisitor)
        }
        return methodVisitor
    }
}