package com.zjt.startmodepro;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.zjt.startmodepro.utils.LocalNotificationUtils;

public class LocalNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("NotificationReceiver", "action is  >>> " + intent.getAction());
        if ("com.zjt.startmodepro.local_notification_clear".equals(intent.getAction())) {
            // 在这里执行你需要的操作
            Toast.makeText(context, "清除通知", Toast.LENGTH_SHORT).show();
            LocalNotificationUtils.getInstance().clearNotification(context);

            // 跳转到 Activity
//            Intent intent1 = new Intent(context, MainActivity.class);
//            if (!(context instanceof Activity)) {
//                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            context.startActivity(intent1);
        } else if("com.zjt.startmodepro.local_notification".equals(intent.getAction())) {
            Toast.makeText(context, "设置10秒后的通知", Toast.LENGTH_SHORT).show();
            LocalNotificationUtils.getInstance().showNotification(context);
        }
    }
}
