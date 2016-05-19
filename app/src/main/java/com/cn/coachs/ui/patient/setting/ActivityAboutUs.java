package com.cn.coachs.ui.patient.setting;

import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.welcome.AscycUpData;
import com.cn.coachs.util.FButton;

public class ActivityAboutUs extends ActivityBasic {
    private TextView tv1;
    private FButton upDataApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
        initial();

    }

    private void initial() {
        tv1 = (TextView) this.findViewById(R.id.tv1_aboutUs);
        upDataApp = (FButton) this.findViewById(R.id.toLogin_MyMoment);
        upDataApp.setOnClickListener(this);
        upDataApp.setCornerRadius(3);

        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("关于我们");
        TextPaint tp = tv1.getPaint();
        tp.setFakeBoldText(true);
        tv1.setText("Ver:" + AppMain.versionName);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.toLogin_MyMoment:
//			upCheck up =	new upCheck();
//			up.execute();
                //这里来检测版本是否需要更新
                AscycUpData ascycUpdate = new AscycUpData(this);
                ascycUpdate.execute();
                break;
            default:
                break;
        }
    }

}
