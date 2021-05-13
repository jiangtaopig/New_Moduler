package com.zjt.startmodepro.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.zjt.startmodepro.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.Arrays;

import bolts.Continuation;
import bolts.Task;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/5/7 5:05 下午
 * @Description : PermissionsChecker
 */


public class PermissionsChecker {
    public static final String[] CAMERA_PERMISSION = {"android.permission.CAMERA"};
    public static final String[] AUDIO_PERMISSION = {"android.permission.RECORD_AUDIO"};
    public static final String[] STORGE_PERMISSION = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final int REQUEST_CODE_PERMISSION_AUDIO = 0x10;
    public static final int REQUEST_CODE_PERMISSION_CAMERA = 0x11;
    public static final int REQUEST_CODE_PERMISSION_STORGE = 0x12;
    private static final SparseArray<Task<Void>.TaskCompletionSource> sPendingTasks = new SparseArray<>();
    private static AlertDialog mDialog;
    /**
     * avoid show rationale again and again
     */
    private static final SparseBooleanArray sShownRationaleCodes = new SparseBooleanArray();

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
        return grantPermissions(activity, CAMERA_PERMISSION, REQUEST_CODE_PERMISSION_CAMERA,
                R.string.dialog_msg_live_request_camera_permission);
    }

    public static Task<Void> grantAudioPermission(Activity activity) {
        return grantPermissions(activity, AUDIO_PERMISSION, REQUEST_CODE_PERMISSION_AUDIO,
                R.string.dialog_msg_live_request_audio_permission);
    }
    public static Task<Void> grantStoragePermission(Activity activity) {
        return grantPermissions(activity, STORGE_PERMISSION, REQUEST_CODE_PERMISSION_STORGE,
                R.string.dialog_msg_live_request_sdcard_write_permission);
    }
    /**
     * @param fragment The subclass of {@link Fragment}
     * @return The task indicates granted or not. only response result if granted.
     * @see #getExternalPublicDir
     */
    public static <F extends Fragment> Task<Void> grantExternalPermissions(F fragment) {
        return grantPermissions(fragment, STORGE_PERMISSION, REQUEST_CODE_PERMISSION_STORGE,
                R.string.dialog_msg_live_request_sdcard_write_permission);
    }
    public static Task<Void> grantExternalPermissions(Activity activity) {
        return grantPermissions(activity, STORGE_PERMISSION, REQUEST_CODE_PERMISSION_STORGE,
                R.string.dialog_msg_live_request_sdcard_write_permission);
    }
    /**
     * request permissions in activity.
     *
     * @param activity       should call {@link #onPermissionResult(int, String[], int[])} later in this
     *                       activity
     * @param requestCode    request permissions code
     * @param rationaleMsgId if should show request permission rationale dialog, it is the msg.
     * @return the asynchronous task
     */
    public static Task<Void> grantPermissions(final Activity activity,
                                              final String[] permissions, final int requestCode, int
                                                      rationaleMsgId) {
        Task<Void>.TaskCompletionSource source = sPendingTasks.get(requestCode);
        if (source != null) // has a pending task already, return it
            return source.getTask();
        final Task<Void>.TaskCompletionSource tsk = Task.create();
        if (!checkSelfPermissions(activity, permissions)) {
            if (!sShownRationaleCodes.get(rationaleMsgId)
                    && shouldShowRequestPermissionRationale(activity, permissions)) {
                showPermissionRationale(activity, rationaleMsgId, new Runnable() {
                    @Override
                    public void run() {
                        sPendingTasks.put(requestCode, tsk);
                        ActivityCompat.requestPermissions(activity, permissions, requestCode);
                    }
                });
//                jump2Setting("bbbbb", activity);
                sShownRationaleCodes.put(rationaleMsgId, true);
            } else {
                sPendingTasks.put(requestCode, tsk);
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            }
        } else {
            tsk.trySetResult(null);
        }
        return tsk.getTask();
    }

    /**
     * request permissions in fragment
     *
     * @param fragment       should call {@link #onPermissionResult(int, String[], int[])} later in this
     *                       fragment
     * @param requestCode    request permissions code
     * @param rationaleMsgId if should show request permission rationale dialog, it is the msg.
     * @return the asynchronous task
     */
    public static Task<Void> grantPermissions(final Fragment fragment, final String[] permissions,
                                              final int requestCode, int rationaleMsgId) {
        Task<Void>.TaskCompletionSource source = sPendingTasks.get(requestCode);
        if (source != null) // has a pending task already, return it
            return source.getTask();
        final Task<Void>.TaskCompletionSource tsk = Task.create();
        Context context = fragment.getContext();
        if (!checkSelfPermissions(context, permissions)) {
            if (!sShownRationaleCodes.get(rationaleMsgId)
                    && shouldShowRequestPermissionRationale(fragment.getActivity(), permissions)) {
//                showPermissionRationale(fragment.getActivity(), rationaleMsgId, new Runnable() {
//                    @Override
//                    public void run() {
//                        sPendingTasks.put(requestCode, tsk);
//                        fragment.requestPermissions(permissions, requestCode);
//                    }
//                });
                jump2Setting("aaaaa", fragment.getActivity());
                sShownRationaleCodes.put(rationaleMsgId, true);
            } else {
                sPendingTasks.put(requestCode, tsk);
                fragment.requestPermissions(permissions, requestCode);
                // the result coming later in onPermissionResult()
            }
        } else {
            tsk.trySetResult(null);
        }
        return tsk.getTask();
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

    private static void showPermissionRationale(Activity host, int msgId, final Runnable action) {
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

    private static void jump2Setting(String txt, Activity host){
        AlertDialog.Builder builder = new AlertDialog.Builder(host);
        builder.setTitle("授权" + ": "+txt);
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



    /**
     * NOTE: should be called from
     */
    public static boolean onPermissionResult(Activity activity, int requestCode, String[]
            permissions,
                                             int[] grantResults) {
        return onPermissionResult(requestCode, permissions, grantResults);
    }

    /**
     * should be called in
     * {@link androidx.fragment.app.FragmentActivity#onRequestPermissionsResult(int, String[], int[])}
     * or {@link Fragment#onRequestPermissionsResult(int, String[], int[])}
     *
     * @param requestCode  the request permissions code
     * @param permissions  the permissions
     * @param grantResults the result
     * @return handled or not
     */
    public static boolean onPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        Task<Void>.TaskCompletionSource source = sPendingTasks.get(requestCode);
        if (source == null) return false;
        Log.v("Permission", String.format("onRequestPermissionsResult(%d,%s,%s)", requestCode,
                Arrays.toString(permissions), Arrays.toString(grantResults)));
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        try {
            Class managerClass = manager.getClass();
            Method method = managerClass.getDeclaredMethod("checkOp", int.class, int.class, String.class);
            int isAllowNum = (Integer) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());

            return AppOpsManager.MODE_ALLOWED == isAllowNum;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * NOTE: should be called from fragment
     */
    public static boolean onPermissionResult(Fragment fragment, int requestCode, String[] permissions,
                                             int[] grantResults) {
        return onPermissionResult(requestCode, permissions, grantResults);
    }
}
