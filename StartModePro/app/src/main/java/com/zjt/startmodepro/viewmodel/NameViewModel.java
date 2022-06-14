package com.zjt.startmodepro.viewmodel;

import androidx.arch.core.util.Function;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.zjt.startmodepro.MyData;

import java.util.List;

/**
 * Creaeted by ${za.zhu.jiangtao}
 * on 2021/3/1
 */
public class NameViewModel extends ViewModel {
    private MutableLiveData<String> currentName;
    private MutableLiveData<MyData> myLiveData = new MutableLiveData<>(null);

    MutableLiveData<List<String>> mActivityEntranceList = new MutableLiveData<>();

    public MutableLiveData<List<String>> innerEntranceList = (MutableLiveData<List<String>>) Transformations.map(mActivityEntranceList, new Function<List<String>, List<String>>() {

        @Override
        public List<String> apply(List<String> input) {
            return mActivityEntranceList.getValue().subList(0,3);
        }
    });

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

    public void setActivityEntranceList(List<String> list) {
        mActivityEntranceList.setValue(list);
    }


}
