package com.example.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodEnterAndExitVisitor extends ClassVisitor {

    public MethodEnterAndExitVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        // <init> 就表示类当中的构造方法。
        if (mv != null && !"<init>".equals(name)) {
            boolean isAbstractMethod = (access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT;
            boolean isNativeMethod = (access & Opcodes.ACC_NATIVE) == Opcodes.ACC_NATIVE;
            // 不是抽象方法且不是 native 方法
            if (!isAbstractMethod && !isNativeMethod) {
                mv = new MethodEnterAndExitAdapter(api, mv);
            }
        }
        return mv;
    }

    /**
     * 在MethodVisitor类中，visitXxx()方法的调用顺序:
     * 第一步，调用visitCode()方法，调用一次。
     * 第二步，调用visitXxxInsn()方法，可以调用多次。
     * 第三步，调用visitMaxs()方法，调用一次。
     * 第四步，调用visitEnd()方法，调用一次。
     */
    private static class MethodEnterAndExitAdapter extends MethodVisitor {

        public MethodEnterAndExitAdapter(int api, MethodVisitor methodVisitor){
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            // 所以方法的进入时加 log，可以在 visitCode中
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn("enter ...");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            super.visitCode();
        }

        /**
         * 方法结束时的日志要加在visitInsn中，why ?
         * 我们需要知道“方法退出”有哪几种情况。方法的退出，有两种情况，一种是正常退出（执行return语句），另一种是异常退出（执行throw语句）；
         * 在MethodVisitor类当中，无论是执行return语句，还是执行throw语句，都是通过visitInsn(opcode)方法来实现的。
         * 所以，如果我们想在“方法退出”时，添加一些语句，那么这些语句放到visitInsn(opcode)方法中就可以了。
         */
        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN || opcode <= Opcodes.RETURN)) {
                super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitLdcInsn("exit ...");
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitEnd() {
            super.visitEnd();
        }
    }
}
