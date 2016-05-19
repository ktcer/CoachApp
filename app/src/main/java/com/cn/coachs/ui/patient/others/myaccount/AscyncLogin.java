package com.cn.coachs.ui.patient.others.myaccount;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.personinfo.BeanPersonInfo;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.CCPAppManager;
import com.cn.coachs.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.coachs.ui.chat.common.utils.ECPreferences;
import com.cn.coachs.ui.chat.core.ClientUser;
import com.cn.coachs.ui.chat.core.ContactsCache;
import com.cn.coachs.ui.chat.ui.SDKCoreHelper;
import com.cn.coachs.ui.patient.main.TabActivityMain;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CreateFolder;
import com.cn.coachs.util.UtilsSharedData;
import com.google.gson.Gson;
import com.yuntongxun.ecsdk.ECInitParams;

import org.json.JSONObject;

import java.util.HashMap;

public class AscyncLogin extends AsyncTask<Integer, Integer, String> {

    private Activity act;
    //	private static String base = "/base/app/";
    private int resultID = -1;
    private String mIdString;
    private String mPwdString;
    /**登录结果，0表示失败，1表示成功*/
    /**
     * 登录详情
     */
    private static String detail;
    private String retString = "";
    private static String macAddress;
    private BeanPersonInfo beanPersonInfo;

    public AscyncLogin(Activity act, String mIdString, String mPwdString, String macAddress) {
        this.act = act;
        this.mIdString = mIdString;
        this.macAddress = macAddress;
        this.mPwdString = mPwdString;
        UtilsSharedData.initDataShare(act);// ////////
    }

    @Override
    protected String doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("telephoneNum", mIdString);
        param.put("password", mPwdString);
        param.put("userType", "1");
        param.put("macAddress", macAddress);
        try {
            String url = AbsParam.getBaseUrl() + "/base/app/login";
            Log.i("input", url + param.toString());
            retString = NetTool.sendHttpClientPost(url, param, "utf-8");
            Log.i("result", retString);
            JsonArrayToList(retString);
        } catch (Exception e) {
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
//			((ActivityBasic)act).setInputEnabled(true);
        }
        return retString;
    }

    @Override
    protected void onPostExecute(String result) {
        if (resultID == 0) {
            Toast.makeText(act.getApplicationContext(), "您输入的账号或者密码错误", Toast.LENGTH_LONG).show();
            Log.e("Login Error", "it occurs to some error!!!!");
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
//            UtilsSharedData.saveKeyMustValue(Constant.USER_ID,)
            ((ActivityBasic) act).setInputEnabled(true);
        } else if (resultID == 1) {
//			//先判断是否已经有视频号码了
//			if(beanPersonInfo.getVideoNumber()!=null){
//				if(beanPersonInfo.getVideoNumber().equals("0")){
//					//没有注册过视频号，需要向青牛后台注册
//					//TODO
//					beanPersonInfo.setVideoNumber("0");
//				    //为后台用户增加视频号
//				}
//			}else{
//				//TODO
//				//没有注册过视频号，需要向青牛后台注册
//				beanPersonInfo.setVideoNumber("0");
//				//为后台用户增加视频号
////				}
//			}
//			JSONObject loginResult;
//			String myPhotos = "";
//			try {
//				loginResult = new JSONObject(result);
////				myPhotos = loginResult.getString("picurl");
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "1");
            UtilsSharedData.saveKeyMustValue(Constant.USER_NAME, beanPersonInfo.getName());
            UtilsSharedData.saveKeyMustValue(Constant.USER_ID, beanPersonInfo.getId());
            UtilsSharedData.saveKeyMustValue(Constant.IFEDITINFO, beanPersonInfo.getIfModify());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_REGION, beanPersonInfo.getRegion());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_HOSPITAL, beanPersonInfo.getHospital());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_GENDER, beanPersonInfo.getSex());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_BIRTHDAY, beanPersonInfo.getBirth());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_GKMUM, beanPersonInfo.getGkNumber());
            UtilsSharedData.saveKeyMustValue(Constant.USER_IMAGEURL, beanPersonInfo.getTxlj());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_NUBE, beanPersonInfo.getVideoNumber());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_INTRODUCTION, beanPersonInfo.getGrjj());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_CREDITURL, beanPersonInfo.getZgzsImgUrl());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_PHOTOS, myPhotos);
//			UtilsSharedData.saveKeyMustValue(Constant.USER_POSITION, beanPersonInfo.getPosition());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_GOODAT, beanPersonInfo.getScbz());
//			UtilsSharedData.saveKeyMustValue(Constant.USER_TWOCODE, beanPersonInfo.getTwocode());
            CreateFolder.createFolder(mIdString);
            setChatUserInfo(beanPersonInfo);
            ContactsCache.getInstance().load();
            ContactsCache.getInstance().setOnGetContactsDoneListener(new ContactsCache.InterfaceGetContactsListener() {
                @Override
                public void getContactsDone() {
                    if (beanPersonInfo.getIfModify() == 1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(act.getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(act, TabActivityMain.class);
                        act.startActivity(intent);
                        act.finish();
                    } else {
                        // 跳转到完善信息页面
                        Intent intent = new Intent(act,
                                ActivityPersonalInfo.class);
//						intent.putExtra(Constant.USER_ACCOUNT, telephonenum);
//						intent.putExtra(Constant.USER_PASS, password1);
//						intent.putExtra(Constant.USER_IDENTIFY, identifyingCode);
                        act.startActivity(intent);
                        act.finish();
                    }

                }
            });

        } else {
            ((ActivityBasic) act).setInputEnabled(true);
            Toast.makeText(act.getApplicationContext(), "您的网络好像有点问题哦，请稍后重试！", Toast.LENGTH_LONG).show();
            UtilsSharedData.saveKeyMustValue(Constant.USER_ACCOUNT, mIdString);
            UtilsSharedData.saveKeyMustValue(Constant.USER_PASS, mPwdString);
            UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
        }
    }

    /*
     * 登录聊天系统
     */
    private void setChatUserInfo(BeanPersonInfo beanPersonInfo) {
        String appkey = getConfig(ECPreferenceSettings.SETTINGS_APPKEY);
        String token = getConfig(ECPreferenceSettings.SETTINGS_TOKEN);
        ClientUser clientUser = new ClientUser(beanPersonInfo.getId() + "1");
        clientUser.setAppKey(appkey);
        clientUser.setAppToken(token);
        clientUser.setUserName(beanPersonInfo.getName());//beanPersonInfo.getUserName()
        clientUser.setLoginAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        clientUser.setPassword("");
        CCPAppManager.setClientUser(clientUser);
        SDKCoreHelper.init(act, ECInitParams.LoginMode.FORCE_LOGIN);
    }

    private String getConfig(ECPreferenceSettings settings) {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        String value = sharedPreferences.getString(settings.getId(), (String) settings.getDefaultValue());
        return value;
    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void JsonArrayToList(String jsonString) throws Exception {
        JSONObject loginResult = new JSONObject(retString);
        resultID = loginResult.getInt("result");
        Gson gson = new Gson();
        if (jsonString != "") {
            beanPersonInfo = gson.fromJson(jsonString, BeanPersonInfo.class);
        }

    }


}
