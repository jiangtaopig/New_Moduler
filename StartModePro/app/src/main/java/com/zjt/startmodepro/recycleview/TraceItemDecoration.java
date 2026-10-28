package com.zjt.startmodepro.recycleview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.zjt.startmodepro.R;

/**
 * 参考链接
 * https://blog.csdn.net/carson_ho/article/details/75004649
 */

public class TraceItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Bitmap mIcon;
    private static final int BOTTOM_MARGIN = 20;

    public TraceItemDecoration(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.tag);
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 其实RecyclerView 中的 ItemView 外面会包裹着一个矩形（outRect）,下面就可以设置 ItemView 距离  outRect 下面20px
        outRect.set(0, 0, 0, BOTTOM_MARGIN);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        /**
         * ItemDecoration 的 onDraw() 绘制会先于 ItemView 的onDraw()绘制，
         * 所以如果在 ItemDecoration 的 onDraw() 中绘制的内容在 ItemView 边界内，就会被 ItemView 遮挡住
         *
         * 比如上面的 getItemOffsets 设置的 outRect.set(0, 0, 0, 20);那么 这里的 onDraw 只能是 ItemView 下面与 outRect 之间的20px高度里面画，否则就会被遮挡
         */

        // 获取RecyclerView的 Child view （就是ItemView）的个数
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            // 获取每个Item的位置
            final View child = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(child);

            // 最后一条不用绘制分割线
            if (index == childCount -1) return;

            // 获取布局参数
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            // 设置矩形(分割线)的高度为 BOTTOM_MARGIN
            final int mDivider = BOTTOM_MARGIN;

            // 矩形左上顶点 = (ItemView的左边界,ItemView的下边界)
            // ItemView的左边界 = RecyclerView 的左边界 + paddingLeft距离 后的位置
            final int left = parent.getPaddingLeft();
            // ItemView的下边界：ItemView 的 bottom坐标 + 距离RecyclerView底部距离 +translationY
            Log.e("---- onDraw ----", "child Bottom : " +child.getBottom() + " , bottomMargin = " + params.bottomMargin
                    + " , translationY = " + Math.round(ViewCompat.getTranslationY(child))+" , child top = "+ child.getTop());
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));

            // 矩形右下顶点 = (ItemView的右边界,矩形的下边界)
            // ItemView的右边界 = RecyclerView 的右边界减去 paddingRight 后的坐标位置
            final int right = parent.getWidth() - parent.getPaddingRight();
            // 绘制分割线的下边界 = ItemView的下边界+分割线的高度
            final int bottom = top + mDivider;


            // 通过Canvas绘制矩形（分割线）
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        // 1. 即不需要考虑绘制内容被 ItemView 遮挡的问题，反而 ItemView 会被 onDrawOver绘制的内容遮挡,
        // 2. 绘制时机比较：
        // ItemDecoration.onDraw（）> ItemView.onDraw() > ItemDecoration.onDrawOver()

        // 获取RecyclerView的 Child view （就是ItemView）的个数
        int childCount = parent.getChildCount();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(48);
        for ( int i = 0; i < childCount; i++ ) {
            View view = parent.getChildAt(i);

            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            String text = "我是悬浮在 ItemView 上面的";
            int height = (int) (fontMetrics.bottom - fontMetrics.top);
            int textWidth = (int) paint.measureText(text);
            Log.e("--- onDrawOver --- ", "text height = " + height);

            final int left = (parent.getWidth() - mIcon.getWidth()) / 2;
            // ItemView的上边界
            float top = view.getTop();
            c.drawBitmap(mIcon, left, top, mPaint);
            c.drawText(text, (parent.getWidth() - textWidth)/2, view.getTop() + height, paint);
        }
    }
}
