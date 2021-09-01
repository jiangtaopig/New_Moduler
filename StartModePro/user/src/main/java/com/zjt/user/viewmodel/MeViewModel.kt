package com.zjt.user.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MeViewModel : ViewModel() {

    var mData = MutableLiveData<String>()

    var jumpToFloatFragment = MutableLiveData<Boolean>()

    var mName = MutableLiveData<String>()

    fun doSth(){
        mData.postValue("哈哈，我是牛人啊！！！")
    }
}