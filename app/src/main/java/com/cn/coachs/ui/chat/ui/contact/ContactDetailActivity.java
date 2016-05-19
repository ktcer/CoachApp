/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.coachs.ui.chat.ui.contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.chat.common.utils.ToastUtil;
import com.cn.coachs.ui.chat.storage.ContactSqlManager;
import com.cn.coachs.ui.chat.ui.ECSuperActivity;
import com.cn.coachs.ui.chat.ui.chatting.ChattingActivity;
import com.cn.coachs.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.coachs.ui.patient.main.detectioncenter.ActivityFansDetectionInfo;
import com.cn.coachs.ui.patient.main.healthdiary.ActivityEditDiaryDay;
import com.cn.coachs.ui.patient.main.healthdiary.ActivityFansSetRemind;
import com.cn.coachs.ui.patient.main.healthdiary.ActivityFansTestReport;
import com.cn.coachs.ui.patient.main.healthdiary.ActivityFansTestReportDetail;
import com.cn.coachs.ui.patient.main.healthdiary.AsynGetDiaryStartAndLength;
import com.cn.coachs.ui.patient.main.healthdiary.AsynGetcpStatus;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.UtilsImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Jorstin on 2015/3/18.
 */
public class ContactDetailActivity extends ECSuperActivity implements
        View.OnClickListener {

    public final static String RAW_ID = "raw_id";
    public final static String MOBILE = "mobile";
    private ImageView mPhotoView;
    private EmojiconTextView mUsername;
    private TextView mNumber, mRemainTime;
    private ImageLoader mImageLoader;
    private DisplayImageOptions options;
    private ECContacts mContacts;
    private String fansId;
    private String isMaster;
    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mContacts == null) {
                return;
            }
            Intent intent = new Intent(ContactDetailActivity.this,
                    ChattingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(ChattingActivity.RECIPIENTS,
                    mContacts.getContactid());
            intent.putExtra(Constant.FANS_NAME, mContacts.getNickname());
            intent.putExtra(Constant.fANS_IMAGEURL, mContacts.getImgUrl());
            intent.putExtra(Constant.FANS_GENDER, mContacts.getSex());
            intent.putExtra(Constant.FANS_AGE, mContacts.getAge());
            intent.putExtra(Constant.FANS_HEIGHT, mContacts.getHeight());
            intent.putExtra(Constant.FANS_WEIGHT, mContacts.getWeight());
            startActivity(intent);
            setResult(RESULT_OK);
            finish();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.layout_contact_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initActivityState(savedInstanceState);
        getTopBarView().setTopBarToStatus(1, R.drawable.ic_action_navigation_arrow_back_inverted, -1,
                R.string.contact_contactDetail, this);// 标题
    }

    /**
     * @param savedInstanceState
     */
    // String imgUrl = "";
    private void initActivityState(Bundle savedInstanceState) {
        long rawId = getIntent().getLongExtra(RAW_ID, -1);
        if (rawId == -1) {
            // //@丑旦，勿动！！！！！
            // mBeanFans = getIntent().getParcelableExtra(Constant.FANS_BEANS);
            // Log.e("=-=-=ContactDetailActivity中的mBeanFans=-=-=",""+mBeanFans.toString());

            String mobile = getIntent().getStringExtra(MOBILE);
            String displayname = getIntent().getStringExtra(Constant.FANS_NAME);
            String imgUrl = getIntent().getStringExtra(Constant.fANS_IMAGEURL);
            String sex = getIntent().getStringExtra(Constant.FANS_GENDER);
            String age = getIntent().getStringExtra(Constant.FANS_AGE);
            String height = getIntent().getStringExtra(Constant.FANS_HEIGHT);
            String weight = getIntent().getStringExtra(Constant.FANS_WEIGHT);
            isMaster = getIntent().getIntExtra(Constant.FANS_ISMASTER, 0) + "";
            // mContacts = ContactSqlManager.getCacheContact(mobile);
            // if(mContacts == null) {

            mContacts = new ECContacts(mobile);
            mContacts.setNickname(displayname);
            mContacts.setImgUrl(imgUrl);
            mContacts.setHeight(height);
            mContacts.setWeight(weight);
            mContacts.setAge(age);
            mContacts.setSex(sex);
            // }
        }

        if (mContacts == null && rawId != -1) {
            mContacts = ContactSqlManager.getContact(rawId);
        }

        if (mContacts == null) {
            ToastUtil.showMessage(R.string.contact_none);
            finish();
            return;
        }
        fansId = mContacts.getContactid().substring(0,
                mContacts.getContactid().length() - 1);
        mImageLoader.displayImage(
                AbsParam.getBaseUrl() + mContacts.getImgUrl(), mPhotoView,
                options);
        mUsername
                .setText(TextUtils.isEmpty(mContacts.getNickname()) ? mContacts
                        .getContactid() : mContacts.getNickname());
        mNumber.setText("性别：" + mContacts.getSex());
        mRemainTime.setText("年龄:" + mContacts.getAge());
    }

    /**
     *
     */
    private void initView() {
        mPhotoView = (ImageView) findViewById(R.id.desc);
        mUsername = (EmojiconTextView) findViewById(R.id.contact_nameTv);
        mNumber = (TextView) findViewById(R.id.contact_numer);
        mRemainTime = (TextView) findViewById(R.id.contact_remaintime);
        mPhotoView.setOnClickListener(this);
        findViewById(R.id.txt_assist_page).setOnClickListener(this);// 设置提醒
        findViewById(R.id.txt_monitor_data).setOnClickListener(this);// 监测数据
        findViewById(R.id.txt_health_test).setOnClickListener(this);// 健康测评
        findViewById(R.id.txt_compile_diary).setOnClickListener(this);// 编辑日记
        FButton chat = (FButton) findViewById(R.id.entrance_chat);
        chat.setOnClickListener(onClickListener);
        chat.setCornerRadius(3);
        mImageLoader = ImageLoader.getInstance();
        options = AppMain.initImageOptions(
                R.drawable.icon_touxiang_persion_gray, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPhotoView != null) {
            mPhotoView.setImageDrawable(null);
        }
        onClickListener = null;
        mContacts = null;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                v.startAnimation(AnimationUtils.loadAnimation(
                        ContactDetailActivity.this, R.anim.icon_scale));
                hideSoftKeyboard();
                finish();
                break;
            case R.id.txt_assist_page:// 设置提醒
                // showRemindDialog();
                Intent intent = new Intent(this, ActivityFansSetRemind.class);
                intent.putExtra(Constant.FANS_ID, mContacts.getContactid()
                        .substring(0, mContacts.getContactid().length() - 1));
                startActivity(intent);
                break;
            case R.id.txt_monitor_data:// 监测数据
                intent = new Intent(this, ActivityFansDetectionInfo.class);
                intent.putExtra(Constant.FANS_NAME, mContacts.getNickname());
                intent.putExtra(Constant.FANS_ID, fansId);
                intent.putExtra(Constant.fANS_IMAGEURL, mContacts.getImgUrl());
                intent.putExtra(Constant.FANS_AGE, mContacts.getAge());
                intent.putExtra(Constant.FANS_GENDER, mContacts.getSex());
                intent.putExtra(Constant.FANS_HEIGHT, mContacts.getHeight());
                intent.putExtra(Constant.FANS_WEIGHT, mContacts.getWeight());
                startActivity(intent);
                break;
            case R.id.txt_compile_diary:// 编辑日记
                getStatus();
                break;
            case R.id.txt_health_test:
                intent = new Intent(this, ActivityFansTestReport.class);
                intent.putExtra(Constant.FANS_ID, fansId);
                startActivity(intent);
                break;
            case R.id.desc:
                UtilsImage.displayBigPic(ContactDetailActivity.this,
                        mContacts.getImgUrl());
                break;
            default:
                break;
        }
    }

    /**
     * @param 以下与日记有关
     * @return 获取粉丝的状态（是否有日记）
     * @author 丑旦
     */

    private void getStatus() {
        showProgressBar();
        AsynGetcpStatus asynGetcpStatus = new AsynGetcpStatus(
                getApplicationContext(), fansId) {
            @Override
            protected void onPostExecute(JSONObject result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                try {
                    int cpStatus = result.getInt("cpstatus");
                    if (cpStatus == 6 || cpStatus == 5) {
                        // 直接编辑方案，直接进入方案编辑界面，先查询已有日记的长度
                        AsynGetDiaryStartAndLength asynGetDiaryStartAndLength = new AsynGetDiaryStartAndLength(
                                fansId) {
                            @Override
                            protected void onPostExecute(JSONArray result) {
                                // TODO Auto-generated method stub
                                super.onPostExecute(result);
                                if (result != null) {
                                    if (result.length() > 0) {
                                        try {
                                            String startDate = result
                                                    .getJSONObject(0)
                                                    .getString("startdate");
                                            int days = result.getJSONObject(0)
                                                    .getInt("days");
                                            if (startDate != null) {
                                                String planLength = String
                                                        .valueOf(days / 7);
                                                Intent intent = new Intent(
                                                        ContactDetailActivity.this,
                                                        ActivityEditDiaryDay.class);
                                                intent.putExtra(
                                                        Constant.START_DATE,
                                                        startDate);
                                                intent.putExtra(
                                                        Constant.PLAN_LENGTH,
                                                        planLength);
                                                intent.putExtra(
                                                        Constant.PATIENT_ID,
                                                        fansId);
                                                startActivity(intent);
                                            }
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                            hideProgressBar();
                                        }
                                    }
                                } else {
                                    ToastUtil.showMessage("数据错误，请联系供应商");
                                }
                            }
                        };
                        asynGetDiaryStartAndLength.execute();
                    } else {
                        // 还没有方案则进入查看测评详情界面，同时制定方案
                        if (isMaster.equals("1")) {
                            Intent intent = new Intent(ContactDetailActivity.this,
                                    ActivityFansTestReportDetail.class);
                            intent.putExtra(Constant.FANS_NAME,
                                    mContacts.getNickname());
                            intent.putExtra(Constant.FANS_ID, fansId);
                            intent.putExtra(
                                    ActivityFansTestReportDetail.TESTREPORTTYPE, 1);
                            intent.putExtra(Constant.FANS_AGE, mContacts.getAge());
                            intent.putExtra(Constant.FANS_GENDER,
                                    mContacts.getSex());
                            intent.putExtra(Constant.FANS_HEIGHT,
                                    mContacts.getHeight());
                            intent.putExtra(Constant.FANS_WEIGHT,
                                    mContacts.getWeight());
                            // intent.putExtra(Constant.FANS_BEANS, mBeanFans);
                            startActivity(intent);
                        } else {
                            ToastUtil.showMessage(R.string.youarenotmaster);
                        }

                    }
                    hideProgressBar();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    hideProgressBar();
                }
            }
        };
        asynGetcpStatus.execute();
    }
}
