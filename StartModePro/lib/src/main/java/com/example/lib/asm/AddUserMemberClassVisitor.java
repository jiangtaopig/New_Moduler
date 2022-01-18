package com.example.lib.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.IFEQ;

public class AddUserMemberClassVisitor extends ClassVisitor {

    private String owner;
    private String fieldName;
    private String fieldDescriptor;

    public AddUserMemberClassVisitor(int api, ClassVisitor classVisitor, String fieldName, String fieldDescriptor) {
        super(api, classVisitor);
        this.fieldName = fieldName;
        this.fieldDescriptor = fieldDescriptor;
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
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        // 为字段 name 赋值无效，但是把 访问修饰符改为 public 生效
        System.out.println("visitField : name = " + name +" , descriptor = >>> " + descriptor);
        if (name.equals(fieldName) && descriptor.equals(fieldDescriptor)) {
            value = "zhujiangt";
            access = Opcodes.ACC_PUBLIC;
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public void visitEnd() {
        // 新增一个字段 needPrint 且赋值为true。 Z 表示 bool 值
        // 为什么不在ClassVisitor.visitField()方法当中来添加字段呢？如果在ClassVisitor.visitField()方法，就可能添加重复的字段
        FieldVisitor fieldVisitor = super.visitField(Opcodes.ACC_PUBLIC+ Opcodes.ACC_STATIC,
                "needPrint", "Z", null, Boolean.TRUE);
        if (fieldVisitor != null)
            fieldVisitor.visitEnd();
        super.visitEnd();
    }
}
