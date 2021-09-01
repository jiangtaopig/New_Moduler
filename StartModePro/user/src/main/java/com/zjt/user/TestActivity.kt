package com.zjt.user

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_layout)

        findViewById<TextView>(R.id.txt_back)
            .setOnClickListener {
                val intent = Intent()
                intent.putExtra("little pig", "zzj")
                setResult(3001, intent)
                finish()
            }
    }
}