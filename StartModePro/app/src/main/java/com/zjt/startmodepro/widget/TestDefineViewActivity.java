package com.zjt.startmodepro.widget;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zjt.startmodepro.R;

public class TestDefineViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_define_view_layout);

        TextView tv = findViewById(R.id.txt_change_size);
        tv.setOnClickListener(v -> {
            Log.e("xxxx", "originW = " + tv.getWidth() + ", originH = " + tv.getHeight());
            tv.setWidth(400);
            tv.setHeight(200);
//            tv.requestLayout();
        });
    }
}
