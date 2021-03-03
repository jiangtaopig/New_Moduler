package com.zjt.startmodepro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.zjt.startmodepro.viewmodel.NameViewModel;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/2
 */
class ViewModelManager {

    private ViewModelManager(){

    }

    private static volatile ViewModelManager mInstance;

    public static ViewModelManager getInstance(){
        if (mInstance == null){
            synchronized (ViewModelManager.class){
                if (mInstance == null)
                    mInstance = new ViewModelManager();
            }
        }
        return mInstance;
    }

    public NameViewModel getNameModel(AppCompatActivity context) {
        return  new ViewModelProvider(context).get(NameViewModel.class);
    }
}
