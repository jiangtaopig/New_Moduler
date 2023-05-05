package com.zjt.startmodepro.apk_download;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

public class FileDownloadThread extends Thread {
    private static final int BUFFER_SIZE = 1024;
    private final String url;
    private final File file;
    private int startPosition;
    private final int endPosition;
    //用于标识当前线程是否下载完成
    private final CountDownLatch countDownLatch;

    public FileDownloadThread(String url, File file, int startPosition, int endPosition, CountDownLatch countDownLatch) {
        this.url = url;
        this.file = file;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        HttpURLConnection conn;
        try {
            long start = System.currentTimeMillis();
            URL url2 = new URL(url);
            conn = (HttpURLConnection) url2.openConnection();
//            conn.setAllowUserInteraction(true);
            conn.setConnectTimeout(5_000);
            conn.setRequestMethod("GET");
            //设置当前线程下载的起点，终点
            conn.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
            //使用java中的RandomAccessFile 对文件进行随机读写操作
            int responseCode = conn.getResponseCode();
            if (206 == responseCode) {
                RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                //设置开始写文件的位置
                randomAccessFile.seek(startPosition);
                int len;
                byte[] buffer = new byte[1024];
//                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                InputStream is = conn.getInputStream();
                while ((len = is.read(buffer)) != -1) {
                    try {
                        randomAccessFile.write(buffer, 0, len);
                        startPosition += len;
                        Log.e("FileDownloadThread", Thread.currentThread().getName() + " , total >>> " + startPosition);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                is.close();
                randomAccessFile.close();
            }
            long time = System.currentTimeMillis() - start;
            Log.e("FileDownloadThread", Thread.currentThread().getName() + " , total >>> " + startPosition +" , time = " + time);
            countDownLatch.countDown();
        } catch (IOException e) {
            Log.d(getName() + " Error:", e.getMessage());
        }
    }

}
