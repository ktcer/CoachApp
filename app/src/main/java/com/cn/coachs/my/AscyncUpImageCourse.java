package com.cn.coachs.my;

import android.app.Activity;
import android.os.AsyncTask;

import com.cn.coachs.coach.model.BeanCourseImage;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * 发布课程
 */
public class AscyncUpImageCourse extends
        AsyncTask<Integer, Integer, BeanCourseImage> {

    private Activity act;
    private String retString = "";
    private String imgUrl;
    private long coachID;//上一个接口返回来的课程Id

    private BeanCourseImage beanCourseImage;

    public AscyncUpImageCourse(long coachID, String imgUrl, Activity act) {
        this.coachID = coachID;
        this.imgUrl = imgUrl;
        this.act = act;
        beanCourseImage = new BeanCourseImage();
    }

    @Override
    protected BeanCourseImage doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        String url = AbsParam.getBaseUrl() + "/ad/app/uploadcover";//+ "?userID=" + coachID;
        param.put("userID", coachID + "");

        try {
            retString = NetTool.uploadFile(url, imgUrl, param, null);
        } catch (Exception e) {
            ((ActivityBasic) act).hideProgressBar();
        }
        try {
            beanCourseImage = JsonArrayToList(retString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanCourseImage;
    }


    @Override
    protected void onPostExecute(BeanCourseImage beanCourseImage) {

    }

    private BeanCourseImage JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanCourseImage beenInfo = new BeanCourseImage();
        int len = jsonString.length();
        if (jsonString != "") {
            beenInfo = gson.fromJson(jsonString,
                    new TypeToken<BeanCourseImage>() {
                    }.getType());
        }
        return beenInfo;
    }

}
