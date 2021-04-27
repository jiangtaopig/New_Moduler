package com.zjt.startmodepro.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author : zhujiangtao01
 * @Time : On 2021/4/26 9:51 上午
 * @Description : TestApi
 */
class TestApi {
    private var service: TestService? = null

    init {

        val retrofit = Retrofit.Builder()
                .baseUrl("http://www.imooc.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
        service = retrofit.create(TestService::class.java)
    }


    fun getArticleData(type: Int, num: Int): Deferred<DataBase?>? {
        return service!!.getArticleData(type, num)
    }


}