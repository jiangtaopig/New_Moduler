package com.zjt.startmodepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.zjt.startmodepro.viewmodel.MyViewModel;
import com.zjt.startmodepro.viewmodel.NameViewModel;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/2
 */
class JetPack2Activity extends AppCompatActivity {

    private TextView mTv;
    private Button mButton;
    private ProgressBar mProgressBar;
    private NameViewModel mNameViewModel;
    private MyViewModel mMyViewModel;
    private Button mGetBtn;
    private Button mJump2JetPack3ActivityBtn;

    public static void enter(Context context) {
        Intent intent = new Intent(context, JetPack2Activity.class);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jetpack_2_layout);

        mTv = findViewById(R.id.txt_tv);
        mButton = findViewById(R.id.btn_refresh);
        mProgressBar = findViewById(R.id.progressbar);
        mGetBtn = findViewById(R.id.btn_data);

        mJump2JetPack3ActivityBtn = findViewById(R.id.btn_2_jetpack3Activity);

        mJump2JetPack3ActivityBtn.setOnClickListener(v -> {
            JetPack3Activity.Companion.enter(this);
//            Intent intent = new Intent(this, JetPack3Activity.class);
//            startActivity(intent);
//            MyKotlinManager kotlinManager = new MyKotlinManager("zhujiangtao");
//            kotlinManager.showTops();
        });

        mNameViewModel = ViewModelManager.getInstance().getNameModel(this);
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("zjt", "s = " + s);
                mTv.setText(s);
            }
        };

        mNameViewModel.getCurrentName().observe(this, observer);

        mButton.setOnClickListener(v -> {
            mNameViewModel.getCurrentName().postValue("xxxx");
        });

        mMyViewModel = ViewModelManager.getInstance().getMyViewModel(this);
        mMyViewModel.getLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mProgressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });

        mMyViewModel.getUserLiveData().observe(this, s -> mTv.setText(s));

        mGetBtn.setOnClickListener(v -> {
            mMyViewModel.getUserInfo();
        });

    }
}
