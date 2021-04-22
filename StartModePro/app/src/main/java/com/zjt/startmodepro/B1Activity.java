package com.zjt.startmodepro;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2020/12/31
 */
public class B1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b1);

        initView();
    }


    private void initView() {
        Map<String, String> map = new HashMap<>(3);
    }
}
