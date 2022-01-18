package com.example.lib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.IF_ICMPLE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.ISTORE;

public class TestHan {
    public static void main(String[] args) {
        changeMethod();
    }

    /**
     * 为 Filter 类添加一个字段
     */
    private static void changeMethod() {
        Class<Han> clazz = Han.class;
        String classPath = FileUtils.getClassFilePath(clazz);
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            ChangeHanMethodClassVisitor changeFilterClassVisitor = new ChangeHanMethodClassVisitor(Opcodes.ASM9, classWriter, "changeMethod", "()I");
            classReader.accept(changeFilterClassVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream("/Users/zhujiangtao/My_Pro/New_Moduler/StartModePro/lib/src/main/java/com/example/lib/asm/Han.class");
                fos.write(classWriter.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

//            MyClassLoader myClassLoader = new MyClassLoader();
//            Class<?> filter = myClassLoader.defineClass("com.example.lib.asm.Han", classWriter.toByteArray());
//            Method show = filter.getDeclaredMethod("changeMethod");
//            show.invoke(filter.newInstance());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void showHan(String val) {
//        Han han = new Han();
        Han.show2(val);

        Han.show(val);
    }

    private static class ChangeHanMethodClassVisitor extends ClassVisitor {

        private String owner;
        private String methodName;
        private String methodDescriptor;

        private boolean isPresent = false;

        public ChangeHanMethodClassVisitor(int api, ClassVisitor classVisitor, String methodName, String methodDescriptor) {
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
//                mv = null; // 删除指定的方法体
                mv = new ChangeHanMethodAdapter(Opcodes.ASM9, mv);
            }
            return mv;
        }
    }

    private static class ChangeHanMethodAdapter extends MethodVisitor {

        public ChangeHanMethodAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitEnd() {
            super.visitInsn(ICONST_2);
            super.visitVarInsn(ISTORE, 1);
            super.visitInsn(ICONST_3);
            super.visitVarInsn(ISTORE, 2);
            super.visitVarInsn(ILOAD, 1);
            super.visitVarInsn(ILOAD, 2);
            super.visitInsn(IADD);
            super.visitInsn(ICONST_4);
            Label label0 = new Label();
            super.visitJumpInsn(IF_ICMPLE, label0);
            super.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn("----- bigger than 4 -----");
            super.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            super.visitLabel(label0);
            super.visitVarInsn(ILOAD, 1);
            super.visitVarInsn(ILOAD, 2);
            super.visitInsn(IADD);
            super.visitInsn(IRETURN);
            super.visitMaxs(2, 3);
            super.visitEnd();
        }
    }
}
