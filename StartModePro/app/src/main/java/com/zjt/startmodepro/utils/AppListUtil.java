package com.zjt.startmodepro.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppListUtil {
    public static List<String> getInstalledAppNames(Context context) {
        List<String> installedApps = new ArrayList<>();

        // 获取PackageManager
        PackageManager packageManager = context.getPackageManager();

        // 获取已安装应用列表
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);

        // 遍历应用列表，获取包名
        for (PackageInfo packageInfo : packages) {
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            installedApps.add(appName);
        }
        return installedApps;
    }

    public static boolean isRooted() {
        return new File("/system/bin/su").exists() || new File("/system/xbin/su").exists();
    }

    public static boolean isRooted2() {
        return new File("/system/bin/busybox").exists();
    }
}
