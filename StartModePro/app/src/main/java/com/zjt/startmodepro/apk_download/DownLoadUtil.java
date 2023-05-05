package com.zjt.startmodepro.apk_download;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class DownLoadUtil {

    /**
     * 使用 DownloaderManager  下载
     *
     * @param downloadUrl
     * @param fileName
     */
    public static void downLoadUrl(Context context, File destFile, String downloadUrl, String fileName) {

        // 创建下载请求
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
        //设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);

        /*
         * 设置在通知栏是否显示下载通知(下载进度), 有 3 个值可选:
         *    VISIBILITY_VISIBLE:                   下载过程中可见, 下载完后自动消失 (默认)
         *    VISIBILITY_VISIBLE_NOTIFY_COMPLETED:  下载过程中和下载完成后均可见
         *    VISIBILITY_HIDDEN:                    始终不显示通知
         */
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);


        // 设置通知的标题和描述
        request.setTitle(fileName);
        request.setDescription(fileName);
        request.setMimeType("application/cn.trinea.download.file");

        // 设置下载文件的保存位置
        File saveFile = new File(destFile, fileName);
        request.setDestinationUri(Uri.fromFile(saveFile));

        /*
         * 2. 获取下载管理器服务的实例, 添加下载任务
         */
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // 将下载请求加入下载队列, 返回一个下载ID
        long downloadId = downloadManager.enqueue(request);
        Log.d("Download", "downloadId=" + downloadId + "\tsaveFile=" + saveFile.getAbsolutePath());

        // 通过 Timer 去轮询下载进度，注意 Timer 是执行在子线程中的
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                DownloadManager.Query down_query = new DownloadManager.Query();
                down_query.setFilterById(downloadId);
                // 向下载管理器查询下载任务，并返回查询结果集的游标
                Cursor cursor = downloadManager.query(down_query);
                while (cursor.moveToNext()) {
                    int uriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                    int totalIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES); // 下载文件总的大小
                    int nowIdx = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR); // 已经下载了多少
                    if (cursor.getString(uriIdx) == null) {
                        break;
                    }
                    // 根据总大小和已下载大小，计算当前的下载进度
                    int progress = (int) (100 * cursor.getLong(nowIdx) / cursor.getLong(totalIdx));
                    Log.d("Download", "-------------- progress >>>>>>>>>>>>> " + progress);
                    if (progress >= 100) { // 表示下载完成，关闭定时任务
                        timer.cancel();
                    }
                }
            }
        }, 1000, 80); // 延时1秒后，每隔80毫秒执行一次
    }

    private void queryDownloadProgress(long downloadId, DownloadManager manager) {
        DownloadManager.Query down_query = new DownloadManager.Query();
        down_query.setFilterById(downloadId);
        // 向下载管理器查询下载任务，并返回查询结果集的游标
        Cursor cursor = manager.query(down_query);
        while (cursor.moveToNext()) {
            int uriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            int totalIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int nowIdx = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            if (cursor.getString(uriIdx) == null) {
                break;
            }
            // 根据总大小和已下载大小，计算当前的下载进度
            int progress = (int) (100 * cursor.getLong(nowIdx) / cursor.getLong(totalIdx));
            Log.d("Download", "-------------- progress >>>>>>>>>>>>> " + progress);
            if (progress >= 0) {

            }
        }
    }

    /**
     * 检查下载状态，是否下载成功
     */
    public static void checkStatus(Context context) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        ;
        DownloadManager.Query query = new DownloadManager.Query();
        // 执行查询, 返回一个 Cursor (相当于查询数据库)
        Cursor cursor = manager.query(query);
        if (!cursor.moveToFirst()) {
            cursor.close();
        }
        int id = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
        //通过下载的id查找
        query.setFilterById(id);

        // 获取下载好的 apk 路径
        String localFilename = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        } else {
            localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
        }

        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    //下载暂停
                    break;
                case DownloadManager.STATUS_PENDING:
                    //下载延迟
                    break;
                case DownloadManager.STATUS_RUNNING:
                    int bytes = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    Log.d("Download", "---------------- bytes:" + bytes);
                    //正在下载
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.d("Download", "localFilename:" + localFilename);
                    //下载完成安装APK
//                    installApp(context, localFilename);
                    Toast.makeText(context, "下载完成：路径 >> " + localFilename, Toast.LENGTH_SHORT).show();
                    cursor.close();
                    break;
                case DownloadManager.STATUS_FAILED:
                    //下载失败
                    cursor.close();
                    break;
                default:
                    break;
            }
        }
    }

}
