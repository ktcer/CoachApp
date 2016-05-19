package com.cn.coachs.model.nurse;


/**
 * @author kuangtiecheng
 * @version 1.0
 * @parameter
 * @return
 */
public class BeanEditDiaryNodeNoContent {
    public String fansId;
    public Long id;
    public int day;
    public int week;
    public String time;

    public String getFansId() {
        return fansId;
    }

    public void setFansId(String fansId) {
        this.fansId = fansId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "BeanEditDiaryNodeNoContent [fansId=" + fansId + ", id=" + id
                + ", day=" + day + ", week=" + week + ", time=" + time + "]";
    }
}
