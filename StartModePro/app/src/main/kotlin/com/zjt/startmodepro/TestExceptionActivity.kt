package com.zjt.startmodepro

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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
    }

    private fun initView() {

        findViewById<Button>(R.id.btn_generate_exception)
                .setOnClickListener {
                    try {
                        val b = 0
//                        var a = 23 / b
                        Integer.valueOf("这种")
                    } catch (e: ArithmeticException) {
                        Log.e("TestExceptionActivity", "e >>> ${e.message}")
                    }
                }
    }

    private fun testException(){
//        arithmeticException(2, 3)
        nullPointerException("vv")
    }

    private fun arithmeticException(a: Int, b: Int): Int {
        if (b == 0) throw ArithmeticException("b == 0")
        else return a / b
    }

    @Throws(NullPointerException::class)
    private fun nullPointerException(value: String) : String {
        return "$value .... "
    }
}