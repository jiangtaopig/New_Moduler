package com.zjt.startmodepro.schedule

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class PusherPlugin : SchedulePlugin() {

    companion object {
        const val MAX_INTERVAL = 10;
    }

    private val format = SimpleDateFormat("HH:mm:ss")
    private var start = 0
    private var info = ReporterInfo()
    private var memoryTotal: Float = 0f

    override fun schedule() {
        start ++
        memoryTotal += start
        info.setMemory(memoryTotal)
        Log.e("PusherPlugin", "start = $start" )
        if (start == MAX_INTERVAL){
            info.setAverageMemory(info.getMemory()/ MAX_INTERVAL)
            Log.e("PusherPlugin", " averageMemory = ${info.getAverageMemory()}" )
            start = 0
            memoryTotal = 0f
        }
//        Log.e("PusherPlugin", "time = ${format.format(Date())}" )
    }
}