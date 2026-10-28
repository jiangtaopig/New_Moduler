package com.zjt.startmodepro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/28 4:49 下午

 * @Description : DispatchActivity

 */


class DispatchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatch_layout)

        val data: Uri? = intent.data
        if (data != null) {
            Log.d("DeepLink", "Data: " + data.toString())
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        val data: Uri? = intent.getData()
        if (data != null) {
            Log.d("DeepLink", "New Intent Data: $data")
        }
    }
}