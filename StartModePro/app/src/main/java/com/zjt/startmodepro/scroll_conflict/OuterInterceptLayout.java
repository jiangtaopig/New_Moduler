package com.zjt.startmodepro.scroll_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * 外部拦截法解决滑动冲突示例
 * 场景：垂直ScrollView中嵌套水平滑动的ViewPager
 */
public class OuterInterceptLayout extends LinearLayout {
    private int mLastX;
    private int mLastY;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public OuterInterceptLayout(Context context) {
        super(context);
        init(context);
    }

    public OuterInterceptLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // 获取系统触摸阈值，用于判断是否为滑动
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mVelocityTracker = VelocityTracker.obtain();
    }

    /**
     * 关键方法：外部拦截法的核心就是重写父容器的onInterceptTouchEvent方法
     * 在事件分发时，父View会先调用dispatchTouchEvent，然后调用onInterceptTouchEvent判断是否拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // DOWN事件永远不拦截，让子View先处理
                // 这与ViewGroup的dispatchTouchEvent实现一致，确保子View能收到DOWN事件
                intercepted = false;
                // 调用父类方法，确保super.dispatchTouchEvent中的初始化逻辑执行
                super.onInterceptTouchEvent(ev);
                break;
                
            case MotionEvent.ACTION_MOVE:
                // 根据滑动方向判断是否拦截
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                
                // 如果是垂直滑动距离大于水平滑动距离，且超过触摸阈值，则父容器拦截
                if (Math.abs(deltaY) > Math.abs(deltaX) && Math.abs(deltaY) > mTouchSlop) {
                    intercepted = true;
                } else {
                    intercepted = false;
                }
                break;
                
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }

        mLastX = x;
        mLastY = y;
        
        return intercepted;
    }

    /**
     * 处理自己的触摸事件
     * 当onInterceptTouchEvent返回true时，事件会传递到这里处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
                
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastY;
                // 处理垂直滑动
                scrollBy(0, -deltaY);
                break;
                
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                // 处理惯性滑动等逻辑
                break;
        }
        
        mLastX = x;
        mLastY = y;
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}