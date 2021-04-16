package com.zjt.startmodepro.concurrent;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/14 1:58 下午
 * @Description : MyFutureTask
 */


public class MyFutureTask<T> extends FutureTask<T> {
    public MyFutureTask(Callable<T> callable) {
        super(callable);
    }

    @Override
    protected void done() {
        super.done();
        Log.e("MyFutureTask", "done");
    }

    @Override
    protected void set(T t) {
        super.set(t);

        Log.e("MyFutureTask", "set t = "+ t.toString());
    }
}
