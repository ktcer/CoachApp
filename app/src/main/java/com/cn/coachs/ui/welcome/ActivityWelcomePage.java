package com.cn.coachs.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.Loger;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.patient.others.myaccount.ActivityLogin;
import com.cn.coachs.ui.patient.others.myaccount.AscyncLogin;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;

public class ActivityWelcomePage extends ActivityBasic {
    //	public static boolean isQuit = false ;
    private Loger log = new Loger("[Welcome]");
    private String userAccout, userPassowrd, macAddress;
    private UtilsSharedData sharedData;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		MobclickAgent.onError(this);
        setContentView(R.layout.wlecome);
        AppMain.isAppRunning = true;
        sharedData = new UtilsSharedData();
        sharedData.initDataShare(this);
        ReadLogIni();
    }

    /**
     * 读取ini文件
     */
    private void ReadLogIni() {
        userAccout = sharedData.getValueByKey(Constant.USER_ACCOUNT);
        userPassowrd = sharedData.getValueByKey(Constant.USER_PASS);
        macAddress = getLocalMacAddress();
        if (sharedData.getValueByKey(Constant.LOGIN_STATUS).equals("1") && userAccout != null && userPassowrd != null) {
            AscyncLogin async = new AscyncLogin(this, userAccout, userPassowrd, macAddress);
            async.execute();
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(ActivityWelcomePage.this, ActivityLogin.class);
                    intent.putExtra("ActivityTag", "MainActivity");
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//		isQuit = true ;
    }

    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
}
