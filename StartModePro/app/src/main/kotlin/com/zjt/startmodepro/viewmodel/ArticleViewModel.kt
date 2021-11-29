package com.zjt.startmodepro.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zjt.startmodepro.api.DataBase
import com.zjt.startmodepro.bean.BMWCar
import com.zjt.startmodepro.bean.Student
import com.zjt.startmodepro.repository.ArticleRepository
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/26 10:16 上午

 * @Description : ArticleViewModel

 */


class ArticleViewModel : ViewModel() {

    private val articleRepository = ArticleRepository(Dispatchers.IO)
    val articleList = MutableLiveData<DataBase>()

    fun loadData() {
        // viewModelScope 默认是在 MAIN 线程
        Log.e("coroutine", "start >>>> ${Thread.currentThread().name}")
        viewModelScope.launch {
            getArticles()
        }
        Log.e("coroutine", "start >>>> ${Thread.currentThread().name}")
    }

    private suspend fun getArticles() {
        // result 表示 withContext代码块的最后一行
        val result = withContext(Dispatchers.IO) {
            delay(1000)
            articleRepository.fetchData()
        }
        articleList.value = result
    }

    /**
     * 模拟第一个接口的返回值作为第二个接口的入参
     */
    fun getSum() {
        viewModelScope.launch {
            val usedTime = measureTimeMillis {
                val sum = withContext(Dispatchers.IO) {
                    // 这样是同步执行的，先获取 one()的返回值作为 two 的参数;
                    val one = articleRepository.one()
                    val two = articleRepository.two(one)
                    one + two
                }
                Log.e("ArticleViewModel", "getSum = $sum")
            }
            Log.e("ArticleViewModel", "usedTime = $usedTime")
        }
    }

    /**
     * 模拟异步执行2个接口，然后将接口合并
     */
    fun getSum2() {
        viewModelScope.launch {
            val usedTime = measureTimeMillis {
                val sum = withContext(Dispatchers.IO) {
                    val one = async { articleRepository.one() }
                    val two = async { articleRepository.two(5) }
                    one.await() + two.await()
                }
                Log.e("ArticleViewModel", "getSum2 sum = $sum")
            }
            Log.e("ArticleViewModel", "getSum2 usedTime = $usedTime")
        }
    }

    fun getStudentAndCarInfo() {
        viewModelScope.launch {
            val usedTime = measureTimeMillis {
                val result = withContext(Dispatchers.IO) {
                    Log.e(
                        "ArticleViewModel",
                        "getStudentAndCarInfo thread = ${Thread.currentThread().name}"
                    )
                    // async 表示去执行异步操作
                    val student = async { articleRepository.getStudentInfo() }
                    val car = async { articleRepository.getCarInfo() }
                    MergeInfo(student.await(), car.await())
                }
                Log.e(
                    "ArticleViewModel", "getStudentAndCarInfo student = ${result.student} ," +
                            " car is ${result.car.label}, threadName = ${Thread.currentThread().name}"
                )
            }
            Log.e("ArticleViewModel", "getStudentAndCarInfo usedTime = $usedTime")
        }
    }

    fun testSuspend() {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                Log.e("coroutine", "-----testSuspend1------")
                val res = articleRepository.test1()
                Log.e("coroutine", "res = $res")
                "|| ----123------"
            }
            Log.e("coroutine", "--------------------------------------------data -----$data")
        }
        // 协程 deley 不会阻塞线程，上面的 articleRepository.test1() 调用了 delay 方法。
        // 后面的 articleRepository.test2() 会立即执行，不会等到 articleRepository.test1() 方法delay完成

        viewModelScope.launch {
            Log.e("coroutine", "-----testSuspend2------")
            val res = articleRepository.test2()
            Log.e("coroutine", "res = $res")
        }
    }

    fun testSuspend1() {
        viewModelScope.launch {
            Log.e("coroutine", "-----testSuspend1------")
            val res = articleRepository.test1()
            Log.e("coroutine", "res = $res")
        }
    }

    fun testSuspend2() {
        Log.e("coroutine", "start >>>> ${Thread.currentThread().name}")
        viewModelScope.launch {
            Log.e("coroutine", "-----testSuspend2------")
            // test2 是 suspend 挂起函数，是非阻塞式的，它不会阻塞你当前的线程，所以不会等到执行完test2后才会继续执行 注释【2】
            // 会阻塞当前的协程（viewModelScope.launch 创建的协程），所以 注释【1】会等到 test2执行完
            val res = articleRepository.test2()
            Log.e("coroutine", "res = $res") // 注释【1】
        }
        Log.e("coroutine", "end >>>> ${Thread.currentThread().name}") // 注释【2】
    }

    fun test3() {
        viewModelScope.launch {
            Log.e("coroutine", "start >>>> ${Thread.currentThread().name}")
//            viewModelScope.launch(Dispatchers.IO) {
//                Log.e("coroutine", "-- start--- ${Thread.currentThread().name}")
//                delay(1000)
//                Log.e("coroutine", "-- end ---${Thread.currentThread().name}")
//            }
            /**
             * viewModelScope.launch(Dispatchers.IO) 和 withContext(Dispatchers.IO) 不同点：
             * withContext(Dispatchers.IO) 会在 withContext {} 代码块执行完成前阻塞当前协程，执行完成之后后面的  注释【1】才能执行,
             * 而注释【2】是不会等到 withContext {} 代码执行完才继续执行，即阻塞当前的协程而不会阻塞当前的线程
             *
             * viewModelScope.launch(Dispatchers.IO) 则不会阻塞当前协程
             */
            withContext(Dispatchers.Main) {
                Log.e("coroutine", "-- start--- ${Thread.currentThread().name}")
                delay(1000)
                Log.e("coroutine", "-- end ---${Thread.currentThread().name}")
            }
            Log.e("coroutine", "end >>>> ${Thread.currentThread().name}") // 注释【1】
        }

        Log.e("coroutine", " 不阻塞当前线程 >>>> ${Thread.currentThread().name}") // 注释【2】
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    data class MergeInfo(val student: Student, val car: BMWCar)
}