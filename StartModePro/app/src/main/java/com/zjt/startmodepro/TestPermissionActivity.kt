package com.zjt.startmodepro

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bolts.Continuation
import bolts.Task
import bolts.Task.UI_THREAD_EXECUTOR
import bolts.Task.create
import com.tencent.mmkv.MMKV
import com.zjt.startmodepro.utils.DisplayUtil
import com.zjt.startmodepro.utils.PermissionsChecker
import com.zjt.startmodepro.utils.PermissionsHelper

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/23 6:10 下午

 * @Description : TestPermissionActivity

 */


class TestPermissionActivity : AppCompatActivity() {

    private var mDialog: AlertDialog? = null
    private lateinit var mMmkv: MMKV
    private var mOrientation: Int = -1
    private val mPermissionDialog: PermissionDialog by lazy {
        PermissionDialog.getInstance(mOrientation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_layout)
        initView()
        mMmkv = MMKV.mmkvWithID("z_aron")!!
//        showPermissionDialog()
        requestCameraAndCheckNotReminder()
//        requestAll()
        val model = Build.MODEL
        Log.e("TestPermissionActivity", "model = $model")
    }

    override fun onDestroy() {
        // 测试 leakcanary
        super.onDestroy()
        val a = 1
    }

    @SuppressLint("NewApi")
    private fun initView() {
        findViewById<TextView>(R.id.btn_change_orientation)
            .setOnClickListener {
                //  横竖屏切换 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                requestedOrientation = if (isPortrait()) {
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                } else {
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }

        findViewById<TextView>(R.id.btn_get_screen_size)
            .setOnClickListener {
                val screenWidth = DisplayUtil.getScreenWidth(this)
                val screenHeight = DisplayUtil.getScreenHeight(this)
                Log.e("TestPermissionActivity", ">>>> screenWidth = $screenWidth, screenHeight = $screenHeight <<<<")
            }

        findViewById<TextView>(R.id.btn_apply_permission)
                .setOnClickListener {
//                    requestCameraAndCheckNotReminder()
//                    requestStorageAndCheckNotReminder()
                    showPermissionDialog()
//                    requestAlbum()
//                    getPermission()
//                    requestAllPermission()
//                    grantStorage()
//                    grantAudio()
//                    requestAllPermission2()
//                    requestAll()


                }
    }

    private fun showPermissionDialog() {
        val mConfiguration: Configuration = this.resources.configuration
        mOrientation = mConfiguration.orientation
        Log.e("permission", "mOrientation = $mOrientation , landscape = ${Configuration.ORIENTATION_LANDSCAPE}")

        val grantedCamera = hasPermission(Manifest.permission.CAMERA)
        val grantedStorage = hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        // 如果所有权限都已开启，则不弹出 Dialog
        if (grantedCamera && grantedStorage) {
            Toast.makeText(this, "相机和读写权限均已开启", Toast.LENGTH_LONG).show()
//            return
        }

        mPermissionDialog.setOnPermissionClickListener(object : PermissionDialog.OnPermissionClickListener {
            override fun onPermissionClick(type: Int) {
                when (type) {
                    PermissionDialog.CAMERA_TYPE -> requestCameraAndCheckNotReminder()
                    PermissionDialog.STORAGE_TYPE -> requestStorageAndCheckNotReminder()
                    PermissionDialog.ALL_TYPE -> requestAll()
                }
            }

        })
        mPermissionDialog.setHasCameraPermission(grantedCamera)
        mPermissionDialog.setStoragePermission(grantedStorage)
        mPermissionDialog.show(supportFragmentManager, "PERMISSION_DIALOG")
    }

    private fun isPortrait () :Boolean {
        val configuration: Configuration = resources.configuration
        val orientation = configuration.orientation
        return Configuration.ORIENTATION_PORTRAIT == orientation
    }

    @SuppressLint("NewApi")
    private fun requestCamera() {
        if (!hasPermission(Manifest.permission.CAMERA)) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    @SuppressLint("NewApi")
    private fun requestCameraAndCheckNotReminder() {
        if (!hasPermission(Manifest.permission.CAMERA)) {
            if (!mMmkv.decodeBool(CAMERA_APPLIED, false)) {
                requestCamera()
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    jump2Setting("相机", CAMERA_REQUEST_CODE)
                } else {
                    requestCamera()
                }
            }
        }
    }

    private fun requestStorage() {
        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun requestStorageAndCheckNotReminder() {
        if (!hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // 之前申请权限时如果是拒绝且不再提醒，那么跳转到系统的权限界面; 拒绝且不再提醒 shouldShowRequestPermissionRationale 返回false
            // 如果之前未申请过该权限 shouldShowRequestPermissionRationale 也返回 false
            if (!mMmkv.decodeBool(STORAGE_APPLIED, false)) { // 表示第一次申请该权限
                requestStorage()
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    jump2Setting("存储", STORAGE_REQUEST_CODE)
                } else {
                    requestStorage()
                }
            }
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("NewApi")
    private fun requestAll() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), ALL_REQUEST_CODE)
    }

    private fun grantStorage() {
        PermissionsHelper.grantExternalPermissions(this) // 相册
                .continueWith(Continuation<Void, Void> { albumTask ->
                    if (albumTask.isFaulted || albumTask.isCancelled) {
                        if (albumTask.isCancelled) {
                            Toast.makeText(this, "未打开读写权限", Toast.LENGTH_LONG).show()
                        }
                    }
                    null
                }, Task.UI_THREAD_EXECUTOR)
    }

    private fun grantCamera() {
        PermissionsHelper.grantCameraPermission(this) // 相册
                .continueWith(Continuation<Void, Void> { albumTask ->
                    if (albumTask.isFaulted || albumTask.isCancelled) {
                        if (albumTask.isCancelled) {
                            Toast.makeText(this, "未打开读写权限", Toast.LENGTH_LONG).show()
                        }
                    }
                    null
                }, Task.UI_THREAD_EXECUTOR)
    }

    private fun grantAudio() {
        PermissionsHelper.grantAudioPermission(this) // 相册
                .continueWith(Continuation<Void, Void> { albumTask ->
                    if (albumTask.isFaulted || albumTask.isCancelled) {
                        if (albumTask.isCancelled) {
                            Toast.makeText(this, "未打开读写权限", Toast.LENGTH_LONG).show()
                        }
                    }
                    null
                }, Task.UI_THREAD_EXECUTOR)
    }

    private fun requestAllPermission() {
        PermissionsChecker.grantCameraPermission(this) // 相机
                .continueWith(Continuation<Void, Void> { cameraTask ->
                    if (cameraTask.isFaulted || cameraTask.isCancelled) { //未获取到权限
                        if (cameraTask.isCancelled) {
                            Toast.makeText(this, "未打开相机权限", Toast.LENGTH_LONG).show()
                        }
                    }
                    PermissionsChecker.grantAudioPermission(this) // 相册
                            .continueWith(Continuation<Void, Void> { albumTask ->
                                if (albumTask.isFaulted || albumTask.isCancelled) { //未获取到权限
                                    if (albumTask.isCancelled) {
                                        Toast.makeText(this, "未打开麦克风权限", Toast.LENGTH_LONG).show()
                                    }
                                }
                                PermissionsChecker.grantExternalPermissions(this) // 相册
                                        .continueWith(Continuation<Void, Void> { albumTask ->
                                            if (albumTask.isFaulted || albumTask.isCancelled) {
                                                if (albumTask.isCancelled) {
                                                    Toast.makeText(this, "未打开读写权限", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                            null
                                        }, Task.UI_THREAD_EXECUTOR)
                                null
                            }, Task.UI_THREAD_EXECUTOR)
                    null
                }, Task.UI_THREAD_EXECUTOR)
    }

    private fun requestAllPermission2() {
        PermissionsHelper.grantCameraPermission(this) // 相机
                .continueWith(Continuation<Void, Void> { cameraTask ->
                    if (cameraTask.isFaulted || cameraTask.isCancelled) { //未获取到权限
                        if (cameraTask.isCancelled) {
                            Toast.makeText(this, "未打开相机权限", Toast.LENGTH_LONG).show()
                        }
                    }
                    PermissionsHelper.grantAudioPermission(this) // 相册
                            .continueWith(Continuation<Void, Void> { albumTask ->
                                if (albumTask.isFaulted || albumTask.isCancelled) { //未获取到权限
                                    if (albumTask.isCancelled) {
                                        Toast.makeText(this, "未打开麦克风权限", Toast.LENGTH_LONG).show()
                                    }
                                }
                                PermissionsHelper.grantExternalPermissions(this) // 相册
                                        .continueWith(Continuation<Void, Void> { albumTask ->
                                            if (albumTask.isFaulted || albumTask.isCancelled) {
                                                if (albumTask.isCancelled) {
                                                    Toast.makeText(this, "未打开读写权限", Toast.LENGTH_LONG).show()
                                                }
                                            }
                                            null
                                        }, Task.UI_THREAD_EXECUTOR)
                                null
                            }, Task.UI_THREAD_EXECUTOR)
                    null
                }, Task.UI_THREAD_EXECUTOR)
    }


    private fun requestAudio() {
        if (!hasPermission(Manifest.permission.RECORD_AUDIO)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), AUDIO_REQUEST_CODE)
            }
        }
    }

    private fun jump2Setting(permission: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("授权 $permission")
        builder.setMessage("需要允许授权才可使用")
        builder.setPositiveButton("去授权") { dialog: DialogInterface?, id: Int ->
            if (mDialog != null && mDialog!!.isShowing()) {
                mDialog!!.dismiss()
            }
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", this.packageName, null)
            intent.data = uri
            //调起应用设置页面
            startActivityForResult(intent, requestCode)
        }
        mDialog = builder.create()
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getPermission() {
        val tsk = create<Void>()
        if (!hasPermission(Manifest.permission.CAMERA)) {
            if (!mMmkv.decodeBool(CAMERA_APPLIED, false)) {
                requestCamera()
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    jump2Setting("相机", CAMERA_REQUEST_CODE)
                } else {
                    requestCamera()
                }
            }
        } else {
            tsk.trySetResult(null)
        }
        tsk.task.continueWith(Continuation<Void, Void> { albumTask ->
            if (albumTask.isFaulted || albumTask.isCancelled) { //未获取到权限
                if (albumTask.isCancelled) {
                    val a = "123"
                }
            }
            null
        }, UI_THREAD_EXECUTOR)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsChecker.onPermissionResult(requestCode, permissions, grantResults)
        PermissionsHelper.onPermissionResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                mMmkv.encode(CAMERA_APPLIED, true)
                val show = shouldShowRequestPermissionRationale(permissions[0])
                if (grantResults[0] == 0) {// 表示用户同意授权
                    mPermissionDialog.setCameraOpen()
                    mMmkv.encode(CAMERA_APPLIED, false)
                } else {
                    Toast.makeText(this, "请去设置中打开相机权限", Toast.LENGTH_LONG).show()
                }
            }
            STORAGE_REQUEST_CODE -> {
                mMmkv.encode(STORAGE_APPLIED, true)
                if (grantResults[0] == 0) {
                    mPermissionDialog.setStorageOpen()
                    mMmkv.encode(STORAGE_APPLIED, false)
                } else {
                    Toast.makeText(this, "请去设置中打开文件权限", Toast.LENGTH_LONG).show()
                }
            }
            ALL_REQUEST_CODE -> {
                Log.e("ALL_REQUEST_CODE", "------")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (hasPermission(Manifest.permission.CAMERA)) {// 表示用户同意授权
//                    mMmkv.encode(CAMERA_APPLIED, false)
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    mMmkv.encode(STORAGE_APPLIED, false)
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 1
        private const val STORAGE_REQUEST_CODE = CAMERA_REQUEST_CODE + 1
        private const val AUDIO_REQUEST_CODE = STORAGE_REQUEST_CODE + 1
        private const val ALL_REQUEST_CODE = AUDIO_REQUEST_CODE + 1

        private const val CAMERA_APPLIED = "camera_applied"
        private const val STORAGE_APPLIED = "storage_applied"
    }
}