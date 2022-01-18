package com.zjt.startmodepro.reinstance;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.zjt.startmodepro.R;

import java.lang.ref.PhantomReference;

/**
 * 测试修改配置导致的销毁重建，比如在设置中将相机权限改为禁止，本例子的 onCreate 将请求相机权限
 */
public class TestReInstanceActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "TestReInstanceActivity";

    private Fragment mReInstanceFragment;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reinstance_layout);

        Log.e(ReInstanceFragment.TAG, "activity onCreate  " + this + " , savedInstanceState = " + savedInstanceState);

        showReInstanceFragment();
        requestCamera();

        TextView remove = findViewById(R.id.txt_remove);
        remove.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mReInstanceFragment)
                    .commitAllowingStateLoss();
        });

        TextView hide = findViewById(R.id.txt_hide);
        hide.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(mReInstanceFragment)
                    .commitAllowingStateLoss();
        });

        TextView show = findViewById(R.id.txt_show);
        show.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(mReInstanceFragment)
                    .commitAllowingStateLoss();
        });

        TextView replace = findViewById(R.id.txt_replace);
        replace.setOnClickListener(v -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ReInstanceFragment(), FRAGMENT_TAG)
                    .commitAllowingStateLoss();
        });
    }

    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    private void showReInstanceFragment() {
        if (mReInstanceFragment == null) {
            mReInstanceFragment = new ReInstanceFragment();
        }

        Fragment preFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);

        Log.e(ReInstanceFragment.TAG, "activity mReInstanceFragment >> " + mReInstanceFragment + " , preFragment = " + preFragment);

        // 方式2 使用 remove 和 add
//        if (preFragment != null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .remove(preFragment)
//                    .commitAllowingStateLoss();
//        }
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.fragment_container, mReInstanceFragment, FRAGMENT_TAG)
//                .commitAllowingStateLoss();

        // 方式2 使用replace， 会将之前添加的fragment 销毁，即走 onDestroy ,然后再把要添加的 fragment添加上去
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mReInstanceFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(ReInstanceFragment.TAG, "activity onDestroy  " + this);
    }
}
