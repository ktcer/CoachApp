package com.cn.coachs.model.nurse;

/**
 * @author kuangtiecheng
 */
public class DepartmentInfo {
    private long departmentID;
    private String departmentName;

    public DepartmentInfo(String departmentName) {
        this.departmentName = departmentName;
    }

    public long getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

}
