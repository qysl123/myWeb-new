package com.zk.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

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

    public static long currentTimeSecs() {
        return Clock.systemUTC().millis() / 1000;
    }

    public static String dateTimeFormatter(String pattern) {
        return dateTimeFormatter(pattern, LocalDateTime.now());
    }

    public static String dateTimeFormatter(String pattern, LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate stringToLocalDate(String date) {
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.CHINESE);
        return LocalDate.parse(date, germanFormatter);
    }

    public static LocalDate stringToLocalDate(String date, String pattern) {
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern(pattern).withLocale(Locale.CHINESE);
        return LocalDate.parse(date, germanFormatter);
    }

    public static LocalDate stringToLocalDate(Long date) {
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern("yyyyMMdd").withLocale(Locale.CHINESE);
        return LocalDate.parse(date.toString(), germanFormatter);
    }


    public static LocalDateTime stringToLocalDatetime(String date) {
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, germanFormatter);
    }


    public enum CurrentDateTime {
        YYYY {
            @Override
            public Long LongValue() {
                return YYYY.LongValue(LocalDateTime.now());
            }

            @Override
            public Long LongValue(LocalDateTime localDateTime) {
                return Long.valueOf(localDateTime.getYear());
            }


            @Override
            public String StringValue(String... pattern) {
                return YYYY.StringValue(LocalDateTime.now(), pattern);
            }

            @Override
            public String StringValue(LocalDateTime localDateTime, String... pattern) {
                return String.valueOf(localDateTime.getYear());
            }
        },
        YYYYMM {
            @Override
            public Long LongValue() {
                return YYYYMM.LongValue(LocalDateTime.now());
            }

            @Override
            public Long LongValue(LocalDateTime localDateTime) {
                return Long.valueOf(localDateTime.getYear() * 1_00 + localDateTime.getMonthValue());
            }

            @Override
            public String StringValue(String... pattern) {
                return YYYYMM.StringValue(LocalDateTime.now(), pattern);
            }

            @Override
            public String StringValue(LocalDateTime localDateTime, String... pattern) {
                if (pattern.length == 0)
                    return dateTimeFormatter("YYYY-MM", localDateTime);
                else
                    return dateTimeFormatter(pattern[0], localDateTime);
            }
        },
        YYYYMMDD {
            @Override
            public Long LongValue() {
                return YYYYMMDD.LongValue(LocalDateTime.now());
            }

            @Override
            public Long LongValue(LocalDateTime localDateTime) {
                return Long.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("YYYYMMdd")));
            }

            @Override
            public String StringValue(String... pattern) {
                return YYYYMMDD.StringValue(LocalDateTime.now(), pattern);
            }

            @Override
            public String StringValue(LocalDateTime localDateTime, String... pattern) {
                if (pattern.length == 0)
                    return dateTimeFormatter("YYYY-MM-dd", localDateTime);
                else
                    return dateTimeFormatter(pattern[0], localDateTime);
            }
        },

        YYYYMMDDHH {
            @Override
            public Long LongValue() {
                return YYYYMMDDHH.LongValue(LocalDateTime.now());
            }

            @Override
            public Long LongValue(LocalDateTime localDateTime) {
                return Long.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("YYYYMMddHH")));
            }

            @Override
            public String StringValue(String... pattern) {
                return YYYYMMDDHH.StringValue(LocalDateTime.now(), pattern);
            }

            @Override
            public String StringValue(LocalDateTime localDateTime, String... pattern) {
                if (pattern.length == 0)
                    return dateTimeFormatter("YYYY-MM-dd HH", localDateTime);
                else
                    return dateTimeFormatter(pattern[0], localDateTime);
            }
        }, YYYYMMDDHHMM {
            @Override
            public Long LongValue() {
                return YYYYMMDDHHMM.LongValue(LocalDateTime.now());
            }

            @Override
            public Long LongValue(LocalDateTime localDateTime) {
                return Long.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("YYYYMMddHHmm")));
            }


            @Override
            public String StringValue(String... pattern) {
                return YYYYMMDDHHMM.StringValue(LocalDateTime.now(), pattern);
            }

            @Override
            public String StringValue(LocalDateTime localDateTime, String... pattern) {
                if (pattern.length == 0)
                    return dateTimeFormatter("YYYY-MM-dd HH:mm", localDateTime);
                else
                    return dateTimeFormatter(pattern[0], localDateTime);
            }
        }, YYYYMMDDHHMMSS {
            @Override
            public Long LongValue() {
                return YYYYMMDDHHMMSS.LongValue(LocalDateTime.now());
            }

            @Override
            public Long LongValue(LocalDateTime localDateTime) {
                return Long.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")));
            }


            @Override
            public String StringValue(String... pattern) {
                return YYYYMMDDHHMMSS.StringValue(LocalDateTime.now(), pattern);
            }

            @Override
            public String StringValue(LocalDateTime localDateTime, String... pattern) {
                if (pattern.length == 0)
                    return dateTimeFormatter("YYYY-MM-dd HH:mm:ss", localDateTime);
                else
                    return dateTimeFormatter(pattern[0], localDateTime);
            }
        };

        public abstract Long LongValue();

        public abstract Long LongValue(LocalDateTime localDateTime);

        public abstract String StringValue(String... pattern);

        public abstract String StringValue(LocalDateTime localDateTime, String... pattern);
    }
}
