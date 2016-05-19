package com.cn.coachs.ui.patient.others.myaccount;

import java.util.HashMap;

import android.content.Context;

import com.cn.coachs.model.myaccount.BeanIncomeDetail;

public class AsyncIncomeDetail extends AsyncModelList<BeanIncomeDetail> {
    private int pageNum;
    private int pageSize;

    public AsyncIncomeDetail(Context mContext, int pageNum, int pageSize) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        setUrl("/mywallet/moneyhistory");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userID", "" + getUserId(1));
        map.put("userType", "" + 1);
        map.put("pageNum", "" + pageNum);
        map.put("pageSize", "" + pageSize);
        return map;
    }
}
