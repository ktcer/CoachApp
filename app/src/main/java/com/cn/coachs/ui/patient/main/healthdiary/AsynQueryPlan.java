package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;

import com.cn.coachs.ui.patient.others.myaccount.AsyncBasic;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/14 下午5:46:49
 * @parameter
 * @return
 */
public class AsynQueryPlan extends AsyncBasic {
    public String startDate, planLength, fansId;

    public AsynQueryPlan(Context mContext, String startDate, String planLength, String fansId) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.fansId = fansId;
        this.planLength = planLength;
        this.startDate = startDate;
        setPath("/interv/expert/queryplan");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = super.getMap();
        map.put("expertId", "" + getUserId(1));
        map.put("patientId", fansId);
        map.put("days", "" + (Integer.parseInt(planLength) * 7));
        map.put("startTime", startDate);

        return map;
    }
}
