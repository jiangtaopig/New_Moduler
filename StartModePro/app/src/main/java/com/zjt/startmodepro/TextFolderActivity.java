package com.zjt.startmodepro;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TextFolderActivity extends AppCompatActivity {

    private final static int LIMIT_LINE = 3;
    private final static int MAX_LINE = 99;

    private TextView tv;
    private TextView tv_close;
    private TextView tv_open;
    private RelativeLayout rl_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_folder_layout);
        tv = findViewById(R.id.tv);
        tv_close = findViewById(R.id.tv_close);
        tv_open = findViewById(R.id.tv_open);
        rl_click = findViewById(R.id.rl_click);

        int lineHeight = tv.getLineHeight();
        int measuredHeight = tv.getMeasuredHeight();
        int allHeight = measureTextViewHeight(tv.getText().toString(), 15, measuredHeight);

        DisplayMetrics dm = new DisplayMetrics();
        dm = getApplicationContext().getResources().getDisplayMetrics();
        float screenW = dm.widthPixels;
        float paddingLeft = tv.getPaddingLeft();
        float paddingReft = tv.getPaddingRight();

        int count = (int) Math.ceil((tv.getPaint().measureText(tv.getText().toString()) / (screenW - paddingLeft - paddingReft - 159)));
        // 计算行数
        if (allHeight % lineHeight > 0) {
            count = allHeight / lineHeight + 1;
        } else {
            count = allHeight / lineHeight;
        }

        if (count > LIMIT_LINE) {
            tv.setMaxLines(LIMIT_LINE);
            tv_close.setVisibility(View.GONE);
            tv_open.setVisibility(View.VISIBLE);
        } else {
            tv.setMaxLines(MAX_LINE);
            tv_open.setVisibility(View.GONE);
            tv_close.setVisibility(View.GONE);
        }

        rl_click.setOnClickListener(v -> {
            if (tv_open.getVisibility() == View.VISIBLE) {
                tv.setMaxLines(MAX_LINE);
                tv_open.setVisibility(View.GONE);
                tv_close.setVisibility(View.VISIBLE);
            } else {
                tv.setMaxLines(LIMIT_LINE);
                tv_close.setVisibility(View.GONE);
                tv_open.setVisibility(View.VISIBLE);
            }
        });
    }

    // 计算TextView的高度
    private int measureTextViewHeight(String text, int textSize, int deviceWidth) {
        TextView textView = new TextView(TextFolderActivity.this);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth,
                View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }
}
