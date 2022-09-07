package com.a243.magicbazaar.util.thread;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private static List<String> list = new ArrayList<>();

    public static List<String> get() {
        return list;
    }

    public static void set(List<String> strings) {
        list = strings;
    }

    public static void remove() {
        list.clear();
    }
}
