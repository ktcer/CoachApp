package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;

import com.cn.coachs.model.nurse.BeanEditDiaryNode;
import com.cn.coachs.ui.patient.others.myaccount.AsyncBasic;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/14 下午10:37:46
 * @parameter
 * @return
 */
public class AsynEditItem extends AsyncBasic {
    public BeanEditDiaryNode beanEditDiaryNode;

    public AsynEditItem(Context mContext, BeanEditDiaryNode beanEditDiaryNode) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.beanEditDiaryNode = beanEditDiaryNode;
        setPath("/interv/expert/eidtitem");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = super.getMap();
        map.put("patientId", "" + beanEditDiaryNode.getFansId());
        map.put("id", "" + beanEditDiaryNode.getId());
        map.put("content", "" + beanEditDiaryNode.getContent());
        map.put("day", "" + beanEditDiaryNode.getDay());
        map.put("week", "" + beanEditDiaryNode.getWeek());
        map.put("time", "" + beanEditDiaryNode.getTime());

        return map;
    }
}
