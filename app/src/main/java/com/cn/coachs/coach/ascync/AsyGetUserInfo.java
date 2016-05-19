package com.cn.coachs.coach.ascync;

import android.app.Activity;
import android.os.AsyncTask;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.coach.model.BeanCoachInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 获取用户个人信息
 *
 * @author kuangtiecheng
 */
public class AsyGetUserInfo extends AsyncTask<Integer, Integer, BeanCoachInfo> {

    String resultUrl = "";
    private Activity act;
    private static String tempUrl = "/home/app/coachinfo";
    private long userId;

    public AsyGetUserInfo(Activity act) {
        this.act = act;
        UtilsSharedData.initDataShare(act);// ////////
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
    }

    @Override
    protected BeanCoachInfo doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        BeanCoachInfo beanCoachInfo = new BeanCoachInfo();
        param.put("coachID", userId + "");
        param.put("latitude", "");
        param.put("longitude", "");
        try {
            String url = AbsParam.getBaseUrl() + tempUrl;
            resultUrl = NetTool.sendPostRequest(url, param, "utf-8");
            beanCoachInfo = JsonArrayToList(resultUrl);
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
            e.printStackTrace();
        }

        return beanCoachInfo;
    }

    @Override
    protected void onPostExecute(BeanCoachInfo beanCoachInfo) {
        ((ActivityBasic) act).hideProgressBar();
        JSONObject loginResult;
        String myPhotos = "";
//		try {
//			loginResult = new JSONObject(resultUrl);
//			myPhotos = loginResult.getString("picurl");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        UtilsSharedData.saveKeyMustValue(Constant.USER_NAME, beanCoachInfo.getName());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_ID,BeanCoachInfo.getId());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_REGION, BeanCoachInfo.getRegion());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_HOSPITAL, BeanCoachInfo.getHospital());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_GENDER, BeanCoachInfo.getSex());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_BIRTHDAY, BeanCoachInfo.getBirth());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_GKMUM, BeanCoachInfo.getGkNumber());
        UtilsSharedData.saveKeyMustValue(Constant.USER_IMAGEURL, beanCoachInfo.getPicUrl());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_NUBE, BeanCoachInfo.getVideoNumber());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_INTRODUCTION, BeanCoachInfo.getGrjj());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_CREDITURL, BeanCoachInfo.getZgzsImgUrl());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_PHOTOS, myPhotos);
//		UtilsSharedData.saveKeyMustValue(Constant.USER_POSITION, BeanCoachInfo.getPosition());
//		UtilsSharedData.saveKeyMustValue(Constant.USER_GOODAT, BeanCoachInfo.getScbz());
    }

    private BeanCoachInfo JsonArrayToList(String jsonString)
            throws Exception {
        Gson gson = new Gson();
        BeanCoachInfo beenInfo = new BeanCoachInfo();
        ;
        int len = jsonString.length();
        if (jsonString != "") {
            if (!(jsonString.equals(-1))) {
                beenInfo = gson.fromJson(jsonString,
                        new TypeToken<BeanCoachInfo>() {
                        }.getType());
            }
        }
        return beenInfo;
    }


}
