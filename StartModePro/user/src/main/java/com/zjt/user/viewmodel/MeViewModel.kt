package com.zjt.user.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MeViewModel : ViewModel() {

    var mData = MutableLiveData<String>()

    var jumpToFloatFragment = MutableLiveData<Boolean>()

    var mName = MutableLiveData<String>()

    var testVal = MutableLiveData(-1) // 有默认值，打开的 activity

    fun doSth(){
        mData.postValue("哈哈，我是牛人啊！！！")
    }
}