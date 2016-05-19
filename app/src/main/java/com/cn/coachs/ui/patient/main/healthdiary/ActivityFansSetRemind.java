package com.cn.coachs.ui.patient.main.healthdiary;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.UtilsSharedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityFansSetRemind extends ActivityBasic implements
        OnClickListener {
    private TextView titleText;
    private EditText et1, et2;
    private FButton btnSetRemind;
    private ImageView iv1, iv2;
    private String fansId;
    private String mornigRemind, afternoonRemind;
    private String remindInfoUrl = "";
    private Handler mHandler = new Handler();
    private ScrollView scrolllview;

    /**
     * 用户类型，0表示已经
     */
    // private static final int pageType=0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fanssetremind);
        initial();
        showProgressBar();
        AscyncGetIfSetRemind mTask = new AscyncGetIfSetRemind();
        mTask.execute();
    }

    private void initial() {
        UtilsSharedData.initDataShare(this);
        fansId = getIntent().getStringExtra(Constant.FANS_ID);
        titleText = (TextView) findViewById(R.id.middle_tv);
        scrolllview = (ScrollView) findViewById(R.id.scrolllview);
        titleText.setText("设置提醒");
        iv1 = (ImageView) findViewById(R.id.delete_morning);
        iv2 = (ImageView) findViewById(R.id.delete_afternoon);
        et1 = (EditText) findViewById(R.id.tv_morning);
        et2 = (EditText) findViewById(R.id.tv_afternoon);

        etClick();

        btnSetRemind = (FButton) findViewById(R.id.btn_submit_Remind);
        btnSetRemind.setOnClickListener(this);
        btnSetRemind.setCornerRadius(3);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_submit_Remind:
                /** 提交注册信息 */
                mornigRemind = et1.getText().toString();
                afternoonRemind = et2.getText().toString();
                showProgressBar();
                AscyncSubmitFansRemind mTask = new AscyncSubmitFansRemind();
                mTask.execute();
                break;

            case R.id.delete_morning:
                et1.setText("");
                break;
            case R.id.delete_afternoon:
                et2.setText("");
                break;
        }
    }

    private void judgeIfCanSetRemind() {
        if ((!et1.getText().toString().equals("")) && (!et2.getText().toString().equals(""))) {
            btnSetRemind.setEnabled(true);
            btnSetRemind.setButtonColor(getResources().getColor(R.color.blue_second));
        } else {
            btnSetRemind.setEnabled(false);
            btnSetRemind.setButtonColor(getResources().getColor(R.color.fbutton_color_concrete));
        }
    }

    public void etClick() {
        et1.setFocusable(true);
        et2.setFocusable(true);
        et1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(final CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv1.setVisibility(View.GONE);
                } else {
                    iv1.setVisibility(View.VISIBLE);
                    //这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //将ScrollView滚动到底
                            scrolllview.scrollTo(0, View.FOCUS_DOWN);
//						   scrolllview.fullScroll(View.FOCUS_DOWN);
                            et1.setSelection(arg0.length());
                        }
                    }, 100);

                }
                judgeIfCanSetRemind();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        et2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(final CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                if (arg0.toString().equals("")) {
                    iv2.setVisibility(View.GONE);
                } else {
                    iv2.setVisibility(View.VISIBLE);
                    //这里必须要给一个延迟，如果不加延迟则没有效果。我现在还没想明白为什么
                    mHandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            //将ScrollView滚动到底
                            scrolllview.scrollTo(0, View.FOCUS_DOWN);
                            et2.setSelection(arg0.length());
                        }
                    }, 100);
                }
                judgeIfCanSetRemind();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

    }

    /**
     * 提交粉丝提醒设置
     *
     * @author kuangtiecheng
     */

    private class AscyncSubmitFansRemind extends
            AsyncTask<Integer, Integer, String> {

        private String retString = "";
        /** 登录结果，0表示失败，1表示成功 */
        /**
         * 登录详情
         */
        private String resultDetail = "";
        private int resultStatus;
        private long userId;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
            param.put("patientID", fansId);
            param.put("expertID", userId + "");
            param.put("forenoonText", mornigRemind);
            param.put("afternoonText", afternoonRemind);
            try {
                String url = AbsParam.getBaseUrl() + "/pushnews/" + remindInfoUrl;
                retString = NetTool.sendPostRequest(url, param, "utf-8");
            } catch (Exception e) {
                hideProgressBar();
            }
            return retString;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject RegisterResult;
            try {
                RegisterResult = new JSONObject(result);
                resultStatus = RegisterResult.getInt("result");
                resultDetail = RegisterResult.getString("detail");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (resultStatus == 1) {
                //成功
                showToastDialog("设置成功");
            } else if (resultStatus == 0) {
                //失败
                showToastDialog("设置失败，" + resultDetail);
            }
            hideProgressBar();

        }

    }

    /**
     * 获取是否已经设计推送信息
     *
     * @author kuangtiecheng
     */

    private class AscyncGetIfSetRemind extends
            AsyncTask<Integer, Integer, String> {

        private String retString = "";
        /** 登录结果，0表示失败，1表示成功 */
        /**
         * 登录详情
         */
        private String resultDetail = "";
        private int resultStatus;
        private long userId;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
            param.put("patientID", fansId);
            param.put("expertID", userId + "");
            try {
                String url = AbsParam.getBaseUrl() + "/pushnews/ifsetnews";
                retString = NetTool.sendPostRequest(url, param, "utf-8");
            } catch (Exception e) {
                hideProgressBar();
            }
            return retString;
        }

        @Override
        protected void onPostExecute(String result) {
            JSONObject RegisterResult = null;
            try {
                RegisterResult = new JSONObject(result);
                resultStatus = RegisterResult.getInt("result");
                resultDetail = RegisterResult.getString("detail");
                if (resultStatus == 1) {
                    //已设置提醒
                    mornigRemind = RegisterResult.getString("amContent");
                    afternoonRemind = RegisterResult.getString("pmContent");
                    et1.setText(mornigRemind);
                    et2.setText(afternoonRemind);
                    remindInfoUrl = "editnews";
                    btnSetRemind.setEnabled(true);
                    btnSetRemind.setButtonColor(getResources().getColor(R.color.blue_second));
                } else if (resultStatus == 0) {
                    //未设置提醒
                    remindInfoUrl = "setnews";
                    btnSetRemind.setEnabled(false);
                    btnSetRemind.setButtonColor(getResources().getColor(R.color.fbutton_color_concrete));
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            hideProgressBar();

        }

    }

}
