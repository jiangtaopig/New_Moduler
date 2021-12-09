package com.example.lib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 变量 descriptor 如下：注意的2个
 *   Java type    Type descriptor
 *   boolean      Z （zero ）
 *   long         J
 */
public class TestAsm {
    public static void main(String[] args) {
//        System.out.println("hello world");
        testGenerateClass();
//        addMember();
//        User user = new User();

        Object object = new Object();

    }


    private static void addMember() {
        Class clazz = User.class;
        String classPath = getClassFilePath(clazz);
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(0);
            // 新增一个字段 needPrint 且赋值为true
            AddUserMemberClassVisitor addUserMemberClassVisitor = new AddUserMemberClassVisitor(Opcodes.ASM5, classWriter);
            classReader.accept(addUserMemberClassVisitor, 0);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("/Users/zhujiangtao/My_Pro/New_Moduler/StartModePro/lib/src/main/java/com/example/lib/asm/User.class");
                fos.write(classWriter.toByteArray());
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MyClassLoader myClassLoader = new MyClassLoader();
            Class<?> user2 = myClassLoader.defineClass("com.example.lib.asm.User", classWriter.toByteArray());
            Method show = user2.getDeclaredMethod("show");
            show.invoke(user2.newInstance());
            for (Field field : user2.getDeclaredFields()) {
                field.setAccessible(true);
                System.out.println(field.getName() + ": " + field.get(user2.newInstance()));
            }

        } catch (IOException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static void testGenerateClass() {
        MyClassLoader myClassLoader = new MyClassLoader();
        byte[] bytes = generateByAsm();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("/Users/zhujiangtao/My_Pro/New_Moduler/StartModePro/lib/src/main/java/com/example/lib/asm/HelloWorld.class");
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Class<?> clazz = myClassLoader.defineClass("com.example.lib.asm.HelloWorld", bytes);
        Method main = null;
        try {
            main = clazz.getMethod("main", String[].class);
            main.invoke(null, new Object[]{new String[]{}});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private static byte[] generateByAsm() {
        ClassWriter cw = new ClassWriter(0);
        // 定义对象头：版本 java 1.8, 修饰符 public , 全路径限定名（类名），签名，父类，实现的接口
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "com/example/lib/asm/HelloWorld", null, "java/lang/Object", null);
        // 添加方法：修饰符 public static , 方法名 main ,入参 String[]， 函数返回值 void，签名，异常
        MethodVisitor methodVisitor = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        // 获取 System 类的静态属性 out
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        // 加载常量
        methodVisitor.visitLdcInsn("Hello World");
        // 调用 println 方法
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        methodVisitor.visitInsn(Opcodes.RETURN); // 为啥要加return？
        methodVisitor.visitMaxs(2, 1);
        methodVisitor.visitEnd();
        cw.visitEnd();

        return cw.toByteArray();
    }

    public static String getClassFilePath(Class clazz) {
        String buildDir = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        String fileName = clazz.getSimpleName() + ".class";
        File file = new File(buildDir + clazz.getPackage().getName().replaceAll("[.]", "/") + "/", fileName);
        return file.getAbsolutePath();
    }


}
