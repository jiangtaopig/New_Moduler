package com.zjt.startmodepro.view_dispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * Created by za-zhujiangtao on 2018/11/5.
 */

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean res = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("view_dispatch", "MyLinearLayout  onInterceptTouchEvent ACTION_DOWN");
//                return true;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("view_dispatch", "MyLinearLayout onInterceptTouchEvent ACTION_MOVE");
//                res = true;
                break;
            case MotionEvent.ACTION_UP:
                Log.d("view_dispatch", "MyLinearLayout onInterceptTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("view_dispatch", "MyLinearLayout onInterceptTouchEvent ACTION_CANCEL");
                break;
        }
        // 上面的 事件赋值为 true
        if (!res) {
            res = super.onInterceptTouchEvent(ev);
            Log.e("view_dispatch", "MyLinearLayout onInterceptTouchEvent return >>> " + res);
        } else {
            Log.e("view_dispatch", "MyLinearLayout onInterceptTouchEvent return >>> " + res + " , 拦截事件传递");
        }

        return res;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("view_dispatch", "MyLinearLayout  dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("view_dispatch", "MyLinearLayout dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("view_dispatch", "MyLinearLayout dispatchTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("view_dispatch", "MyLinearLayout dispatchTouchEvent ACTION_CANCEL");
                break;
        }
        boolean res = super.dispatchTouchEvent(event);
        Log.e("view_dispatch", "MyLinearLayout dispatchTouchEvent return >>> " + res);
        return res;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("view_dispatch", "MyLinearLayout  onTouchEvent ACTION_DOWN");
//                return false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("view_dispatch", "MyLinearLayout onTouchEvent ACTION_MOVE ");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("view_dispatch", "MyLinearLayout onTouchEvent ACTION_UP ");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("view_dispatch", "MyLinearLayout onTouchEvent ACTION_CANCEL");
                break;
        }
        boolean res = super.onTouchEvent(event);
        Log.e("view_dispatch", "MyLinearLayout onTouchEvent return >>> " + res);
        return res;
    }
}
