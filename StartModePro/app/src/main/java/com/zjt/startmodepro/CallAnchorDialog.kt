package com.zjt.startmodepro

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.text.DecimalFormat
import kotlin.math.floor

class CallAnchorDialog : DialogFragment() {

    lateinit var counterTv: TextView
    private var callAnchorViewModel: CallAnchorViewModel? = null
    private lateinit var countDownTimer: BlinkCountDownTimer

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        var params: WindowManager.LayoutParams = window?.attributes!!
        params.gravity = Gravity.CENTER

        window.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity(), R.style.CenterDialogTheme)
//        var params: WindowManager.LayoutParams = dialog?.window?.attributes!!
//        params.type = 1999
        dialog.setContentView(R.layout.dialog_call_anchor_layout)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false) //触摸对话框外面的区域，对话框消失
        initView(dialog)
        return dialog
    }

    private fun initView(dialog: Dialog) {
        counterTv = dialog.findViewById(R.id.txt_counter)
        callAnchorViewModel = requireActivity().let {
            ViewModelProvider(it).get(CallAnchorViewModel::class.java)
        }

        callAnchorViewModel?.counterTime?.observe(this, {
            counterTv.text = getTimeStr(it)
        })
        Log.e("CallAnchorDialog", "-----value = ${callAnchorViewModel?.counterTime?.value}")


        // 为何只赋值一次，防止横竖屏切换的时候导致的从头开始计时，因为ViewModel 能够记住值。
        if (callAnchorViewModel?.counterTime?.value == null || callAnchorViewModel?.counterTime?.value!! < 0L) {
            callAnchorViewModel?.counterTime?.value = 660_000
        }

        countDownTimer = BlinkCountDownTimer(callAnchorViewModel?.counterTime?.value!!, 1000) { timeStr ->
            Log.e("CallAnchorDialog", "onTimer time = $timeStr , thread = ${Thread.currentThread().name}")
            callAnchorViewModel?.counterTime?.value = callAnchorViewModel?.counterTime?.value!! - 1000
        }
        countDownTimer.start()
    }

    private fun getTimeStr(time :Long) :String{
        val dec = DecimalFormat("##.##")
        var seconds = dec.format((time % 60_000) / 1_000)
        if (seconds.length < 2) {
            seconds = "0$seconds"
        }
        val minutes = (floor((time / 60_000.0))).toInt()
        var result = "$minutes:$seconds"
        if (minutes / 10 == 0) {
            result = "0$minutes:$seconds"
        }
        Log.e("CallAnchorDialog", "time = $time , result = $result , minutes = $minutes")
        return result
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    companion object {
        private const val TAG = "CallAnchorDialog"

        fun show(fragment: Fragment) {
            try {
                val previousFrag = fragment.childFragmentManager.findFragmentByTag(TAG)
                if (previousFrag != null) {
                    fragment.childFragmentManager.beginTransaction().remove(previousFrag)
                        .commitAllowingStateLoss()
                }
                val callAnchorDialog = CallAnchorDialog()
                fragment.childFragmentManager.beginTransaction().add(callAnchorDialog, TAG)
                    .commitAllowingStateLoss()
            } catch (e: Exception) {
                Log.e(TAG, "static show() occursException", e)
            }
        }
    }
}