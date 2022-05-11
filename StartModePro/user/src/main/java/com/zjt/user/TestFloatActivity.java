package com.zjt.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zjt.router.RouteHub;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/8 4:34 下午
 * @Description : TestFloatActivity
 */


@Route(path = RouteHub.User.USER_FLOAT_ACTIVITY)
public class TestFloatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_float_layout);
    }
}
