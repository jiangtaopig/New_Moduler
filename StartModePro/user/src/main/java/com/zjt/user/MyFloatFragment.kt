package com.zjt.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zjt.user.viewmodel.MeViewModel

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

        var params: WindowManager.LayoutParams = activity?.window?.attributes!!
        params.gravity = Gravity.BOTTOM // BUTTON 表示在显示在屏幕的底部，CENTER 表示在屏幕的中间
        activity?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        initView()
    }

    private fun initView() {

        // 持有 activity 引用的 ViewModel 可以让多个 Fragment 数据共享
        val meViewModel = activity?.let { ViewModelProvider(it) }?.get(MeViewModel::class.java)
        meViewModel?.mData?.observe(viewLifecycleOwner, Observer {
            Log.e("MyFloatFragment", "data = $it")
        })
        Log.e("MyFloatFragment", "meViewModel = $meViewModel")
    }

    fun dp2px(context: Context?, dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context?.resources?.displayMetrics).toInt()
    }
}