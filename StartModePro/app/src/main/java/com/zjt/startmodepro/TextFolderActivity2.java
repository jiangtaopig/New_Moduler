package com.zjt.startmodepro;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TextFolderActivity2 extends AppCompatActivity {

    private final static int LIMIT_LINE = 3; //TextView默认最大展示行数

    TextView descriptionView;
    ImageView expandImageView; //LinearLayout布局和ImageView

    private int VERTICAL_PADDING ; //  top 和 bottom padding 之和

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_folder_layout2);

        descriptionView =  findViewById(R.id.description_view);
        expandImageView = findViewById(R.id.expand_view);

        VERTICAL_PADDING = descriptionView.getPaddingTop() +  descriptionView.getPaddingBottom();

        //根据高度来判断是否需要再点击展开
        descriptionView.post(new Runnable() {

            @Override
            public void run() {
                int realLine = Math.min(descriptionView.getLineCount(), LIMIT_LINE);
                //descriptionView设置默认显示高度
                descriptionView.setHeight(descriptionView.getLineHeight() * realLine + VERTICAL_PADDING);
                expandImageView.setVisibility(descriptionView.getLineCount() > LIMIT_LINE ? View.VISIBLE : View.GONE);
            }
        });

        expandImageView.setOnClickListener(new View.OnClickListener() {
            boolean isExpand;//是否已展开的状态

            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                descriptionView.clearAnimation();//清楚动画效果
                final int deltaValue;//默认高度，即前边由maxLine确定的高度
                final int startValue = descriptionView.getHeight();//起始高度
                int durationMillis = 350;//动画持续时间
                if (isExpand) {
                    /**
                     * 折叠动画
                     * 从实际高度缩回起始高度
                     */
                    deltaValue = descriptionView.getLineHeight() * descriptionView.getLineCount() - startValue + VERTICAL_PADDING;
//                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    animation.setDuration(durationMillis);
//                    animation.setFillAfter(true);
//                    expandImageView.startAnimation(animation);
                } else {
                    /**
                     * 展开动画
                     * 从起始高度增长至实际高度
                     */
                    deltaValue = descriptionView.getLineHeight() * LIMIT_LINE - startValue + VERTICAL_PADDING;
//                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    animation.setDuration(durationMillis);
//                    animation.setFillAfter(true);
//                    expandImageView.startAnimation(animation);
                }
                Animation animation = new Animation() {
                    protected void applyTransformation(float interpolatedTime, Transformation t) { //根据ImageView旋转动画的百分比来显示textview高度，达到动画效果
                        descriptionView.setHeight((int) (startValue + deltaValue * interpolatedTime));
                    }
                };
                animation.setDuration(durationMillis);
                descriptionView.startAnimation(animation);
            }
        });
    }

}
