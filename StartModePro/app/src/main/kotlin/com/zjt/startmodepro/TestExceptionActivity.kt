package com.zjt.startmodepro

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import com.zjt.base.BaseActivity
import com.zjt.startmodepro.viewmodel.NameViewModel
import java.io.FileInputStream
import java.io.FileNotFoundException

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/16 9:25 上午

 * @Description : TestExceptionActivity

 */


class TestExceptionActivity : BaseActivity() {

    companion object {
        const val DATA_KEY = "data_kay"
        var mydata: MyData? = null
    }

    private var nameViewModel: NameViewModel? = null
    private lateinit var myData: MyData
    private lateinit var titleTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TestExceptionActivity", "onCreate mydata = $mydata")
        setContentView(R.layout.activity_exception_layout)

        nameViewModel = getApplicationScopeViewModel(NameViewModel::class.java)
        initView()
        initData(savedInstanceState)
        val tl = ThreadLocal<String>()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        Log.e("TestExceptionActivity", "onSaveInstanceState")
        outState.putParcelable(DATA_KEY, mydata)
    }

    private fun initView() {
        titleTv = findViewById(R.id.txt_title)
        nameViewModel?.currentName?.observe(this, Observer {
            Log.e("TestExceptionActivity", "data ==>> $it")
        })
        Log.e("TestExceptionActivity", "nameViewModel ==>> $nameViewModel")
//        delay()
        findViewById<Button>(R.id.btn_generate_exception)
            .setOnClickListener {
                nameViewModel?.apply {
                    currentName.value = "我是 ExceptionActivity 中的livedata 数据"
                }
            }
    }

    private fun initData(savedInstanceState: Bundle?) {
        Log.e("TestExceptionActivity", "initData ==>> $mydata")
        if (mydata != null) {
            if (nameViewModel?.myLiveData?.value == null){
                nameViewModel?.myLiveData?.value = mydata
            }
        }
        myData = nameViewModel?.myLiveData?.value!!
        val txt = myData.name + " , " + myData.title
        titleTv.text = txt
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

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TestExceptionActivity", "onDestroy")
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