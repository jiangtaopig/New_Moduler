package com.zjt.startmodepro

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zjt.startmodepro.viewmodel.NameViewModel

class MyKotlinDialog : DialogFragment(), DialogInterface {

    private lateinit var mTitleTv: TextView
    private var mTitle: String? = null
    private var mNameViewModel: NameViewModel? = null

    companion object {
        private const val TITLE_KEY = "title"
        private const val TAG = "MyKotlinDialog"
        fun getInstance(title: String?): MyKotlinDialog {
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
        Log.e(TAG, "onStart")
        val window = dialog?.window
        var params: WindowManager.LayoutParams = window?.attributes!!
        params.gravity = Gravity.BOTTOM // BOTTOM 表示在显示在屏幕的底部，CENTER 表示在屏幕的中间
//        params.windowAnimations = R.style.MyDialogStyleBottom
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.e(TAG, "onCreateDialog")
        val dialog = Dialog(activity!!, R.style.BottomDialogTheme)
//        dialog.setContentView(R.layout.dialog_my_kotlin_layout)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true) //触摸对话框外面的区域，对话框消失
        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e(TAG, "onCreateView")
        return inflater.inflate(R.layout.dialog_my_kotlin_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(TAG, "onViewCreated")
        // Fragment 持有 Activity 的 ViewModel ,因为这里我让 ViewModelProvider(context) 传入了 activity，所以多个Fragment就可以通过相同的方法持有 唯一的 ViewModel
        // 但是， 这里 observe 传入的是 Fragment 的 lifecycle 所以当 Fragment 不处于活动状态时就不会收到 LiveData 回调
        mNameViewModel = activity?.let {
            ViewModelProvider(it).get(NameViewModel::class.java)
        }
        mNameViewModel?.currentName?.observe(viewLifecycleOwner, Observer<String> {
            Log.e("MyKotlinDialog", "data = $it")
        })

        initView(view)
        initData()
    }

    private fun initView(view: View) {
        mTitleTv = view.findViewById(R.id.txt_title)
    }

    private fun initData() {
        arguments?.run {
            mTitle = getString(TITLE_KEY)
        }
        mTitleTv.text = mTitle
        mTitleTv.setOnClickListener {
//            val myTask = MyTask()
//            myTask.setOnDelayTaskListener { it ->
//                it + "xxx"
//                Log.e("MyKotlinDialog", "dialog isDetached $isDetached , value = $it , mTitleTv txt = ${mTitleTv.text}")
//                mTitleTv.text = it
//            }
//            myTask.delay()
            mNameViewModel?.apply {
                currentName.value = "我是在 MyKotlinDialog 中利用 NameViewModel 修改的数据"
            }
        }
    }

    fun setTitle(title :String) {
        Log.e(TAG, "setTitle")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("xxx", "onDestroy $isDetached")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("xxx", "onDetach $isDetached")
    }

    override fun dismiss() {
        super.dismiss()
        Log.e("xxx", "dismiss isDetach $isDetached")
    }
}