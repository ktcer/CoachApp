package com.cn.coachs.ui.patient.others.myaccount;

import java.util.HashMap;

import android.content.Context;

public class AsyncFansList extends AsyncBasic {
    public String date;

    public AsyncFansList(Context mContext, String date) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.date = date;
        setPath("/member/newfanscounts");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = super.getMap();
        map.put("expertID", "" + getUserId(1));
        map.put("date", date);
        return map;
    }
}

