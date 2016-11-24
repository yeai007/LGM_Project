/*
 * FileName:DateTools.java文件名：
 * Desc：
 * Author：李广明
 * Date:2015年10月19日
 * Copyright：麦格科技
 */
package com.lgm.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/*
 * @category:Tool Class
 * @fileName:DateTools.java
 * @desc：日期工具类,处理Date相关
 * @author：李广明
 * @date:2015年10月19日
 * @copyright：Moogeek
 *
 */

public class DateTools {
    static Calendar now_time = Calendar.getInstance();

    /**
     * 获取当前时间戳
     *
     * @return Date-String
     */
    public static Long getTimeMillis() {
        long timeMillis = Calendar.getInstance().getTimeInMillis();
        return timeMillis;// new Date()为获取当前系统时间
    }

    /**
     * String类型日期转Date类型
     *
     * @return Date-String
     */
    public static String getNowDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());// 设置日期格式
        return df.format(new Date());// new Date()为获取当前系统时间
    }

    /**
     * String类型日期转Date类型
     *
     * @param Str_Date
     * @return Date
     * @传入String类型：'yyyy-MM-dd'
     */
    public static Date StringToDate(String Str_Date) {
        Date return_date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return_date = sdf.parse(Str_Date);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return return_date;
    }

    /**
     * String类型日期转短日期格式String
     *
     * @param Str_DateTime
     * @return Date
     * @传入String类型：'yyyy-MM-dd'
     */
    public static String StringDateTimeToDate(String Str_DateTime) {
        String return_StrDate;
        Date return_datetime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return_datetime = sdf.parse(Str_DateTime);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return_StrDate = formatter.format(return_datetime);
        return return_StrDate;
    }

    /**
     * String类型日期转无年份短日期格式String
     *
     * @param Str_DateTime
     * @return Date
     * @传入String类型：'yyyy-MM-dd'
     */
    public static String StringDateTimeToDateNoYear(String Str_DateTime) {
        String return_StrDate;
        Date return_datetime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return_datetime = sdf.parse(Str_DateTime);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        return_StrDate = formatter.format(return_datetime);
        return return_StrDate;
    }

    /**
     * String类型日期转Date类型
     *
     * @param Str_DateTime
     * @return DateTime
     * @传入String类型：'yyyy-MM-dd HH:mm:ss'
     */
    public static Date StringToDateTime(String Str_DateTime) {
        Date return_datetime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return_datetime = sdf.parse(Str_DateTime);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return return_datetime;
    }

    /**
     * 通过Date获取该Day
     *
     * @param date
     * @return int
     */
    public static int GetDayFromDate(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.YEAR);
        return day;
    }

    /**
     * 通过Date获取该Date所在月份
     *
     * @param date
     * @return int
     */
    public static int GetYearFromDate(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    /**
     * 通过Date获取该Date所在月份
     *
     * @param date
     * @return int
     */
    public static int GetMonthFromDate(Date date) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 通过Date获取该Date所在年月日
     *
     * @param date
     * @return Map<String, Integer>
     */
    public static Map<String, Integer> GetAllFromDate(Date date) {
        Map<String, Integer> date_info = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        date_info.put("day", cal.get(Calendar.DATE));
        date_info.put("month", cal.get(Calendar.MONTH) + 1);
        date_info.put("year", cal.get(Calendar.YEAR));
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return date_info;
    }

    /**
     * 获取当前日期所在年份
     */
    public static int getYear() {

        return now_time.get(Calendar.YEAR);
    }

    /**
     * 获取当前日期所在月份
     */
    public static int getMonth() {

        return now_time.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前日期所在天
     */
    public static int getDay() {

        return now_time.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前日期所在时
     */
    public static int getHour() {

        return now_time.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前日期所在分
     */
    public static int getMinute() {

        return now_time.get(Calendar.MINUTE);
    }

    /**
     * 获取当前日期所在秒
     */
    public static int getSecond() {

        return now_time.get(Calendar.SECOND);
    }

    /**
     * 获取当前日期前n天日期,返回Date
     *
     * @param n
     * @return Date
     * @throws ParseException
     */
    public Date getTimeagoDate(int n) throws ParseException {
        Date new_date = null;
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -n);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String towDaysBefore = sdf.format(cal1.getTime());
        new_date = sdf.parse(towDaysBefore);
        return new_date;
    }

    /**
     * 获取当前日期前n天日期,返回String
     *
     * @param n
     * @return String
     */
    public static String getTimeagoString(int n) throws ParseException {
        String new_date = null;
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -n);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        new_date = sdf.format(cal1.getTime());
        return new_date;
    }

    /**
     * 计算两个时间差
     *
     * @param StrDate1,StrDate2
     * @return String
     */
    public static Long[] getDiffTime(String StrDate1, String StrDate2) throws ParseException {
        Long[] longDiff = new Long[4];
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date d1 = df.parse(StrDate1);
            Date d2 = df.parse(StrDate2);
            longDiff[0] = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            longDiff[1] = longDiff[0] / (1000 * 60 * 60 * 24);//DAY
            longDiff[2] = (longDiff[0] - longDiff[1] * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);//HOUR
            longDiff[3] = (longDiff[0] - longDiff[1] * (1000 * 60 * 60 * 24) - longDiff[2] * (1000 * 60 * 60)) / (1000 * 60);//MINITUS

            System.out.println("" + longDiff[1] + "天" + longDiff[2] + "小时" + longDiff[3] + "分");
        } catch (Exception e) {
        }
        return longDiff;
    }

    /**
     * 获取当前时间,返回String
     *
     * @return String
     */
    public static String getNowTime() throws ParseException {
        String new_date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }


}
