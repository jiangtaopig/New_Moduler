package com.zjt.startmodepro;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestLeakCanaryActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper()) ;

    private void doSchedule() {
        handler.postDelayed(runnable, 5000);
    }


    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.e("TestLeakCanaryActivity", "------ doSchedule ------- ");
            handler.postDelayed(this::run, 5000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leakcanary_layout);

        // 修改 leakcanary 的配置
//        AppWatcher.getConfig().copy( true, true, false, false, 5000, true);
        doSchedule();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("TestLeakCanaryActivity", "------ onDestroy ------- ");
        int a = 1;
    }
}
