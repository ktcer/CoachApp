package com.cn.coachs.ui.patient.main.healthdiary;

import java.util.HashMap;

import org.json.JSONObject;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncIsFansMaster extends AsyncTask<Integer, Integer, String> {
    private String master;
    private Context mContext;
    private String fansId;

    public AsyncIsFansMaster(Context mContext, String fansId) {
        super();
        this.mContext = mContext;
        this.fansId = fansId;
        UtilsSharedData.initDataShare(mContext);
    }

    @Override
    protected String doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        HashMap<String, String> param = new HashMap<String, String>();
        String expertId = UtilsSharedData.getLong(Constant.USER_ID, 0) + "";
        param.put("expertId", expertId);
        param.put("patientId", fansId);
        try {
            String url = AbsParam.getBaseUrl() + "/member/request/isMasterExpert";
            String retString = NetTool.sendHttpClientPost(url, param, "utf-8");
            JSONObject json = new JSONObject(retString);
            master = json.getString("result");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return master;
    }
}
