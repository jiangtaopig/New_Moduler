package com.zjt.startmodepro

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/23 7:06 下午

 * @Description : PermissionDialog

 */


class PermissionDialog : DialogFragment() {

    private lateinit var onPermissionClickListener: OnPermissionClickListener
    private lateinit var mCameraTxt :TextView
    private lateinit var mStorageTxt :TextView
    private lateinit var mAllTxt :TextView


    companion object{
        const val CAMERA_TYPE = 1
        const val STORAGE_TYPE = CAMERA_TYPE + 1
        const val ALL_TYPE = STORAGE_TYPE + 1

        private const val TITLE_KEY = "title"
        fun getInstance (title: String?) : PermissionDialog {
            val dialog = PermissionDialog()
            dialog.apply {
                arguments = Bundle().apply {
                    putString(TITLE_KEY, title)
                }
            }
            return dialog
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        var params : WindowManager.LayoutParams = window?.attributes!!
        params.gravity = Gravity.CENTER // BOTTOM 表示在显示在屏幕的底部，CENTER 表示在屏幕的中间
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!)
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

    fun setOnPermissionClickListener(listener: OnPermissionClickListener) {
        onPermissionClickListener = listener
    }

    interface OnPermissionClickListener{
        fun onPermissionClick(type :Int)
    }

}