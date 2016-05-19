package com.cn.coachs.model.nurse;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @parameter
 * @return
 */
public class BeanEditDiaryNodeNoId {
    public String fansId;
    public String content;
    public int day;
    public int week;
    public String time;

    public String getFansId() {
        return fansId;
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BeanEditDiaryNodeNoId [fansId=" + fansId + ",  content=" + content + ", day=" + day + ", week=" + week
                + ", time=" + time + "]";
    }
}
