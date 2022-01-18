package com.example.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

/**
 * 打印User 类 sub 方法的参数
 */
public class PrintMethodParamsVisitor extends ClassVisitor {
    private String methodName;
    private String methodDescriptor;
    private boolean isMethodPresent;

    public PrintMethodParamsVisitor(int api, ClassVisitor classVisitor, String methodName, String methodDescriptor) {
        super(api, classVisitor);
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.isMethodPresent = false;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if ((name.equals(methodName) && descriptor.equals(methodDescriptor))) {
            mv = new PrintMethodParamsAdapter(api, mv);
        }
        return mv;
    }

    private static class PrintMethodParamsAdapter extends MethodVisitor {

        public PrintMethodParamsAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            System.out.println("------- visitCode ------");
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitVarInsn(ILOAD, 1);
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitVarInsn(ILOAD, 2);
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
            super.visitCode();
        }
    }
}
