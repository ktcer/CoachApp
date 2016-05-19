package com.cn.coachs.coach.model;

/**
 * Created by ktcer on 2016/1/17.
 */
public class BeanCourseImage {
    private String detail;//详情 string
    private String fileurl;// 头像路径 string
    private int result;// 0失败，1成功 number
    private long userID;//classID

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
