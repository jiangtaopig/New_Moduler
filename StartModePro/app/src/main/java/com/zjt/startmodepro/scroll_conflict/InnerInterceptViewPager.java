package com.zjt.startmodepro.scroll_conflict;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.viewpager.widget.ViewPager;

/**
 * 内部拦截法解决滑动冲突示例
 * 场景：垂直ScrollView中嵌套水平滑动的ViewPager
 */
public class InnerInterceptViewPager extends ViewPager {
    private int mLastX;
    private int mLastY;
    private int mTouchSlop;
    
    // 标记是否已经请求父View不要拦截
    private boolean mDisallowIntercept;

    public InnerInterceptViewPager(Context context) {
        super(context);
        init(context);
    }

    public InnerInterceptViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = android.view.ViewConfiguration.get(context).getScaledTouchSlop();
    }

    /**
     * 关键方法：内部拦截法的核心是重写子View的dispatchTouchEvent方法
     * 在事件分发时，子View通过调用parent.requestDisallowInterceptTouchEvent()
     * 来控制父View是否拦截事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // DOWN事件时，先请求父容器不要拦截
                // 这样能确保子View能收到完整的事件序列
                getParent().requestDisallowInterceptTouchEvent(true);
                mDisallowIntercept = true;
                break;
                
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                
                // 如果是水平滑动距离大于垂直滑动距离，且超过触摸阈值，则子View处理
                if (Math.abs(deltaX) > Math.abs(deltaY) && Math.abs(deltaX) > mTouchSlop) {
                    // 水平滑动，子View自己处理
                    mDisallowIntercept = true;
                } else {
                    // 垂直滑动，让父View处理
                    mDisallowIntercept = false;
                }
                
                // 根据判断结果，决定是否允许父View拦截事件
                // 【重要】：一旦调用requestDisallowInterceptTouchEvent(false)
                // 父View的onInterceptTouchEvent会被重新调用
                // 如果父View的onInterceptTouchEvent返回true，那么后续的所有事件
                // （包括当前的MOVE事件和后续的UP事件）都会被父View拦截
                // 子View将无法再收到任何事件，这个触摸序列对子View来说就结束了
                getParent().requestDisallowInterceptTouchEvent(mDisallowIntercept);
                
                // 如果父View拦截了事件，那么这里的super.dispatchTouchEvent可能不会被执行
                // 或者执行后返回false，因为事件已经被父View处理了
                break;
                
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 重置状态
                mDisallowIntercept = false;
                break;
        }
        
        mLastX = x;
        mLastY = y;
        
        return super.dispatchTouchEvent(ev);
    }
}