package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/6 4:06 下午

 * @Description : com.zjt.startmodepro.MyKotlinActivity

 */


class MyKotlinActivity : AppCompatActivity() {

    companion object{
        fun enter(context: Context) {
            val intent = Intent(context, MyKotlinActivity::class.java)
            if (context !is Activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_kotlin_layout)
    }
}