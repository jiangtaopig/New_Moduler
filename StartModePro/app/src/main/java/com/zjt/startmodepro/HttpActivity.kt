package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/3/30 9:34 上午

 * @Description : HttpActivity

 */


class HttpActivity : AppCompatActivity() {


    companion object {
        fun enter(context: Context) {
            val intent = Intent(context, HttpActivity::class.java)
            if (context !is Activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_http_layout)

        findViewById<Button>(R.id.btn_fetch).setOnClickListener {
            fetchData()
        }
    }

    private fun fetchData() {
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .url("http://www.imooc.com/api/teacher?type=4&num=2")
                .build()

        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("HttpActivity", "onFailure >> e = ${e.printStackTrace()}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (Looper.getMainLooper() != Looper.myLooper()){
                    Log.e("HttpActivity", "onResponse 在子线程中执行的")
                }
                val data = response.body()?.string()
                Log.e("HttpActivity", "onResponse >> data = $data")
            }
        })
    }
}