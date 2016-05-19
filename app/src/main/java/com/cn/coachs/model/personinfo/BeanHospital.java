package com.cn.coachs.model.personinfo;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @parameter
 * @return
 */
public class BeanHospital {
    public long hospitalID;
    public String hospitalName;

    public long getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(long hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @Override
    public String toString() {
        return "BeanHospital [hospitalID=" + hospitalID + ", hospitalName="
                + hospitalName + "]";
    }

}
