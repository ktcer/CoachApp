/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.dialog.ECProgressDialog;
import com.cn.coachs.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.coachs.ui.chat.common.utils.ECPreferences;
import com.cn.coachs.ui.chat.common.view.SettingItem;
import com.cn.coachs.ui.chat.ui.SDKCoreHelper;
import com.cn.coachs.ui.patient.main.healthclass.ActivityDetailWebService;
import com.cn.coachs.ui.patient.setting.ActivityAboutUs;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.UtilsSharedData;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECInitParams;

import java.io.InvalidClassException;

/**
 * @author kuangtiecheng
 *         我的-设置-请求个人信息、退出登录
 */
public class Setting extends ActivityBasic {
    private SettingItem helpCenter, aboutUs;
    private ECProgressDialog mPostingdialog;
    private FButton logoutBtn;
    private int resultdata;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.myaccount_setting);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("设置");
        helpCenter = (SettingItem) findViewById(R.id.help_center);
        helpCenter.setOnClickListener(this);

        aboutUs = (SettingItem) findViewById(R.id.about_us);
        aboutUs.setOnClickListener(this);

        logoutBtn = (FButton) findViewById(R.id.unload_informationMange);
        logoutBtn.setOnClickListener(this);
        logoutBtn.setCornerRadius(3);
        setdata();
    }

    private void setdata() {
        UtilsSharedData.initDataShare(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.help_center:
//			intent = new Intent();
//			intent.setClass(this, ActivityHelp.class);
//			startActivity(intent);
                String URL = "http://101.200.90.103/static/helper/help.html";
                intent = new Intent(this,
                        ActivityDetailWebService.class);
                intent.putExtra("url", URL);
                intent.putExtra("title", "帮助中心");
                startActivity(intent);
                break;
            case R.id.about_us:
                intent = new Intent();
                intent.setClass(this, ActivityAboutUs.class);
                startActivity(intent);
                break;
            case R.id.unload_informationMange:
                handleLogout();
                break;
            default:
                break;
        }
    }


    /**
     * 处理退出操作
     */
    private void handleLogout() {
        mPostingdialog = new ECProgressDialog(this, R.string.posting_logout);
        mPostingdialog.show();
        SDKCoreHelper.init(this, ECInitParams.LoginMode.FORCE_LOGIN);
        SDKCoreHelper.logout();
        ECDevice.unInitial();
        try {
            ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_REGIST_AUTO, "", true);
        } catch (InvalidClassException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        UtilsSharedData.saveKeyMustValue(Constant.LOGIN_STATUS, "0");
        finishAllAct();
        startActivity(ActivityLogin.class);
    }

}
