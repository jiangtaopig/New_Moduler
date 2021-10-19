package com.zjt.startmodepro

import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.widget.MyPopWindow

class TestPopWindowActivity : AppCompatActivity() {

    private var tv :TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_window_layout)
        tv = findViewById(R.id.test_pop_window)
        tv?.setOnClickListener {
            val popupWindow = MyPopWindow(this)
            popupWindow.show(tv!!, "pop_window 哈哈哈太好了")
        }

        val tv2 = findViewById<TextView>(R.id.test_pop_window2)
        tv2.setOnClickListener {
            val popupWindow = MyPopWindow(this)
            popupWindow.show(tv2!!, "pop window 2 哈哈哈太好了吧")
        }

    }

    override fun onResume() {
        super.onResume()
        // 此时 tv 的 windowToken 还未赋值，所以会报
        val popupWindow = MyPopWindow(this)
        tv?.let { popupWindow.show(it, "我是 popWindow 哈哈") }
    }

    private fun setBackgroundAlpha( bgAlpha :Float){
        val layoutParams = window.attributes
        layoutParams.alpha = bgAlpha
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = layoutParams
    }
}