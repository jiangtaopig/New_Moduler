package com.zjt.startmodepro.concurrent

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.R
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask
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

    private fun initView(){
        findViewById<Button>(R.id.btn_add_task)
                .setOnClickListener {
                    val executorService = ThreadPoolExecutor(2, 5, 10_000, TimeUnit.MILLISECONDS, ArrayBlockingQueue(10))
//                    for (i in 0..14) {
//                        val runnable = Runnable {
//                            Log.e("gggg", "任务" + (i + 1) + "开始")
//                            try {
//                                Thread.sleep(3000)
//                            } catch (e: InterruptedException) {
//                                e.printStackTrace()
//                            }
//                            Log.e("gggg", "任务" + (i + 1) + "结束")
//                        }
//                        executorService.execute(runnable)
//                    }

                    val myFutureTask = MyFutureTask {
                        val a = "123"
                        Thread.sleep(500)
                        a;
                    }
                    executorService.submit(myFutureTask)
                    executorService.shutdown()


                }
    }


}