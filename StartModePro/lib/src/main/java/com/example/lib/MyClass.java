package com.example.lib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;

public class MyClass {
    public static void main(String[] args) {


        String.valueOf(123);

        StringBuilder sb = new StringBuilder();
        sb.append(1);
        System.out.printf("sss------");

        testException("");
        reSize(3);

    }

    private static void testException(String val)  {
        try {
            testThrow("cc");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testThrows() throws NullPointerException, InterruptedException {
        Integer i = null;
        System.out.println(i + 1);
        Thread.sleep(333);
    }

    /**
     * 抛出非运行时异常，调用本函数的方法，必须捕获或抛出 IOException 异常
     * @param filePath
     * @throws IOException
     */
    private static void testThrow(String filePath) throws IOException {
        if (filePath == null) {
            throw new IOException();//运行时异常不需要在方法上申明
        }
    }

    private static void readFromFile(String filePath) {
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void testList() {
        Student student = null;
        List<Student> studentList = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            student = new Student();
            student.name = "zjt" + "_" + i;
            student.age = i;
            studentList.add(student);
        }

        int size = studentList.size();
    }

    /**
     * 返回大于cap的 2 的幂次方
     * @param cap
     * @return
     */
    private static int reSize(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : n + 1;
    }

}