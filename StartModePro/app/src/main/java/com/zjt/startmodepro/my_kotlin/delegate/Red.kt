package com.zjt.startmodepro.my_kotlin.delegate

import android.util.Log
import com.zjt.startmodepro.my_kotlin.Constants

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/3/31 10:26 上午

 * @Description : Red

 */


class Red : Color {
    override fun color() {
        Log.d(Constants.MyDelegate.TAG, "I am Red Color")
    }
}