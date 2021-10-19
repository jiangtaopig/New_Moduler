package com.zjt.startmodepro.scroll_conflict;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/1/7
 * 讲述内部拦截的故事
 */
public class InterceptRecycleView extends RecyclerView {

    private boolean mIsScrollToTop;
    private boolean mIsScrollToBottom;
    private int mLastX;
    private int mLastY;

    public InterceptRecycleView(@NonNull Context context) {
        this(context, null);
    }

    public InterceptRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterceptRecycleView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mIsScrollToBottom = false;
                mIsScrollToTop = false;
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem == 0) {
                    mIsScrollToTop = true;
                }

                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem == layoutManager.getItemCount() - 1) {
                    mIsScrollToBottom = true;
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // DOWN 时间要求外层 ScrollView 不能拦截，拦截后那么后续的事件都由 ScrollView 自己处理不会传递到 RecycleView了。
                getParent().requestDisallowInterceptTouchEvent(true);
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                Log.e("InterceptRecycleView", "mLastX = " + mLastX + ", mLastY = " + mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                // 外层布局的 ScrollView 默认是拦截 MOVE 事件的，不然调用 requestDisallowInterceptTouchEvent也是没用的。
                // 因为 ViewGroup 的 dispatchTouchEvent 中是这样判断的==> if (!disallowIntercept && onInterceptTouchEvent(ev))
                if (superDispatchMoveEvent(ev)) { // 希望外层的 ScrollView 来处理事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("InterceptRecycleView", "ACTION_UP");
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean superDispatchMoveEvent(MotionEvent ev) {
        Log.e("InterceptRecycleView", "y = " + ev.getY() + ", mIsScrollToTop = " + mIsScrollToTop + ", mIsScrollToBottom = " + mIsScrollToBottom);
        // 下滑
        boolean canScrollBottom = mIsScrollToTop && (ev.getY() - mLastY) > 0;
        // 上滑
        boolean canScrollTop = mIsScrollToBottom && (ev.getY() - mLastY) < 0;
        return canScrollBottom || canScrollTop;
    }

}
