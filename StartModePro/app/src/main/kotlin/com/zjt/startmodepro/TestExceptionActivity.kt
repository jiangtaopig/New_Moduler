package com.zjt.startmodepro

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.FileInputStream
import java.io.FileNotFoundException

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/16 9:25 上午

 * @Description : TestExceptionActivity

 */


class TestExceptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exception_layout)

        initView()
        /// 我修改了 dev/develop 的分之，然后将master cherry-pick 过来会造成冲突
    }

    private fun initView() {
        delay()
        findViewById<Button>(R.id.btn_generate_exception)
                .setOnClickListener {
                    try {
                        testDelay()
                    } catch (e: ArithmeticException) {
                        Log.e("TestExceptionActivity", "e >>> ${e.message}")
                    }
                }
    }

    private fun testDelay() {
        Thread() {
            kotlin.run {
                Log.e("TestExceptionActivity", "testDelay start")
                Thread.sleep(550)
                Log.e("TestExceptionActivity", "testDelay end")
            }
        }.start()
    }

    private fun delay() {
        Log.e("TestExceptionActivity", "testDelay start")
        Thread.sleep(200)
        Log.e("TestExceptionActivity", "testDelay end")
    }

    private fun testException() {
//        arithmeticException(2, 3)
        nullPointerException("vv")
    }

    private fun arithmeticException(a: Int, b: Int): Int {
        if (b == 0) throw ArithmeticException("b == 0")
        else return a / b
    }

    @Throws(NullPointerException::class)
    private fun nullPointerException(value: String): String {
        return "$value .... "
    }

    private fun readFromFile(filePath: String) {
        try {
            val input = FileInputStream(filePath)
        } catch (ex: FileNotFoundException) {

        }
    }
}