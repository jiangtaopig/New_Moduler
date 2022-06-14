package com.zjt.base.user;

public class UserNavigator {

    private UserNavigator(){

    }

    private static class UserNavigatorHolder {
        private static UserNavigator instance = new UserNavigator();
    }

    public static UserNavigator getInstance() {
        return UserNavigatorHolder.instance;
    }

    public User getUser() {
        User user = null;
        try {
            Class c = Class.forName("com.zjt.user.UserServiceImpl");
            IUserService service = (IUserService) c.newInstance();
            if (service != null) {
                user = service.getUser();
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return user;
    }
}
