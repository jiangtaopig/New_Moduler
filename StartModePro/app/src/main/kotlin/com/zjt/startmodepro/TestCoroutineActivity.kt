package com.zjt.startmodepro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zjt.startmodepro.viewmodel.ArticleViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/22 3:00 下午

 * @Description : TestCoroutineActivity

 */


class TestCoroutineActivity : AppCompatActivity() {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    companion object {
        fun enter(context: Context) {
            val intent = Intent(context, TestCoroutineActivity::class.java)
            if (context !is Activity) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_layout)

        initView()

        

    }

    private fun initView() {

        val viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)
        viewModel.articleList.observe(this, Observer {
            if (it != null) {
                Log.e("coroutine", "article size = ${it.data.size}")
            }
        })

        findViewById<Button>(R.id.btn_coroutine_1)
                .setOnClickListener {
//                    viewModel.loadData()
//                    viewModel.getStudentAndCarInfo()
//                    viewModel.testSuspend()
//                    testCoroutine()
//                    viewModel.test3()
                    viewModel.testSuspend2()


//                    testFlow3()

//                    scope.launch {
//                        val ret = countdown(10, 2) { remianTime -> calculate(remianTime) }
//                            .onStart { Log.v("test", "countdown start") }
//                            .onCompletion { Log.v("test", "countdown end") }
//                            .reduce { acc, value ->
//                                Log.v("test", "coutdown acc  = $acc， value = $value")
//                                acc + value
//                            }
//                        Log.v("test", "coutdown acc ret = $ret")
//                    }
                }
    }

    private fun calculate(value: Long) :Long {
        Log.v("test", "calculate value = $value")
        return value +10;
    }

    fun <T> countdown(
        duration: Long,
        interval: Long,
        onCountdown: suspend (Long) -> T
    ): Flow<T> =
        flow { (duration - interval downTo 0 step interval).forEach { emit(it) } }
            .onEach { delay(interval) }
            .onStart { emit(duration) }
            .map { onCountdown(it) }
            .flowOn(Dispatchers.Default)

    /**
     * 视图不应直接触发任何协程来执行业务逻辑，而应将这项工作委托给 ViewModel，这里都是测试代码所以直接写在了 Activity 中了。
     */
    private fun testCoroutine() {
        Log.e("coroutine", "   click !!  ")
        val job = scope.launch {
            Log.e("coroutine", " job ---------- thread = ${Thread.currentThread().name}")
        }

        job.start()

        Handler().postDelayed({
            // 这样不会影响 scope， 即此scope还可以创建协程
            job.cancel()
        }, 1_000)

        scope.launch(Dispatchers.Default) {
            Log.e("coroutine", "---------- thread -----  = ${Thread.currentThread().name}")
        }
    }


    private fun testFlow() {
        Log.e("flow", "test flow")
        scope.launch {
            val flow = sample()
            Log.e("flow", "flow start collecting")
            flow.collect { value -> Log.e("flow", "value = $value") }
        }
    }

    private fun sample(): Flow<Int> {
        Log.e("flow", "flow start")
        return flow {
            delay(100)
            for (i in 1..4) {
                emit(i)
            }
        }
    }

    private suspend fun request(request: Int): String {
        delay(1_000)
        return "request : $request"
    }

    private fun testFlow2() {
        Log.e("flow", "testFlow2 start")
        scope.launch {
            (1..6).asFlow()
                    .filter { v -> v > 1 } // 过滤所有大于1的, 即 2，3，4，5，
                    .take(3) // 只取前3个 即 2，3，4，剩下的 5不再发射
                    .map { value -> request(value) }
                    .collect { value -> Log.e("flow", "testFlow2 value = $value") }
        }
        TimeUnit.SECONDS.sleep(1)
        Log.e("flow", "testFlow2 end")
    }

    private fun testFlow3() {
        scope.launch {
            (1..3).asFlow()
                    .map {
                        Thread.sleep(1000)
                        Log.e("flow", "testFlow3 map value = $it , thread = ${Thread.currentThread().name}")
                        it + 1
                    }
                    .flowOn(Dispatchers.IO)
                    .collect { value -> Log.e("flow", "testFlow3 value = $value, thread = ${Thread.currentThread().name}") }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        // 停止正在运行在 scope 作用域上的协程
        // 已取消的作用域无法再创建协程。因此，仅当控制其生命周期的类被销毁时，才应调用 scope.cancel()
        scope.cancel()
    }

}