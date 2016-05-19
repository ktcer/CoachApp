package com.cn.coachs.coach.ascync;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.coach.model.BeandituserUIinfo;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by ktcer on 2016/1/7.
 */
public class AscynCoachEditUserInfo extends AsyncTask<Integer, Integer, BeandituserUIinfo> {

    private Activity act;
    private String retString;
    private String id;// 用户id number Long
    private String name;//姓名 string
    private String programType;//擅长项目类型（只有教练角色传） string 若擅长多个，用“，”分隔，如：“1，3，6”
    private String resume;//个人简介（只有教练角色传） string
    private String rewards;// 获奖情况（只有教练角色传） string
    private String sex;// 性别 string
    private String workAddress;//

    private BeandituserUIinfo beandituserUIinfo;

    public AscynCoachEditUserInfo(Activity act, String id, String name, String programType, String resume, String rewards, String sex, String workAddress) {
        this.act = act;
        this.id = id;
        this.name = name;
        this.programType = programType;
        this.resume = resume;
        this.rewards = rewards;
        this.sex = sex;
        this.workAddress = workAddress;
        beandituserUIinfo = new BeandituserUIinfo();
    }

    @Override
    protected BeandituserUIinfo doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("id", id);
        param.put("name", name);
        param.put("programType", programType);
        param.put("resume", resume);
        param.put("rewards", rewards);
        param.put("sex", sex);
        param.put("workAddress", workAddress);
        try {
            String url = AbsParam.getBaseUrl() + "/base/app/edituserinfo";
            Log.i("input", url + param.toString());
            retString = NetTool.sendHttpClientPost(url, param, "utf-8");
            Log.i("result", retString);
            beandituserUIinfo = JsonArrayToList(retString);
        } catch (Exception e) {
            e.printStackTrace();
//            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
//            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
//            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
//			((ActivityBasic)act).setInputEnabled(true);
        }
        return beandituserUIinfo;
    }

    @Override
    protected void onPostExecute(BeandituserUIinfo result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeandituserUIinfo JsonArrayToList(String jsonString) throws Exception {
        BeandituserUIinfo bean = new BeandituserUIinfo();
        Gson gson = new Gson();
        if (jsonString != "") {
            bean = gson.fromJson(jsonString, BeandituserUIinfo.class);
        }
        return bean;

    }


}
