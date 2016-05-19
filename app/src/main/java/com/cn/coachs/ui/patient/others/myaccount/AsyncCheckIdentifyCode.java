package com.cn.coachs.ui.patient.others.myaccount;

import android.os.AsyncTask;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.nurse.BeanCheckIndentifyCode;
import com.cn.coachs.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 */
public class AsyncCheckIdentifyCode extends AsyncTask<Integer, Integer, BeanCheckIndentifyCode> {

    String result = "";
    private String phoneNumber;
    private String indetifyCode;
    private static String lastUrl = "/base/app/checkidentifycode";
    private BeanCheckIndentifyCode beanCheckIndentifyCode = new BeanCheckIndentifyCode();

    public AsyncCheckIdentifyCode(String phoneNumber, String indetifyCode) {
        this.phoneNumber = phoneNumber;
        this.indetifyCode = indetifyCode;
    }

    @Override
    protected BeanCheckIndentifyCode doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("telephoneNum", phoneNumber);
//		param.put("terminal", "ANDROID");
        param.put("identifyingCode", indetifyCode);
//		param.put("userType", 1+"");
        try {
            String url = AbsParam.getBaseUrl() + lastUrl;
            result = NetTool.sendHttpClientPost(url, param, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Gson gson = new Gson();
            // 添加我自己的信息
            if (result != null) {
                if (!(result.equals(-1))) {
                    beanCheckIndentifyCode = gson.fromJson(result,
                            new TypeToken<BeanCheckIndentifyCode>() {
                            }.getType());
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return beanCheckIndentifyCode;
    }

    @Override
    protected void onPostExecute(BeanCheckIndentifyCode beanCheckIndentifyCode) {

    }


}
