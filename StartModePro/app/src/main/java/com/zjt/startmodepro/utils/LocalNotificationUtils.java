package com.zjt.startmodepro.utils;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.zjt.startmodepro.LocalNotificationReceiver;

import com.zjt.startmodepro.R;

import java.util.concurrent.TimeUnit;

public class LocalNotificationUtils {

    private volatile static LocalNotificationUtils mInstance;

    private LocalNotificationUtils(){}


    public static LocalNotificationUtils getInstance() {
        if (mInstance == null) {
            synchronized (LocalNotificationUtils.class) {
                if (mInstance == null) {
                    mInstance = new LocalNotificationUtils();
                }
            }
        }
        return mInstance;
    }


    public void scheduleNotification(Context context) {
        Log.e("scheduleNotification", "开始执行定时任务 10s");
        // 设置一个延迟 10 秒钟的 OneTimeWorkRequest
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(NotificationWork.class)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .addTag("xxx")
                .build();

        // 将任务调度给 WorkManager
        WorkManager.getInstance(context).enqueue(notificationWork);
    }

    public void cancelWork(Context context) {
        Log.e("scheduleNotification", "取消任务 -----");
        WorkManager.getInstance(context).cancelAllWorkByTag("xxx");
    }

    public void showNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "ttt";
            CharSequence name = "My Notification Channel";
            String description = "Channel for scheduled notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});

            NotificationManager mNotifyMgr =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            // 创建通知管理器
//            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            mNotifyMgr.createNotificationChannel(channel);

//            Intent intent = new Intent(context, MainActivity.class);
//            PendingIntent pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE);

            Intent intent = new Intent(context, LocalNotificationReceiver.class);
            intent.setAction("com.zjt.startmodepro.local_notification_clear");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1110, intent, PendingIntent.FLAG_IMMUTABLE);


            // 创建通知构建器
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_background) // 设置通知图标
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setOngoing(false)
                    .setContentTitle("定时通知") // 设置通知标题
                    .setContentText("记得去签到额！")
                    .setWhen(System.currentTimeMillis())// 设置通知时间
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT); // 设置优先级

            // 发送通知
            mNotifyMgr.notify(1001, builder.build());
        }
    }

    public void clearNotification(Context context) {
        NotificationManager mNotifyMgr =  (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(1001);
    }

    public void scheduleNotification(Context context, long timeInMillis) {
        // 创建 Intent 指向接收通知的广播接收器
        Intent intent = new Intent(context, LocalNotificationReceiver.class);
        intent.setAction("com.zjt.startmodepro.local_notification");

        int flag = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            flag = PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag);

        // 获取 AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Log.e("scheduleNotification", "10s 后执行");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //MARSHMALLOW OR ABOVE
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
        } else { //LOLLIPOP 21 OR ABOVE
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(timeInMillis, pendingIntent);
            alarmManager.setAlarmClock(alarmClockInfo, pendingIntent);
        }

    }



}
