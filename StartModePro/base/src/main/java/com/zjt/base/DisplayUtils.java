package com.zjt.base;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/8 3:52 下午
 * @Description : Displayutils
 */


public class DisplayUtils {

    public static void getDisplay(Context context, Point point) {
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        //获取屏幕的宽、高，单位是像素
        defaultDisplay.getSize(point);
    }



}
