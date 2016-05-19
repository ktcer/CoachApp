/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.os.AsyncTask;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.util.AbsParam;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 */
public abstract class AsyncModel extends AsyncTask<Integer, Integer, JSONObject> {
    public final String TAG = this.getClass().getName();
    public String url;

    @Override
    protected JSONObject doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        JSONObject json = null;
        try {
            String result = NetTool.sendPostRequest(getUrl(), getMap(), "utf-8");
            System.out.println(TAG + "=-=-=-=-=-= result...=-=-=-=-=-=" + result);
            if (result.equals("sendText error!")) {
                System.out.println(TAG + "=-=-=-=-=-= 网络不通...=-=-=-=-=-=");
            } else
                json = new JSONObject(result);
            System.out.println(TAG + "=-=-=-=-=-= json...=-=-=-=-=-=" + json);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(TAG + "=-=-=-=-=-= 网络问题...=-=-=-=-=-=");
        }
        return json;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String path) {
        this.url = AbsParam.getBaseUrl() + path;
//		this.url = "http://192.168.202.108:8080/serviceplatform"+path;
    }

    protected abstract HashMap<String, String> getMap();

    protected abstract void setPath(String path);
}

