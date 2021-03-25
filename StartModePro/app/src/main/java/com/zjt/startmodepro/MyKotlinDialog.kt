package com.zjt.startmodepro

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class MyKotlinDialog : DialogFragment() , DialogInterface {

    private lateinit var mTitleTv : TextView
    private var mTitle : String? = null

    companion object{
        private const val TITLE_KEY = "title"
        fun getInstance (title: String?) : MyKotlinDialog {
            val dialog = MyKotlinDialog()
            dialog.apply {
                arguments = Bundle().apply {
                    putString(TITLE_KEY, title)
                }
            }
            return dialog
        }
    }

    override fun cancel() {

    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        var params : WindowManager.LayoutParams = window?.attributes!!
        params.gravity = Gravity.BOTTOM // BUTTON 表示在显示在屏幕的底部，CENTER 表示在屏幕的中间
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.dialog_my_kotlin_layout, container, false)
//    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.BottomDialogTheme)
        dialog.setContentView(R.layout.dialog_my_kotlin_layout)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true) //触摸对话框外面的区域，对话框消失
        initView(dialog)
        initData()
        return dialog
    }

    private fun initData() {
        arguments?.run {
            mTitle = getString(TITLE_KEY)
        }
        mTitleTv.text = mTitle
    }

    private fun initView(dialog: Dialog) {
        mTitleTv = dialog.findViewById(R.id.txt_title)
    }
}