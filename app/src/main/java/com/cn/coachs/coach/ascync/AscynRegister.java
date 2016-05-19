package com.cn.coachs.coach.ascync;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.coach.model.BeanRegister;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by ktcer on 2016/1/7.
 */
public class AscynRegister extends AsyncTask<Integer, Integer, BeanRegister> {

    private Activity act;
    private String mIdString;
    private String mPwdString;
    private String retString = "";
    private static String identifyingCode;
    private BeanRegister beanRegister;

    public AscynRegister(Activity act, String mIdString, String mPwdString, String identifyingCode) {
        this.act = act;
        this.mIdString = mIdString;
        this.identifyingCode = identifyingCode;
        this.mPwdString = mPwdString;
        beanRegister = new BeanRegister();
        UtilsSharedData.initDataShare(act);// ////////
    }

    @Override
    protected BeanRegister doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("telephoneNum", mIdString);
        param.put("password", mPwdString);
        param.put("userType", "1");
        param.put("identifyingCode", identifyingCode);
        try {
            String url = AbsParam.getBaseUrl() + "/base/app/regist";
            Log.i("input", url + param.toString());
            retString = NetTool.sendHttpClientPost(url, param, "utf-8");
            Log.i("result", retString);
            beanRegister = JsonArrayToList(retString);
        } catch (Exception e) {
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
//			((ActivityBasic)act).setInputEnabled(true);
        }
        return beanRegister;
    }

    @Override
    protected void onPostExecute(BeanRegister result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanRegister JsonArrayToList(String jsonString) throws Exception {
        BeanRegister bean = new BeanRegister();
        Gson gson = new Gson();
        if (jsonString != "") {
            bean = gson.fromJson(jsonString, BeanRegister.class);
        }
        return bean;

    }


}
