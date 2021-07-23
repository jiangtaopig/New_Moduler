package com.zjt.startmodepro.singleinstance

import android.content.Context
import android.util.Log

class DataManager private constructor(context: Context) {
    init {
        Log.e("single_instance", "DataManager init ....")
    }
    companion object : SingletonHolder<DataManager, Context>(::DataManager)


    fun doSth(){

    }
}