package com.zjt.startmodepro.define_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.zjt.startmodepro.R;
import com.zjt.startmodepro.utils.DisplayUtil;


/**
 *
 * 参考文章： https://blog.csdn.net/iispring/article/details/50472485
 *
 * Creaeted by ${za.zhu.jiangtao}
 * on 2020/4/10
 * 刮刮乐
 */
public class GuaguaLeView extends View {

    private final static String TAG = "GuaguaLeView";

    private Bitmap mDstBm;
    private Bitmap mSrcBm;
    private Bitmap mTextBm;
    private Paint mPaint;
    private Path mPath;
    private float mPreX;
    private float mPreY;

    private Canvas traceCanvas;

    public GuaguaLeView(Context context) {
        this(context, null);
    }

    public GuaguaLeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuaguaLeView(Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(45);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();

        mSrcBm = BitmapFactory.decodeResource(getResources(), R.drawable.guaguale, null);
        mDstBm = Bitmap.createBitmap(mSrcBm.getWidth(), mSrcBm.getHeight(), Bitmap.Config.ARGB_8888);//透明的
        mTextBm = makeWinnerBm(mSrcBm.getWidth(), mSrcBm.getHeight());

        traceCanvas = buildTraceCanvas();
    }

    private Bitmap makeWinnerBm(int w, int h) {
        Log.e("GuaguaLeView", "makeWinnerBm w = " + w + ", h = " + h);
        //首先，新建一个空白的bitmap，这个空白bitmap上每个像素的值都是0x00000000；即全透明
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //然后在bitmap上绘图
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(getResources().getColor(R.color.color_46d286));
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0xffffcc44);
        paint.setTextSize(DisplayUtil.dip2px(20));
        String val = "我中奖了";
        int width = (int) paint.measureText(val);
        int cx = (w - width) / 2;
        canvas.drawText(val, cx, h / 2, paint); //只是绘制在 新建的 bitmap 上，要显示出来的话，还得在 onDraw 中 调用 canvas.drawBitmap(mTextBm, 0, 0, mPaint);
        return bitmap;
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

    private Canvas buildTraceCanvas() {
        Canvas c = new Canvas(mDstBm);
        return c;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mTextBm, 0, 0, mPaint);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //先把手指轨迹画到目标Bitmap上
//        Canvas c = new Canvas(mDstBm);
        traceCanvas.drawPath(mPath, mPaint);
        //然后把目标图像画到画布上
        canvas.drawBitmap(mDstBm, 0, 0, mPaint);
        //计算源图像区域
        //简单来讲Mode.SRC_OUT模式，当目标图像有图像时计算结果为空白像素，当目标图像没有图像时，显示源图像
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(mSrcBm, 0, 0, mPaint);
        // 调用 Paint.setXfermode(null) 将画笔去除 Xfermode
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreX = event.getX();
                mPreY = event.getY();
                Log.e(TAG, "onTouchEvent ACTION_DOWN >>> : mPreX = " + mPreX + " , mPreY = " + mPreY);
                mPath.moveTo(mPreX, mPreY);
                return true;//return true 之后的 move 和 up 才能执行

            case MotionEvent.ACTION_MOVE:
                //画的线不平滑
//                mPath.lineTo(event.getX(), event.getY());
                //二阶贝塞尔曲线 更顺滑
                float endX = (mPreX + event.getX()) / 2;
                float endY = (mPreY + event.getY()) / 2;
                mPath.quadTo(mPreX, mPreY, endX, endY);
                mPreX = event.getX();
                mPreY = event.getY();
                postInvalidate();
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
