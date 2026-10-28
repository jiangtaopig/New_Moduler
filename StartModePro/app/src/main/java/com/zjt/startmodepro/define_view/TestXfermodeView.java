package com.zjt.startmodepro.define_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zjt.startmodepro.R;
import com.zjt.startmodepro.utils.DisplayUtil;

public class TestXfermodeView extends View {

    private Paint paint1 ;
    private Bitmap mSrcBm;
    private Bitmap mDstBm;
    private Bitmap bm2;

    public TestXfermodeView(Context context) {
        super(context, null);
    }

    public TestXfermodeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestXfermodeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint1 = new Paint();
        paint1.setColor(Color.GRAY);
        mSrcBm = BitmapFactory.decodeResource(getResources(), R.drawable.guaguale, null);
        mDstBm = Bitmap.createBitmap(mSrcBm.getWidth(), mSrcBm.getHeight(), Bitmap.Config.ARGB_8888);//透明的

        bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.a2, null);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 139, 197, 186);
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        Bitmap dest = makeDst(200, 200);
        canvas.drawBitmap(dest, 0, 0, paint1);

        paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        Bitmap src = makeSrc(200, 200);
        canvas.drawBitmap(src, 100, 100, paint1);
        paint1.setXfermode(null);

        canvas.restoreToCount(layerId);
    }

    // create a bitmap with a circle, used for the "dst" image
    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0, 0, w, h), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFF0000);
        c.drawRect(0, 0, w, h, p);
        return bm;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int resultW = width;
        int resultH = height;

        if (heightMode == MeasureSpec.AT_MOST) {
            resultH = DisplayUtil.dip2px(300);
        }
        setMeasuredDimension(resultW, resultH);
    }
}
