package com.zk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换工具类
 * Created by Ken on 2016/8/5.
 */
public class DateUtils {

    public static String YYYYMM = "yyyy年MM月";
    public static String YYYYMMDD = "yyyy年MM月dd日";
    public static String YYYYMMMDDHHMMSS = "yyyy年MM月dd日 hh时mm分ss秒";


    public static String YYYY_MM = "yyyy-MM";
    public static String YYYY_MM_DD = "yyyy-MM-dd";
    public static String YYYY_MM_DDHHMMSS = "yyyy-MM-dd hh:mm:ss";


    /**
     * 格式化date
     * 默认yyyy-MM-dd hh:mm:ss
     *  
     * @param date 时间
     * @return String
     */
    public static String formatDate(Date date) {
        return formatDate(date, YYYY_MM_DD);
    }

    /**
     * 格式化date
     *
     * @param date    时间
     * @param pattern 格式
     * @return String
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * 转换date
     * 默认yyyy-MM-dd hh:mm:ss
     *
     * @param dateStr 时间
     * @return Date
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, YYYY_MM_DDHHMMSS);
    }

    /**
     * 转换date
     *
     * @param dateStr 时间
     * @param pattern 格式
     * @return Date
     */
    public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
