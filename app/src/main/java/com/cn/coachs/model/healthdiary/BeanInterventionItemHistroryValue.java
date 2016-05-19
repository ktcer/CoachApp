package com.cn.coachs.model.healthdiary;

/**
 * 干预项目历史值，某一天的值
 * 国康驿站
 *
 * @author kuangtiecheng
 */
public class BeanInterventionItemHistroryValue {

    private long _id;
    private String date;
    private double value;

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

}