package com.example.lib.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.IFEQ;

public class AddUserMemberClassVisitor extends ClassVisitor {

    private String owner;

    public AddUserMemberClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
//        if (methodVisitor != null && !name.equals("<init>")) {
//            MethodVisitor newMethodVisitor = new MethodVisitor(api, methodVisitor) {
//                @Override
//                public void visitCode() {
////                    mv.visitCode();
////                    Label label0 = new Label();
////                    mv.visitJumpInsn(IFEQ, label0);
////                    mv.visitLabel(label0);
//                }
//
//                @Override
//                public void visitInsn(int opcode) {
////                    mv.visitInsn(Opcodes.RETURN);
////                    mv.visitMaxs(1, 1);
////                    mv.visitInsn(opcode);
//                }
//            };
//            return newMethodVisitor;
//        }

        return methodVisitor;
    }

    @Override
    public void visitEnd() {
//        super.visitEnd();
        // 新增一个字段 needPrint 且赋值为true。 Z 表示 bool 值
        FieldVisitor fieldVisitor = cv.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_STATIC,
                "needPrint", "Z", null, Boolean.TRUE);
        if (fieldVisitor != null)
            fieldVisitor.visitEnd();
        cv.visitEnd();
    }
}
