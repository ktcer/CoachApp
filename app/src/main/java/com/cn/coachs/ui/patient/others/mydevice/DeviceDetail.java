package com.cn.coachs.ui.patient.others.mydevice;

import android.os.Bundle;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.basic.ActivityBasic;

public class DeviceDetail extends ActivityBasic {
    private TextView title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_res_device_detail);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        title = (TextView) findViewById(R.id.middle_tv);
        title.setText("设备详情");
    }


}
