package com.zk.utils;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {

    public static Integer getYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static Integer getShortYear() {
        return getYear() % 100;
    }

    /**
     * 将日期设为一天的开始，即00时00分00秒
     *
     * @param date
     * @return
     */
    public static Date dayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 将日期设为一天的结束，即23时59分59秒
     *
     * @param date
     * @return
     */
    public static Date dayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 返回目标日期前指定年数的日期
     *
     * @param dateTarget 目标日期
     * @param years      年数
     * @return
     */
    public static Date beforeOfYear(Date dateTarget, int years) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateTarget);
        c.add(Calendar.YEAR, -years);
        return c.getTime();
    }

    /**
     * 返回目标日期前指定月数的日期
     *
     * @param dateTarget 目标日期
     * @param months     月数
     * @return
     */
    public static Date beforeOfMonth(Date dateTarget, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateTarget);
        c.add(Calendar.MONTH, -months);
        return c.getTime();
    }

    /**
     * 返回目标日期前指定天数的日期
     *
     * @param dateTarget 目标日期
     * @param days       天数
     * @return
     */
    public static Date beforeOfDays(Date dateTarget, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(dateTarget);
        c.add(Calendar.DAY_OF_MONTH, -days);
        return c.getTime();
    }

    public static Date addDate(Date date, int field, int number) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, number);
        return c.getTime();
    }

    /**
     * 获取两个日期相差的xx年xx月xx天
     *
     * @param StartDate 开始日期
     * @param endDate   结束日期
     * @return 相差xx年xx月xx天
     */
    public static String calTimeDifference(Date StartDate, Date endDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(StartDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        if (end.compareTo(start) < 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int day = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (day < 0) {
            day = end.getActualMaximum(Calendar.DAY_OF_MONTH) - Math.abs(day);
            end.add(Calendar.MONTH, -1);
        }
        int month = end.get(Calendar.MONTH) - start.get(Calendar.MONTH);

        if (month < 0) {
            month = 12 - Math.abs(month);
            end.add(Calendar.YEAR, -1);
        }
        int year = end.get(Calendar.YEAR) - start.get(Calendar.YEAR);


        sb.append(year).append("年");
        sb.append(month).append("月");
        sb.append(day).append("天");
        return sb.toString();
    }

    /**
     * 清空时分秒
     *
     * @param date 时间
     * @return Calendar
     */
    public static Calendar getClearTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        return c;
    }


    //获取当前周的开始时间
    public static Date getCurrentStartWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    //获取上一周的开始时间
    public static Date getPreStartWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, -1);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    //获取当前周的结束时间
    public static Date getCurrentEndtWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        //增加一个星期，才是我们中国人理解的本周日的日期
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    //获取上一周的结束时间
    public static Date getPreEndtWeekDay() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    //当月开始时间
    public static Date getCurrentMonthStartDate() {
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    //当月结束时间
    public static Date getCurrentMonthEndDate() {
        // 获取Calendar
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
