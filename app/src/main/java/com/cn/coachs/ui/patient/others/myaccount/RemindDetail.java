package com.cn.coachs.ui.patient.others.myaccount;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.basic.ActivityBasic;

public class RemindDetail extends ActivityBasic {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.myaccount_my_remind_detail);

        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("提醒详情");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            default:
                break;
        }
    }
}
