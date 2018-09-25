package com.jhh.dc.baika.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date getDate(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateHHmmss(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateHHmm(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getTimeDifference(Date one, Date two) {
        Calendar calendarOne = Calendar.getInstance();
        calendarOne.setTime(one);

        Calendar calendarTwo = Calendar.getInstance();
        calendarTwo.setTime(two);

        long difference = calendarOne.getTimeInMillis() - calendarTwo.getTimeInMillis();

        return difference / (60 * 1000);
    }

    public static String getDateTimeString(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        try {
            return sdf.format(datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateTime_String(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
        try {
            return sdf.format(datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateString(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.format(datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateString(Date datetime,String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.format(datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateStringyyyymmdd(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            return sdf.format(datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateStringToHHmmss(Date datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.format(datetime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateStringToHHmmss() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        try {
            return sdf.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
 * 将时间转换为时间戳
 */
    public static long dateToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }

    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 获取当前日期的前一个月
     */
    public static String getLastMonth() {
        return getLastMonth(null);
    }

    /**
     * 获取当前日期的前一个月
     * @param format 日期格式化format 默认yyyy-MM-dd
     */
    public static String getLastMonth(String format) {
        if(format == null || "".equals(format))
            format = "yyyy-MM-dd";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        SimpleDateFormat dft = new SimpleDateFormat(format);
        String lastMonth = dft.format(cal.getTime());
        return lastMonth;
    }

    public static void main(String args[]) {
        System.out.println(DateUtil.getDateTimeString(new Date()));
    }
}
