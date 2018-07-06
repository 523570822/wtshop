package com.wtshop.util;

import java.util.UUID;

/**
 * Created by jobfo on 2017/7/30.
 */
public class UUIDUtils {

    public static String getLongUUID() {
        Long uuid;
        do {
            uuid = Math.abs(UUID.randomUUID().getMostSignificantBits());
        } while (uuid <= 0L);
        return uuid.toString();
    }

    public static String getStringUUID() {
       return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) {
        for (int i = 0; i <10000 ; i++) {
            System.out.println(getStringUUID());
        }

    }
}
