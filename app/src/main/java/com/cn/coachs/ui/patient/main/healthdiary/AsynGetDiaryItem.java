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
 * @return 获取日记某一天的内容
 */
public class AsynGetDiaryItem extends AsyncTask<Integer, Integer, JSONArray> {
    public String fansId;
    public int position;//第几天

    public AsynGetDiaryItem(String fansId, int position) {
        super();
        this.fansId = fansId;
        this.position = position;
    }

    @Override
    protected JSONArray doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        JSONArray array = null;
        try {
            String result = NetTool.sendPostRequest(getUrl(), getMap(), "utf-8");
            System.out.println("=-=-=-=-=-= result...=-=-=-=-=-=" + result);
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
        map.put("day", "" + position);
        return map;
    }

    protected String getUrl() {
        // TODO Auto-generated method stub
        return AbsParam.getBaseUrl() + "/interv/expert/getitem";
    }
}
