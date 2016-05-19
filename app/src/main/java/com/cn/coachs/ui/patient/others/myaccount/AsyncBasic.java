package com.cn.coachs.ui.patient.others.myaccount;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;

import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;

public abstract class AsyncBasic extends AsyncModel {
    public Context mContext;

    public AsyncBasic(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userID", "" + getUserId(1));
        map.put("userType", "" + 1);
        return map;
    }

    @Override
    protected void setPath(String path) {
        // TODO Auto-generated method stub
        setUrl(path);
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        System.out.println("=-=-= result =-=-=" + result);
        if (result == null) {
//			Toast.makeText(mContext, "查无记录", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    protected long getUserId(int role) {
        UtilsSharedData.initDataShare(mContext);
        long userId = UtilsSharedData.getLong(Constant.USER_ID, role);
        return userId;
    }

}
