package com.zjt.startmodepro.concurrent

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.R
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.TimeUnit

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/13 7:42 下午

 * @Description : TestThreadPoolActivity

 */

class TestThreadPoolActivity : AppCompatActivity() {

    lateinit var executorService: ThreadPoolExecutor

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

                Thread {
                    for (i in 6..10) {
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
            /**
             * SynchronousQueue ：它的特点是不存储数据，当添加一个元素时，必须等待一个消费线程取出它，否则一直阻塞，
             * 如果当前有空闲线程则直接在这个空闲线程执行，如果没有则新启动一个线程执行任务。
             * 通常用于需要快速响应任务的场景，在网络请求要求低延迟的大背景下比较合适。
             */
            executorService =
                ThreadPoolExecutor(
                    0,
                    3,
                    60_000,
                    TimeUnit.MILLISECONDS,
                    SynchronousQueue(), //
                    ZjtThreadFactory()
                )

            for (i in 1..5) { // 超过最大线程数3的直接reject了
                executorService.execute {
                    Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 $i 开始执行")
                    Thread.sleep(1000)
                    Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 $i 执行结束")
                }
            }
        }

    }
}
