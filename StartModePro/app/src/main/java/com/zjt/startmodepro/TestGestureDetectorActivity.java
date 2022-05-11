package com.zjt.startmodepro;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 手势的 activity
 */
public class TestGestureDetectorActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, View.OnTouchListener {

    private View mGestureView;
    private ViewGroup rootLayout;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gesture_detector_layout);

        mGestureView = findViewById(R.id.view_gesture);
        rootLayout = findViewById(R.id.root_layout);
        rootLayout.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
//        mGestureView.setVisibility(View.INVISIBLE);

//        mGestureView.setOnClickListener(v -> anim());

//        mGestureView.setX(300);

    }

    private void enterAnim() {
        Animation enterAnimation =
                AnimationUtils.loadAnimation(this, R.anim.anim_blink_live_capture_focus_enter_in);
        mGestureView.startAnimation(enterAnimation);
    }

    private void anim() {
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mGestureView, "scaleY", 1f, 0.8f);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mGestureView, "scaleX", 1f, 0.8f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorX, animatorY);
        set.setDuration(600);
        set.start();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        // 必须要返回 true
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        mGestureView.setVisibility(View.VISIBLE);
        int rectWidth = mGestureView.getWidth();
        int rectHalfWidth = rectWidth / 2;
        float x = e.getX();
        float y = e.getY();
        //超出区域,重新计算x,y坐标
        if (x - rectHalfWidth < 0 || x + rectHalfWidth > rootLayout.getWidth() || y - rectHalfWidth < 0 || y + rectHalfWidth > rootLayout.getHeight()) {
            if (x - rectHalfWidth < 0) {
                x = rectHalfWidth;
            } else if (x + rectHalfWidth > rootLayout.getWidth()) {
                x = rootLayout.getWidth() - rectHalfWidth;
            }
            if (y - rectHalfWidth < 0) {
                y = rectHalfWidth;
            } else if (y + rectHalfWidth > rootLayout.getHeight()) {
                y = rootLayout.getHeight() - rectHalfWidth;
            }
        }
        mGestureView.setX(x - rectHalfWidth);
        mGestureView.setY(y - rectHalfWidth);

        anim();

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float x1 = e1.getX();
        float y1 = e1.getY();

        float x2 = e2.getX();
        float y2 = e2.getY();

        Log.e("test gesture", "onScroll x1 = " + x1 + ", x2 = " + x2 + ", y1 = " + y1 + ", y2 = " +
                y2 + " , distanceX = " + distanceX + " , distanceY = " + distanceY);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
}
