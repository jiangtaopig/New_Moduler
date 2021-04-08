package com.zjt.user

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/8 3:01 下午

 * @Description : MyFloatFragment

 */


class MyFloatFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_my_float, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topMargin = 0F
        val lp = (view.parent as? ViewGroup)?.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin = dp2px(context, topMargin)
        }

        var params : WindowManager.LayoutParams = activity?.window?.attributes!!
        params.gravity = Gravity.BOTTOM // BUTTON 表示在显示在屏幕的底部，CENTER 表示在屏幕的中间
        activity?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)


    }

    fun dp2px(context: Context?, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context?.resources?.displayMetrics).toInt()
    }
}