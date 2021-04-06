package com.zjt.startmodepro.my_kotlin.delegate

import android.util.Log
import com.zjt.startmodepro.my_kotlin.Constants

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/3/31 10:20 上午

 * @Description : Apple

 */


class Banana : Fruit {
    override fun name() {
       Log.e(Constants.MyDelegate.TAG, " I am a Banana")
    }
}