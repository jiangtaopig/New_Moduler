package com.zjt.startmodepro.reinstance;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zjt.startmodepro.R;

import org.jetbrains.annotations.NotNull;

public class ReInstanceFragment extends Fragment {

    public static String TAG = "ReInstance__";

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, " fragment onCreate >> " + this);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.e(TAG, " fragment onCreateView >> " + this);
        return inflater.inflate(R.layout.fragment_reinstance_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Log.e(TAG, " fragment onViewCreated >> " + this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, " fragment onDestroyView >> " + this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, " fragment onDestroy >> " + this);
        super.onDestroy();
    }
}
