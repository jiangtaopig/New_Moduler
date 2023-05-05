package com.zjt.startmodepro.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MISmsPermission {

    private final static int REQUEST_CODE_SERVICE_SMS = 10010;
    private final static String TAG = "MISmsPermission";

    public static void goPermissionSettings(Activity activity) {
        if (isMIUI()) {
            Intent intent;
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", activity.getPackageName());
            if (isIntentAvailable(intent, activity)) {
                activity.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
            } else {
                intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", activity.getPackageName());
                if (isIntentAvailable(intent, activity)) {
                    activity.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
                } else {
                    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivityForResult(intent, REQUEST_CODE_SERVICE_SMS);
                }
            }
        }
    }

    public static void goSetting(Activity context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        //调起应用设置页面
        context.startActivityForResult(intent, 1010);
    }

    /**
     * 判断是否是小米机型
     *
     * @return
     */
    public static boolean isMIUI() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }


    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    /**
     * 判断是否存在可用的跳转意图
     */
    public static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null || context == null || context.getPackageManager() == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
