package com.zjt.user;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zjt.base.BaseActivity;
import com.zjt.router.RouteHub;
import com.zjt.user.viewmodel.MeViewModel;

import java.util.List;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        findViewById(R.id.txt_get_all_fragments)
                .setOnClickListener(v -> {
                    List<Fragment> fragments = getSupportFragmentManager().getFragments();
                    fragments.size();
                });


        viewModel = new ViewModelProvider(this).get(MeViewModel.class);

        viewModel.getTestVal().setValue(1);

        viewModel.getTestVal().observe(this, integer -> {
            Toast.makeText(this, "xxxx", Toast.LENGTH_LONG).show();
        });

//        viewModel.getMData().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                Log.e("UserActivity", "s = " + s);
//            }
//        });
//
//        viewModel.getJumpToFloatFragment().observe(this, aBoolean -> {
//            if (aBoolean) {
//                showFloatFragment();
//            }
//        });

        showKtFragment();
//        FragmentHelper.showKtFragment(this);
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
            mKtFragment = KtFragment.Companion.getInstance(() -> {
                Log.e("xxxxxx", "---- onDestroy-------");
            });
        }
        Log.e("zzzz", "showKtFragment mKtFragment = " + mKtFragment + ", mKtFragment.isAdded() >> " + mKtFragment.isAdded()
                + " , findFragmentByTag = " + getSupportFragmentManager().findFragmentByTag(KT_FRAGMENT_TAG));
        if (!mKtFragment.isAdded() && getSupportFragmentManager().findFragmentByTag(KT_FRAGMENT_TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, mKtFragment, KT_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(mKtFragment)
                    .commitAllowingStateLoss();
        }
    }

    private void showFloatFragment() {
        Fragment preViewFragment = getSupportFragmentManager().findFragmentByTag(KT_FRAGMENT_TAG);
        if (preViewFragment != null) {
            getSupportFragmentManager().beginTransaction().hide(preViewFragment).commitAllowingStateLoss();
        }

        if (mSettingFragment == null) {
            mSettingFragment = new MyFloatFragment();
        }
        if (!mSettingFragment.isAdded() && getSupportFragmentManager().findFragmentByTag(FLOAT_FRAGMENT_TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(mSettingFragment)
                    .commitAllowingStateLoss();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, mSettingFragment, FLOAT_FRAGMENT_TAG)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(mSettingFragment)
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
