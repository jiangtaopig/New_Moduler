package com.example.lib.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * 为类添加一个方法的抽象类
 */
public abstract class AbstractClassMethodVisitor extends ClassVisitor {

    private int methodAccess;
    private String methodName;
    private String methodDescriptor;
    private String signature; // 一般泛型用到的，所以一般赋值为null
    private String [] exceptions;
    private boolean isMethodPresent ;

    public AbstractClassMethodVisitor(int api, ClassVisitor classVisitor, int methodAccess, String methodName, String methodDescriptor,
                                      String signature, String [] exceptions) {
        super(api, classVisitor);
        this.methodAccess = methodAccess;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.signature = signature;
        this.exceptions = exceptions;
        this.isMethodPresent = false;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name.equals(methodName) && descriptor.equals(methodDescriptor)) {
            isMethodPresent = true;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        if (!isMethodPresent) {
            MethodVisitor methodVisitor = super.visitMethod(methodAccess, methodName, methodDescriptor, signature, exceptions);
            if (methodVisitor != null) {
                generateMethodBody(methodVisitor);
            }
        }
        super.visitEnd();
    }

    protected abstract void generateMethodBody(MethodVisitor mv);
}
