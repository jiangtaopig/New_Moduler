package com.zjt.startmodepro.cpu_info

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import android.util.Log
import androidx.annotation.WorkerThread

class MemoryMeter(context: Context) {
    private val mActivityManager: ActivityManager? =
        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?

    /**
     * @return App 内存信息
     */
    @WorkerThread
    fun getMemInfo(): Debug.MemoryInfo? {
        val memInfo = Debug.MemoryInfo()
        try {
            Debug.getMemoryInfo(memInfo)
        } catch (e: Throwable) {
            Log.e("MemoryMeter", "getMemInfo " + e.message + e.printStackTrace())
        }
        return memInfo
    }

    /**
     * @return App 内存使用量(单位:mb)
     */
    @WorkerThread
    fun getMem(unit: Unit = Unit.MB): Int {
        val mem = getMemInfo()?.totalPss ?: 0
        return when (unit) {
            Unit.KB -> mem
            Unit.MB -> mem.shr(10)
            else -> 0
        }
    }

    /**
     * @return App 堆内存使用量(单位:mb)
     */
    @WorkerThread
    fun getDalvikMem(): Int {
        return (getMemInfo()?.dalvikPss ?: 0).shr(10)
    }

    /**
     * @return App Native内存使用量(单位:mb)
     */
    @WorkerThread
    fun getNativeMem(): Int {
        return (getMemInfo()?.nativePss ?: 0).shr(10)
    }

    /**
     * @return App最大堆内存(单位:mb)
     */
    fun getDalvikLimitMem(): Int {
        return mActivityManager?.largeMemoryClass ?: 0
    }

    /**
     * 一般没有限制，系统会动态分配
     * @return App最大Native内存(单位:mb)
     */
    fun getNativeLimitMem(): Int {
        return Debug.getNativeHeapSize().shr(20).toInt()
    }

    /**
     * @return 系统可用内存(单位:mb)
     */
    fun getSystemAvailMem(unit: Unit = Unit.MB): Long {
        return try {
            val memoryInfo = ActivityManager.MemoryInfo()
            mActivityManager?.getMemoryInfo(memoryInfo)
            val mem = memoryInfo.availMem
            when (unit) {
                Unit.Byte -> mem
                Unit.MB -> mem.shr(20)
                else -> 0L
            }
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * @return 系统总内存(单位:mb)
     */
    fun getSystemTotalMem(unit: Unit = Unit.MB): Long {
        return try {
            val memoryInfo = ActivityManager.MemoryInfo()
            mActivityManager?.getMemoryInfo(memoryInfo)
            val mem = memoryInfo.totalMem
            when (unit) {
                Unit.Byte -> mem
                Unit.MB -> mem.shr(20)
                else -> 0L
            }
        } catch (e: Exception) {
            0L
        }
    }
}

enum class Unit {
    Bit, Byte, KB, MB, GB
}