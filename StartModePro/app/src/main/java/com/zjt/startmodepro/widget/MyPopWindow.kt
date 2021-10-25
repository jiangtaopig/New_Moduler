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
import android.widget.PopupWindow
import android.widget.RelativeLayout
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
    private var leftArrowImg: ImageView? = null
    private var rightArrowImg : ImageView? = null
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
            leftArrowImg = rootView?.findViewById(R.id.left_arrow_img)
            rightArrowImg = rootView?.findViewById(R.id.right_arrow_img)
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

    fun showBubble(anchorView: View, content: String, position: Int, dismissOpt: () -> Unit) {
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
        val anchorViewWidth = anchorView.width
        val anchorViewHeight = anchorView.height

        /**
         * anchorView 在屏幕中的位置
         */
        val anchorPos = intArrayOf(-1, -1)
        anchorView.getLocationOnScreen(anchorPos)
        val posX = anchorPos[0]
        val posY = anchorPos[1]
        val statusBarHeight = DisplayUtil.getStatusBarHeight(context) // 状态栏高度

        handleArrowVisibility(position)

        /**
         * 因为这时候还未绘制，所以 width = 0 ,height = 0; 所以要主动测量
         * 获取 measuredWidth
         */
        getSizeBeforeDraw()

        val contentViewWidth = contentView.measuredWidth
        val contentViewHeight = contentView.measuredHeight
        Log.e(
            "MyPopWindow",
            "x = $x, y = $y, width = $anchorViewWidth, height = $anchorViewHeight, posX = $posX, posY = $posY, " +
                    "\n statusBarHeight = $statusBarHeight, contentViewWidth = $contentViewWidth, contentViewHeight = $contentViewHeight" +
                    "\n screenWidth = $screenWidth"

        )

        var offX = 0
        if (position == BELOW_ANCHOR_VIEW || position == ABOVE_ANCHOR_VIEW) {
            offX = (anchorViewWidth - contentViewWidth) / 2
            val marin = 3
            // 防止贴着屏幕的右边，所以去掉 marin
            if (posX + contentViewWidth > screenWidth) {
                offX = screenWidth - (posX + contentViewWidth) - marin
            }

            // 防止贴着屏幕的左边，所以去掉 margin
            if (offX < 0) {
                if (abs(offX) > posX) {
                    offX = -(posX - marin)
                }
            }
        }

        Log.e("MyPopWindow", "offX = $offX")

        var arrowImgWidth = 0
        var offY = 0
        // 箭头位置
        var arrowX = 0
        when (position) {
            BELOW_ANCHOR_VIEW -> {
                arrowImgWidth = upArrowImg?.measuredWidth ?: 0
                // anchorView 中点在屏幕上的位置 减去popWindow 的x在屏幕上的位置 减去 小箭头的一半
                arrowX = posX + anchorViewWidth / 2 - (posX + offX) - arrowImgWidth / 2
                val params: RelativeLayout.LayoutParams =
                    upArrowImg?.layoutParams as RelativeLayout.LayoutParams
                params.leftMargin = arrowX
            }
            ABOVE_ANCHOR_VIEW -> {
                offY = anchorViewHeight + contentViewHeight
                // anchorView 中点在屏幕上的位置 减去popWindow 的x在屏幕上的位置 减去 小箭头的一半
                arrowX = posX + anchorViewWidth / 2 - (posX + offX) - arrowImgWidth / 2
                val params: RelativeLayout.LayoutParams =
                    downArrowImg?.layoutParams as RelativeLayout.LayoutParams
                params.leftMargin = arrowX
            }
            RIGHT_ANCHOR_VIEW -> {
                offX = anchorViewWidth
                offY = (anchorViewHeight + contentViewHeight) / 2
                val params: RelativeLayout.LayoutParams =
                    leftArrowImg?.layoutParams as RelativeLayout.LayoutParams
                params.topMargin = (contentViewHeight - (leftArrowImg?.measuredHeight ?: 0)) / 2
            }

            LEFT_ANCHOR_VIEW -> {
                offX = -contentViewWidth
                offY = (anchorViewHeight + contentViewHeight) / 2
                val params: RelativeLayout.LayoutParams =
                    rightArrowImg?.layoutParams as RelativeLayout.LayoutParams
                params.topMargin = (contentViewHeight - (rightArrowImg?.measuredHeight ?: 0)) / 2
            }
        }
        Log.e(
            "MyPopWindow",
            "offX = $offX, arrowX = $arrowX, upArrowImgWidth = $arrowImgWidth, offY = $offY"
        )
        showAsDropDown(anchorView, offX, -offY)
        viewShowAlpha()
    }

    private fun handleArrowVisibility(position: Int) {
        when (position) {
            BELOW_ANCHOR_VIEW -> {
                leftArrowImg?.visibility = View.GONE
                downArrowImg?.visibility = View.GONE
                rightArrowImg?.visibility = View.GONE
                upArrowImg?.visibility = View.VISIBLE
            }
            ABOVE_ANCHOR_VIEW -> {
                leftArrowImg?.visibility = View.GONE
                upArrowImg?.visibility = View.GONE
                downArrowImg?.visibility = View.VISIBLE
            }
            RIGHT_ANCHOR_VIEW -> {
                upArrowImg?.visibility = View.GONE
                downArrowImg?.visibility = View.GONE
                rightArrowImg?.visibility = View.GONE
                leftArrowImg?.visibility = View.VISIBLE
            }
            LEFT_ANCHOR_VIEW -> {
                upArrowImg?.visibility = View.GONE
                downArrowImg?.visibility = View.GONE
                leftArrowImg?.visibility = View.GONE
                rightArrowImg?.visibility = View.VISIBLE
            }
        }
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

    /// -------END -----------
}