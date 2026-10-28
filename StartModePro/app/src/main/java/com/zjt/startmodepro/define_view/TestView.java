package com.zjt.startmodepro.define_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.zjt.startmodepro.R;


/**
 * 参考文章： https://juejin.cn/post/6844903478762930183
 * Created by ${za.zhu.jiangtao}
 * on 2019/12/16
 */
public class TestView extends View {



    private int mTextSize;
    private String mText;

    private Paint mPaint;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TestView);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.TestView_android_textSize, 22);
        mText = typedArray.getString(R.styleable.TestView_android_text);

        typedArray.recycle(); //一定要回收掉

        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(getResources().getColor(R.color.color_46d286));
        mPaint.setTextSize(mTextSize);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = getPaddingLeft();
        int cy = (int) (getHeight() / 2 + Math.abs(mPaint.ascent() + mPaint.descent()) / 2);

        canvas.drawColor(Color.RED);
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        canvas.drawText(mText, cx, cy, mPaint);  //cy 指的是绘制字符串的 baseline 线

        Log.e("xxx", "mText = "+mText +" , cx = " + cx + ", cy = " + cy);

        mPaint.setColor(getResources().getColor(R.color.color_333333));
        mPaint.setStrokeWidth(3);
        canvas.drawLine(getPaddingLeft(), (float) getHeight() / 2, (float) getWidth() - getPaddingRight(), (float) getHeight() / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 这里的 widthMeasureSpec 是 TestView 的父布局 MeasureSpec 和 TestView 本身的 LayoutParams (精确值，如20dp/match_parent/wrap_content) 测量后传进来的.
         * ViewGroup 中的 measureChildren(int widthMeasureSpec, int heightMeasureSpec)
         * -> measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec)
         * -> child.measure(childWidthMeasureSpec, childHeightMeasureSpec) 再到 View 的 onMeasure(widthMeasureSpec, heightMeasureSpec)
         *
         *
         * 所以，传到这里的 onMeasure(int widthMeasureSpec, int heightMeasureSpec) 都是已经确定的了。
         * 为啥当 我们设置 TestView 的宽高为 wrap_content 时，需要重新计算 TestView 的真实宽高呢？
         *
         * 是因为，在 ViewGroup 测量子 View 的过程中，getChildMeasureSpec 的方法如下：
         *        switch (specMode) {
         *             // Parent has imposed an exact size on us
         *             case MeasureSpec.EXACTLY:
         *                 if (childDimension >= 0) {
         *                     resultSize = childDimension;
         *                     resultMode = MeasureSpec.EXACTLY;
         *                 } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
         *                     resultSize = size;
         *                     resultMode = MeasureSpec.EXACTLY;
         *                 } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
         *                     resultSize = size;
         *                     resultMode = MeasureSpec.AT_MOST;
         *                 }
         *                 break;
         *
         *             // Parent has imposed a maximum size on us
         *             case MeasureSpec.AT_MOST:
         *                 if (childDimension >= 0) {
         *                     resultSize = childDimension;
         *                     resultMode = MeasureSpec.EXACTLY;
         *                 } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
         *
         *                     resultSize = size;
         *                     resultMode = MeasureSpec.AT_MOST;
         *                 } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
         *                     resultSize = size;
         *                     resultMode = MeasureSpec.AT_MOST;
         *                 }
         *                 break;
         *
         *             // Parent asked to see how big we want to be
         *             case MeasureSpec.UNSPECIFIED:
         *                 if (childDimension >= 0) {
         *                     // Child wants a specific size... let them have it
         *                     resultSize = childDimension;
         *                     resultMode = MeasureSpec.EXACTLY;
         *                 } else if (childDimension == ViewGroup.LayoutParams.MATCH_PARENT) {
         *                     // Child wants to be our size... find out how big it should
         *                     // be
         *                     resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
         *                     resultMode = MeasureSpec.UNSPECIFIED;
         *                 } else if (childDimension == ViewGroup.LayoutParams.WRAP_CONTENT) {
         *                     // Child wants to determine its own size.... find out how
         *                     // big it should be
         *                     resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
         *                     resultMode = MeasureSpec.UNSPECIFIED;
         *                 }
         *                 break;
         *         }
         *
         *         当 父布局的 MeasureSpec 为 AT_MOST 或 EXACTLY 的时候，子 View 的  LayoutParams 是 wrap_content 的时候，
         *         给 子 View MeasureSpec 设置为 AT_MOST， 宽度为父布局宽度的剩余大小，高度也是这样
         */



        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int maxWidth, maxHeight;
        int resultW = width;
        int resultH = height;

        Log.e("TestView", "onMeasure : >>> widthMode = "+widthMode+" , width = "+width + " , height= " +height
                +" , MeasureSpec.AT_MOST = " + MeasureSpec.AT_MOST);

        if (widthMode == MeasureSpec.AT_MOST) {
            maxWidth = (int) (getPaddingLeft() + getPaddingRight() + mPaint.measureText(mText));
            resultW = Math.min(maxWidth, width);
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            maxHeight = getPaddingBottom() + getPaddingTop() + mTextSize;
            resultH = Math.min(maxHeight, height);
        }
        resultW += getPaddingLeft() + getPaddingRight();
        resultH += getPaddingBottom() + getPaddingTop();
        Log.e("TestView", "onMeasure : >>> resultW = " + resultW + " , resultH = " + resultH + " , pLeft = " + getPaddingLeft() + " , pRight = " + getPaddingRight());
        setMeasuredDimension(resultW, resultH);
    }
}
