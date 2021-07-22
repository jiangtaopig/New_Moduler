package com.zjt.startmodepro.schedule

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.R

class ScheduleActivity : AppCompatActivity() {

    val pusherPlugin = PusherPlugin()

    companion object {
        fun enter(context: Context) {
            val intent = Intent(context, ScheduleActivity::class.java)
            if (context !is Activity) {
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_layout)

        findViewById<Button>(R.id.start_schedule)
            .setOnClickListener {
                pusherPlugin.onStart()
            }

        findViewById<Button>(R.id.stop_schedule)
            .setOnClickListener {
                pusherPlugin.onStop()
            }
    }
}