package com.zjt.startmodepro.repository

import android.util.Log
import com.zjt.startmodepro.api.DataBase
import com.zjt.startmodepro.api.TestApi
import com.zjt.startmodepro.bean.BMWCar
import com.zjt.startmodepro.bean.Student
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/26 9:44 上午

 * @Description : ArticleRepository

 */


class ArticleRepository(private val ioDispatcher: CoroutineDispatcher) {

    private val mApi by lazy {
        TestApi()
    }

    suspend fun fetchData(): DataBase {
        return mApi.getArticleDataAsync(2, 4)?.await()!!
    }

    suspend fun one(): Int {
        delay(1000)
        return 1
    }

    suspend fun two(one: Int): Int {
        delay(2_000)
        return one + 2
    }

    suspend fun getStudentInfo(): Student {
        delay(1500)
        return Student("xiaozhuge", 32)
    }

    suspend fun getCarInfo(): BMWCar {
        delay(2_000)
        return BMWCar("bmw")
    }

    suspend fun test1(): String {
        Log.e("coroutine", "test1 enter")
        return withContext(ioDispatcher) {// android 官方推荐使用 注入 Dispatchers ，而不是 硬编码  withContext(Dispatchers.IO)
            Log.e("coroutine", "test1  thread = ${Thread.currentThread().name}")
            delay(1500)
            "test1 result"
        }
    }

    suspend fun test2(): String {
        Log.e("coroutine", "test2 enter thread = ${Thread.currentThread().name}")
        delay(2500)
        return "---- test2 result ---"
    }

}