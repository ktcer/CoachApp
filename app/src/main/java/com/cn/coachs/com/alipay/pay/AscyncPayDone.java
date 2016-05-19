package com.cn.coachs.com.alipay.pay;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.nurse.BeanPayDone;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class AscyncPayDone extends AsyncTask<Integer, Integer, BeanPayDone> {

    String result;
    private ActivityBasic act;
    private static String requestAddInfo = "/travel/paydone";
    private BeanPayDone beenPayDone;
    private String ddh;
    private float money;

    public AscyncPayDone() {

    }

    public AscyncPayDone(ActivityBasic act, String ddh, float money) {
        super();
        this.act = act;
        this.ddh = ddh;
        this.money = money;
        beenPayDone = new BeanPayDone();
    }

    @Override
    protected BeanPayDone doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("ddh", ddh);
        param.put("money", money + "");
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beenPayDone = JsonArrayToList(result);
        } catch (Exception e) {
            act.hideProgressBar();
            e.printStackTrace();
        }
        return beenPayDone;
    }

    @Override
    protected void onPostExecute(BeanPayDone result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanPayDone JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanPayDone beenPayDone = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beenPayDone = gson.fromJson(jsonString,
                        new TypeToken<BeanPayDone>() {
                        }.getType());
            }
        }
        return beenPayDone;
    }

}
