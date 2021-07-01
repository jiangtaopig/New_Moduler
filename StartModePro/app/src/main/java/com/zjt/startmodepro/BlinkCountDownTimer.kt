package com.zjt.startmodepro

import android.os.CountDownTimer
import android.util.Log
import java.text.DecimalFormat
import kotlin.math.floor

class BlinkCountDownTimer(
    millisInFuture: Long,
    countDownInterval: Long, val opt: (Long) -> Unit
) : CountDownTimer(millisInFuture, countDownInterval) {

    override fun onTick(millisUntilFinished: Long) {
        opt(millisUntilFinished)
    }

    override fun onFinish() {
        Log.e("BlinkCountDownTimer", "onFinish")
    }
}