package com.example.lib.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.RETURN;

public class MeasureMethodCostTimeVisitor extends ClassVisitor {

    private int api;
    private String className;

    public MeasureMethodCostTimeVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
        this.api = api;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (className != null) {
//            mv = new MeasureMethodCostTimeAdapter(api, mv, access, name,descriptor, className);
            mv = new MethodCostTimeAdapter(Opcodes.ASM9, mv);
        }
        return mv;
    }


    private static class MethodCostTimeAdapter extends MethodVisitor {

        public MethodCostTimeAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            super.visitVarInsn(LSTORE, 3);
            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.ATHROW || (opcode >= IRETURN || opcode <= RETURN)) {
                super.visitVarInsn(LSTORE, 5);
                super.visitVarInsn(LLOAD, 5);
                super.visitVarInsn(LLOAD, 3);
                super.visitInsn(LSUB);
                super.visitVarInsn(LSTORE, 7);
                super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitVarInsn(LLOAD, 7);
                super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
                super.visitMaxs(8, 9);
                super.visitInsn(IRETURN);
//                super.visitEnd();
            }
            super.visitInsn(opcode);
        }

        @Override
        public void visitEnd() {

        }
    }
}
