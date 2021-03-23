package com.zjt.startmodepro.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zjt.startmodepro.utils.DisplayUtil;


/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2020/8/27
 */
public class BitmapView extends View {

    private static final int MAX_SIZE = DisplayUtil.dip2px(200);
    private static final int ITEM_SIZE = DisplayUtil.dip2px(50);
    private int radius = DisplayUtil.dip2px(20);

    private Paint mPaint;

    public BitmapView(Context context) {
        this(context, null);
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int lastW = width;
        int lastH = height;
        if (widMode == MeasureSpec.AT_MOST) {
            lastW = MAX_SIZE;
        }

        if (hMode == MeasureSpec.AT_MOST) {
            lastH = MAX_SIZE;
        }
        setMeasuredDimension(lastW, lastH);
    }

    private Bitmap makeSrc(int w, int h, int color) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
//        c.drawRect(0, 0, w, h, p);
        c.drawCircle(radius, radius, radius, p);
        return bm;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bm = makeSrc(ITEM_SIZE, ITEM_SIZE, Color.RED);
        canvas.drawBitmap(bm, 0, 0, mPaint);
        Bitmap bm2 = makeSrc(ITEM_SIZE, ITEM_SIZE, Color.BLUE);
        canvas.drawBitmap(bm2, ITEM_SIZE, 0, null);
    }
}
