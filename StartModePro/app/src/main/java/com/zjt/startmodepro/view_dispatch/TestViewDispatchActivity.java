package com.zjt.startmodepro.view_dispatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.zjt.base.BaseActivity;
import com.zjt.startmodepro.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by za-zhujiangtao on 2018/11/5.
 */

public class TestViewDispatchActivity extends BaseActivity {

    public static void enter(Context context) {
        Intent intent = new Intent(context, TestViewDispatchActivity.class);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    MyLinearLayout myLinearLayout;
    MyButton myButton;
    MyTextView myTextView;
    EditText editText;
    View view;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view_dispatch_layout);

        myLinearLayout = findViewById(R.id.my_layout);
        myButton = findViewById(R.id.my_btn);
        myTextView = findViewById(R.id.my_txt);
        editText = findViewById(R.id.edit_text);


        /**
         *
         * 问题：如果 我们在 MyLinearLayout 的 onInterceptTouchEvent 的 MOVE 事件中拦截了事件，
         * 那么下面设置的  myLinearLayout.setOnClickListener 点击事件能走到吗？
         *
         * 分析：
         *
         * 1. 由于在 MyLinearLayout 的 onInterceptTouchEvent 的 MOVE 事件中拦截了事件，那么就会走到 MyLinearLayout 的 onTouchEvent 中的 MOVE 和 UP 事件。
         * 2. 尽管执行了 UP 事件，下面设置的 myLinearLayout 点击事件任然不会执行到。 因为没有执行到 MyLinearLayout 的 onTouchEvent 中的 DOWN 事件；
         * 为什么呢？
         *
         * 在 View 的事件分发中可知，在 ACTION_DOWN 事件中 ，最终会调用 （mPrivateFlags |= PFLAG_PREPRESSED）或者 （mPrivateFlags |= PFLAG_PRESSED）设置为 press 状态，
         * 而点击事件是在 ACTION_UP 中执行的，有个前置条件就是判断是否处在 press 状态，即 (mPrivateFlags & PFLAG_PRESSED) != 0 或者（mPrivateFlags |= PFLAG_PREPRESSED）!= 0。
         * 所以，尽管在 MyLinearLayout 的 onInterceptTouchEvent 中拦截了 MOVE 事件，但是不会执行到 MyLinearLayout 的 ACTION_DOWN 事件，即不会设置 press 状态。
         *
         * 如果在 MyLinearLayout 的 onInterceptTouchEvent 中拦截了 DOWN 事件，那么点击事假就可以执行
         */
        myLinearLayout.setOnClickListener(v -> {
            Toast.makeText(this, "My Layout", Toast.LENGTH_LONG).show();
            Log.d("view_dispatch", "MyLinearLayout click!!!!");
        });

        myButton.setOnClickListener(v -> {
            Toast.makeText(this, "My Button", Toast.LENGTH_LONG).show();
            Log.d("view_dispatch", "MyButton click!!!!");
        });

//        myButton.setOnTouchListener((v, event) -> {
//            Log.e("view_dispatch", "onTouch");
//            return true;
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void testHash() {
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("Aa", "Aa");
        hashMap.put("BB", "BB");
        hashMap.put("AaAa", "AaAa");
        hashMap.put("AaBB", "AaBB");
        hashMap.put("BBAa", "BBAa");
        hashMap.put("BBBB", "BBBB");
        hashMap.put("AaAaAa", "AaAaAa");
        hashMap.put("AaAaBB", "AaAaBB");
        hashMap.put("AaBBAa", "AaBBAa");
        hashMap.put("AaBBBB", "AaBBBB");
        hashMap.put("BBAaAa", "BBAaAa");
        hashMap.put("BBAaBB", "BBAaBB");
        hashMap.put("BBBBAa", "BBBBAa");
        hashMap.put("BBBBBB", "BBBBBB");

    }

}
