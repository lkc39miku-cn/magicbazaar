package com.a243.magicbazaar.util;

import java.util.Random;

public class RandomUtil {
    public static String code() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }
}
