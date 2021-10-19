package com.zjt.startmodepro.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.JavaInterface
import com.zjt.startmodepro.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

class TestCoroutineActivity : AppCompatActivity() {

    companion object {
        fun enter(context: Context) {
            val intent = Intent(context, TestCoroutineActivity::class.java)
            if (context !is Activity)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_coroutine_layout)

        val tv = findViewById<TextView>(R.id.txt_click)

        // kotlin 兼容 Java中的SAM （Single Abstract Method）转换
        tv.setOnClickListener {

            testMyInterface(object : MyInterface {
                override fun doSth(input: Int): String {
                    return input.toString()
                }
            })

            setMyClickListener {

            }

            delegateWork {
                it
            }


            testSuspend2()
            // 协程挂起后，延迟时间到了，也要等下面的主线程执行完成才能继续执行
            for (i in 1..200) {
                Log.e("coroutines", " i = $i ")
            }
//            testAsync()
        }


        findViewById<TextView>(R.id.txt_flow_click)
            .setOnClickListener {

                GlobalScope.launch {
                    flow {
                        List(3) {
                            emit(it)
                        }
                    }
                        .map { it ->
                            Log.e("test_flow", "map ... it = $it")
                            flow {
                                List(it) {
                                    emit(it)
                                }
                            }
                        }.flattenConcat()
                        .collect {
                            Log.e("test_flow", "it = $it")
                        }
                }
            }
        findViewById<TextView>(R.id.txt_flow_click2)
            .setOnClickListener {
                ProgressDialog.showProgressDialog(this@TestCoroutineActivity)
//                collectWithIndex()
                GlobalScope.launch {
//                    val value = flow {
//                        for (i in 1..6) {
//                            Log.e("flow_high", "emit value = $i")
//                            emit(i.toString())
//                        }
//                    }
//                        .toList()
//                    Log.e("flow_high", "value = $value")

                    // --------------------------------------

                     // toList reduce 和 collect 一样都是末端操作符
//                    val m = flowOf(1, 2, 3).reduce { a, b ->
//                        Log.e("flow_high", "a = $a, b = $b")
//                        a + b
//                    }
//                    Log.e("flow_high", "m = $m")
                    // --------------------------------------





                }
            }
    }

    /**
     * 带下标的 collect
     */
    private fun collectWithIndex() {
        // collectLatest用于在collect中取消未来得及处理的数据，只保留当前最新的生产数据
        GlobalScope.launch {
            val time = measureTimeMillis {
                flow {
                    for (i in 1..4) {
                        Log.e("flow_high", "emit value = $i")
                        emit(i.toString())
                    }
                }.collectIndexed { index, value ->
                    Log.e("flow_high", "result index = $index, value = $value")
                }
            }
            Log.e("flow_high", "time = $time")
        }
    }

    private fun testHighOrderFunction() {
        val block: (Int, Int) -> String // 函数类型
        block = ::sum // 双冒号 用来创建函数类型对象
        block.invoke(3, 4) // 只有函数类型的对象可以调用 invoke 方法，你无法直接调用 sum.invoke
        block(2, 3)

        // lambda 表达式就是就是以大括号括起来的， -> 前面的是入参，后面是逻辑代码，最后以后表示返回值，如下:
        testHighOrder("zjt") { a, b ->
            a + b
        }

        val noNameFun = fun(a: Int, b: Int) = a > b // 匿名函数实质上也是函数类型的对象，lambda表达式也是
        noNameFun(2, 3)
        // 把上面的匿名函数写成 lambda 表达式的形式

        val noNameFun2 = { a: Int, b: Int -> // 不能省略入参的参数类型，因为无法从其他地方推断入参类型
            a + b
        }
        // 你可以以如下的方式来写，为左边的变量指明类型
        val noNameFun3: (Int, Int) -> Int = { a, b ->
            a + b
        }
        // 注意 lambda 表达式的返回值不用 return 来返回，最后以后就是返回值

        testHighOrder("test") { a, b -> // 这里可以省略入参的参数类型，因为可以从 testHighOrder 中的函数类型推断出入参 a 合 b 都是 Int 型
            a + b
        }

        // 有参数有返回值的 lambda 表达式
        val lambda1 = { a: Int, b: Int ->
            a + b
        }

        // 有参数无返回值的 lambda 表达式

        val lambda2 = { name: String ->
            Log.e("xx", "name = $name")
        }

        // 无参数无返回值的表达式

        val lambda3 = {
            Log.e("xx", "无参数无返回值的 lambda 表达式")
        }

        val f = ::sum
        (::sum)(2, 3)
        f.invoke(2, 3)

        test("lambda") { a, b ->
            a * b
        }
        test3 { a, b ->
            a + b
        }

        test4 {
            val value = it // 用 it表示唯一的参数
        }
    }

    private fun f2(): (Int, Int) -> String {
        return ::sum
    }

    private fun report(opt: (String) -> Unit) {
        opt.invoke("哈哈，小猪哥")
    }

    private fun doReport(msg: String) {
        Log.e("xxx", "msg = $msg")
    }

    private fun test() {
//        report(::doReport)
        report(fun(msg) {
            Log.e("xxx", "msg = $msg")
        })
    }

    fun test(name: String, opt: (a: Int, b: Int) -> Int): String {
        return name + opt.invoke(3, 4)
    }

    private fun test3(opt: (a: Int, b: Int) -> Int): String {
        return opt.invoke(3, 4).toString()
    }

    fun test4(opt: (String) -> Unit) {
        opt.invoke("123")
    }

    private fun testMyInterface(myInterface: MyInterface): String {
        return myInterface.doSth(12)
    }

    private fun setMyClickListener(listener: View.OnClickListener) {
//        listener.onClick()
    }

    private fun delegateWork(f: JavaInterface): String {
        return f.doSth("sss")
    }

    private fun sum(a: Int, b: Int): String {
        return (a + b).toString()
    }

    private fun testHighOrder(name: String, operation: (Int, Int) -> Int): String {
        return name + operation.invoke(2, 3)
    }

    private fun testFlow1() {
        testFun(name = "bb")
        val intFlow = flow {
            (1..3).forEach {
                emit(it)
                delay(100)
//                        it / 0
            }
        }.onEach {
            Log.e("test_flow", "onEach  it = $it")
        }.catch { t: Throwable ->
            Log.e("test_flow", "抛出异常啦", t) // catch 函数只能捕获它的上游异常
        }.onCompletion { th: Throwable? ->
            Log.e("test_flow", "onCompletion") // onCompletion 类似于 try catch 中的 finally
            test { a, b ->
                a + b
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            Log.e("test_flow", " ---  thread = >> ${Thread.currentThread().name}")
            intFlow.flowOn(Dispatchers.Default)
                .collect {
                    Log.e(
                        "test_flow",
                        "it = $it, thread = >> ${Thread.currentThread().name}"
                    )
                }
        }
    }

    private fun testFun(age: Int = 23, name: String = "zz") {
        Log.e("test_flow", "age = $age, name = $name")
    }

    private fun test(operation: (Int, Int) -> Int) {
        val r = operation.invoke(3, 4)
        Log.e("test_flow", "test r = $r")
    }

    /**
     * 测试多协程 suspend 方法
     * 协程挂起后，虽然延迟的时间到了，但是还得等到线程空闲时才能继续执行
     */
    private fun testSuspend2() {
        GlobalScope.launch {
            Log.e("coroutines", " ------------ time >>> ${System.currentTimeMillis()} ")
            val token = GlobalScope.async {
                return@async getToken()
            }.await() // await 会阻塞外部的协程

            val response = GlobalScope.async {
                return@async getResponse(token)
            }.await()
            Log.e(
                "coroutines",
                " --------- response = $response time >>> ${System.currentTimeMillis()} "
            )
        }
    }

    /**
     * 测试单协程 suspend 函数
     */
    private fun testSuspend1() {
        GlobalScope.launch {
            Log.e("coroutines", " ------------ time >>> ${System.currentTimeMillis()} ")
            // 在 getToken 方法将协程挂起时，getResponse 函数永远不会运行，只有等 getToken 挂起结速将协程恢复时才会运行
            val token = getToken()
            val response = getResponse(token)
            Log.e(
                "coroutines",
                " ------------ response = $response,  time >>> ${System.currentTimeMillis()} "
            )
        }
    }

    /**
     * suspend 修饰的方法挂起的是协程本身，而非该方法
     */
    private suspend fun getToken(): String {
        Log.e("coroutines", " getToken start  , time >>> ${System.currentTimeMillis()}")
        delay(3)
        Log.e("coroutines", " getToken end  , time >>> ${System.currentTimeMillis()}")
        return "token"
    }

    private suspend fun getResponse(token: String): String {
        Log.e(
            "coroutines",
            " getResponse start  , token = $token ,  time >>> ${System.currentTimeMillis()}"
        )
        delay(1)
        Log.e("coroutines", " getResponse end  , time >>> ${System.currentTimeMillis()}")
        return "返回了数据"
    }

    private fun testRunBlocking() {
        runBlocking {
            // runBlocking 会阻塞当前线程
            Log.e("coroutines", " runBlocking start  , time >>> ${System.currentTimeMillis()}")
            delay(1000)
            Log.e("coroutines", " runBlocking end  , time >>> ${System.currentTimeMillis()}")
        }
        Thread.sleep(40)
        Log.e("coroutines", " main thread start  , time >>> ${System.currentTimeMillis()}")
    }

    private fun testAsync() {
        GlobalScope.launch {
            // async 和 launch 的区别：async 是有返回值的
            // async 并不会阻塞线程，只是阻塞锁调用的协程
            val deferred = GlobalScope.async {
                Log.e("coroutines", " async start  , time >>> ${System.currentTimeMillis()}")
                delay(1_000)
                Log.e("coroutines", " async end  , time >>> ${System.currentTimeMillis()}")
                return@async "我是 async 的结果"
            }

            Log.e("coroutines", " --- start  , time >>> ${System.currentTimeMillis()}")
            val result = deferred.await() // 接收的是 async 中返回的值，会阻塞外部协程
            Log.e("coroutines", " --- result = $result  , time >>> ${System.currentTimeMillis()}")
        }
    }

    private fun test1() {
        Log.e("coroutines", " start , time >>> ${System.currentTimeMillis()}")
        // Dispatchers.Unconfined -- 没指定就是当前线程
        val job = GlobalScope.launch(Dispatchers.Unconfined) {
            Log.e(
                "coroutines",
                " 协程初始化完成 , thread = ${Thread.currentThread().name} , time >>> ${System.currentTimeMillis()}"
            )
            for (i in 1..3) {
                Log.e("coroutines", "  i = $i , time >>> ${System.currentTimeMillis()}")
            }
            // 协程的delay不会阻塞线程
            delay(2_000)
            Log.e("coroutines", "  delay 结束 , time >>> ${System.currentTimeMillis()}")
        }

        Thread.sleep(50)
//            job.cancel() // 取消协程

        GlobalScope.launch {
            job.join()
            Log.e("coroutines", "  等job对应的协程执行结束 , time >>> ${System.currentTimeMillis()}")
        }
    }


    interface MyInterface {
        fun doSth(input: Int): String
    }
}