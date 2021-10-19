package com.zjt.startmodepro.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.zjt.startmodepro.R
import com.zjt.startmodepro.utils.DisplayUtil
import kotlin.math.abs

class MyPopWindow constructor(val context: Context?) :
    PopupWindow(context, null, 0) { // 0 就是为了去掉PopWindow的阴影效果

//    constructor(context: Context?, attrs : AttributeSet?) : this(context = context, attrs = null)

    private var titleTv: TextView? = null
    private var rootView: View? = null
    private var upArrowImg: ImageView? = null
    private var screenWidth  = 0

    init {
        initView()
    }

    @SuppressLint("InflateParams")
    private fun initView() {

        context?.let {
            screenWidth = DisplayUtil.getScreenWidth(it)
            rootView = LayoutInflater.from(it).inflate(R.layout.pop_window_layout, null, false)
            titleTv = rootView?.findViewById(R.id.pop_title_tv)
            upArrowImg = rootView?.findViewById(R.id.up_arrow_img)
            contentView = rootView
            contentView.apply {
                isOutsideTouchable = true
                isFocusable = false

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    fun show(anchorView: View, content: String) {

        titleTv?.text = content

        val x = anchorView.x
        val y = anchorView.y
        val width = anchorView.width
        val height = anchorView.height

        /**
         * anchorView 在屏幕中的位置
         */
        val anchorPos = intArrayOf(-1, -1)
        anchorView.getLocationOnScreen(anchorPos)
        val posX = anchorPos[0]
        val posY = anchorPos[1]
        val statusBarHeight = DisplayUtil.getStatusBarHeight(context)

        /**
         * 因为这时候还未绘制，所以 width = 0 ,height = 0; 所以要主动测量
         * 获取 measuredWidth
         */
        getSizeBeforeDraw()

        val contentViewWidth = contentView.measuredWidth
        val contentViewHeight = contentView.measuredHeight
        Log.e(
            "MyPopWindow",
            "x = $x, y = $y, width = $width, height = $height, posX = $posX, posY = $posY, " +
                    "\n statusBarHeight = $statusBarHeight, contentViewWidth = $contentViewWidth, contentViewHeight = $contentViewHeight" +
                    "\n screenWidth = $screenWidth"

        )
        // anchorView.windowToken == null 防止在 onCreate/onResume 中调用时 anchorView.windowToken 还没有被赋值
        if (context == null || anchorView.windowToken == null) return
        if (context is Activity) {
            val activity = context
            if (activity.isFinishing || activity.isDestroyed) return
        }

        val upArrowImgWidth = upArrowImg?.measuredWidth ?: 0
        var offX = (width - contentViewWidth) / 2
        val arrowX = contentViewWidth / 2 - upArrowImgWidth / 2

        Log.e("MyPopWindow", "offX = $offX, arrowX = $arrowX, upArrowImgWidth = $upArrowImgWidth")

        val params: LinearLayout.LayoutParams = upArrowImg?.layoutParams as LinearLayout.LayoutParams
        params.leftMargin = arrowX
        // 防止贴着屏幕的右边，所以 -3
        if (posX + contentViewWidth > screenWidth){
            offX = screenWidth - (posX + contentViewWidth) -3
        }

        // 防止贴着屏幕的左边，所以 -3
        if (offX < 0) {
            if (abs(offX) > posX) {
                offX = -(posX - 3)
            }
        }
        Log.e("MyPopWindow", "offX = $offX")

        showAsDropDown(anchorView, offX, 0)
    }

    private fun makeDropDownMeasureSpec(measureSpec: Int): Int {
        val mode: Int = if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            View.MeasureSpec.UNSPECIFIED
        } else {
            View.MeasureSpec.EXACTLY
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode)
    }

    private fun getSizeBeforeDraw() {
        contentView?.measure(makeDropDownMeasureSpec(width), makeDropDownMeasureSpec(height))
    }

}