package com.zjt.startmodepro.repository

import com.zjt.startmodepro.api.DataBase
import com.zjt.startmodepro.api.TestApi
import com.zjt.startmodepro.bean.BMWCar
import com.zjt.startmodepro.bean.Student
import kotlinx.coroutines.delay

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/26 9:44 上午

 * @Description : ArticleRepository

 */


class ArticleRepository {

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

    suspend fun getCarInfo() :BMWCar{
        delay(2_000)
        return BMWCar("bmw")
    }

}