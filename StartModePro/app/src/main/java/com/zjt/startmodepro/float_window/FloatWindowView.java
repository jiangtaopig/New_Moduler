package com.zjt.startmodepro.float_window;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zjt.startmodepro.R;

public class FloatWindowView extends LinearLayout {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWmParams;
    private int statusBarHeight = 0;
    private int mScreenWidth;
    private int mScreenHeight;

    public FloatWindowView(Context context) {
        this(context, null);
    }

    public FloatWindowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FloatWindowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.float_window, this);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mWmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mWmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            mWmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        mWmParams.format = PixelFormat.RGBA_8888;
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWmParams.gravity = Gravity.START | Gravity.TOP;
        mWmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWmParams.x = 0;
        mWmParams.y = 0;

        mWindowManager.addView(this, mWmParams);

        final int mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        mScreenWidth = getScreenWidth(context);
        mScreenHeight = getScreenHeight(context);

        setOnTouchListener(new View.OnTouchListener() {
            int startX, startY;  //起始点
            boolean isPerformClick;  //是否点击
            int finalMoveX;  //最后通过动画将mView的X轴坐标移动到finalMoveX

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getX();
                        startY = (int) event.getY();
                        isPerformClick = true;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //判断是CLICK还是MOVE
                        //只要移动过，就认为不是点击
                        if (Math.abs(startX - event.getX()) >= mTouchSlop || Math.abs(startY - event.getY()) >= mTouchSlop) {
                            isPerformClick = false;
                        }
                        if (isPortrait()) {
                            mWmParams.x = (int) (event.getRawX() - startX);
                            //这里修复了刚开始移动的时候，悬浮窗的y坐标是不正确的，要减去状态栏的高度，可以将这个去掉运行体验一下
                            mWmParams.y = (int) (event.getRawY() - startY - statusBarHeight);
                        } else {
                            mWmParams.x = (int) (event.getRawX() - startX - statusBarHeight);
                            mWmParams.y = (int) (event.getRawY() - startY);
                        }
                        updateCollectViewLayout();
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (isPerformClick) {
                            performClick();
                        }
                        //判断mView是在Window中的位置，以中间为界
                        if (mWmParams.x + getMeasuredWidth() / 2 >= mScreenWidth / 2) {
                            finalMoveX = mScreenWidth - getMeasuredWidth();
                        } else {
                            finalMoveX = 0;
                        }
//                        stickToSide(); // 吸边的效果
                        return !isPerformClick;
                }
                return false;
            }
        });
    }

    private void updateCollectViewLayout() {
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    private boolean isPortrait() {
        Configuration configuration = getResources().getConfiguration();
        int orientation = configuration.orientation;
        return Configuration.ORIENTATION_PORTRAIT == orientation;
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }

    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.heightPixels;
    }
}
