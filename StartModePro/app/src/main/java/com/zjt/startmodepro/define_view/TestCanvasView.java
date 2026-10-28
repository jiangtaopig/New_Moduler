package com.zjt.startmodepro.define_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TestCanvasView extends View {

    private Paint mPaint;

    public TestCanvasView(Context context) {
        this(context, null);
    }

    public TestCanvasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rect = new RectF(50, 50, 250, 200);
        canvas.drawRect(rect, mPaint);
        mPaint.setColor(Color.BLACK);

        // 绘制圆弧
        /**
         * 其实就是以 rect 的中心为原点，顺时针开始画。
         * startAngle = 0, sweepAngle = 360 其实就是在 rect 中画了一个椭圆
         * userCenter 为 false 的话，不会连接原点，只是连接起点和终点所对应的弧线
         */
        canvas.drawArc(rect, 0, 270, true, mPaint);

        int startY = (int) (50+(rect.bottom-rect.top)/2);
        mPaint.setColor(Color.WHITE);

        canvas.drawLine(50, startY, rect.right, startY, mPaint);

        int x = (int) (50 + (rect.right - rect.left)/2);
        canvas.drawLine(x, 50, x , 50 + rect.bottom - rect.top, mPaint );

        // 平移画布，此时画布的原点是(250, 200)
        canvas.translate(250, 200);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3);

        canvas.drawLine(0, 0, getWidth(), 0, mPaint);
        canvas.drawLine(0, 0, 0, getHeight(), mPaint);

        // 再次平移画布，此时不是相对于屏幕的左上角(0,0)的位置，而是相对之前已经移动的位置(250.200)
        canvas.translate(30, 30);

        mPaint.setColor(Color.WHITE);
        canvas.drawLine(0, 0, getWidth(), 0, mPaint);
        canvas.drawLine(0, 0, 0, getHeight(), mPaint);



    }
}
