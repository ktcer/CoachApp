package com.cn.coachs.ui.patient.others.myaccount;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;

public class AsyncGetMonthInfo extends AsyncTask<Integer, Integer, String> {

    String result = "";
    private Activity act;
    private static String mycoins = "/mywallet/moneydata";
    private long userId;

    public AsyncGetMonthInfo(Activity act) {
        this.act = act;
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected String doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("userID", userId + "");
        param.put("userType", 1 + "");
        try {
            String url = AbsParam.getBaseUrl() + mycoins;
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }
        try {
            JSONObject json = new JSONObject(result);
            SettlementFrag.monthIncome = Float.parseFloat(json
                    .getString("monthIncome"));
            SettlementFrag.monthCash = Float.parseFloat(json
                    .getString("monthCash"));
            IncomeFrag.allIncome = Float.parseFloat(json
                    .getString("allIncome"));
            IncomeFrag.allCash = Float.parseFloat(json
                    .getString("allCash"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
//		((ActivityBasic)act).hideProgressBar();

    }


}
