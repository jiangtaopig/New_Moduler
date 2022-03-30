package com.zjt.startmodepro;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoadSoActivity extends AppCompatActivity {

    public native String getStringFromCPP();//工程自带的native方法

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_so_layout);

//        btn_load_so
        Button button = findViewById(R.id.btn_load_so);
        button.setOnClickListener(v -> {
            //4，此处测试随apk安装的so库，系统加载的，无需动态加载，apk安装时就有的
            System.loadLibrary("nonostub");//工程自带的so
            final String msg = getStringFromCPP();
            Log.d("dq-so", "工程自带的cpp代码方法=" + msg);
            Toast.makeText(LoadSoActivity.this, "工程自带的cpp=" + msg, Toast.LENGTH_LONG).show();
        });
    }
}
