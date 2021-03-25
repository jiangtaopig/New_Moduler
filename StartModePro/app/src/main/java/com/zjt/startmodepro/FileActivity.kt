package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.utils.FileUtil

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/3/25 9:34 上午

 * @Description : FileActivity

 */


class FileActivity : AppCompatActivity() {


    private lateinit var mWrite2File: Button
    private lateinit var mReadFromFile: Button
    private lateinit var mDeleteFile: Button


    companion object {
        fun enter(context: Context) {
            val intent = Intent(context, FileActivity::class.java)
            if (context !is Activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_layout)

        mWrite2File = findViewById(R.id.btn_write_to_file)
        mReadFromFile = findViewById(R.id.btn_read_from_file)
        mDeleteFile = findViewById(R.id.btn_delete_file)

        mWrite2File.setOnClickListener {
            FileUtil.writeFile("zjt", "zhu.txt", "i am chinese aa ")
        }

        mReadFromFile.setOnClickListener {
            val data = FileUtil.readFile("zjt", "zhu.txt")
            Log.e("zzzzzzz", "data = $data")
        }

        mDeleteFile.setOnClickListener {
            FileUtil.deleteFile("zjt", "zhu.txt")
        }

    }
}