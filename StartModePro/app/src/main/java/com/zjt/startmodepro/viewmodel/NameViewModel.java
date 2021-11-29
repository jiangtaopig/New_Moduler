package com.zjt.startmodepro.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zjt.startmodepro.MyData;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/3/1
 */
public class NameViewModel extends ViewModel {
    private MutableLiveData<String> currentName;
    private MutableLiveData<MyData> myLiveData = new MutableLiveData<>(null);

    public MutableLiveData<String> getCurrentName(){
        if (currentName == null)
            currentName = new MutableLiveData<>();
        return currentName;
    }

    public MutableLiveData<MyData> getMyLiveData() {
        return myLiveData;
    }

    public void setCurrentName(String name) {
        if (currentName == null){
            getCurrentName();
        }
        currentName.setValue(name);
    }
}
