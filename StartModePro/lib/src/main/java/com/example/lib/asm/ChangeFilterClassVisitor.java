package com.example.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;

public class ChangeFilterClassVisitor extends ClassVisitor {
    private String owner;
    private String methodName;
    private String methodDescriptor;

    private boolean isPresent = false;

    public ChangeFilterClassVisitor(int api, ClassVisitor classVisitor, String methodName, String methodDescriptor) {
        super(api, classVisitor);
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals(methodName) && descriptor.equals(methodDescriptor)) {
            mv = new ChangeFilterAdapter(Opcodes.ASM9, mv);
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    private static class ChangeFilterAdapter extends MethodVisitor {

        public ChangeFilterAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitVarInsn(ALOAD, 0);
            super.visitMethodInsn(INVOKEVIRTUAL, "com/example/lib/asm/Filter", "getWhiteList", "()Ljava/util/List;", false);
            super.visitVarInsn(ASTORE, 2);
            super.visitVarInsn(ALOAD, 2);
            super.visitVarInsn(ALOAD, 1);
            super.visitMethodInsn(INVOKEINTERFACE, "java/util/List", "contains", "(Ljava/lang/Object;)Z", true);
            Label label0 = new Label();
            super.visitJumpInsn(IFNE, label0);
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn("------ bad url -------");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            super.visitInsn(RETURN);
            super.visitLabel(label0);
            super.visitCode();
        }

    }
}
