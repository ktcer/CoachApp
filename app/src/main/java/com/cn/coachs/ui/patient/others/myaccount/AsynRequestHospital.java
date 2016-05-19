package com.cn.coachs.ui.patient.others.myaccount;

import android.content.Context;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/11/13 下午4:41:42
 * @parameter
 * @return
 */
public class AsynRequestHospital extends AsyncBasic {
    private String inputHospitalName;
    private int pageNum;
    private int pageSize;

    public AsynRequestHospital(Context mContext, String inputHospitalName, int pageNum, int pageSize) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.inputHospitalName = inputHospitalName;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        setPath("/base/app/querymatchhospital");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = super.getMap();
        map.put("inputHospitalName", inputHospitalName);
        map.put("pageNum", "" + pageNum);
        map.put("pageSize", "" + pageSize);
        return map;
    }
}
