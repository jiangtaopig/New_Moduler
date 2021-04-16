package com.zjt.user;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.zjt.base.DisplayUtils;


/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/8 2:31 下午
 * @Description : MyLinearLayout
 */


public class MyLinearLayout extends LinearLayout {

    private float mDownY = 0;
    private float mDownX = 0;
    private float mRawY = 0;
    private float mRawX = 0;
    private ViewGroup parent;
    private float mScreenWidth;
    private float mScreenHeight;

    public MyLinearLayout(Context context) {
        this(context, null);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.my_linearlayout, this, true);
        Point point = new Point();
        DisplayUtils.getDisplay(context, point);
        mScreenWidth = point.x;
        mScreenHeight = point.y;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                float pendingTranslationY = getY() + event.getY() - mDownY;
                float pendingTranslationX = getX() + event.getX() - mDownX;
                if (getParent() instanceof ViewGroup) {
                    parent = (ViewGroup) getParent();
                }

                Log.e("xxxxxxx", "getX() = " + getX() + ", event.getX() = " + event.getX()
                        + " , mDownX = " + mDownX+ ",pendingTranslationX = " + pendingTranslationX+", getWidth() = "+getWidth()+", mScreenWidth = "+mScreenWidth);
//                Log.e("xxxxxxx", "getY() = " + getY() + ", event.getY() = " + event.getY()
//                        + " , mDownY = " + mDownY + ",pendingTranslationY = " + pendingTranslationY+", getHeight() = "+getHeight()+", parent.getBottom() = "+parent.getBottom());
                if (pendingTranslationY < 0)
                    setTranslationY(0);
                else if ((pendingTranslationY + getHeight()) >= parent.getBottom()) {
                    setTranslationY(parent.getBottom() - getHeight());
                } else {
                    setTranslationY(pendingTranslationY);
                }

                if (pendingTranslationX < 0){
                    setTranslationX(0);
                } else if ((pendingTranslationX + getWidth()) >= mScreenWidth) {
                    setTranslationX(mScreenWidth - getWidth());
                }else {
                    setTranslationX(pendingTranslationX);
                }
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
