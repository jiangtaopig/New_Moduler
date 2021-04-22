package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/22 3:00 下午

 * @Description : TestCoroutineActivity

 */


class TestCoroutineActivity : AppCompatActivity() {

    companion object {
        fun enter(context: Context) {
            val intent = Intent(context, TestCoroutineActivity::class.java)
            if (context !is Activity) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_layout)

        initView()
    }

    private fun initView() {

        findViewById<Button>(R.id.btn_coroutine_1)
                .setOnClickListener {
//                    GlobalScope.launch {
//                        Log.e("TestCoroutineActivity", "thread = ${Thread.currentThread().name}")
//                        delay(1000)
//                        Log.e("TestCoroutineActivity", "--- end ---")
//                    }
//                    Log.e("TestCoroutineActivity", "... thread = ${Thread.currentThread().name}")

                    runBlocking {

                        launch {
                            Log.e("TestCoroutineActivity", "thread = ${Thread.currentThread().name}")
                            delay(1000)
                            Log.e("TestCoroutineActivity", "--- end ---")
                        }

                        coroutineScope {
                            delay(500)
                            Log.e("TestCoroutineActivity", "coroutineScope thread = ${Thread.currentThread().name}")
                        }
                        Log.e("TestCoroutineActivity", "runBlock thread = ${Thread.currentThread().name}")
                    }
                    Log.e("TestCoroutineActivity", "... thread = ${Thread.currentThread().name}")
                }
    }

}