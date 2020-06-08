package com.academic.calendar.util;


import com.academic.calendar.entity.User;
import org.springframework.stereotype.Component;

/**
 * 持有登录用户信息的容器， 用于代替session对象
 * 线程隔离
 */
@Component
public class UserHolder {

    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear() {
        users.remove();
    }


}
