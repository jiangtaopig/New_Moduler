package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.test_compileonly.CompileOnlyUtils
import com.zjt.startmodepro.utils.BitmapUtils

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap_clip_layout)

        clippedImg = findViewById(R.id.img_after_clip)
        clipBitmapBtn = findViewById(R.id.btn_clip_bitmap)

        initView()
    }

    private fun initView() {
        clipBitmapBtn.setOnClickListener {
            // 由于 app 的 build.gradle 是以 compileOnly 的方式依赖的 module test_compileonly ,
            // 所以这里面调用 CompileOnlyUtils 类，会报 Didn't find class "com.example.test_compileonly.CompileOnlyUtils"
            // 但是编译不会报错。compileOnly 的作用是编译的时候可用，不会把 module test_compileonly 打包到 apk 中。

//            val name = CompileOnlyUtils().compileOnly
            // 获取父容器
            var parent: LinearLayout = clipBitmapBtn.parent as LinearLayout

            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.test2)
            val bm = BitmapUtils.scaleBitmap(bitmap, 100f, 120f)
            clippedImg.setImageBitmap(bm)
        }

        val img = findViewById<ImageView>(R.id.inSample_img)

        findViewById<Button>(R.id.btn_inSample_bitmap)
            .setOnClickListener {
                val bitmap =
                    BitmapUtils.bitmapInSampleSize(this, R.drawable.zz, img.width, img.height)
                img.setImageBitmap(bitmap)
            }


        // 能这样写说明这里的接口 必现是 Java 接口
        setOnTestSamListener { str ->
            str.length
        }

        // 这样写就会报错，因为 OnTestListener 是 kotlin 中的接口，而 OnTestSamListener 是定义在 Java 中的接口，是支持 SAM 操作的。

//        setOnTestListener { str ->
//
//        }
    }

    private fun setOnTestSamListener(listener: OnTestSamListener) {

    }

    private fun setOnTestListener(listener: OnTestListener) {

    }


    interface OnTestListener {
        fun doSth(str: String)
    }
}