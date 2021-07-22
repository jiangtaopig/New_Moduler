package com.zjt.startmodepro.schedule

import android.os.Handler
import android.os.HandlerThread
import androidx.annotation.WorkerThread


abstract class SchedulePlugin {
    var timestamp: Long = System.currentTimeMillis()
    private val monitorThread = HandlerThread("sky-eye-monitor")
    private val monitorHandler: Handler

    init {
        monitorThread.start()
        monitorHandler = Handler(monitorThread.looper)
    }


    private val scheduleRunnable = object : Runnable {
        override fun run() {
            monitorHandler?.postDelayed(this, scheduleDuration)
            timestamp = System.currentTimeMillis()
            schedule()
        }
    }

    open val scheduleDuration = MILLIS_PER_SECOND

    open val handler get() = monitorThread

    open fun onStop() {
        monitorHandler?.removeCallbacks(scheduleRunnable)
    }

    open fun onStart() {
        monitorHandler?.postDelayed(scheduleRunnable, scheduleDuration)
    }

    /**
     * this function will be run on the thread to which [container.monitorHandler] is attached periodically
     */
    @WorkerThread
    abstract fun schedule()

    companion object {
        const val MILLIS_PER_SECOND = 1000L
    }
}