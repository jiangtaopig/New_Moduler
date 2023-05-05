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
                /**
                 * coreSize = 0 配合 SynchronousQueue的特征：
                 *  SynchronousQueue ：它的特点是不存储数据，当添加一个元素时，必须等待一个消费线程取出它，否则一直阻塞；它的 offer 方法会返回false,
                 *  SynchronousQueue.offer 的用法: 如果有线程在等待队列插入数据，那么此时 offer 为true，否则为false，不会阻塞当前线程。
                 *
                 */
                val executorService =
                    ThreadPoolExecutor(
                        0,
                        3,
                        2_000,
                        TimeUnit.MILLISECONDS,
                        SynchronousQueue(),
                        ZjtThreadFactory()
                    )

//                for (i in 1..5) {
                executorService.execute {
                    Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 1 开始执行")
                    Thread.sleep(1000)
                    Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 1 执行结束")
                }
                executorService.taskCount
//                }


                Thread.sleep(4_000)

                executorService.execute {
                    Log.e("zzzzz", "${Thread.currentThread().name} >> ---- 任务 2 开始执行")
                    Thread.sleep(1000)
                    Log.e("zzzzz", "${Thread.currentThread().name} >> ----- 任务 2 执行结束")
                }

            }

        findViewById<Button>(R.id.btn_add_task2).setOnClickListener {
            /**
             * SynchronousQueue ：它的特点是不存储数据，当添加一个元素时，必须等待一个消费线程取出它，否则一直阻塞，
             * 如果当前有空闲线程则这个空闲线程直接执行传入的任务，如果没有则新启动一个线程执行任务。
             * 通常用于需要快速响应任务的场景，在网络请求要求低延迟的大背景下比较合适。
             */
            val executorService =
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

        findViewById<Button>(R.id.btn_add_task3).setOnClickListener {
            val executorService =
                ThreadPoolExecutor(
                    1,
                    2,
                    60_000,
                    TimeUnit.MILLISECONDS,
                    LinkedBlockingQueue(), //
                    ZjtThreadFactory()
                )

            for (i in 1..3) { // 超过最大线程数3的直接reject了
                executorService.execute {
                    Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 $i 开始执行")
                    Thread.sleep(1000)
                    Log.e("zzzzz", "${Thread.currentThread().name} >> 任务 $i 执行结束")
                }
            }
        }

    }


}
