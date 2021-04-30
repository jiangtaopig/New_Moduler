package com.zjt.user;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.zjt.base.BaseActivity;
import com.zjt.router.RouteHub;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/3/4
 */

@Route(path = RouteHub.User.USER_MAIN_PATH)
public class UserActivity extends BaseActivity {

    private TextView mMeTxt;
    private TextView mSettingTxt;

    private Fragment mMeFragment;
    private Fragment mSettingFragment;
    private Fragment mCurFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_layout);

        mMeTxt = findViewById(R.id.txt_me);
        mSettingTxt = findViewById(R.id.txt_setting);

        mMeFragment = new KtFragment();
        switchFragment(mMeFragment);

        mMeTxt.setOnClickListener(v -> {
            switchFragment(mMeFragment);
        });


        mSettingTxt.setOnClickListener(v -> {
            mSettingFragment = new MyFloatFragment();
            switchFragment(mSettingFragment);
        });
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
}
