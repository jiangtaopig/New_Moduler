package com.zjt.startmodepro;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zjt.startmodepro.utils.DisplayUtil;

import java.util.Objects;

public class RightDialog extends DialogFragment {

    public static RightDialog getInstance(){
        RightDialog rightDialog = new RightDialog();
        return rightDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.MyDialogStyleRight);
        dialog.setContentView(R.layout.dialog_right_layout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);//触摸对话框外面的区域，对话框消失
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = Objects.requireNonNull(getDialog()).getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = isPortrait() ? Gravity.BOTTOM : Gravity.END;

        int width = isPortrait() ? ViewGroup.LayoutParams.MATCH_PARENT : landWidth();
        int height = isPortrait() ? portraitHeight() :  ViewGroup.LayoutParams.MATCH_PARENT;
        window.setLayout(width, height);
//        window.setWindowAnimations(R.style.MyDialogStyleRight);
    }

    private boolean isPortrait () {
        Configuration configuration = getResources().getConfiguration();
        int orientation = configuration.orientation;
        return Configuration.ORIENTATION_PORTRAIT == orientation;
    }

    private int portraitHeight() {
        return DisplayUtil.dip2px(300f);
    }

    private int landWidth() {
        return DisplayUtil.dip2px(376f);
    }
}
