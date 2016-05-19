package com.cn.coachs.my;

import android.app.Activity;
import android.os.AsyncTask;

import com.cn.coachs.coach.model.BeanSubmitCourse;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * 发布课程
 */
public class AscyncSubmitCourseInfo extends
        AsyncTask<Integer, Integer, BeanSubmitCourse> {

    private Activity act;
    private String retString = "";
    /**
     * 活动好处 string
     */
    private String advantage;//
    private long coachID;//教练id number
    private String detail;//详情 string
    private String duration;//课程时长 number
    private String finalPrice;// 价格 number
    private String goal;//目的 string 若是多个，用“，”分隔，如：“健身，排汗，瘦身”
    private double latitude;//课堂纬度 number
    private String location;//课堂位置 string
    private double longitude;//课堂经度 number
    private String nums;//课堂容量 number
    private String programTypeID;//项目类别ID（课程分类） number
    private String serveType;// 服务类型 string
    private String startTime;// 活动时间 string
    private String tag;//功效（标题下面的那行） string
    private String title;//

    private BeanSubmitCourse beanSubmitCourse;


    public AscyncSubmitCourseInfo(Activity act, String advantage, long coachID, String detail, String duration, String finalPrice, String goal, double latitude, String location, double longitude, String nums, String programTypeID, String serveType, String startTime, String tag, String title) {
        this.act = act;
        this.advantage = advantage;
        this.coachID = coachID;
        this.detail = detail;
        this.duration = duration;
        this.finalPrice = finalPrice;
        this.goal = goal;
        this.latitude = latitude;
        this.location = location;
        this.longitude = longitude;
        this.nums = nums;
        this.programTypeID = programTypeID;
        this.serveType = serveType;
        this.startTime = startTime;
        this.tag = tag;
        this.title = title;
        beanSubmitCourse = new BeanSubmitCourse();
    }

    @Override
    protected BeanSubmitCourse doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        String url = AbsParam.getBaseUrl() + "/ad/app/publish";
        param.put("coachID", coachID + "");
        param.put("location", location);
        param.put("advantage", advantage);
        param.put("detail", detail);
        param.put("duration", duration + "");
        param.put("finalPrice", finalPrice + "");
        param.put("goal", goal);
        param.put("latitude", latitude + "");
        param.put("longitude", longitude + "");
        param.put("nums", nums + "");
        param.put("programTypeID", programTypeID);
        param.put("serveType", serveType);
        param.put("tag", tag);
        param.put("title", title);
        param.put("startTime", startTime);

        try {
            retString = NetTool.sendPostRequest(url, param, "utf-8");
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
        }
        try {
            beanSubmitCourse = JsonArrayToList(retString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanSubmitCourse;
    }


    @Override
    protected void onPostExecute(BeanSubmitCourse beanSubmitCourse) {

    }

    private BeanSubmitCourse JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanSubmitCourse beenInfo = new BeanSubmitCourse();
        ;
        int len = jsonString.length();
        if (jsonString != "") {
            beenInfo = gson.fromJson(jsonString,
                    new TypeToken<BeanSubmitCourse>() {
                    }.getType());
        }
        return beenInfo;
    }

}
