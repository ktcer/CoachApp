package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;

import com.cn.coachs.ui.patient.others.myaccount.AsyncBasic;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/15 上午10:52:49
 * @parameter
 * @return
 */
public class AsynGetcpStatus extends AsyncBasic {
    public String patientID;

    public AsynGetcpStatus(Context mContext, String patientID) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.patientID = patientID;
        setPath("/base/app/getcpstatus");
    }

    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("patientID", patientID);
        return map;
    }
}
