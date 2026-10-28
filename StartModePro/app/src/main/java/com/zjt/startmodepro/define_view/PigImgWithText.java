package com.zjt.startmodepro.define_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.zjt.startmodepro.R;
import com.zjt.startmodepro.utils.DisplayUtil;

import kotlin.jvm.internal.PropertyReference0Impl;

public class PigImgWithText extends View {

    private final static String TAG = "PigImgWithText";

    private final static int ICON_IN_LEFT = 0;
    private final static int ICON_IN_CENTER = 1;
    private final static int ICON_IN_RIGHT = 2;

    private String mText;
    private int mTextColor;
    private int mTextBackgroundColor;
    private float mTextSize;

    private Paint mPaint;


    private Paint mTextBackgroundPaint;

    // 图标
    private Bitmap mIcon;

    private int mTextHeight;

    // icon 的 top margin
    private int ICON_TOP_MARGIN ;

    // icon 的位置 0 居左， 1 居中， 2 靠右
    private int mIconPosition;

    public PigImgWithText(Context context) {
        this(context, null);
    }

    public PigImgWithText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PigImgWithText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PigImgWithText, defStyleAttr, 0);
        mText = array.getString(R.styleable.PigImgWithText_pig_text);
        mTextSize = array.getDimension(R.styleable.PigImgWithText_pig_text_size, 22f);
        mTextColor = array.getColor(R.styleable.PigImgWithText_pig_text_color, context.getResources().getColor(R.color.color_e93939));
        mTextBackgroundColor = array.getColor(R.styleable.PigImgWithText_pig_text_background_color, context.getResources().getColor(R.color.color_e93939));
        ICON_TOP_MARGIN = (int) array.getDimension(R.styleable.PigImgWithText_pig_icon_top_margin, 10f);
        // 获取自定义 View 属性的枚举值
        mIconPosition = array.getInt(R.styleable.PigImgWithText_pig_position, 0);

        mIcon = BitmapFactory.decodeResource(getResources(), array.getResourceId(R.styleable.PigImgWithText_pig_imgSrc, R.drawable.a2));
        Log.d("PigImgWithText", "mText >>> " + mText + " , ICON_TOP_MARGIN = " + ICON_TOP_MARGIN + " , mIconPosition = " +mIconPosition);

        array.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

        mTextBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextBackgroundPaint.setColor(mTextBackgroundColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 测量模式和宽高
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int maxWidth, maxHeight;
        int resultW = width, resultH = height;

        mTextHeight = (int) (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent);

        int iconWidth = mIcon.getWidth();
        int iconHeight = mIcon.getHeight();

        Log.e(TAG, "onMeasure ------ >>> widthMode = " + widthMode + " , width = " + width + " , height = " + height +
                " , mTextHeight = " + mTextHeight
                + " , iconWidth = " + iconWidth + " , iconHeight = " + iconHeight);

        Log.e(TAG, "onMeasure   ---------------- getWidth = " + getWidth() + " , getHeight = " + getHeight());

        if (widthMode == MeasureSpec.AT_MOST) {
            int textMaxWidth = (int) (getPaddingLeft() + getPaddingRight() + mPaint.measureText(mText));
            int iconMaxWidth = iconWidth + getPaddingLeft() + getPaddingRight();
            maxWidth = Math.max(textMaxWidth, iconMaxWidth); // 取文字和图片的最大宽度
            resultW = Math.min(maxWidth, width);
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            maxHeight = getPaddingBottom() + getPaddingTop() + mTextHeight + iconHeight + ICON_TOP_MARGIN + 1;
            resultH = Math.min(maxHeight, height);
        }

        setMeasuredDimension(resultW, resultH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = getPaddingLeft();
        int startY = (int) (mTextHeight / 2 + Math.abs(mPaint.ascent() + mPaint.descent()) / 2);

        // 绘制 text 的背景
        canvas.drawRect(0, 0, getWidth(), mTextHeight, mTextBackgroundPaint);

        canvas.drawText(mText, startX, startY, mPaint);

        // 绘制 icon 的横坐标起点
        int x ;

        switch (mIconPosition) {
            case ICON_IN_CENTER:
                x = (getWidth() - getPaddingLeft() - getPaddingRight() -mIcon.getWidth())/2;
                break;
            case ICON_IN_RIGHT:
                x = getWidth() - getPaddingRight() - mIcon.getWidth();
                break;
            default:
                x = getPaddingLeft();
        }
        canvas.drawBitmap(mIcon, x, mTextHeight + ICON_TOP_MARGIN, mPaint);
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        Log.e(TAG, " ----------xxx layout getWidth = " + getWidth() + " , getHeight = " + getHeight()
                + " , l = "+l+" , t = " + t + " , r = "+r + " , b = " + b);
        super.layout(l, t, r, b);
        Log.e(TAG, " ------------------ layout   ---------------- getWidth = " + getWidth() + " , getHeight = " + getHeight());
    }
}
