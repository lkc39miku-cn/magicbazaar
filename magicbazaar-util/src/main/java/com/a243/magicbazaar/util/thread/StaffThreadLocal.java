package com.a243.magicbazaar.util.thread;

import com.a243.magicbazaar.dao.entity.Staff;

public class StaffThreadLocal {
    private final static ThreadLocal<Staff> staffThreadLocal = new ThreadLocal<>();

    public static Staff get() {
        return staffThreadLocal.get();
    }

    public static void set(Staff staff) {
        staffThreadLocal.set(staff);
    }

    public static void remove() {
        staffThreadLocal.remove();
    }
}
