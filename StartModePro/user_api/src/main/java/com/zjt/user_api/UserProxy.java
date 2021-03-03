package com.zjt.user_api;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/3
 */
class UserProxy {

    @Autowired
    UserProvider userProvider;

    private  UserProxy(){
        ARouter.getInstance().inject(this);
    }

    private static UserProxy mInstance;

    public static UserProxy getInstance() {
        if (mInstance == null) {
            synchronized (UserProxy.class){
                if (mInstance == null)
                    mInstance = new UserProxy();
            }
        }
        return mInstance;
    }

    public UserProvider getUserProvider() {
        return userProvider;
    }
}
