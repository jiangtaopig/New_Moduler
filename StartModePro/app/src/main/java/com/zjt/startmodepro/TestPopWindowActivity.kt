package com.zjt.startmodepro

import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.widget.MyPopWindow

class TestPopWindowActivity : AppCompatActivity() {

    private var tv: TextView? = null
    private lateinit var popWindowHelper: PopWindowHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_window_layout)
        tv = findViewById(R.id.test_pop_window)

        popWindowHelper = PopWindowHelper(this)

        tv?.setOnClickListener {
//            val popupWindow = MyPopWindow(this)
//            popupWindow.showBubble(tv!!, "pop_window", MyPopWindow.ABOVE_ANCHOR_VIEW)
            popWindowHelper.show(tv!!, "pop_window", MyPopWindow.ABOVE_ANCHOR_VIEW)
        }

        val tv2 = findViewById<TextView>(R.id.test_pop_window2)
        tv2.setOnClickListener {
//            val popupWindow = MyPopWindow(this)
//            popupWindow.showBubble(tv2!!, "pop window 2 哈哈哈太好了吧", MyPopWindow.ABOVE_ANCHOR_VIEW)
            popWindowHelper.show(tv2!!, "pop window 2 哈哈哈太好了吧", MyPopWindow.ABOVE_ANCHOR_VIEW)
        }

        val tv3 = findViewById<TextView>(R.id.test_pop_window3)
        tv3.setOnClickListener {
            popWindowHelper.show(tv3!!, "pop window 3 牛逼啊", MyPopWindow.BELOW_ANCHOR_VIEW)
//            val popupWindow = MyPopWindow(this)
//            popupWindow.showBubble(tv3!!, "pop window 2 哈哈哈太好了吧", MyPopWindow.BELOW_ANCHOR_VIEW)
        }

        val rightPop = findViewById<TextView>(R.id.test_right_pop_window)

        rightPop.setOnClickListener {
            val popupWindow = MyPopWindow(this)
            popupWindow.showBubble(it, "我是 right bubble", MyPopWindow.RIGHT_ANCHOR_VIEW) {

            }
        }

        val leftPop = findViewById<TextView>(R.id.test_left_pop_window)
        leftPop.setOnClickListener {
            val popupWindow = MyPopWindow(this)
            popupWindow.showBubble(it, "我是 left bubble", MyPopWindow.LEFT_ANCHOR_VIEW) {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 此时 tv 的 windowToken 还未赋值，所以会报
        val popupWindow = MyPopWindow(this)
        tv?.let {
            popupWindow.showBubble(it, "我是 popWindow 哈哈", MyPopWindow.ABOVE_ANCHOR_VIEW) {

            }
        }
    }

    private fun setBackgroundAlpha(bgAlpha: Float) {
        val layoutParams = window.attributes
        layoutParams.alpha = bgAlpha
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = layoutParams
    }
}