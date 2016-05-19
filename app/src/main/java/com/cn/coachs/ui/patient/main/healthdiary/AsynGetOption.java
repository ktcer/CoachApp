package com.cn.coachs.ui.patient.main.healthdiary;

import android.os.AsyncTask;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.util.AbsParam;

import org.json.JSONArray;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/14 下午10:36:25
 * @parameter
 * @return
 */
public class AsynGetOption extends AsyncTask<Integer, Integer, JSONArray> {

    @Override
    protected JSONArray doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        JSONArray array = null;
        try {
            String result = NetTool.sendPostRequest(getUrl(), null, "utf-8");
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

    protected String getUrl() {
        // TODO Auto-generated method stub
        return AbsParam.getBaseUrl() + "/interv/expert/getoption";
    }
}
