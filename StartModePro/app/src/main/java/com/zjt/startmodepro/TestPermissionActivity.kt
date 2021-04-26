package com.zjt.startmodepro

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.tencent.mmkv.MMKV

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
    }

    @SuppressLint("NewApi")
    private fun initView() {
        findViewById<Button>(R.id.btn_apply_permission)
                .setOnClickListener {
//                    requestCameraAndCheckNotReminder()
//                    requestStorageAndCheckNotReminder()
                    showPermissionDialog()
                }
    }

    private fun showPermissionDialog() {
        val mConfiguration: Configuration = this.resources.configuration
        mOrientation = mConfiguration.orientation
        Log.e("permission", "mOrientation = $mOrientation , landscape = ${Configuration.ORIENTATION_LANDSCAPE}")

        mPermissionDialog.setOnPermissionClickListener(object : PermissionDialog.OnPermissionClickListener {
            override fun onPermissionClick(type: Int) {
                when (type) {
                    PermissionDialog.CAMERA_TYPE -> requestCameraAndCheckNotReminder()
                    PermissionDialog.STORAGE_TYPE -> requestStorageAndCheckNotReminder()
                    PermissionDialog.ALL_TYPE -> requestAll()
                }
            }

        })
        mPermissionDialog.show(supportFragmentManager, "PERMISSION_DIALOG")
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
            if (mMmkv.decodeBool(CAMERA_APPLIED, false) && !shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                jump2Setting("相机", CAMERA_REQUEST_CODE)

            } else {
                requestCamera()
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
            if (mMmkv.decodeBool(STORAGE_APPLIED, false) && !shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                jump2Setting("存储", STORAGE_REQUEST_CODE)
            } else {
                requestStorage()
            }
        }
    }

    private fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("NewApi")
    private fun requestAll() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                mMmkv.encode(CAMERA_APPLIED, true)
                if (grantResults[0] == 0) {// 表示用户同意授权
                    mPermissionDialog.setCameraOpen()
                    mMmkv.encode(CAMERA_APPLIED, false)
                }
            }
            STORAGE_REQUEST_CODE -> {
                mMmkv.encode(STORAGE_APPLIED, true)
                if (grantResults[0] == 0) {
                    mPermissionDialog.setStorageOpen()
                    mMmkv.encode(STORAGE_APPLIED, false)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (hasPermission(Manifest.permission.CAMERA)) {// 表示用户同意授权
                    mMmkv.encode(CAMERA_APPLIED, false)
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    mMmkv.encode(STORAGE_APPLIED, false)
                }
            }
        }
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 1
        private const val STORAGE_REQUEST_CODE = CAMERA_REQUEST_CODE + 1

        private const val CAMERA_APPLIED = "camera_applied"
        private const val STORAGE_APPLIED = "storage_applied"
    }
}