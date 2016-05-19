package com.cn.coachs.ui.patient.others.myaccount;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.nurse.BeanRegion;
import com.cn.coachs.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 查询地区列表
 *
 * @author kuangtiecheng
 */
public class QueryregionList extends AsyncTask<Integer, Integer, List<BeanRegion>> {
    String result = null;
    List<BeanRegion> regionList = new ArrayList<BeanRegion>();

    @Override
    protected List<BeanRegion> doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        try {
            result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                    + "/base/app/queryregionlist", param, "utf-8");
            Log.i("result", result);
            JsonArrayToList(result);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return regionList;
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                regionList = gson.fromJson(jsonString,
                        new TypeToken<List<BeanRegion>>() {
                        }.getType());
            }
        }
    }

    @Override
    protected void onPostExecute(List<BeanRegion> result) {

    }
}