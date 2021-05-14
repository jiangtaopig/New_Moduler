package com.zjt.startmodepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bilibili.bilibililive.virtuallive.downloader.DownloadRequestTask;
import com.bilibili.bilibililive.virtuallive.downloader.IRequestDownloadCallback;
import com.zjt.startmodepro.range_download.DownloadRequest;
import com.zjt.startmodepro.utils.FileUtil;
import com.zjt.startmodepro.viewmodel.MyViewModel;
import com.zjt.startmodepro.viewmodel.NameViewModel;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/2
 */
class JetPack2Activity extends AppCompatActivity {

    private TextView mTv;
    private Button mButton;
    private ProgressBar mProgressBar;
    private NameViewModel mNameViewModel;
    private MyViewModel mMyViewModel;
    private Button mGetBtn;
    private Button mJump2JetPack3ActivityBtn;
    private DownloadRequestTask downloadRequestTask;

    public static void enter(Context context) {
        Intent intent = new Intent(context, JetPack2Activity.class);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jetpack_2_layout);

        mTv = findViewById(R.id.txt_tv);
        mButton = findViewById(R.id.btn_refresh);
        mProgressBar = findViewById(R.id.progressbar);
        mGetBtn = findViewById(R.id.btn_data);


        // 我修改了  JetPack2Activity 类

        mJump2JetPack3ActivityBtn = findViewById(R.id.btn_2_jetpack3Activity);

        // 为了测试 git 的 cherry-pick id 功能

        mJump2JetPack3ActivityBtn.setOnClickListener(v -> {
            JetPack3Activity.Companion.enter(this);
            Intent intent = new Intent(this, JetPack3Activity.class);
            startActivity(intent);
//            MyKotlinManager kotlinManager = new MyKotlinManager("zhujiangtao");
//            kotlinManager.showTops();
//            download();
        });

        Message message = new Message();
        new Handler().sendMessage(message);

        mNameViewModel = ViewModelManager.getInstance().getNameModel(this);
        mNameViewModel.getCurrentName().observe(this, s -> {
            Log.e("zjt", "s = " + s);
            mTv.setText(s);
        });

        mButton.setOnClickListener(v -> {
            mNameViewModel.getCurrentName().setValue("xxxx");
        });

        mMyViewModel = ViewModelManager.getInstance().getMyViewModel(this);
        mMyViewModel.getLoadingLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mProgressBar.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });

        mMyViewModel.getUserLiveData().observe(this, s -> mTv.setText(s));

        mGetBtn.setOnClickListener(v -> {
            mMyViewModel.getUserInfo();
            downloadRequestTask.pause();
        });

        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("a");
        int i , j= 0;
    }

    /**
     * 断点下载
     */
    private void download() {
        DownloadRequest request = new DownloadRequest();
        request.setUrl("http://gdown.baidu.com/data/wisegame/df65a597122796a4/weixin_821.apk");
        request.setLocalPath(FileUtil.getCachePath() + "/zjt/weixin_821.apk");


        downloadRequestTask = new DownloadRequestTask(request, new IRequestDownloadCallback() {

            @Override
            public void onTerminated(int reqId) {

            }

            @Override
            public void onDownloaded(int reqId) {

            }

            @Override
            public void onDownloadFailed(int reqId) {

            }

            @Override
            public void onDownloadProgress(int reqId, double percent, long downloadedByteLength) {
                Log.e("xxxxxx", "onDownloadProgress percent = " + percent);
            }

            @Override
            public void onDownloadPause(int reqId) {

            }

            @Override
            public void onDownloadStarted(int reqId) {

            }
        });
        downloadRequestTask.run();
    }
}
