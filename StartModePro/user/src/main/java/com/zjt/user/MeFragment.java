package com.zjt.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public
/**
 * Created by ${za.zhu.jiangtao}
 * on 2021/3/4
 */
class MeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("MeFragment", "onViewCreated");

        view.findViewById(R.id.txt_me_kt)
                .setOnClickListener(v -> {
                    // 获取 KtFragment 中加入的 Fragment
                    List<Fragment> fragments = getParentFragmentManager().getFragments();
                    for (Fragment fragment : fragments) {
                        Fragment parentFragment = fragment.getParentFragment();
                        if (parentFragment != null) {
                            parentFragment.isAdded();
                        }
                    }
                });
    }

    public static void show(Fragment fragment, int containerId) {
        FragmentManager fragmentManager = fragment.getChildFragmentManager();
        Fragment previewFragment = fragmentManager.findFragmentByTag("ME_FRAGMENT");
        if (previewFragment != null) {
            fragmentManager.beginTransaction().remove(previewFragment)
                    .commitAllowingStateLoss();
        }
        MeFragment meFragment = new MeFragment();

        /**
         * 如果不加 containerId 的话 add一个 dialogFragment 是可以的；
         * 但是 add 的是 Fragment 的话，也会显示这个 fragment，但是是看不到界面的，glide 等 SDK 就是利用这个来管理生命周期的
         */

        fragmentManager.beginTransaction().add(containerId, meFragment, "ME_FRAGMENT")
                .commitAllowingStateLoss();

    }
}
