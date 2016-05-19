package com.cn.coachs.ui.patient.main.healthdiary;

import android.os.AsyncTask;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.chat.common.utils.ToastUtil;
import com.cn.coachs.util.AbsParam;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/11/15 下午10:37:13
 * @parameter
 * @return
 */
public class AsynSearchHospital extends AsyncTask<Integer, Integer, JSONArray> {
    private String inputHospitalName;
    private int pageNum;
    private int pageSize;

    public AsynSearchHospital(String inputHospitalName, int pageNum,
                              int pageSize) {
        super();
        this.inputHospitalName = inputHospitalName;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    @Override
    protected JSONArray doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        JSONArray json = null;
        try {
            String result = NetTool.sendPostRequest(getUrl(), getMap(), "utf-8");
            if (result.equals("sendText error!")) {
                ToastUtil.showMessage("网络出问题了");
                System.out.println("=-=-=-=-=-= 网络不通...=-=-=-=-=-=");
            } else
                json = new JSONArray(result);
            System.out.println("=-=-=-=-=-= json...=-=-=-=-=-=" + json);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    private String getUrl() {
        // TODO Auto-generated method stub
        return AbsParam.getBaseUrl() + "/base/app/querymatchhospital";
    }

    private Map<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("inputHospitalName", inputHospitalName);
        map.put("pageNum", "" + pageNum);
        map.put("pageSize", "" + pageSize);
        return map;
    }
}
