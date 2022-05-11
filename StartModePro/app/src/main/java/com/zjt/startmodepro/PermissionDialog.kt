package com.zjt.startmodepro

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.zjt.startmodepro.utils.DisplayUtil

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/23 7:06 下午

 * @Description : PermissionDialog

 */


class PermissionDialog : DialogFragment() {

    private var onPermissionClickListener: OnPermissionClickListener? = null
    private var mCameraTxt: TextView? = null
    private var mStorageTxt: TextView? = null
    private var mAllTxt: TextView? = null

    private var mHasCameraPermission: Boolean = false
    private var mHasStoragePermission: Boolean = false


    companion object {
        const val CAMERA_TYPE = 1
        const val STORAGE_TYPE = CAMERA_TYPE + 1
        const val ALL_TYPE = STORAGE_TYPE + 1

        private const val ORIENTATION_KEY = "orientation"
        fun getInstance(orientation: Int): PermissionDialog {
            val dialog = PermissionDialog()
            dialog.apply {
                arguments = Bundle().apply {
                    putInt(ORIENTATION_KEY, orientation)
                }
            }
            return dialog
        }
    }


    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        val params: WindowManager.LayoutParams = window?.attributes!!
        params.gravity = if (isScreenPortrait()) Gravity.BOTTOM else Gravity.END // BOTTOM 表示在显示在屏幕的底部，CENTER 表示在屏幕的中间

        val width = if (isScreenPortrait()) ViewGroup.LayoutParams.MATCH_PARENT else landWidth()
        val height = if (isScreenPortrait()) portraitHeight() else 800
        window.setLayout(width, height)

//        if (isScreenPortrait()) {
//            window.setWindowAnimations(R.style.BottomDialogTheme)
//        } else {
//            window.setWindowAnimations(R.style.MyDialogStyleRight)
//        }
    }

    private fun portraitHeight(): Int {
        return DisplayUtil.dip2px(300f)
    }

    private fun landWidth(): Int {
        return DisplayUtil.dip2px(376f)
    }

    private fun isScreenPortrait(): Boolean {
        return Configuration.ORIENTATION_PORTRAIT == arguments?.getInt(ORIENTATION_KEY)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val style = if (isScreenPortrait()) {
            R.style.BottomDialogTheme
        } else {
            R.style.MyDialogStyleRight
        }
        val dialog = Dialog(activity!!, style)
        dialog.setContentView(R.layout.dialog_permission_layout)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true) //触摸对话框外面的区域，对话框消失
        initView(dialog)
        initData()
        return dialog
    }

    private fun initView(dialog: Dialog) {
        mCameraTxt = dialog.findViewById(R.id.txt_camera)
        mStorageTxt = dialog.findViewById(R.id.txt_storage)
        mAllTxt = dialog.findViewById(R.id.txt_all)

        dialog.findViewById<TextView>(R.id.txt_slide_from_right)
            .setOnClickListener {
                RightDialog.getInstance().show(childFragmentManager, "RightDialog")
            }

        mAllTxt?.setOnClickListener {
            onPermissionClickListener?.onPermissionClick(ALL_TYPE)
        }
    }

    private fun initData() {
        if (mHasCameraPermission) {
            mCameraTxt?.text = "相机权限已开启"
            mCameraTxt?.setOnClickListener { }
        } else {
            mCameraTxt?.setOnClickListener {
                onPermissionClickListener?.onPermissionClick(CAMERA_TYPE)
            }
        }

        if (mHasStoragePermission) {
            mStorageTxt?.text = "存储权限已开启"
            mStorageTxt?.setOnClickListener { }
        } else {
            mStorageTxt?.setOnClickListener {
                onPermissionClickListener?.onPermissionClick(STORAGE_TYPE)
            }
        }
    }

    fun setCameraOpen() {
        mCameraTxt?.text = "相机权限已开启"
    }

    fun setStorageOpen() {
        mStorageTxt?.text = "存储权限已开启"
    }

    fun setHasCameraPermission(hasPermission: Boolean) {
        mHasCameraPermission = hasPermission
    }

    fun setStoragePermission(hasPermission: Boolean) {
        mHasStoragePermission = hasPermission
    }

    fun setOnPermissionClickListener(listener: OnPermissionClickListener) {
        onPermissionClickListener = listener
    }

    interface OnPermissionClickListener {
        fun onPermissionClick(type: Int)
    }

}