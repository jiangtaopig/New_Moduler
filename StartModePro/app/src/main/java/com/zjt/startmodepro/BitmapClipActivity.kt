package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.bean.Student
import com.zjt.startmodepro.utils.BitmapUtils
import okhttp3.OkHttpClient

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/3/29 6:09 下午

 * @Description : BitmapClipActivity

 */


class BitmapClipActivity : AppCompatActivity() {

    companion object {
        fun enter(context: Context) {
            val intent = Intent(context, BitmapClipActivity::class.java)
            if (context !is Activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private lateinit var clippedImg: ImageView
    private lateinit var clipBitmapBtn: Button
    private val student = Student("zjt", 20)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap_clip_layout)

        clippedImg = findViewById(R.id.img_after_clip)
        clipBitmapBtn = findViewById(R.id.btn_clip_bitmap)

        initView()
    }

    var cnt = 1;

    private fun initView() {

        clipBitmapBtn.setOnClickListener {
            cnt++
            student.id += "_$cnt"

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test2)
            val bm = BitmapUtils.scaleBitmap(bitmap, 100f, 120f)
            clippedImg.setImageBitmap(bm)
        }
    }
}