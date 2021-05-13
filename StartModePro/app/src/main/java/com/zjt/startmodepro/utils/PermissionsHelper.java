package com.zjt.startmodepro.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.tencent.mmkv.MMKV;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/5/7 5:05 下午
 * @Description : PermissionsChecker
 */


public class PermissionsHelper {
    public static final String[] CAMERA_PERMISSION = {"android.permission.CAMERA"};
    public static final String[] AUDIO_PERMISSION = {"android.permission.RECORD_AUDIO"};
    public static final String[] STORAGE_PERMISSION = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int REQUEST_CODE_PERMISSION_AUDIO = 0x10;
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 0x11;
    public static final int REQUEST_CODE_PERMISSION_STORGE = 0x12;
    private static final SparseArray<TaskCompletionSource<Void>> sPendingTasks = new SparseArray<>();
    private static AlertDialog mDialog;

    private static MMKV mMmkv = MMKV.mmkvWithID("permission_mmkv");
    private static final String CAMERA_APPLY = "camera_applied";
    private static final String AUDIO_APPLY = "audio_applied";
    private static final String STORAGE_APPLY = "storage_applied";


    private PermissionsHelper(Context context) {

    }

    private static PermissionsHelper mInstance;

    public static PermissionsHelper getInstance(Context context){
        if (mInstance == null) {
            synchronized (PermissionsHelper.class) {
                if (mInstance == null){
                    mInstance = new PermissionsHelper(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * @param activity The activity, should be subclass of {@link Activity}
     * @param type     The type of storage directory to return. Environment.DIRECTORY_*
     * @param subdir   sub directory in public dir, may be null.
     */
    public static <A extends Activity> Task<File> getExternalPublicDir(A activity,
                                                                       String type, String subdir) {
        return grantExternalPermissions(activity)
                .onSuccess(continuationOfGettingPublicDir(type, subdir));
    }

    public static <A extends FragmentActivity> Task<File> getExternalPublicDir(A activity,
                                                                               String type, String subdir) {
        return grantExternalPermissions(activity)
                .onSuccess(continuationOfGettingPublicDir(type, subdir));
    }

    /**
     * @param fragment The fragment, should be subclass of {@link Fragment}
     * @param type     The type of storage directory to return. Environment.DIRECTORY_*
     * @param subdir   sub directory in public dir, may be null.
     */
    public static <F extends Fragment> Task<File> getExternalPublicDir(F fragment, String type,
                                                                       String subdir) {
        return grantExternalPermissions(fragment)
                .onSuccess(continuationOfGettingPublicDir(type, subdir));
    }

    private static Continuation<Void, File> continuationOfGettingPublicDir(final String type,
                                                                           final String subdir) {
        return new Continuation<Void, File>() {
            @Override
            public File then(Task<Void> task) throws Exception {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File pubDir = Environment.getExternalStoragePublicDirectory(type);
                    if (subdir == null)
                        return pubDir;
                    return new File(pubDir, subdir);
                } else {
                    throw new FileNotFoundException("External storage isn't mounted");
                }
            }
        };
    }

    public static Task<Void> grantCameraPermission(Activity activity) {
        return grantPermissions(activity, CAMERA_PERMISSION, REQUEST_CODE_PERMISSION_CAMERA);
    }

    public static Task<Void> grantAudioPermission(Activity activity) {
        return grantPermissions(activity, AUDIO_PERMISSION, REQUEST_CODE_PERMISSION_AUDIO);
    }

    public static <F extends Fragment> Task<Void> grantExternalPermissions(F fragment) {
        return grantPermissions(fragment, STORAGE_PERMISSION, REQUEST_CODE_PERMISSION_STORGE);
    }

    public static Task<Void> grantExternalPermissions(Activity activity) {
        return grantPermissions(activity, STORAGE_PERMISSION, REQUEST_CODE_PERMISSION_STORGE);
    }


    public static Task<Void> grantPermissions(final Activity activity, final String[] permissions, final int requestCode) {
//        TaskCompletionSource<Void> source = sPendingTasks.get(requestCode);
//        if (source != null) // has a pending task already, return it
//            return source.getTask();
        final TaskCompletionSource<Void> task = new TaskCompletionSource<>();
        if (!checkSelfPermissions(activity, permissions)) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_CAMERA:
                    requestCameraAndCheckNotReminder(activity, requestCode, task);
                    break;
                case REQUEST_CODE_PERMISSION_AUDIO:
                    requestAudioAndCheckNotReminder(activity, requestCode, task);
                    break;
                case REQUEST_CODE_PERMISSION_STORGE:
                    requestStorageAndCheckNotReminder(activity, requestCode, task);
                    break;
            }
        } else {
            task.trySetResult(null);
        }
        return task.getTask();
    }

    private static boolean hasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static void requestStorageAndCheckNotReminder(Activity activity, int requestCode, TaskCompletionSource<Void> task) {
        if (!hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // 之前申请权限时如果是拒绝且不再提醒，那么跳转到系统的权限界面; 拒绝且不再提醒 shouldShowRequestPermissionRationale 返回false
            // 如果之前未申请过该权限 shouldShowRequestPermissionRationale 也返回 false
            String[] storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
            sPendingTasks.put(requestCode, task);
            if (!mMmkv.decodeBool(STORAGE_APPLY, false)) { // 表示第一次申请该权限
                requestStorage(activity, storagePermissions);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    jump2Setting("存储", activity);
                } else {
                    requestStorage(activity, storagePermissions);
                }
            }
        }
    }

    private static void requestCameraAndCheckNotReminder(Activity activity, int requestCode, TaskCompletionSource<Void> task) {
        if (!hasPermission(activity, Manifest.permission.CAMERA)) {
            // 之前申请权限时如果是拒绝且不再提醒，那么跳转到系统的权限界面; 拒绝且不再提醒 shouldShowRequestPermissionRationale 返回false
            // 如果之前未申请过该权限 shouldShowRequestPermissionRationale 也返回 false
            String[] permissions = new String[]{Manifest.permission.CAMERA};
            sPendingTasks.put(requestCode, task);
            if (!mMmkv.decodeBool(CAMERA_APPLY, false)) { // 表示第一次申请该权限
                requestCamera(activity, permissions);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                    jump2Setting("相机", activity);
                } else {
                    requestCamera(activity, permissions);
                }
            }
        }
    }

    private static void requestAudioAndCheckNotReminder(Activity activity, int requestCode, TaskCompletionSource<Void> task) {
        if (!hasPermission(activity, Manifest.permission.RECORD_AUDIO)) {
            // 之前申请权限时如果是拒绝且不再提醒，那么跳转到系统的权限界面; 拒绝且不再提醒 shouldShowRequestPermissionRationale 返回false
            // 如果之前未申请过该权限 shouldShowRequestPermissionRationale 也返回 false
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO};
            sPendingTasks.put(requestCode, task);
            if (!mMmkv.decodeBool(AUDIO_APPLY, false)) { // 表示第一次申请该权限
                requestAudio(activity, permissions);
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECORD_AUDIO)) {
                    jump2Setting("麦克风", activity);
                } else {
                    requestAudio(activity, permissions);
                }
            }
        }
    }

    private static void requestStorage(Activity activity, String[] permissions) {
        if (!hasPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSION_STORGE);
            }
        }
    }

    private static void requestCamera(Activity activity, String[] permissions) {
        if (!hasPermission(activity, Manifest.permission.CAMERA)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSION_CAMERA);
            }
        }
    }

    private static void requestAudio(Activity activity, String[] permissions) {
        if (!hasPermission(activity, Manifest.permission.RECORD_AUDIO)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSION_AUDIO);
            }
        }
    }

    public static Task<Void> grantPermissions(final Fragment fragment, final String[] permissions,
                                              final int requestCode) {
        final TaskCompletionSource<Void> task = new TaskCompletionSource<>();
        if (!checkSelfPermissions(fragment.getActivity(), permissions)) {
            switch (requestCode) {
                case REQUEST_CODE_PERMISSION_CAMERA:
                    requestCameraAndCheckNotReminder(fragment.getActivity(), requestCode, task);
                    break;
                case REQUEST_CODE_PERMISSION_AUDIO:
                    requestAudioAndCheckNotReminder(fragment.getActivity(), requestCode, task);
                    break;
                case REQUEST_CODE_PERMISSION_STORGE:
                    requestStorageAndCheckNotReminder(fragment.getActivity(), requestCode, task);
                    break;
            }
        } else {
            task.trySetResult(null);
        }
        return task.getTask();
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return true;
        }
        return false;
    }

    /**
     * @param permissions permissions array, cannot be null
     * @return return true, if all permissions have been granted.
     */
    public static boolean checkSelfPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    private static void showPermissionRationale(Activity host, final Runnable action) {
        new AlertDialog.Builder(host)
                .setMessage("权限")
                .setCancelable(false)
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action.run();
                    }
                }).show();
    }

    private static void jump2Setting(String txt, Activity host) {
        AlertDialog.Builder builder = new AlertDialog.Builder(host);
        builder.setTitle("授权" + ": " + txt);
        builder.setMessage("需要允许授权才可使用");
        builder.setPositiveButton("去授权", (dialog, id) -> {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", host.getPackageName(), null);
            intent.setData(uri);
            //调起应用设置页面
            host.startActivityForResult(intent, 2);
        });
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    public static boolean onPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        TaskCompletionSource<Void> source = sPendingTasks.get(requestCode);
        if (source == null) return false;
        Log.v("Permission", String.format("onRequestPermissionsResult(%d,%s,%s)", requestCode,
                Arrays.toString(permissions), Arrays.toString(grantResults)));
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_CAMERA:
                mMmkv.encode(CAMERA_APPLY, true);
                break;
            case REQUEST_CODE_PERMISSION_AUDIO:
                mMmkv.encode(AUDIO_APPLY, true);
                break;
            case REQUEST_CODE_PERMISSION_STORGE:
                mMmkv.encode(STORAGE_APPLY, true);
                break;
        }
        boolean granted = false;
        for (int result : grantResults) {
            // has granted permission
            if (granted = result == 0)
                break;
        }
        if (granted) {
            source.trySetResult(null);
        } else {
            source.trySetCancelled();
        }
        // delete from queue
        sPendingTasks.delete(requestCode);
        return true;
    }
}
