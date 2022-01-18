package com.example.lib.weakreference;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;


public class MyWeakReference {

    private  TestData testData;

    public void testWeakReference() {

        TestData testData1 = new TestData("111", "222");
        ReferenceQueue<TestData> referenceQueue = new ReferenceQueue<>();
        // 由于是testData1强引用，所以GC不会回收，需要在后面把 testData1 设置为null,才能回收
        WeakReference<TestData> weakReference1 = new WeakReference<>(testData1, referenceQueue);

        System.out.println("gc 调用前");
        Reference<? extends TestData> reference = null;
        //还没有进行垃圾回收，所以 referenceQueue 中没有对象
        while ((reference = referenceQueue.poll()) != null) {
            System.out.println(reference);
        }

        System.out.println("weakReference = " + weakReference1);

        System.out.println("s1 = " + weakReference1.get()); //获取 weakReference1 引用的对象

        System.out.println("开始调用GC");

//        testData = weakReference1.get(); // 这里被属性 testData 引用也会无法回收

        testData1 = null; // 由于testData1是强引用，即使 使用 WeakReference也不会被回收，所以要主动设置为null

        System.gc();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //被回收了，所以输出为null
        if (weakReference1.get() != null) {
            System.out.println("未被回收， s1 = " + weakReference1.get());
        } else {
            System.out.println("被回收了");
        }

        //由于对象被回收了，会将weakReference1 添加到 referenceQueue
        while ((reference = referenceQueue.poll()) != null) {
            System.out.println("被回收了，存放到引用队列中， referenceQueue 中 ：" + reference);
        }
    }
}
