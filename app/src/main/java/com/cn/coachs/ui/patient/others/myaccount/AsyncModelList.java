/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author kuangtiecheng
 */
public abstract class AsyncModelList<T> extends AsyncTask<Integer, Integer, ArrayList<T>> {
    public final String TAG = this.getClass().getName();
    public String url;
    public Context mContext;

    public AsyncModelList(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    protected ArrayList<T> doInBackground(Integer... params) {
        // TODO Auto-generated method stub
        ArrayList<T> list = null;
        try {
            String result = NetTool.sendPostRequest(getUrl(), getMap(), "utf-8");
            Type type = new TypeToken<ArrayList<T>>() {
            }.getType();
            Gson gson = new Gson();
            list = gson.fromJson(result, type);
            System.out.println(TAG + " =-=-=-=-=-= list...=-=-=-=-=-=" + list.size());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(TAG + " =-=-=-=-=-= json解析出错了...=-=-=-=-=-=");
        }
        return list;
    }

    @Override
    protected void onPostExecute(ArrayList<T> result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (result == null) {
            Toast.makeText(mContext, "网络出问题了", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String path) {
        this.url = AbsParam.getBaseUrl() + path;
        // this.url = "http://192.168.202.108:8080/serviceplatform"+path;
    }

    protected long getUserId(int role) {
        UtilsSharedData.initDataShare(mContext);
        long userId = UtilsSharedData.getLong(Constant.USER_ID, role);
        return userId;
    }

    protected abstract HashMap<String, String> getMap();

}
