package com.cn.coachs.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UtilGetWeekData {
    //二、一周日期的函数：通过在本周的星期一的Calendar上连续加1，获取一周的日期
    // （1）获得当前日期与本周一相差的天数
    private int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    public Calendar getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        Date monday = currentDate.getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(monday);
        return c;
    }

    //（3）通过在本周的星期一的Calendar上连续加1，获取一周的日期
    //  public ArrayList<// （3）通过在本周的星期一的Calendar上连续加1，获取一周的日期
    public ArrayList<WeekInfo> getCurrentWeekInfoList() {
        String todayInfo;
        Calendar now = Calendar.getInstance();
        todayInfo = now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-"
                + now.get(Calendar.DAY_OF_MONTH);

        ArrayList<WeekInfo> weekInfoList = new ArrayList<WeekInfo>();
        Calendar mondayDate = getCurrentMonday(); // 本周星期一的Calendar
        String monday = (mondayDate.get(Calendar.MONTH) + 1) + "月"
                + mondayDate.get(Calendar.DAY_OF_MONTH) + "日" + " " + "星期一";
        WeekInfo weekInfo = new WeekInfo();
        weekInfo.setTodayInfo(todayInfo);
        weekInfo.setWeekDayInfo(monday);
        weekInfo.setWeekDate(mondayDate.get(Calendar.YEAR) + "-"
                + (mondayDate.get(Calendar.MONTH) + 1) + "-"
                + mondayDate.get(Calendar.DAY_OF_MONTH));
        weekInfoList.add(weekInfo);

        mondayDate.add(Calendar.DAY_OF_MONTH, 1);
        String tuesday = (mondayDate.get(Calendar.MONTH) + 1) + "月"
                + mondayDate.get(Calendar.DAY_OF_MONTH) + "日" + " " + "星期二";
        WeekInfo weekInfo2 = new WeekInfo();
        weekInfo2.setTodayInfo(todayInfo);
        weekInfo2.setWeekDayInfo(tuesday);
        weekInfo2.setWeekDate(mondayDate.get(Calendar.YEAR) + "-"
                + (mondayDate.get(Calendar.MONTH) + 1) + "-"
                + mondayDate.get(Calendar.DAY_OF_MONTH));
        weekInfoList.add(weekInfo2);

        mondayDate.add(Calendar.DAY_OF_MONTH, 1);
        String wednesday = (mondayDate.get(Calendar.MONTH) + 1) + "月"
                + mondayDate.get(Calendar.DAY_OF_MONTH) + "日" + " " + "星期三";
        WeekInfo weekInfo3 = new WeekInfo();
        weekInfo3.setTodayInfo(todayInfo);
        weekInfo3.setWeekDayInfo(wednesday);
        weekInfo3.setWeekDate(mondayDate.get(Calendar.YEAR) + "-"
                + (mondayDate.get(Calendar.MONTH) + 1) + "-"
                + mondayDate.get(Calendar.DAY_OF_MONTH));
        weekInfoList.add(weekInfo3);

        mondayDate.add(Calendar.DAY_OF_MONTH, 1);
        String thursday = (mondayDate.get(Calendar.MONTH) + 1) + "月"
                + mondayDate.get(Calendar.DAY_OF_MONTH) + "日" + " " + "星期四";
        WeekInfo weekInfo4 = new WeekInfo();
        weekInfo4.setTodayInfo(todayInfo);
        weekInfo4.setWeekDayInfo(thursday);
        weekInfo4.setWeekDate(mondayDate.get(Calendar.YEAR) + "-"
                + (mondayDate.get(Calendar.MONTH) + 1) + "-"
                + mondayDate.get(Calendar.DAY_OF_MONTH));
        weekInfoList.add(weekInfo4);

        mondayDate.add(Calendar.DAY_OF_MONTH, 1);
        String friday = (mondayDate.get(Calendar.MONTH) + 1) + "月"
                + mondayDate.get(Calendar.DAY_OF_MONTH) + "日" + " " + "星期五";
        WeekInfo weekInfo5 = new WeekInfo();
        weekInfo5.setTodayInfo(todayInfo);
        weekInfo5.setWeekDayInfo(friday);
        weekInfo5.setWeekDate(mondayDate.get(Calendar.YEAR) + "-"
                + (mondayDate.get(Calendar.MONTH) + 1) + "-"
                + mondayDate.get(Calendar.DAY_OF_MONTH));
        weekInfoList.add(weekInfo5);

        mondayDate.add(Calendar.DAY_OF_MONTH, 1);
        String saturday = (mondayDate.get(Calendar.MONTH) + 1) + "月"
                + mondayDate.get(Calendar.DAY_OF_MONTH) + "日" + " " + "星期六";
        WeekInfo weekInfo6 = new WeekInfo();
        weekInfo6.setTodayInfo(todayInfo);
        weekInfo6.setWeekDayInfo(saturday);
        weekInfo6.setWeekDate(mondayDate.get(Calendar.YEAR) + "-"
                + (mondayDate.get(Calendar.MONTH) + 1) + "-"
                + mondayDate.get(Calendar.DAY_OF_MONTH));
        weekInfoList.add(weekInfo6);

        mondayDate.add(Calendar.DAY_OF_MONTH, 1);
        String sunday = (mondayDate.get(Calendar.MONTH) + 1) + "月"
                + mondayDate.get(Calendar.DAY_OF_MONTH) + "日" + " " + "星期日";
        WeekInfo weekInfo7 = new WeekInfo();
        weekInfo7.setTodayInfo(todayInfo);
        weekInfo7.setWeekDayInfo(sunday);
        weekInfo7.setWeekDate(mondayDate.get(Calendar.YEAR) + "-"
                + (mondayDate.get(Calendar.MONTH) + 1) + "-"
                + mondayDate.get(Calendar.DAY_OF_MONTH));
        weekInfoList.add(weekInfo7);

        return weekInfoList;
    }

}
