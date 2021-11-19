package com.zjt.startmodepro.concurrent

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.R
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.TimeUnit

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/13 7:42 下午

 * @Description : TestThreadPoolActivity

 */


class TestThreadPoolActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_pool_layout)
        initView()


    }

    private fun initView() {
        findViewById<Button>(R.id.btn_add_task)
            .setOnClickListener {
                val executorService =
                    ThreadPoolExecutor(1, 1, 10_000, TimeUnit.MILLISECONDS, SynchronousQueue(), ZjtThreadFactory())
                for (i in 0..3) {
                    val runnable = Runnable {
                        Log.e(
                            "test thread pool",
                            "thread name = " + Thread.currentThread().name + "任务" + (i + 1) + "开始"
                        )
                        try {
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                        Log.e("test thread pool", "任务" + (i + 1) + "结束")
                    }
                    executorService.execute(runnable)
                }

//                val runnable1 = Runnable {
//                    Log.e(
//                        "test thread pool",
//                        "thread name = " + Thread.currentThread().name + "任务1" + "开始"
//                    )
//                    try {
//                        Thread.sleep(1000)
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//                    }
//                    Log.e("test thread pool", "任务1" + "结束")
//                }
//                executorService.execute(runnable1)
//
//                Thread.sleep(2000)
//
//                val runnable2 = Runnable {
//                    Log.e(
//                        "test thread pool",
//                        "thread name = " + Thread.currentThread().name + "任务2" + "开始"
//                    )
//                    try {
//                        Thread.sleep(1000)
//                    } catch (e: InterruptedException) {
//                        e.printStackTrace()
//                    }
//                    Log.e("test thread pool", "任务2" + "结束")
//                }
//                executorService.execute(runnable2)

//                    val myFutureTask = MyFutureTask {
//                        val a = "123"
//                        Thread.sleep(500)
//                        a;
//                    }
//                    executorService.submit(myFutureTask)
//                    executorService.shutdown()


            }
    }


}