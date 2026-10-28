package com.zjt.startmodepro.utils;

import android.content.Context;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWork extends Worker {

    public NotificationWork(Context context, WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Log.e("NotificationWork", "----------------------------------NotificationWork -----------------");
        // 在这里执行通知的显示逻辑
        LocalNotificationUtils.getInstance().showNotification(getApplicationContext());
//        showNotification("Scheduled Notification", "This is your scheduled notification.");
        return Result.success();
    }

    private void showNotification(String title, String message) {
//        NotificationManager notificationManager =
//                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // 创建点击通知时打开的 Intent（可根据需要修改）
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Android 8.0 及以上需要 NotificationChannel
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("notification_channel", "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "notification_channel")
//                .setContentTitle(title)
//                .setContentText(message)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        notificationManager.notify(1, notificationBuilder.build());

        LocalNotificationUtils.getInstance().showNotification(getApplicationContext());
    }
}

