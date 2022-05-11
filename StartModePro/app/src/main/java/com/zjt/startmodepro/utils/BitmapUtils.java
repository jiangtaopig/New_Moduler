package com.zjt.startmodepro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


/**
 * @Author : zhujiangtao01
 * @Time : On 2021/3/29 6:20 下午
 * @Description : BitmapUtils
 */


public class BitmapUtils {

    /**
     * 等比例缩放
     *
     * @param bitmap
     * @param w      想要缩放后的 宽度 比如 给定ImageView 设置了精确的 宽/高
     * @param h      想要缩放后的 高度
     * @return
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, float w, float h) {
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        float x = 0, y = 0, scaleWidth = width, scaleHeight = height;
        Bitmap newbmp;
        Log.e("BitmapUtils", "width:" + width + " height:" + height);
        if (w > h) {//比例宽度大于高度的情况
            float scale = w / h;
            float tempH = width / scale;
            if (height > tempH) {//
                x = 0;
                y = (height - tempH) / 2;
                scaleWidth = width;
                scaleHeight = tempH;
            } else {
                scaleWidth = height * scale;
                x = (width - scaleWidth) / 2;
                y = 0;
            }
            Log.e("BitmapUtils", "scale:" + scale + " scaleWidth:" + scaleWidth + " scaleHeight:" + scaleHeight);
        } else if (w < h) {//比例宽度小于高度的情况
            float scale = h / w;
            float tempW = height / scale;
            if (width > tempW) {
                y = 0;
                x = (width - tempW) / 2;
                scaleWidth = tempW;
                scaleHeight = height;
            } else {
                scaleHeight = width * scale;
                y = (height - scaleHeight) / 2;
                x = 0;
                scaleWidth = width;
            }

        } else {//比例宽高相等的情况
            if (width > height) {
                x = (width - height) / 2;
                y = 0;
                scaleHeight = height;
                scaleWidth = height;
            } else {
                y = (height - width) / 2;
                x = 0;
                scaleHeight = width;
                scaleWidth = width;
            }
        }
        try {
            // createBitmap() 方法中定义的参数 x + width 要小于或等于 bitmap.getWidth()，y + height 要小于或等于 bitmap.getHeight()
            newbmp = Bitmap.createBitmap(bitmap, (int) x, (int) y, (int) scaleWidth, (int) scaleHeight, null, false);
            //bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return newbmp;
    }

    /**
     * 采样率压缩
     * @param w      图片容器如 ImageView 的宽度
     * @param h      图片容器如 ImageView 的高度
     * @return
     */
    public static Bitmap bitmapInSampleSize(Context context, int resId, int w, int h) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置 inJustDecodeBounds 为 true 后 decodeResource 方法只会去读取 Bitmap 的宽高属性而不会去进行实际加载
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, options);
        int inSampleSize = calculateInSampleSize(options, w, h);
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(context.getResources(), resId, options);

    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
