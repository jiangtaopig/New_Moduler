package com.example.lib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.objectweb.asm.Opcodes.AASTORE;
import static org.objectweb.asm.Opcodes.ANEWARRAY;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

public class TestFilter {
    public static void main(String[] args) {
        addField();
    }

    /**
     * 为 Filter 类添加一个字段
     */
    private static void addField() {
        Class<Filter> clazz = Filter.class;
        String classPath = FileUtils.getClassFilePath(clazz);
        try {
            ClassReader classReader = new ClassReader(new FileInputStream(classPath));
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

            /**
             *  为 Filter 类添加如下方法
             *  public List<String> getWhiteList() {
             *      return Arrays.asList("baidu", "google", "facebook");
             *  }
             */
            ClassVisitor addMethodCv = new AbstractClassMethodVisitor(Opcodes.ASM9, classWriter, Opcodes.ACC_PUBLIC, "getWhiteList",
                    "()Ljava/util/List;", "()Ljava/util/List<Ljava/lang/String;>;", null) {
                @Override
                protected void generateMethodBody(MethodVisitor methodVisitor) {
                    methodVisitor.visitInsn(ICONST_3);
                    methodVisitor.visitTypeInsn(ANEWARRAY, "java/lang/String");
                    methodVisitor.visitInsn(DUP);
                    methodVisitor.visitInsn(ICONST_0);
                    methodVisitor.visitLdcInsn("baidu");
                    methodVisitor.visitInsn(AASTORE);
                    methodVisitor.visitInsn(DUP);
                    methodVisitor.visitInsn(ICONST_1);
                    methodVisitor.visitLdcInsn("google");
                    methodVisitor.visitInsn(AASTORE);
                    methodVisitor.visitInsn(DUP);
                    methodVisitor.visitInsn(ICONST_2);
                    methodVisitor.visitLdcInsn("facebook");
                    methodVisitor.visitInsn(AASTORE);
                    methodVisitor.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "asList", "([Ljava/lang/Object;)Ljava/util/List;", false);
                    methodVisitor.visitInsn(ARETURN);

                    /**
                     *  因为 上面的 ClassWriter 构建时传入的是 ClassWriter.COMPUTE_FRAMES，所以这里可以写成 visitMaxs(0, 0)
                     *  但是不能省略不写，ClassWriter 会自己去计算；如果知道怎么计算的话可以写确定值，如这里的 visitMaxs(2, 3)
                     */
                    methodVisitor.visitMaxs(0, 0);
//                    mv.visitMaxs(2, 3);
                    methodVisitor.visitEnd();

                }
            };

            ChangeFilterClassVisitor changeFilterClassVisitor = new ChangeFilterClassVisitor(Opcodes.ASM9, addMethodCv, "filterBadUrl", "(Ljava/lang/String;)V");
            classReader.accept(changeFilterClassVisitor, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            FileOutputStream fos;
            try {
                fos = new FileOutputStream("/Users/zhujiangtao/My_Pro/New_Moduler/StartModePro/lib/src/main/java/com/example/lib/asm/Filter.class");
                fos.write(classWriter.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            MyClassLoader myClassLoader = new MyClassLoader();
            Class<?> filter = myClassLoader.defineClass("com.example.lib.asm.Filter", classWriter.toByteArray());
            Method show = filter.getDeclaredMethod("filterBadUrl", String.class);
            show.invoke(filter.newInstance(), "http://www.baidu.com");

        } catch (IOException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
