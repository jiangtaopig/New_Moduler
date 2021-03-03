package com.zjt.startmodepro.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/3/1
 */
public class NameViewModel extends ViewModel {
    private MutableLiveData<String> currentName;

    public MutableLiveData<String> getCurrentName(){
        if (currentName == null)
            currentName = new MutableLiveData<>();
        return currentName;
    }
}
