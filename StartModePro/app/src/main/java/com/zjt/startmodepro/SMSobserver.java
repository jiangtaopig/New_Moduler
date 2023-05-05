package com.zjt.startmodepro;

import android.content.ClipboardManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSobserver extends ContentObserver {

    private Context mContext;
    private static int id=0; //这里必须用静态的，防止程序多次意外初始化情况

    public SMSobserver(Context context) {
        super(new Handler(Looper.getMainLooper()));
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.e("xxxx","----------------------------------onChange------------------------ ");
        //过滤可能界面调用初始化两次的情况
        if (uri.toString().contains("content://sms/raw")) {
            return;
        }

        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor cursor = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String _id = cursor.getString(cursor.getColumnIndex("_id"));
                //比较id 解决重复问题
                if (id < Integer.parseInt(_id)) {
                    id = Integer.parseInt(_id);//将获取到的当前id记录，防止重复
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String body = cursor.getString(cursor.getColumnIndex("body"));
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String time1 = dateFormat.format(new Date(Long.parseLong(date)));
                    Log.e("xxxx","验证码：" + body);
                    //写自己的处理逻辑，我这里是复制验证码到剪切板
                    if(body.contains("短信中包含某字符才匹配复制到剪切板")) {
                        Pattern pattern = Pattern.compile("(\\d{4,6})");
                        Matcher matcher = pattern.matcher(body);
                        if (matcher.find()) {
                            String code = matcher.group(0);
                            Log.e("xxxx","验证码：" + code);
                            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                            cmb.setText(code);
                            Toast.makeText(mContext.getApplicationContext(), "复制成功" + code, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                Log.e("xxxx","xxxxxxxxxxxxxxxxxxxx move to first ------");
            }
            cursor.close();
        }
    }
}