package com.zjt.startmodepro.view_dispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by za-zhujiangtao on 2018/11/5.
 */

public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("view_dispatch", "MyTextView  dispatchTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("view_dispatch", "MyTextView dispatchTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("view_dispatch", "MyTextView dispatchTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("view_dispatch", "MyTextView dispatchTouchEvent ACTION_CANCEL");
                break;

        }
        boolean res = super.dispatchTouchEvent(event);
        Log.e("view_dispatch", "MyTextView dispatchTouchEvent return >>> " + res);
        return res;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e("view_dispatch", "MyTextView  onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("view_dispatch", "MyTextView onTouchEvent ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e("view_dispatch", "MyTextView onTouchEvent ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e("view_dispatch", "MyTextView onTouchEvent ACTION_CANCEL");
                break;
        }
        boolean res = super.onTouchEvent(event);
        Log.e("view_dispatch", "MyTextView onTouchEvent return >>> " + res);
        return res;
    }

}
