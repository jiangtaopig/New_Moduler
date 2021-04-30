package com.zjt.startmodepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.zjt.startmodepro.lifecycle.MyObserver;
import com.zjt.startmodepro.viewmodel.NameViewModel;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/3/1
 */
public class JetPackActivity extends AppCompatActivity {

    private TextView mTitleTv;
    private Button mRefreshDataBtn;
    private Button mJumpBtn;

    private NameViewModel mNameViewModel;
    private Observer<String> mNameObserver;

    public static void enter(Context context) {
        Intent intent = new Intent(context, JetPackActivity.class);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jetpack_layout);

        mTitleTv = findViewById(R.id.txt_title);
        mRefreshDataBtn = findViewById(R.id.btn_refresh_data);
        mJumpBtn = findViewById(R.id.btn_jump);

        mNameViewModel = new ViewModelProvider(this).get(NameViewModel.class);
        mNameObserver = s -> mTitleTv.setText(s);

        // 数据转换
        Transformations.map(mNameViewModel.getCurrentName(), input -> {
            Log.e("zjt", "input = " + input);
            return input + " zcy is a good boy";
        }).observe(this, mNameObserver);

        // LifeCycle 的使用
        getLifecycle().addObserver(new MyObserver());

        mRefreshDataBtn.setOnClickListener(v -> {
            mNameViewModel.getCurrentName().postValue("zhujiangtao");
        });

        mJumpBtn.setOnClickListener(v -> {
            JetPack2Activity.enter(this);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mNameViewModel.getCurrentName().removeObserver(mNameObserver);
    }
}
