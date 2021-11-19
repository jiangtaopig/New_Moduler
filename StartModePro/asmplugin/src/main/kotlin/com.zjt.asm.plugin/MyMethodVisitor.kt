package com.zjt.asm.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class MyMethodVisitor(methodVisitor: MethodVisitor) :MethodVisitor(Opcodes.ASM5, methodVisitor){
    override fun visitCode() {
        super.visitCode()
        mv.visitLdcInsn("TAG")
        mv.visitLdcInsn("===== This is just a test message =====")
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "android/util/Log",
            "e",
            "(Ljava/lang/String;Ljava/lang/String;)I",
            false
        )
        mv.visitInsn(Opcodes.POP)
    }
}