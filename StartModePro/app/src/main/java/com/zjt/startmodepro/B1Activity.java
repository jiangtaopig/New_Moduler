package com.zjt.startmodepro;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zjt.base.BaseActivity;
import com.zjt.startmodepro.apk_download.DownLoadUtil;
import com.zjt.startmodepro.apk_download.FileDownloadThread;
import com.zjt.startmodepro.utils.BitmapUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2020/12/31
 */
public class B1Activity extends AppCompatActivity {

    private static final String TAG = "APP_INSTALL";
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b1);

        initView();
        File apkFile = new File(getBaseContext().getExternalFilesDir(null), "ydgjApp_pro_v3330105_temp.apk");
        findViewById(R.id.tttx).setOnClickListener(v -> {
//            sendErrMsg("测试的华为报错");
            File file = getBaseContext().getExternalFilesDir(null);
//            if (file.exists()) {
//                File[] files = file.listFiles();
//                Log.e(TAG, "file size = " + files.length);
//                List<File> fileList = Arrays.asList(files);
//                Iterator<File> iterator = fileList.iterator();
//                while (iterator.hasNext()) {
//                    File tmpFile = iterator.next();
//                    if (tmpFile.getName().endsWith(".apk")) {
//                        tmpFile.delete();
//                    }
//                }
//            }

            String apkName = "ydgjApp_pro_v3330105.apk";

            File file1 = new File(file, apkName);
            if (file1.exists()) {
                return; // 已下载过此安装包，就不在下载
            }
            downloadAPK(file, apkName);
//            installAPK(apkFile);
//            download2();
//            DownLoadUtil.downLoadUrl(this, getBaseContext().getExternalFilesDir(null), "https://files.com/app/apk/ydgjApp_pro_v3330105.apk", "ydgjApp_pro_v3330105.apk");
        });

        findViewById(R.id.tttx2).setOnClickListener(v -> {
//            File apkFile2 = new File(getExternalFilesDir(null), "startModePro_pro_v1.apk");
            File apkFile2 = new File(getBaseContext().getExternalFilesDir(null), "ydgjApp_pro_v3330105.apk");
            installAPK(apkFile2);

//            try {
//                getAppVersion();
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }

//            getYctbVersion();
//
//            // pin baidu
//            new Thread(() -> {
//                boolean connected = connectionNetwork();
//                Log.e(TAG, "connected  >>> " + connected);
//            }).start();
        });


        findViewById(R.id.download_continue).setOnClickListener(v -> {
            downloadByContinue();
        });

        ImageView view = findViewById(R.id.image_v2);

        Bitmap srcBm = BitmapFactory.decodeResource(getResources(), R.drawable.single_business_card);

        Bitmap bm = clipBitmap(srcBm, 20, 0);
        bm = drawBitmapWithBg(bm);
        view.setImageBitmap(bm);

        imageView = findViewById(R.id.image_v);
        imageView.setImageBitmap(clipBitmap2(srcBm));

        ImageView img3 = findViewById(R.id.img3);
        img3.setImageResource(R.drawable.test);

        ImageView img4 = findViewById(R.id.img4);
        // 质量压缩，质量压缩不会改变图片的宽和高，但是 压缩后的额字节数组长度会变小
        byte [] img4Byte = bmpToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.test), 5);
        Bitmap afterCompressBm = BitmapFactory.decodeByteArray(img4Byte, 0, img4Byte.length);
        byte [] noCompressByte = bmpToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.test), 100);
        Log.e("afterCompressBm >> ", " noCompressByte size = " + noCompressByte.length+" , img4Byte length = " + img4Byte.length);
        img4.setImageBitmap(afterCompressBm);



        findViewById(R.id.add_text_to_bm).setOnClickListener(v -> {
            Glide.with(B1Activity.this).asBitmap()
                    .load("https://img2.baidu.com/it/u=1361506290,4036378790&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500")
//                        .apply(options)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(
                                    resource,
                                    500,
                                    500,
                                    true
                            );

                            int size = resource.getByteCount();

                            Bitmap waterMakerBm = BitmapFactory.decodeResource(B1Activity.this.getResources(), R.drawable.seek_button);

                            // 在图片上添加文字
                            Bitmap bm = BitmapUtils.drawTextToRightTop(B1Activity.this, thumbBmp, "朱建军", 18, Color.RED, 0, 0);
                            int size2 = bm.getByteCount();

                            Bitmap bm2 = BitmapUtils.createWaterMaskLeftTop(B1Activity.this, bm, waterMakerBm, 0, 0);
                            byte[] bb = bmpToByteArray(bm2, 100);
                            Log.e("压缩前", "length = " + (bb.length / 1024) + "kb");
                            byte[] a = bmpToByteArray2(bm2);
                            Log.e("压缩后", "length = " + (a.length / 1024) + "kb");
                            Bitmap bitmap = BitmapFactory.decodeByteArray(a, 0, a.length);
//                            Bitmap bitmap = BitmapUtils.bitmapInSampleSize(bm2, 140);
                            Log.e("xxxxxxxxxxxxx", "size = " + size + ", size2 = " + size2 + ", size3 = " + bm2.getByteCount() + " , size4 = " + bitmap.getByteCount());
                            imageView.setImageBitmap(bitmap);
                        }
                    });


            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.test);
            Log.e("xxxxxxxxxxxxx", "size = " + bitmap.getByteCount());
        });
    }

    /**
     * 图片裁减
     * @param x 从原图片宽的开始截取的位置
     * @param y 从原图片高的开始截取的位置
     */
    private Bitmap clipBitmap(Bitmap srcBitmap, int x, int y) {
        // 表示从 x,y 开始，截取 srcBitmap.getWidth() - x 和 srcBitmap.getHeight() - y
        Bitmap bm = Bitmap.createBitmap(srcBitmap, x, y, srcBitmap.getWidth() - x, srcBitmap.getHeight() - y);
        return bm;
    }

    /**
     * 给 Bitmap 加背景颜色
     */
    private Bitmap drawBitmapWithBg(Bitmap srcBitmap) {
        Bitmap newBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(getResources().getColor(R.color.color_ff6600));
        Paint paint = new Paint();
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        return newBitmap;
    }

    private Bitmap clipBitmap2(Bitmap srcBm) {
        // 新的图片大小是老的图片的2倍
        Bitmap newBitmap = Bitmap.createBitmap(srcBm.getWidth()*2, srcBm.getHeight()*2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(getResources().getColor(R.color.color_ff6600));
        Paint paint = new Paint();
        // 将老的图片宽为高的2倍，下面这个是缩放原始图片的作用，跟  canvas.drawBitmap(srcBitmap, 0, 0, paint) 的作用不一样
        canvas.drawBitmap(srcBm, null, new Rect(0, 0, srcBm.getHeight()*2, srcBm.getHeight()), paint);
        return newBitmap;
    }

    private byte[] bmpToByteArray(final Bitmap bmp, int quality) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private byte[] bmpToByteArray2(final Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int quality = 98;
        bmp.compress(Bitmap.CompressFormat.WEBP, quality, output); // 注意：png 格式是无损图片，无法压缩
        byte[] result = output.toByteArray();
        Log.e("压缩中", "size = " + result.length / 1024 + " kb");

        while (result.length / 1024 > 128) {
            Log.e("压缩中", "size = " + result.length / 1024 + " kb");
            quality -= 4;
            bmp.compress(Bitmap.CompressFormat.WEBP, quality, output);
            result = output.toByteArray();
        }
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * ping "www.baidu.com"
     *
     * @return
     */
    private boolean connectionNetwork() {
        boolean result = false;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL("https://www.baidu.com/").openConnection();
            connection.setConnectTimeout(30_000);
            connection.connect();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                connection.disconnect();
            }
        }
        return result;
    }

    private void initView() {
        Map<String, String> map = new HashMap<>(3);
    }

    private void sendErrMsg(String msg) {
        Log.e("sendErrMsg", msg);
        new Thread() {
            public void run() {
                try {
                    URL url = new URL("http://xxx/app/business/appErrSave");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    connection.setRequestMethod("POST");
                    //连接超时
                    connection.setConnectTimeout(80_000);
                    connection.setReadTimeout(80_000);
                    connection.setDoOutput(true);
                    //连接打开输出流
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    ;

                    JSONObject json = new JSONObject();
                    json.put("errMsg", msg);
                    json.put("taskId", "016");

                    // 解决入参为中文导致的 400 , 下面的 writeBytes 就会导致400
//            out.writeBytes(json.toJSONString());
                    out.write(json.toString().getBytes("utf-8"));
                    System.out.println("code = " + connection.getResponseCode());
                    if (connection.getResponseCode() == 200) {
                        //接收服务器输入流信息
                        InputStream is = connection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        //拿到信息
                        String result = br.readLine();
                        System.out.println("result >>> " + result);
                        is.close();
                    }
                    out.close();
                    connection.disconnect();
                } catch (IOException | JSONException ioException) {
                    ioException.printStackTrace();
                }
            }

        }.start();

    }

    public void downloadAPK(File destFile, String apkName) {
        //准备用于保存APK文件的File对象 : /storage/sdcard/Android/package_name/files/xxx.apk
        File apkFile = new File(destFile, apkName + ".tmp");
        // 开启子线程, 请求下载APK文件
        new Thread(() -> {
            try {
                long start = System.currentTimeMillis();
                //1. 得到连接对象
                String path = "https://files.com/app/apk/ydgjApp_pro_v3330105.apk";
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //2. 设置
                //connection.setRequestMethod("GET");
                connection.setConnectTimeout(30_000);
                connection.setReadTimeout(30_000);
                //3. 连接
                connection.connect();

                long fileSize = connection.getContentLength();

                //4. 请求并得到响应码200
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    //设置dialog的最大进度
                    //5. 得到包含APK文件数据的InputStream
                    InputStream is = connection.getInputStream();
                    //6. 创建指向apkFile的FileOutputStream
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    //7. 边读边写
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    int progress = 0;
                    long downloadSizeSoFar = 0;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        downloadSizeSoFar += len;
                        progress = (int) (downloadSizeSoFar * 100 / fileSize);
                        Log.e(TAG, " downloading progress = " + progress);
                    }
                    fos.close();
                    is.close();
                }
                //9. 下载完成, 关闭连接
                connection.disconnect();
                long end = System.currentTimeMillis();
                Log.e(TAG, "--------------下载完成-------------" + (end - start));

                // 下载成功后，重新命名为正式名称，以此判断是否下载完成，
                boolean res = apkFile.renameTo(new File(getBaseContext().getExternalFilesDir(null), "ydgjApp_pro_v3330105.apk"));
                if (!res) {
                    Log.e(TAG, apkFile.getAbsolutePath() + " , 重命名失败");
                }

                runOnUiThread(() -> {
                    Log.e(TAG, "--------------完成-------------" + (end - start));
                    Toast.makeText(B1Activity.this, "下载完成，耗时：" + (end - start), Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }


    /**
     * 断点下载
     */
    public void downloadByContinue() {
        new Thread(() -> {
            long startPosition = 0;
            long endPosition = 0;
            String urlStr = "https://files.com/app/apk/ydgjApp_pro_v3330105.apk";
            String fileName = "ydgjApp_pro_v3330105.apk";
            URL url;
            try {
                url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20_000);
                conn.setRequestMethod("GET");
                //获取下载文件的总大小
                endPosition = conn.getContentLength();

                String spKey = "APK_DOWNLOAD_BYTES_SO_FAR";
                SharedPreferences sharedPreferences = getSharedPreferences(spKey, MODE_PRIVATE);
                // 下载的临时文件存在的话，才会断点下载
                File file = getBaseContext().getExternalFilesDir(null);
                File tempFile = new File(file, fileName + ".temp");
                if (tempFile.exists()) {
                    startPosition = sharedPreferences.getLong(spKey, 0L);
                }

                DownLoadTask2 downLoadTask2 = new DownLoadTask2(urlStr, fileName, startPosition, endPosition, sharedPreferences, spKey);
                new Thread(downLoadTask2).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }


    private void installAPK(File apkFile) {
        if (apkFile.exists() && apkFile.canRead()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(this, getBaseContext().getPackageName() + ".fileprovider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            startActivity(intent);
        } else {
            Toast.makeText(this, "安装文件不存在", Toast.LENGTH_LONG).show();
        }
    }

    private void getAppVersion() throws PackageManager.NameNotFoundException {
        PackageManager packageManager = getPackageManager();
        if (packageManager != null) {
            PackageInfo packinfo = packageManager.getPackageInfo(getPackageName(), 0);
            if (packinfo != null) {
                int version = packinfo.versionCode;
                Log.e(TAG, "version >>> " + version);
            }
        }
    }


    private void getYctbVersion() {
        new Thread() {
            public void run() {
                try {

                    URL url = new URL("");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    connection.setRequestMethod("POST");
                    //连接超时
                    connection.setConnectTimeout(80_000);
                    connection.setReadTimeout(80_000);
                    connection.setDoOutput(true);
                    //连接打开输出流
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    ;

                    JSONObject json = new JSONObject();
                    json.put("appCode", "APP04");
                    json.put("version", "3100105");
                    json.put("mobile", "mobile");
                    json.put("deviceSystem", "android");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("global", json);

                    // 解决入参为中文导致的 400 , 下面的 writeBytes 就会导致400
//            out.writeBytes(json.toJSONString());
                    out.write(jsonObject.toString().getBytes("utf-8"));
                    System.out.println("code = " + connection.getResponseCode());
                    if (connection.getResponseCode() == 200) {
                        //接收服务器输入流信息
                        InputStream is = connection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        //拿到信息
                        String result = br.readLine();
                        System.out.println("result >>> " + result);
                        is.close();
                    }
                    out.close();
                    connection.disconnect();
                } catch (IOException | JSONException ioException) {
                    ioException.printStackTrace();
                }
            }

        }.start();
    }

    class DownLoadTask2 implements Runnable {
        private String url;
        private String fileName;
        private long startPosition;
        private long endPosition;
        private SharedPreferences sharedPreferences;
        private String spKey;

        public DownLoadTask2(String url, String fileName, long startPos, long endPos, SharedPreferences sharedPreferences, String spKey) {
            this.url = url;
            this.fileName = fileName;
            this.startPosition = startPos;
            this.endPosition = endPos;
            this.sharedPreferences = sharedPreferences;
            this.spKey = spKey;
        }

        @Override
        public void run() {
            Log.e("DownLoadTask2", Thread.currentThread().getName() + " , startPosition >>> " + startPosition + " , endPosition = " + endPosition);
            File file = getBaseContext().getExternalFilesDir(null);
            File tempFile = new File(file, fileName + ".temp");
            HttpURLConnection conn;
            try {
                long start = System.currentTimeMillis();
                URL url2 = new URL(url);
                conn = (HttpURLConnection) url2.openConnection();
                conn.setConnectTimeout(5_000);
                conn.setRequestMethod("GET");
                //设置当前线程下载的起点，终点
                conn.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
                //使用java中的RandomAccessFile 对文件进行随机读写操作
                int responseCode = conn.getResponseCode();
//                String key = fileName.substring(0, fileName.indexOf(".apk"));
                if (206 == responseCode) {
                    RandomAccessFile randomAccessFile = new RandomAccessFile(tempFile, "rw");
                    //设置开始写文件的位置
                    randomAccessFile.seek(startPosition);
                    int len;
                    byte[] buffer = new byte[1024];
                    InputStream is = conn.getInputStream();
                    long total = startPosition;
                    try {
                        while ((len = is.read(buffer)) != -1) {
                            randomAccessFile.write(buffer, 0, len);
                            total += len;
                            sharedPreferences.edit().putLong(spKey, total).apply();
                            Log.e("DownLoadTask2", Thread.currentThread().getName() + " , total >>> " + total);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        Log.d(" Error:", exception.getMessage());
                    } finally {
                        is.close();
                        randomAccessFile.close();
                    }
                } else {
                    Log.e(TAG, "--------------下载出错-------------responseCode = " + responseCode + " , url = " + url);
                }
                long time = System.currentTimeMillis() - start;
                Log.e("DownLoadTask2", Thread.currentThread().getName() + " time = " + time);

                Log.e(TAG, "--------------下载完成-------------");
                // 下载成功后，重新命名为正式名称，以此判断是否下载完成，
                boolean res = tempFile.renameTo(new File(getBaseContext().getExternalFilesDir(null), fileName));
                if (!res) {
                    Log.e(TAG, tempFile.getAbsolutePath() + " , 重命名失败");
                    return;
                }
                // 下载完成后删除sp中的下载大小
                sharedPreferences.edit().clear().apply();

            } catch (IOException e) {
                Log.d(" Error:", e.getMessage());
            }
        }
    }


    private void download2() {
//        int coreNum = Runtime.getRuntime().availableProcessors();
//        int a = 1;
        DownLoadTask downLoadTask = new DownLoadTask("https://files.com/app/apk/ydgjApp_pro_v3330105.apk", 1, "ydgjApp_pro_v3330105.apk");
        downLoadTask.start();
    }


    class DownLoadTask extends Thread {
        private int blockSize;
        private int threadNum;
        private String urlStr;
        private String fileName;

        public DownLoadTask(String urlStr, int threadNum, String fileName) {
            this.urlStr = urlStr;
            this.threadNum = threadNum;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            CountDownLatch countDownLatch = new CountDownLatch(threadNum);
            try {
                long startTime = System.currentTimeMillis();
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5_000);
                conn.setRequestMethod("GET");
                //获取下载文件的总大小
                int fileSize = conn.getContentLength();
                //计算每个线程要下载的数据量
                blockSize = fileSize / threadNum;

//                File file = new File(fileName);
                File apkFile = new File(getBaseContext().getExternalFilesDir(null), fileName);
                for (int i = 0; i < threadNum; i++) {
                    //启动线程，分别下载自己需要下载的部分
                    int startPos = i * blockSize;
                    int endPos = (i + 1) * blockSize - 1;
                    if (i == threadNum - 1) {
                        endPos = fileSize;
                    }
                    FileDownloadThread fdt = new FileDownloadThread(urlStr, apkFile,
                            startPos, endPos, countDownLatch);
//                    if (i == 0) {
                    fdt.setName("Thread" + i);
                    fdt.start();
//                    }
                }
                countDownLatch.await();
                long end = System.currentTimeMillis();
                Log.e(TAG, "下载完成, 耗时 >>> " + (end - startTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

