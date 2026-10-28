package com.zjt.startmodepro.scroll_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * 内部拦截法的父容器
 * 配合InnerInterceptViewPager使用
 */
public class InnerInterceptParentLayout extends LinearLayout {

    public InnerInterceptParentLayout(Context context) {
        super(context);
    }

    public InnerInterceptParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 记录上次触摸的位置
    private int mLastX;
    private int mLastY;
    private int mTouchSlop;

    {
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * 内部拦截法中，父容器必须重写onInterceptTouchEvent
     * 在内部拦截法中，我们需要更智能地判断是否拦截事件
     * 子View可以通过requestDisallowInterceptTouchEvent方法来控制父容器是否拦截事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        
        boolean intercepted = false;
        
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 不拦截ACTION_DOWN事件，确保子View能收到DOWN事件
                intercepted = false;
                // 重置disallowIntercept标志，确保子View可以重新控制拦截
                super.onInterceptTouchEvent(ev);
                break;
                
            case MotionEvent.ACTION_MOVE:
                // 计算滑动距离
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                
                // 如果子View调用了requestDisallowInterceptTouchEvent(true)
                // 那么这里的判断会被忽略，事件会继续传递给子View
                // 这样可以让子View控制事件流向
                
                // 只有在明显是垂直滑动时才拦截
                if (Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > mTouchSlop) {
                    intercepted = true;
                } else {
                    // 其他情况（水平滑动或点击）不拦截
                    intercepted = false;
                }
                break;
                
            case MotionEvent.ACTION_UP:
                // 不拦截ACTION_UP事件，确保子View能收到完整的事件序列
                // 这样子View中的点击事件才能正常触发
                intercepted = false;
                break;
        }
        
        // 记录上次触摸位置
        mLastX = x;
        mLastY = y;
        
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 处理自己的触摸逻辑，例如垂直滑动
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 处理垂直滑动
                break;
        }
        return true;
    }
}