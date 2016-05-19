package com.cn.coachs.my;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.coach.ascync.AsyGetUserInfo;
import com.cn.coachs.coach.model.BeanCoachInfo;
import com.cn.coachs.coach.model.BeanGetStar;
import com.cn.coachs.comment.ActivityQueryCoachMomment;
import com.cn.coachs.comment.AscynGetStar;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.patient.others.myaccount.ActivityPersonalInfo;
import com.cn.coachs.ui.patient.others.myaccount.Setting;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.UtilsSharedData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by ktcer on 2016/1/14.
 */
public class ActivityMyPage extends ActivityBasic implements
        View.OnClickListener {
    private ImageView iv, imagestar1, imagestar2, imagestar3, imagestar4, imagestar5;
    private TextView tv_name, tv_score;
    private String userName;
    private long userId;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String imageUrl = "";
    private Bitmap head;// 头像Bitmap
    private FButton course;

    private LinearLayout myMoney;
    private LinearLayout layout_mybonus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_my_pages);
        setdata();
        initial();
    }

    private void setdata() {
        UtilsSharedData.initDataShare(this);
        options = AppMain.initImageOptions(R.drawable.default_user_icon, true);// 构建完成
        imageLoader = ImageLoader.getInstance();
    }

    /**
     *
     */
    private void initial() {
        userName = UtilsSharedData.getValueByKey(Constant.USER_NAME);
        userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
        tv_name = (TextView) findViewById(R.id.name_informationManage);
        tv_score = (TextView) findViewById(R.id.score_text);
        iv = (ImageView) findViewById(R.id.image_informationManage);
        imagestar1 = (ImageView) findViewById(R.id.image1);
        imagestar2 = (ImageView) findViewById(R.id.image2);
        imagestar3 = (ImageView) findViewById(R.id.image3);
        imagestar4 = (ImageView) findViewById(R.id.image4);
        imagestar5 = (ImageView) findViewById(R.id.image5);
//        setDataImgStar(3);
//        tv_score.setText("1.5");

        tv_name.setText(userName);
        imageLoader.displayImage(imageUrl, iv, options);

        findViewById(R.id.setting).setOnClickListener(this);
        findViewById(R.id.boutique_experience_class_ll).setOnClickListener(this);
        findViewById(R.id.training_lesson_plan_ll).setOnClickListener(this);
        course = (FButton) findViewById(R.id.btn_distribute_course);
        course.setOnClickListener(this);

        myMoney = (LinearLayout) findViewById(R.id.layout_mymoney);
        myMoney.setOnClickListener(this);

        layout_mybonus = (LinearLayout) findViewById(R.id.layout_mybonus);
        layout_mybonus.setOnClickListener(this);


        iv.setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setDataImgStar(int num) {
        tv_score.setText(num + "");
        switch (num) {
            case 0:
                imagestar1.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar2.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar3.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar4.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar5.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                break;
            case 1:
                imagestar1.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar2.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar3.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar4.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar5.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                break;
            case 2:
                imagestar1.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar2.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar3.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar4.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar5.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                break;
            case 3:
                imagestar1.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar2.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar3.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar4.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                imagestar5.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                break;
            case 4:
                imagestar1.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar2.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar3.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar4.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar5.setBackground(getResources().getDrawable(R.drawable.level_unselected));
                break;
            case 5:
                imagestar1.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar2.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar3.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar4.setBackground(getResources().getDrawable(R.drawable.star_orange));
                imagestar5.setBackground(getResources().getDrawable(R.drawable.star_orange));
                break;
        }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        userName = UtilsSharedData.getValueByKey(Constant.USER_NAME);
        tv_name.setText(userName);
        imageUrl = AbsParam.getBaseUrl() + UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
        imageLoader.displayImage(imageUrl, iv, options);
        showProgressBar();
        AsyGetUserInfo userTask = new AsyGetUserInfo(this) {

            @Override
            protected void onPostExecute(BeanCoachInfo result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                hideProgressBar();
                userName = result.getName();
                tv_name.setText(userName);
                imageUrl = AbsParam.getBaseUrl() + result.getPicUrl();
                imageLoader.displayImage(imageUrl, iv, options);
            }
        };
        userTask.execute();

        showProgressBar();
        AscynGetStar task = new AscynGetStar(this, userId) {
            @Override
            protected void onPostExecute(BeanGetStar beanGetStar) {
                super.onPostExecute(beanGetStar);
                hideProgressBar();
                if (beanGetStar == null) {
                    return;
                } else {
                    setDataImgStar(beanGetStar.getStar());
                }
            }
        };
        task.execute();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_distribute_course:
                intent = new Intent(this, ActivityPublishCourse.class);//
                startActivity(intent);
                break;
            case R.id.training_lesson_plan_ll://
                showToastDialog("尚未开放");
//                intent = new Intent();
//                intent.setClass(this, Setting.class);//ActivityMyCoursrList
//                startActivity(intent);
                break;
            case R.id.boutique_experience_class_ll://我的课程
                intent = new Intent();
                intent.setClass(this, ActivityMyCoursrList.class);
                startActivity(intent);
                break;
            case R.id.setting: /* 设置 */
                intent = new Intent();

                intent.setClass(this, Setting.class);
                startActivity(intent);
                break;
            case R.id.image_informationManage://修改个人信息
                intent = new Intent();
                intent.setClass(this, ActivityPersonalInfo.class);
                startActivity(intent);
                break;

            case R.id.layout_mymoney:
                startActivity(ActivityMyOrdersList.class);// 我的钱包Purse.class
                break;
            case R.id.layout_mybonus:
//			intent = new Intent(ActivityMyAccountCenter.this,ActivityFans.class);// 我的粉丝
                intent = new Intent(this, ActivityQueryCoachMomment.class);// 我的粉丝
                startActivity(intent);
                break;
            case R.id.btn_remark:
                // //与后台交互...
//			remarkData();
                break;
//		case R.id.my_two_dimension_codeing:
//			getMyTDC();
//		break;
            default:
                break;
        }
    }


}
