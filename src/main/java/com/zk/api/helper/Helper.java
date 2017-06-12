package com.zk.api.helper;

/**
 * bird on 2017/6/1.
 */
public class Helper {

    public static String imagePath(String image) {
        return MessageSource.message("domain.url") + image;
    }


}
