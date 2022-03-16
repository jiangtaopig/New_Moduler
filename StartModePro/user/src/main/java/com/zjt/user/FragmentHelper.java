package com.zjt.user;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class FragmentHelper {

    public static void showKtFragment(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment previewFragment = fragmentManager.findFragmentByTag("ME_FRAGMENT");
        if (previewFragment != null) {
            fragmentManager.beginTransaction().remove(previewFragment)
                    .commitAllowingStateLoss();
        }

        MeFragment meFragment = new MeFragment();
        fragmentManager.beginTransaction().add(meFragment, "ME_FRAGMENT")
                .commitAllowingStateLoss();
    }


}
