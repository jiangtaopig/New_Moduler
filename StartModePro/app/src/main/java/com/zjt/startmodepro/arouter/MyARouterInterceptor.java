package com.zjt.startmodepro.arouter;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

@Interceptor(priority = 2)
public class MyARouterInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {
        Log.e("MyARouterInterceptor", "---- process ---- path = " + postcard.getPath());
        switch (postcard.getPath()) {
            case "/user/main":
            default:
        }
        callback.onContinue(postcard);
    }

    @Override
    public void init(Context context) {
        Log.e("MyARouterInterceptor", "---- init ----");
    }
}
