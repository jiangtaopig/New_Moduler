package com.zjt.startmodepro.api

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/26 9:52 上午
 * @Description : TestService
 */
interface TestService {
    @GET("api/teacher")
    fun getArticleData(@Query("type") type: Int, @Query("num") num: Int): Deferred<DataBase?>?
}