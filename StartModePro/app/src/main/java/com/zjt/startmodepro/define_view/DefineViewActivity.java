package com.zjt.startmodepro.define_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Choreographer;
import android.view.View;

import androidx.annotation.Nullable;

import com.zjt.base.BaseActivity;
import com.zjt.startmodepro.R;

public class DefineViewActivity extends BaseActivity {

    public static void enter(Context context) {
        Intent intent = new Intent(context, DefineViewActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_define_view_layout);
//        setContentView(R.layout.activity_define_view_layout2);


//        Choreographer




//        getApplication().getSystemService()
//        startActivity(new Intent());

//        findViewById(R.id.tt_btn).setOnClickListener(v -> {
//          int a = 1;
//        });
    }
}
