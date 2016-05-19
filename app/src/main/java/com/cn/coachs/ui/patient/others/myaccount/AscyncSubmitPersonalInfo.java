package com.cn.coachs.ui.patient.others.myaccount;

import android.app.Activity;
import android.os.AsyncTask;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.coach.model.BeandituserUIinfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * 提交个人信息
 *
 * @author kuangtiecheng
 */

public class AscyncSubmitPersonalInfo extends
        AsyncTask<Integer, Integer, BeandituserUIinfo> {

    private Activity act;
    private String retString = "";
    /** 登录结果，0表示失败，1表示成功 */
    /**
     * 登录详情
     */
    private String userName, userGender, userBirth, teacherTime, athleticsType,
            teacheraddress, userGoodat, userIntroduction;
    private long userId;
    private BeandituserUIinfo beandituserUIinfo;
    private double latitude;
    private double longitude;


    public AscyncSubmitPersonalInfo(long userId, Activity act, String userName, String userGender, String userBirth, String teacherTime, String athleticsType, String teacheraddress, String userGoodat, String userIntroduction, double latitude, double longitude) throws Exception {
        this.userId = userId;
        this.act = act;
        this.userName = userName;
        this.userGender = userGender;
        this.userBirth = userBirth;
        this.teacherTime = teacherTime;
        this.athleticsType = athleticsType;
        this.teacheraddress = teacheraddress;
        this.userGoodat = userGoodat;
        this.userIntroduction = userIntroduction;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    @Override
    protected BeandituserUIinfo doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        String url = AbsParam.getBaseUrl() + "/base/app/edituserinfo";
        BeandituserUIinfo BeandituserUIinfo = new BeandituserUIinfo();
        // 修改个人信息
        param.put("id", userId + "");
//			param.put("userType", "1");
        param.put("name", userName);
        param.put("programType", athleticsType);
        param.put("birth", userBirth);
        param.put("sex", userGender);
        param.put("teachTime", teacherTime);
//        param.put("sex", teacherway);
        param.put("workAddress", teacheraddress);
        param.put("rewards", userGoodat);
        param.put("resume", userIntroduction);
        param.put("latitude", latitude + "");
        param.put("longitude", longitude + "");
        param.put("height", "");
        param.put("weight", "");

        try {
            retString = NetTool.sendPostRequest(url, param, "utf-8");
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
        }
        try {
            beandituserUIinfo = JsonArrayToList(retString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beandituserUIinfo;
    }


    @Override
    protected void onPostExecute(BeandituserUIinfo beandituserUIinfo) {

    }

    private BeandituserUIinfo JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeandituserUIinfo beenInfo = new BeandituserUIinfo();
        ;
        int len = jsonString.length();
        if (jsonString != "") {
            beenInfo = gson.fromJson(jsonString,
                    new TypeToken<BeandituserUIinfo>() {
                    }.getType());
        }
        return beenInfo;
    }

}
