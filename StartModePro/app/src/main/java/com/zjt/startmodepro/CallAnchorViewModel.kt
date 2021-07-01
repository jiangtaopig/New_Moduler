package com.zjt.startmodepro

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CallAnchorViewModel() : ViewModel() {
    var counterTime = MutableLiveData<Long>(-1)
}