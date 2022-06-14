package com.zjt.user;

import com.zjt.base.user.User;
import com.zjt.user_api.UserInfo;

public
/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/3
 */
class UserManager {

    private UserManager(){

    }

    private static UserManager mInstance;

    public static UserManager getInstance(){
        if (mInstance == null){
            synchronized (UserManager.class){
                if (mInstance == null)
                    mInstance = new UserManager();
            }
        }
        return mInstance;
    }

    public UserInfo getUserInfo(){
        UserInfo userInfo = new UserInfo("xju", 23);
        return userInfo;
    }

    public User getUser() {
        User user = new User();
        user.setName("zzjj");
        user.setAge(32);
        return user;
    }

}
