package com.zjt.startmodepro.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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

    companion object {
        const val BELOW_ANCHOR_VIEW = 1 //  在目标 View 的下面
        const val ABOVE_ANCHOR_VIEW = 2 // 在目标 View 的上面
        const val LEFT_ANCHOR_VIEW = 4 // 在目标 View 的左边
        const val RIGHT_ANCHOR_VIEW = 8 // 在目标 View 的右边
    }

    private var titleTv: TextView? = null
    private var rootView: View? = null
    private var upArrowImg: ImageView? = null
    private var downArrowImg: ImageView? = null
    private var screenWidth = 0
    private var mWillNotAutoDismiss = false
    private lateinit var mDismissOpt: () -> Unit

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
            downArrowImg = rootView?.findViewById(R.id.down_arrow_img)
            contentView = rootView
            contentView.apply {
                isOutsideTouchable = false
                isFocusable = false

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

    fun showBubble(anchorView: View, content: String, position :Int, dismissOpt: () -> Unit) {
        // anchorView.windowToken == null 防止在 onCreate/onResume 中调用时 anchorView.windowToken 还没有被赋值
        if (context == null || anchorView.windowToken == null) return
        if (context is Activity) {
            val activity = context
            if (activity.isFinishing || activity.isDestroyed) return
        }

        mDismissOpt = dismissOpt
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
        val statusBarHeight = DisplayUtil.getStatusBarHeight(context) // 状态栏高度

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
        var offX = (width - contentViewWidth) / 2

        // 防止贴着屏幕的右边，所以 -3
        if (posX + contentViewWidth > screenWidth) {
            offX = screenWidth - (posX + contentViewWidth) - 3
        }

        // 防止贴着屏幕的左边，所以 -3
        if (offX < 0) {
            if (abs(offX) > posX) {
                offX = -(posX - 3)
            }
        }

        Log.e("MyPopWindow","offX = $offX")

        var arrowImgWidth = downArrowImg?.measuredWidth ?: 0
        var offY = 0
        var arrowX = 0
        when (position) {
            BELOW_ANCHOR_VIEW -> {
                downArrowImg?.visibility = View.GONE
                upArrowImg?.visibility = View.VISIBLE
                arrowImgWidth = upArrowImg?.measuredWidth ?: 0
                // anchorView 中点在屏幕上的位置 减去popWindow 的x在屏幕上的位置 减去 小箭头的一半
                arrowX = posX + width / 2 - (posX + offX) - arrowImgWidth / 2
                val params: LinearLayout.LayoutParams = upArrowImg?.layoutParams as LinearLayout.LayoutParams
                params.leftMargin = arrowX
            }
            ABOVE_ANCHOR_VIEW -> {
                upArrowImg?.visibility = View.GONE
                downArrowImg?.visibility = View.VISIBLE
                arrowImgWidth = downArrowImg?.measuredWidth ?: 0
                offY = height + contentViewHeight
                // anchorView 中点在屏幕上的位置 减去popWindow 的x在屏幕上的位置 减去 小箭头的一半
                 arrowX = posX + width / 2 - (posX + offX) - arrowImgWidth / 2
                val params: LinearLayout.LayoutParams = downArrowImg?.layoutParams as LinearLayout.LayoutParams
                params.leftMargin = arrowX
            }
        }
        Log.e("MyPopWindow", "offX = $offX, arrowX = $arrowX, upArrowImgWidth = $arrowImgWidth, offY = $offY")
        showAsDropDown(anchorView, offX, -offY)
        viewShowAlpha()
    }

    /**
     * 在 anchorView 的下面
     */
    @Deprecated("废弃该方法")
    fun showBelow(anchorView: View, content: String) {
        // anchorView.windowToken == null 防止在 onCreate/onResume 中调用时 anchorView.windowToken 还没有被赋值
        if (context == null || anchorView.windowToken == null) return
        if (context is Activity) {
            val activity = context
            if (activity.isFinishing || activity.isDestroyed) return
        }

        downArrowImg?.visibility = View.GONE
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
        val statusBarHeight = DisplayUtil.getStatusBarHeight(context) // 状态栏高度

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

        val upArrowImgWidth = upArrowImg?.measuredWidth ?: 0
        var offX = (width - contentViewWidth) / 2

        // 防止贴着屏幕的右边，所以 -3
        if (posX + contentViewWidth > screenWidth) {
            offX = screenWidth - (posX + contentViewWidth) - 3
        }

        // 防止贴着屏幕的左边，所以 -3
        if (offX < 0) {
            if (abs(offX) > posX) {
                offX = -(posX - 3)
            }
        }

        // anchorView 中点在屏幕上的位置 减去popWindow 的x在屏幕上的位置 减去 小箭头的一半
        val arrowX = posX + width / 2 - (posX + offX) - upArrowImgWidth / 2
        Log.e("MyPopWindow", "offX = $offX, arrowX = $arrowX, upArrowImgWidth = $upArrowImgWidth")

        val params: LinearLayout.LayoutParams =
            upArrowImg?.layoutParams as LinearLayout.LayoutParams
        params.leftMargin = arrowX

        showAsDropDown(anchorView, offX, 0)
        viewShowAlpha()
    }

    /**
     * 在指定 anchorView 的上面
     */
    @Deprecated("废弃该方法")
    fun showUp(anchorView: View, content: String, dismissOpt: () -> Unit) {
//        mDismissOpt = dismissOpt
        // anchorView.windowToken == null 防止在 onCreate/onResume 中调用时 anchorView.windowToken 还没有被赋值
        if (context == null || anchorView.windowToken == null) return
        if (context is Activity) {
            val activity = context
            if (activity.isFinishing || activity.isDestroyed) return
        }
        upArrowImg?.visibility = View.GONE

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
        val statusBarHeight = DisplayUtil.getStatusBarHeight(context) // 状态栏高度

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

        val downArrowImgWidth = downArrowImg?.measuredWidth ?: 0
        val downArrowImgHeight = downArrowImg?.measuredHeight ?: 0
        var offX = (width - contentViewWidth) / 2
//        val arrowX = contentViewWidth / 2 - downArrowImgWidth / 2

        // 防止贴着屏幕的右边，所以 -3
        if (posX + contentViewWidth > screenWidth) {
            offX = screenWidth - (posX + contentViewWidth) - 3
        }

        // 防止贴着屏幕的左边，所以 -3
        if (offX < 0) {
            if (abs(offX) > posX) {
                offX = -(posX - 3)
            }
        }

        // anchorView 中点在屏幕上的位置 减去popWindow 的x在屏幕上的位置 减去 小箭头的一半
        val arrowX = posX + width / 2 - (posX + offX) - downArrowImgWidth / 2

        Log.e(
            "MyPopWindow",
            "offX = $offX, arrowX = $arrowX, downArrowImgWidth = $downArrowImgWidth, downArrowImgHeight = $downArrowImgHeight"
        )

        val params: LinearLayout.LayoutParams =
            downArrowImg?.layoutParams as LinearLayout.LayoutParams
        params.leftMargin = arrowX

        val offY = height + contentViewHeight

        showAsDropDown(anchorView, offX, -offY)
        viewShowAlpha()
    }

    private fun viewShowAlpha() {
        Log.e("MyPopWindow", "----start-----")
        contentView?.run {
            val animatorSet = AnimatorSet()
            val alphaShow = ObjectAnimator.ofFloat(this, "alpha", 0F, 1F)
            alphaShow.duration = 500
            val alphaDismiss = ObjectAnimator.ofFloat(this, "alpha", 1F, 0F)
            alphaDismiss.startDelay = 3000
            alphaDismiss.duration = 500
            if (!mWillNotAutoDismiss) {
                animatorSet.playTogether(alphaShow, alphaDismiss)
                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        Log.e("MyPopWindow", "----dismiss-----")
                        mDismissOpt.invoke()
                        dismiss()
                    }
                })
            } else {
                animatorSet.play(alphaShow)
            }
            animatorSet.start()
        }
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