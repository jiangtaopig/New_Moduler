package com.example.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.LLOAD;
import static org.objectweb.asm.Opcodes.LSTORE;
import static org.objectweb.asm.Opcodes.LSUB;
import static org.objectweb.asm.Opcodes.RETURN;

/**
 * 打印User 类 sub 方法的参数
 */
public class TimeMethodVisitor extends ClassVisitor {
    private String methodName;
    private String methodDescriptor;

    public TimeMethodVisitor(int api, ClassVisitor classVisitor, String methodName, String methodDescriptor) {
        super(api, classVisitor);
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if ((name.equals(methodName) && descriptor.equals(methodDescriptor))) {
            mv = new TimeMethodAdapter(api, mv);
        }
        return mv;
    }

    private static class TimeMethodAdapter extends MethodVisitor {

        public TimeMethodAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
//            System.out.println("------- visitCode ------");
            super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            super.visitVarInsn(LSTORE, 1);
            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN || opcode <= RETURN)) {
                super.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
                super.visitVarInsn(LSTORE, 3);
                super.visitVarInsn(LLOAD, 3);
                super.visitVarInsn(LLOAD, 1);
                super.visitInsn(LSUB);
                super.visitVarInsn(LSTORE, 5);
                super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitVarInsn(LLOAD, 5);
                super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(J)V", false);
                super.visitInsn(RETURN);
            }
            super.visitInsn(opcode);
        }
    }
}
