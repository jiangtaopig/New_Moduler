package com.example.lib.weakreference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2019/10/18
 */
public class TestWeakReference {
    private Student student;
    public static void main(String [] args){

        ReferenceQueue<Student> referenceQueue = new ReferenceQueue<>();

        Student s1 = new Student("xiaoming", 12);
        Student s2 = new Student("mazi", 33);
        //这里的WeakReference 的参数1 不能用上面注释的 s1，如果用了垃圾回收就不会回收 s1 ,因为s1是个强引用。
        WeakReference<Student> weakReference1 = new WeakReference<>(new Student("xiaoming", 12), referenceQueue);
        WeakReference<Student> weakReference2 = new WeakReference<>(new Student("mazi", 33), referenceQueue);

        //弱引用在GC的时候一定会被回收，回收后会将 弱引用对象 weakReference1 和 weakReference2 添加到 referenceQueue ，注意添加到referenceQueue队列中的
        //不是 new 出来的2个Student对象，这两个对象已经被回收了

        System.out.println("gc 调用前");
        Reference<? extends Student> reference = null;
        //还没有进行垃圾回收，所以 referenceQueue 中没有对象
        while ((reference = referenceQueue.poll()) != null){
            System.out.println(reference);
        }

        System.out.println("weakReference1 = "+weakReference1);
        System.out.println("weakReference2 = "+weakReference2);

        System.out.println("s1 = "+weakReference1.get()); //获取 weakReference1 引用的对象 s1
        System.out.println("s2 = "+weakReference2.get());

        System.out.println("开始调用GC");


        System.gc();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //被回收了，所以输出为null
        System.out.println("s1 = "+weakReference1.get());
        System.out.println("s2 = "+weakReference2.get());

        //由于对象被回收了，会将weakReference1 和 weakReference2 添加到 referenceQueue
        while ((reference = referenceQueue.poll()) != null){
            System.out.println("referenceQueue 中 ：" + reference);
        }

        // 任何一个对象的finalize()方法都只会被系统自动调用一次，如果对象面临下一次回收，它的finalize()方法不会被再次执行
        System.out.println("再次调用垃圾回收");
        System.gc(); //不会再调用 Student 对象的finalize()方法
    }
}


class Student {
    private String name;
    private int age;

    public Student(String name, int age){
        this.name = name;
        this.age = age;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println(name + " finalize ");
    }

    @Override
    public String toString() {
        return "Student{"+"name = "+name
                + ", age = "+age +", " +
                "hashCode = "+this.hashCode();
    }
}