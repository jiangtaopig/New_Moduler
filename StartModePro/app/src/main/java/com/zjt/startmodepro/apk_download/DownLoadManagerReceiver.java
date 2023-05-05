package com.zjt.startmodepro.apk_download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DownLoadManagerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("Download", "action >>> " + action);
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            Log.d("Download", "下载完成");

        }
        DownLoadUtil.checkStatus(context);
    }
}
