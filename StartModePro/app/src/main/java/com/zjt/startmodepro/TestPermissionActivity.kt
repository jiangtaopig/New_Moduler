package com.zjt.startmodepro

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/23 6:10 下午

 * @Description : TestPermissionActivity

 */


class TestPermissionActivity : AppCompatActivity() {

    private var mDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_layout)
        initView()


        showPermissionDialog()
//        requestAll()
    }

    @SuppressLint("NewApi")
    private fun initView() {
        findViewById<Button>(R.id.btn_apply_permission)
                .setOnClickListener {
                    requestCameraAndCheckNotReminder()
                    requestStorageAndCheckNotReminder()
                }
    }

    private fun showPermissionDialog() {
        val permissionDialog = PermissionDialog.getInstance("xxx")
        permissionDialog.setOnPermissionClickListener(object : PermissionDialog.OnPermissionClickListener {
            override fun onPermissionClick(type: Int) {
                when (type) {
                    PermissionDialog.CAMERA_TYPE -> requestCamera()
                    PermissionDialog.STORAGE_TYPE -> requestStorage()
                    PermissionDialog.ALL_TYPE -> requestAll()
                }
            }

        })
        permissionDialog.show(supportFragmentManager, "PERMISSION_DIALOG")
    }

    @SuppressLint("NewApi")
    private fun requestCamera() {
        if (ContextCompat.checkSelfPermission(this!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    @SuppressLint("NewApi")
    private fun requestCameraAndCheckNotReminder() {
        if (ContextCompat.checkSelfPermission(this!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 第一次申请权限时 返回false
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                requestCamera()
            } else {
                jump2Setting("相机")
            }
        }
    }

    private fun requestStorage() {
        if (ContextCompat.checkSelfPermission(this!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun requestStorageAndCheckNotReminder() {
        if (ContextCompat.checkSelfPermission(this!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                requestStorage()
            } else {
                jump2Setting("存储")
            }
        }
    }

    @SuppressLint("NewApi")
    private fun requestAll() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    private fun jump2Setting(permission: String) {
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
            startActivityForResult(intent, 2)
        }
        mDialog = builder.create()
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            for (i in permissions.indices) {
                //已授权
                if (grantResults[i] == 0) {
                    continue
                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    //选择禁止/拒绝
                } else {
                    //选择拒绝并不再询问
                }
            }
        }
    }

}