package com.cn.coachs.coach.model;

/**
 * Created by ktcer on 2016/1/16.
 */
public class BeanSubmitCourse {
    private long classID;//返回classID，上传图片是有重要作用 number
    private String detail;//详情 string
    private int result;//

    public long getClassID() {
        return classID;
    }

    public void setClassID(long classID) {
        this.classID = classID;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
