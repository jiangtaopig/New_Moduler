package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.refactor.PushClientManager
import com.zjt.startmodepro.singleinstance.DataManager

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/6 4:06 下午

 * @Description : com.zjt.startmodepro.MyKotlinActivity

 */


class TestRefactorActivity : AppCompatActivity() {

    private val mPushClientManager by lazy {
        PushClientManager()
    }

    companion object {
        fun enter(context: Context) {
            val intent = Intent(context, TestRefactorActivity::class.java)
            if (context !is Activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_kotlin_layout)


        findViewById<Button>(R.id.btn_camera_preview)
                .setOnClickListener {
                    mPushClientManager.startCameraPreview(this)
                    mPushClientManager.startPush()
                    mPushClientManager.onStart()
                }

        findViewById<Button>(R.id.btn_voice_preview)
                .setOnClickListener {
                    mPushClientManager.startVoicePreview(this)
                    mPushClientManager.startPush()
                    mPushClientManager.onDestroy()
                }


    }


}