package com.cn.coachs.ui.patient.main.healthdiary;

import android.os.AsyncTask;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.util.AbsParam;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/14 下午10:30:41
 * @parameter
 * @return 日记开始日期、长度
 */

public class AsynGetDiaryStartAndLength extends AsyncTask<Integer, Integer, JSONArray> {
    public String fansId;

    public AsynGetDiaryStartAndLength(String fansId) {
        super();
        this.fansId = fansId;
    }

    @Override
    protected JSONArray doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        JSONArray array = null;
        try {
            String result = NetTool.sendPostRequest(getUrl(), getMap(), "utf-8");
            if (result.equals("sendText error!")) {
                System.out.println("=-=-=-=-=-= 网络不通...=-=-=-=-=-=");
            } else
                array = new JSONArray(result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return array;
    }

    protected Map<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("patientId", "" + fansId);
        return map;
    }

    protected String getUrl() {
        // TODO Auto-generated method stub
        return AbsParam.getBaseUrl() + "/interv/expert/getitem";
    }
}
