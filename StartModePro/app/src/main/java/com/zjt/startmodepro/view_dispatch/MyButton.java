package com.zjt.startmodepro.view_dispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatButton;

/**
 * Created by za-zhujiangtao on 2018/11/5.
 */

public class MyButton extends AppCompatButton {
    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                getParent().requestDisallowInterceptTouchEvent(true);//true 不允许父布局拦截

                Log.e("view_dispatch", "MyButton  dispatchTouchEvent ACTION_DOWN");
                // true 不允许父布局拦截 , 我在 MyLinearLayout 中的 onInterceptTouchEvent 的 ACTION_MOVE中 return true 即拦截了，
                // 这里又调用方法让父布局不要拦截；但是 如果 MyLinearLayout 在 onInterceptTouchEvent 的 ACTION_DOWN 事件拦截了，
                // 那么此方法是没用的，因为都走不到 MyButton 的 dispatchTouchEvent

                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("view_dispatch", "MyButton dispatchTouchEvent ACTION_MOVE");
//                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
                Log.e("view_dispatch", "MyButton dispatchTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("view_dispatch", "MyButton dispatchTouchEvent ACTION_CANCEL");
                break;
        }
        boolean res = super.dispatchTouchEvent(event);
        Log.e("view_dispatch", "MyButton dispatchTouchEvent return >>> " + res);
        return res;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("view_dispatch", "MyButton  onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("view_dispatch", "MyButton onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("view_dispatch", "MyButton onTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("view_dispatch", "MyButton onTouchEvent ACTION_CANCEL");
                break;
        }
        boolean res = super.onTouchEvent(event);
        Log.e("view_dispatch", "MyButton onTouchEvent return >>> " + res);
        return res;
    }
}
