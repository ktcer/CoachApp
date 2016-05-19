package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;

import com.cn.coachs.model.nurse.BeanEditDiaryNodeNoContent;
import com.cn.coachs.ui.patient.others.myaccount.AsyncBasic;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/16 上午11:16:30
 * @parameter
 * @return
 */
public class AsynEditItemNoContent extends AsyncBasic {
    public BeanEditDiaryNodeNoContent beanEditDiaryNodeNoContent;

    public AsynEditItemNoContent(Context mContext, BeanEditDiaryNodeNoContent beanEditDiaryNodeNoContent) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.beanEditDiaryNodeNoContent = beanEditDiaryNodeNoContent;
        setPath("/interv/expert/eidtitem");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = super.getMap();
        map.put("patientId", "" + beanEditDiaryNodeNoContent.getFansId());
        map.put("day", "" + beanEditDiaryNodeNoContent.getDay());
        map.put("week", "" + beanEditDiaryNodeNoContent.getWeek());
        map.put("time", "" + beanEditDiaryNodeNoContent.getTime());
        map.put("id", "" + beanEditDiaryNodeNoContent.getId());
        return map;
    }
}
