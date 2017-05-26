package com.zk.utils;

import java.math.BigInteger;
import java.util.UUID;

/**
 * Created by werewolf on 2017/2/19.
 */
public class UUIDUtils {

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        String temp;
        temp = str.replaceAll("-", "");
        return temp;
    }

    public static String[] uuids(int number) {
        if (number < 1) {
            return null;
        }
        String[] ss = new String[number];
        for (int i = 0; i < number; i++) {
            ss[i] = uuid();
        }
        return ss;
    }


    public static String sn() {
        String uuid = uuid();
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, 12);
    }

    public static String sn(int num) {
        String uuid = uuid();
        String longSn = new BigInteger(uuid, 16).toString();
        return longSn.substring(0, num);
    }
}
