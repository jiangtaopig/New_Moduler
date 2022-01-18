package com.zjt.startmodepro.concurrent

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.R
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/13 7:42 下午

 * @Description : TestThreadPoolActivity

 */

class TestThreadPoolActivity : AppCompatActivity() {

    lateinit var executorService :ThreadPoolExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thread_pool_layout)
        initView()

        val executorService = Executors.newFixedThreadPool(5);
        executorService.execute {
            Log.e(
                "TestThreadPoolActivity",
                "thread name = " + Thread.currentThread().name + "--------- 1234 ---------"
            )
        }

        val single = Executors.newSingleThreadScheduledExecutor()

        for (i in 1..3) {
            single.execute {
                Log.e(
                    "TestThreadPoolActivity",
                    "thread name = " + Thread.currentThread().name + "--------- 5678 ---------$i"
                )
            }
        }

        TestHookThreadPool().testHook()
    }

    private fun initView() {
        findViewById<Button>(R.id.btn_add_task)
            .setOnClickListener {
                executorService =
                    ThreadPoolExecutor(
                        2,
                        3,
                        60_000,
                        TimeUnit.MILLISECONDS,
                        LinkedBlockingQueue(2),
                        ZjtThreadFactory()
                    )

                for (i in 1..5) {
                    executorService.execute {
                        Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 $i 开始执行")
                        Thread.sleep(1000)
                        Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 $i 执行结束")
                    }
                    executorService.taskCount
                }

                Thread.sleep(10_000)

                Thread{
                    for(i in 6..10) {
                        Thread.sleep(3000)
                        executorService.execute {
                            Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 $i 开始执行")
                            Thread.sleep(1000)
                            Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 $i 执行结束")
                        }
                    }
                }.start()

            }

        findViewById<Button>(R.id.btn_add_task2).setOnClickListener {


        }

    }
}
