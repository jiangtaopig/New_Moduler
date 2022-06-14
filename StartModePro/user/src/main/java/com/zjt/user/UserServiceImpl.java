package com.zjt.user;

import com.zjt.base.user.IUserService;
import com.zjt.base.user.User;

public class UserServiceImpl implements IUserService {
    @Override
    public User getUser() {
        return UserManager.getInstance().getUser();
    }
}
