package com.zjt.startmodepro;

import android.content.Context;
import android.util.Log;

import com.zjt.base.ApplicationDelegate;

public class HomeApplicationDelegate implements ApplicationDelegate {
    static {
        Log.e("zjt_application", "xxxx");
    }

    public void dost() {

    }

    @Override
    public void onCreate(Context context) {
        Log.e("zjt_application", "HomeApplicationDelegate onCreate");
    }
}
