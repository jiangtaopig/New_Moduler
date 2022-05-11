package com.zjt.startmodepro.scroll_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class MyScrollerView extends ScrollView {
    public MyScrollerView(Context context) {
        super(context);
    }

    public MyScrollerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // ScrollView 默认在拖拽状态下会拦截 MOVE 事件，默认不拦截UP事件，所以这里只拦截了 UP 事件，
        boolean intercepted  = super.onInterceptTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            intercepted = true;
        }
        return intercepted;
    }
}
