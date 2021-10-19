package com.zjt.startmodepro.widget

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.zjt.startmodepro.R

class ProgressDialog(context: Context) : AlertDialog(context) {

    companion object {
        fun showProgressDialog(context: Context) {
            val progressDialog = ProgressDialog(context)
            val window = progressDialog.window
            window?.setDimAmount(0f)
//            var params: WindowManager.LayoutParams = window?.attributes!!
//          params.gravity = Gravity.CENTER // BOTTOM 表示在显示在屏幕的底部，CENTER 表示在屏幕的中间
//          params.windowAnimations = R.style.MyDialogStyleBottom
//            params.width = WindowManager.LayoutParams.WRAP_CONTENT
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT
//
//            window.attributes = params
//            window.setBackgroundDrawable(null)

            progressDialog.show()
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.progreass_dialog_layout, null as ViewGroup?)
        setView(view)
        super.onCreate(savedInstanceState)
    }
}