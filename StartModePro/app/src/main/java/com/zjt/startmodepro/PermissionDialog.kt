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

    private lateinit var onPermissionClickListener: OnPermissionClickListener
    private lateinit var mCameraTxt: TextView
    private lateinit var mStorageTxt: TextView
    private lateinit var mAllTxt: TextView


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
        params.gravity = Gravity.BOTTOM // BOTTOM 表示在显示在屏幕的底部，CENTER 表示在屏幕的中间

        val width = if (isScreenPortrait()) ViewGroup.LayoutParams.MATCH_PARENT else landWidth()
        val height = if (isScreenPortrait()) portraitHeight() else ViewGroup.LayoutParams.MATCH_PARENT

        window.setLayout(width, height)
        params.gravity = if (isScreenPortrait()) Gravity.BOTTOM else Gravity.RIGHT
        if (isScreenPortrait()) {
            window.setWindowAnimations(R.style.MyDialogStyleBottom)
        } else {
            window.setWindowAnimations(R.style.MyDialogStyleRight)
        }
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
        val dialog = Dialog(activity!!, R.style.MyDialogStyleBottom)
        dialog.setContentView(R.layout.dialog_permission_layout)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true) //触摸对话框外面的区域，对话框消失
        initView(dialog)
//        initData()
        return dialog
    }

    private fun initView(dialog: Dialog) {
        mCameraTxt = dialog.findViewById(R.id.txt_camera)
        mStorageTxt = dialog.findViewById(R.id.txt_storage)
        mAllTxt = dialog.findViewById(R.id.txt_all)

        mCameraTxt.setOnClickListener {
            if (onPermissionClickListener != null) onPermissionClickListener.onPermissionClick(CAMERA_TYPE)
        }

        mStorageTxt.setOnClickListener {
            if (onPermissionClickListener != null) onPermissionClickListener.onPermissionClick(STORAGE_TYPE)
        }

        mAllTxt.setOnClickListener {
            if (onPermissionClickListener != null) onPermissionClickListener.onPermissionClick(ALL_TYPE)
        }
    }

    fun setCameraOpen() {
        mCameraTxt.text = "相机权限已开启"
    }

    fun setStorageOpen() {
        mStorageTxt.text = "存储权限已开启"
    }

    fun setOnPermissionClickListener(listener: OnPermissionClickListener) {
        onPermissionClickListener = listener
    }

    interface OnPermissionClickListener {
        fun onPermissionClick(type: Int)
    }

}