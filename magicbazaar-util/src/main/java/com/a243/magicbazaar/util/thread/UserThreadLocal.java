package com.a243.magicbazaar.util.thread;

import com.a243.magicbazaar.dao.entity.User;

public class UserThreadLocal {
    private final static ThreadLocal<User> staffThreadLocal = new ThreadLocal<>();

    public static User get() {
        return staffThreadLocal.get();
    }

    public static void set(User user) {
        staffThreadLocal.set(user);
    }

    public static void remove() {
        staffThreadLocal.remove();
    }
}
