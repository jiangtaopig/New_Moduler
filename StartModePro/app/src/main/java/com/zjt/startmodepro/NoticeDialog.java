package com.zjt.startmodepro;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2020/8/19
 */
public class NoticeDialog extends DialogFragment implements DialogInterface {

    private final static String DATA_KEY = "NOTICE";
    private TextView mTitleTv;
    private TextView mOkTv;

    public static NoticeDialog getInstance(String title){
        NoticeDialog noticeDialog = new NoticeDialog();
        Bundle bundle = new Bundle();
        bundle.putString(DATA_KEY, title);
        noticeDialog.setArguments(bundle);
        return noticeDialog;
    }

    @Override
    public void cancel() {

    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialogTheme);
        dialog.setContentView(R.layout.dialog_notice_layout);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);//触摸对话框外面的区域，对话框消失
        initView(dialog);
        initData();
        return dialog;
    }

    private void initView(Dialog dialog) {
        mTitleTv = dialog.findViewById(R.id.txt_title);
        mOkTv = dialog.findViewById(R.id.txt_ok);

        mOkTv.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void initData(){
        String txt = getArguments().getString(DATA_KEY);
        mTitleTv.setText(txt);
    }
}
