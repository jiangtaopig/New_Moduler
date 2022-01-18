package com.example.lib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;

/**
 * 变量 descriptor 如下：注意的2个
 * Java type    Type descriptor
 * boolean      Z （zero ）
 * long         J
 */
public class TestAsm {
    public static void main(String[] args) {
//        testGenerateClass();
//        addField();
//        addMethod();

//        addEnterAndExit();
//        printMethodParams();
//        testMethodTime();
        testMethodTime2();
    }


    /**
     * 为 User 类添加一个字段
     */
    private static void addField() {
        Class clazz = User.class;
        String classPath = getClassFilePath(clazz);
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            // 新增一个字段 needPrint 且赋值为true
            AddUserMemberClassVisitor addUserMemberClassVisitor = new AddUserMemberClassVisitor(Opcodes.ASM5, classWriter, "name", "Ljava/lang/String;");
            classReader.accept(addUserMemberClassVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream("/Users/zhujiangtao/My_Pro/New_Moduler/StartModePro/lib/src/main/java/com/example/lib/asm/User.class");
                fos.write(classWriter.toByteArray());
                fos.flush();
                fos.close();
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

    /**
     * 为 User 类的show 方法添加一个进入和退出日志
     *      public void show() {
     *         System.out.println("enter ..."); // 进入日志
     *         System.out.println(this.name);
     *         System.out.println("exit ..."); // 退出日志
     *     }
     */
    private static void addEnterAndExit() {
        Class clazz = User.class;
        String classPath = getClassFilePath(clazz);
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new MethodEnterAndExitVisitor(Opcodes.ASM9, classWriter);
            classReader.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            outputByteToFile(classWriter);

            MyClassLoader myClassLoader = new MyClassLoader();
            Class<?> user2 = myClassLoader.defineClass("com.example.lib.asm.User", classWriter.toByteArray());
            Method show = user2.getDeclaredMethod("show");
            show.invoke(user2.newInstance());
        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印 User 类 sub 方法的参数
     */
    private static void printMethodParams() {
        Class clazz = User.class;
        String classPath = getClassFilePath(clazz);
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new PrintMethodParamsVisitor(Opcodes.ASM9, classWriter, "sub", "(II)I");
            classReader.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            outputByteToFile(classWriter);

            MyClassLoader myClassLoader = new MyClassLoader();
            Class<?> user2 = myClassLoader.defineClass("com.example.lib.asm.User", classWriter.toByteArray());
            Method show = user2.getDeclaredMethod("sub", int.class, int.class);
            int sum = (int) show.invoke(user2.newInstance(), 5, 2);
            System.out.println("sub = "+sum);
        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void testMethodTime() {
        Class clazz = User.class;
        String classPath = getClassFilePath(clazz);
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new TimeMethodVisitor(Opcodes.ASM9, classWriter, "testDuration", "()V");
            classReader.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            outputByteToFile(classWriter);

            MyClassLoader myClassLoader = new MyClassLoader();
            Class<?> user2 = myClassLoader.defineClass("com.example.lib.asm.User", classWriter.toByteArray());
            Method testDuration = user2.getDeclaredMethod("testDuration");
            testDuration.invoke(user2.newInstance());
        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void testMethodTime2() {
        Class clazz = MyMath.class;
        String classPath = getClassFilePath(clazz);
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            ClassVisitor cv = new MeasureMethodCostTimeVisitor(Opcodes.ASM9, classWriter);
            classReader.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            outputByteToFile2(classWriter, "/Users/zhujiangtao/My_Pro/New_Moduler/StartModePro/lib/src/main/java/com/example/lib/asm/MyMath.class");

            MyClassLoader myClassLoader = new MyClassLoader();
            Class<?> user2 = myClassLoader.defineClass("com.example.lib.asm.MyMath", classWriter.toByteArray());
            Method testDuration = user2.getDeclaredMethod("add", int.class, int.class);
            testDuration.invoke(user2.newInstance(), 2, 3);

        } catch (IOException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将 ClassWriter 生成的字节数组 转成 User.class
     * @param classWriter
     */
    private static void outputByteToFile(ClassWriter classWriter) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("/Users/zhujiangtao/My_Pro/New_Moduler/StartModePro/lib/src/main/java/com/example/lib/asm/User.class");
            fos.write(classWriter.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outputByteToFile2(ClassWriter classWriter, String classPath) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(classPath);
            fos.write(classWriter.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为 User 类添加一个 add 方法
     */
    private static void addMethod() {
        Class clazz = User.class;
        try {
            String classPath = getClassFilePath(clazz);
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            ClassVisitor cv = new AbstractClassMethodVisitor(Opcodes.ASM9, classWriter, Opcodes.ACC_PUBLIC, "add",
                    "(II)I", null, null) {
                @Override
                protected void generateMethodBody(MethodVisitor mv) {
                    mv.visitCode();
                    mv.visitVarInsn(ILOAD, 1);
                    mv.visitVarInsn(ILOAD, 2);
                    mv.visitInsn(IADD);
                    mv.visitInsn(IRETURN);
                    /**
                     *  因为 上面的 ClassWriter 构建时传入的是 ClassWriter.COMPUTE_FRAMES，所以这里可以写成 visitMaxs(0, 0)
                     *  但是不能省略不写，ClassWriter 会自己去计算；如果知道怎么计算的话可以写确定值，如这里的 visitMaxs(2, 3)
                     */
                    mv.visitMaxs(0, 0);
//                    mv.visitMaxs(2, 3);
                    mv.visitEnd();

                }
            };
            classReader.accept(cv, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream("/Users/zhujiangtao/My_Pro/New_Moduler/StartModePro/lib/src/main/java/com/example/lib/asm/User.class");
                fos.write(classWriter.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MyClassLoader myClassLoader = new MyClassLoader();
            Class<?> user2 = myClassLoader.defineClass("com.example.lib.asm.User", classWriter.toByteArray());
            // 获取 add 方法， 参数为 方法名和参数类型。getDeclaredMethod 能够获取私有方法，getMethod 只能获取公有方法
            Method add = user2.getDeclaredMethod("add", int.class, int.class);
            int sum = (int) add.invoke(user2.newInstance(), 2, 3);
            System.out.println("sum >>> " + sum);
            Method[] declaredMethods = user2.getDeclaredMethods();
            for (Method m : declaredMethods) {
                System.out.println("    " + m.getName());
            }
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        Class<?> clazz = myClassLoader.defineClass("com.example.lib.asm.HelloWorld", bytes);
        Method main = null;
        try {
            main = clazz.getMethod("main", String[].class);
            main.invoke(null, new Object[]{new String[]{}});
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
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
