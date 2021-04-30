package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zjt.startmodepro.viewmodel.ArticleViewModel

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

        val viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        viewModel.articleList.observe(this, Observer {
            if (it != null) {

            }
        })

        findViewById<Button>(R.id.btn_coroutine_1)
                .setOnClickListener {
//                    viewModel.loadData()
                    viewModel.getStudentAndCarInfo()
                }
    }

}