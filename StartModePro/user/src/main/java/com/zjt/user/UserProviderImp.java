package com.zjt.user;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zjt.router.RouteHub;
import com.zjt.user_api.UserInfo;
import com.zjt.user_api.UserProvider;


/**
 *Creaeted by ${za.zhu.jiangtao}
 *on 2021/3/3
 */

@Route(path = RouteHub.User.USER_PROVIDER_PATH)
public class UserProviderImp implements UserProvider {

    @Override
    public UserInfo getUserInfo() {
        // UserInfo 的信息只能在 user 模块中获取
        return UserManager.getInstance().getUserInfo();
    }

    @Override
    public void init(Context context) {

    }
}
