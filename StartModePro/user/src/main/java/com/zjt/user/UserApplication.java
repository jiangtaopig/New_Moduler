package com.zjt.user;

import android.content.Context;
import android.util.Log;

import com.zjt.base.ApplicationDelegate;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/3/8
 */
public class UserApplication implements ApplicationDelegate {

    /**
     * 可以在这里面进行业务相关的初始化
     * @param context
     */
    @Override
    public void onCreate(Context context) {
        Log.e("zjt_application", "UserApplication ----- onCreate");
    }
}
