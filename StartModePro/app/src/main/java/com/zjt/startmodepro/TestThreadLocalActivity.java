package com.zjt.startmodepro;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.zjt.base.BaseActivity;

import java.lang.reflect.Field;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/5/17 7:16 下午
 * @Description : TestThreadLocalActivity
 */


public class TestThreadLocalActivity extends BaseActivity {

    private TextView mRecycleThreadLocalTv;
    private ThreadLocal<String> threadLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_local_layout);
        mRecycleThreadLocalTv = findViewById(R.id.txt_recycle_reference);

        initData();
    }

    private void initData() {

        threadLocal = new ThreadLocal<>();
        threadLocal.set("我是在主线程中设置的值");
        mRecycleThreadLocalTv.setOnClickListener(v -> {
            threadLocal = null; // 断开 ThreadLocal 的强引用
            System.gc(); // 主动垃圾回收

            Thread curThread = Thread.currentThread();
            Class<? extends Thread> clz = curThread.getClass();
            Field field = null;
            try {
                field = clz.getDeclaredField("threadLocals");
                field.setAccessible(true);
                Object threadLocalMap = field.get(curThread);

                Class<?> tlmClass = threadLocalMap.getClass();
                // 下面这一行会报异常，因为引用的是 Android 的sdk, 所以还是得去Java 项目中去验证
                Field tableField = tlmClass.getDeclaredField("table");
                tableField.setAccessible(true);
                Object[] arr = (Object[]) tableField.get(threadLocalMap);

                for (Object o : arr) {
                    if (o == null) continue;
                    Class<?> entryClass = o.getClass();
                    Field valueField = entryClass.getDeclaredField("value");
                    Field referenceField = entryClass.getSuperclass().getSuperclass().getDeclaredField("referent");
                    valueField.setAccessible(true);
                    referenceField.setAccessible(true);
                    Log.e("TestThreadLocalActivity", String.format("弱引用key:%s    值:%s", referenceField.get(o), valueField.get(o)));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }
}
