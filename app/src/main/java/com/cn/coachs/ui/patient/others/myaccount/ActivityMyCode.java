package com.cn.coachs.ui.patient.others.myaccount;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.welcome.AscycUpData;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.CircleImageView;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ActivityMyCode extends ActivityBasic {

    private String userName, userHospital, userGoodat, userPositon;
    private String imageUrl = "";
    private long userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycode);
        initial();

    }

    private void initial() {
        UtilsSharedData.initDataShare(this);
        userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        userName = UtilsSharedData.getValueByKey(Constant.USER_NAME);
        imageUrl = UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
        userHospital = UtilsSharedData.getValueByKey(Constant.USER_HOSPITAL);
        userGoodat = UtilsSharedData.getValueByKey(Constant.USER_GOODAT);
        userPositon = UtilsSharedData.getValueByKey(Constant.USER_POSITION);

        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("我的二维码");
        CircleImageView myHeadImage = (CircleImageView) findViewById(R.id.my_head_image);

        TextView myname = (TextView) findViewById(R.id.my_name);
        TextView mystaff = (TextView) findViewById(R.id.my_staff);
        TextView mygoodat = (TextView) findViewById(R.id.my_good_at);

        ImageView mytwodimentioncode = (ImageView) findViewById(R.id.my_twodimentioncode);
        myname.setText(userName);
        mystaff.setText(userPositon);
        mygoodat.setText("擅长" + userGoodat);
        ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + imageUrl, myHeadImage,
                AppMain.initImageOptions(R.drawable.default_user_icon, true));
        ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + UtilsSharedData.getValueByKey(Constant.USER_TWOCODE), mytwodimentioncode,
                AppMain.initImageOptions(R.drawable.default_healthdiary, true));
//		UtilsImage.getcodePic(mytwodimentioncode,"addexpert"
//				+ "," + userId + "," + imageUrl,450);
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
