package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zjt.startmodepro.my_kotlin.MyKotlinManager
import com.zjt.startmodepro.viewmodel.JetPack3ViewModel

class JetPack3Activity : AppCompatActivity() {

    companion object{
        fun enter(context : Context){
            var intent = Intent(context, JetPack3Activity::class.java)
            if (!(context is Activity))
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private lateinit var mDataChangeTv : TextView
    private lateinit var mShowBtn : Button

    lateinit var mViewModel : JetPack3ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jetpack_3_layout)


        mDataChangeTv = findViewById(R.id.txt_title)
        mShowBtn = findViewById(R.id.btn_change_data)



        mViewModel = ViewModelProvider(this).get(JetPack3ViewModel::class.java)
        mViewModel.mStudent.observe(this, Observer{
            mDataChangeTv.text = "name = ${it.name}, age = ${it.age}"
        })


        mShowBtn.setOnClickListener {

            var a : String? = null
            val b = a?: "zz" // 表示如果 a 为 null 的话，那么 b的值等于后面的 "zz"
            Log.e("zjt", "b = $b, a length = ${a?.length}")

            val myKotlinManager = MyKotlinManager("zjt", 32)
            myKotlinManager.showTops()

            mViewModel.getStudentInfo()
        }


    }

}