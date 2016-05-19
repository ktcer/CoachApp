package com.cn.coachs.ui.patient.others.myaccount;

import java.util.HashMap;

import android.content.Context;


public class AsyncInfo extends AsyncBasic {

    public AsyncInfo(Context mContext) {
        super(mContext);
        // TODO Auto-generated constructor stub
        setPath("/baseinformation");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        return super.getMap();
    }
}

