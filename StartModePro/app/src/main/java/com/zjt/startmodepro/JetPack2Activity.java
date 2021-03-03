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

import com.zjt.startmodepro.viewmodel.NameViewModel;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/2
 */
class JetPack2Activity extends AppCompatActivity {

    private TextView mTv;
    private Button mButton;
    private NameViewModel mNameViewModel;

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
    }
}
