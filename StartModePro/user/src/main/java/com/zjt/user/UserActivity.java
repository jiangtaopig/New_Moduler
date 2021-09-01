package com.zjt.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zjt.base.BaseActivity;
import com.zjt.router.RouteHub;
import com.zjt.user.viewmodel.MeViewModel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/3/4
 */

@Route(path = RouteHub.User.USER_MAIN_PATH)
public class UserActivity extends BaseActivity {

    private static final String KT_FRAGMENT_TAG = "kt_fragment";
    private static final String FLOAT_FRAGMENT_TAG = "float_fragment";


    private TextView mMeTxt;
    private TextView mSettingTxt;

    private Fragment mKtFragment;
    private Fragment mSettingFragment;
    private Fragment mCurFragment;

    private MeViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_layout);

        mMeTxt = findViewById(R.id.txt_me);
        mSettingTxt = findViewById(R.id.txt_setting);

//        mKtFragment = new KtFragment();
//        switchFragment(mKtFragment);

        mMeTxt.setOnClickListener(v -> {
//            switchFragment(mKtFragment);
            showKtFragment();
        });


        mSettingTxt.setOnClickListener(v -> {
//            mSettingFragment = new MyFloatFragment();
//            switchFragment(mSettingFragment);
            showFloatFragment();
        });


        viewModel = new ViewModelProvider(this).get(MeViewModel.class);
        viewModel.getMData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("UserActivity","s = "+s);
            }
        });

        viewModel.getJumpToFloatFragment().observe(this, aBoolean -> {
            if (aBoolean) {
                showFloatFragment();
            }
        });

        showKtFragment();
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            if (mCurFragment != null)
                transaction.hide(mCurFragment);
            transaction.add(R.id.fragment_container, targetFragment)
                    .commit();
        } else {
            transaction.hide(mCurFragment)
                    .show(targetFragment)
                    .commit();
        }
        mCurFragment = targetFragment;
    }

    private void showKtFragment() {
        Fragment preViewFragment = getSupportFragmentManager().findFragmentByTag(FLOAT_FRAGMENT_TAG);
        if (preViewFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(preViewFragment).commitAllowingStateLoss();
        }
        if (mKtFragment == null) {
            mKtFragment = new KtFragment();
        }
        if (mKtFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().show(mKtFragment)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mKtFragment, KT_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
        }
    }

    private void showFloatFragment(){
        Fragment preViewFragment = getSupportFragmentManager().findFragmentByTag(KT_FRAGMENT_TAG);
        if (preViewFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(preViewFragment).commitAllowingStateLoss();
        }

        if (mSettingFragment == null) {
            mSettingFragment = new MyFloatFragment();
        }
        if (mSettingFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().show(mSettingFragment)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mSettingFragment, FLOAT_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(this, "xxxx", Toast.LENGTH_LONG).show();
        finish();
    }
}
