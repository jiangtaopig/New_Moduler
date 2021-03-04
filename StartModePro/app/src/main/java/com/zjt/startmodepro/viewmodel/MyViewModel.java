package com.zjt.startmodepro.viewmodel;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/4
 */
class MyViewModel extends ViewModel {
    private MutableLiveData<String> userLiveData;
    private MutableLiveData<Boolean> loadingLiveData;

    public MyViewModel(){
        userLiveData = new MutableLiveData<>();
        loadingLiveData = new MutableLiveData<>();
    }

    @SuppressLint("StaticFieldLeak")
    public void getUserInfo(){
        loadingLiveData.setValue(true);
        new AsyncTask<Void, Void, String>(){

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loadingLiveData.setValue(false);
                userLiveData.setValue(s);
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Thread.sleep(2_000); // 模拟网络请求耗时
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "我是大老爷啊，我在学习呢";
            }
        }.execute();
    }

    public LiveData<String> getUserLiveData(){
        return userLiveData;
    }

    public LiveData<Boolean> getLoadingLiveData(){
        return loadingLiveData;
    }
}
