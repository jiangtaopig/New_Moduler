package com.zjt.startmodepro.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TestCanvas extends View {

    private Paint backPaint;
    private Paint linePaint;

    public TestCanvas(Context context) {
        this(context, null);
    }

    public TestCanvas(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestCanvas(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint.setColor(Color.GRAY);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(4);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int px = 500;
        int py = 500;
        canvas.drawRect(0, 0 , px, py, backPaint);
        canvas.save();
        canvas.translate(100, 100);
        // 都是在平移后的画布上处理
        backPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0 , 300, 300, backPaint);
        canvas.drawCircle(150, 150, 100, linePaint);
        canvas.restore();
        // 在平移之前的画布上处理
        canvas.drawCircle(50, 50, 50, linePaint);
    }


    private void testRotate(Canvas canvas) {
        int px = 500;
        int py = 500;
        canvas.drawRect(0, 0 , px, py, backPaint);
        canvas.save(); // 先保存之前的画布状态
        /**
         * 画布旋转90度，下面的操作到 restore 之前都是在旋转后的画布上
         * 默认的Android坐标系是， X 轴指向右方 Y 指向下方；旋转90度后，X 轴指向下 ，Y 轴指向左侧
         */
        canvas.rotate(90, px/2, py/2);
        canvas.drawLine(px/2, 0, 0, py/2, linePaint);
        canvas.drawLine(px/2, 0, px, py/2, linePaint);
        canvas.drawLine(px/2, 0, px/2, py, linePaint);
        canvas.drawRect(0, 0, 50, 50, linePaint);
        canvas.restore(); // 画布恢复到旋转之前
        canvas.drawRect(0, 0, 50, 50, linePaint);
    }
}
