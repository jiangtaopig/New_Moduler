package com.zjt.startmodepro.pagerSnapHelper;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zjt.startmodepro.R;

public class STLView extends FrameLayout {
    private ImageView mImgArrow;
    private TextView mTxtHint;
    private boolean mIsPullStatus = true;

    public STLView(Context context) {
        this(context, null);
    }

    public STLView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public STLView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.hotel_view_stl, this, true);
        mImgArrow = findViewById(R.id.img_arrow);
        mTxtHint = findViewById(R.id.txt_hint);
        bindHint();
    }

    public void setPullStatus(boolean isPullStatus) {
        if(mIsPullStatus == isPullStatus) {
            return;
        }
        mIsPullStatus = isPullStatus;
        bindHint();
        animArrow();
    }

    private void bindHint() {
        if(mIsPullStatus) {
            mTxtHint.setText("查看更多");

        } else {
            mTxtHint.setText("释放进相册");
        }
    }

    private void animArrow() {
        if(mIsPullStatus) {
            ObjectAnimator.ofFloat(mImgArrow, "rotation", 180f, 00f)
                    .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                    .start();
        } else {
            ObjectAnimator.ofFloat(mImgArrow, "rotation", 0f, 180f)
                    .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                    .start();
        }
    }
}
