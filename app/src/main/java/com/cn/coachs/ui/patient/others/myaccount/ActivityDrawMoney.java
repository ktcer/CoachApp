package com.cn.coachs.ui.patient.others.myaccount;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.UtilsSharedData;

public class ActivityDrawMoney extends ActivityBasic {
    private EditText et_gatherAcount, et_withdrawalAmount, et_realName, et_passward;
    private TextView tv_balance;
    private FButton btn_drawConfirm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_money);
        et_gatherAcount = (EditText) findViewById(R.id.gatherAccount);
        et_withdrawalAmount = (EditText) findViewById(R.id.withdrawalAmount);
        et_realName = (EditText) findViewById(R.id.realName);
        et_passward = (EditText) findViewById(R.id.et_passward);
        tv_balance = (TextView) findViewById(R.id.balance);
        btn_drawConfirm = (FButton) findViewById(R.id.drawing);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("提现");
        String blance = "还可以提款" + Purse.balance + "元";
        int length = blance.length();
        SpannableStringBuilder style = new SpannableStringBuilder(blance);
        style.setSpan(new ForegroundColorSpan(/*android.R.color.holo_red_light*/0xffff0000), 5, length - 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_balance.setText(style);
        btn_drawConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String account = et_gatherAcount.getText().toString();
                String amount = et_withdrawalAmount.getText().toString();
                String realName = et_realName.getText().toString();
                String passward = et_passward.getText().toString();
                if (account.equals("")) {
                    Toast.makeText(ActivityDrawMoney.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                } else if (realName.equals("")) {
                    Toast.makeText(ActivityDrawMoney.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                } else if (amount.equals("")) {
                    Toast.makeText(ActivityDrawMoney.this, "金额不能为空", Toast.LENGTH_SHORT).show();
                    amount = "0";
                } else if (Float.parseFloat(amount) > Purse.balance) {
                    Toast.makeText(ActivityDrawMoney.this, "提现金额不能大于账户余额", Toast.LENGTH_SHORT).show();
                } else if (passward.equals("")) {
                    Toast.makeText(ActivityDrawMoney.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    AsyPasswardChecking asyPasswardChecking = new AsyPasswardChecking(ActivityDrawMoney.this);
                    asyPasswardChecking.execute();
                    showProgressBar();
                }
            }
        });
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    private class AsyPasswardChecking extends AsyncTask<Void, Void, String> {

        private Context context;
        private long userId;
        private String passwardChecking = "/mywallet/checkpassword";
        private String result = "";

        public AsyPasswardChecking(Context context) {
            this.context = context;
            UtilsSharedData.initDataShare(context);// ////////
            userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        }

        @Override
        protected String doInBackground(Void... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userID", userId + "");
            param.put("userType", "1");
            param.put("password", et_passward.getText().toString());
            String url = AbsParam.getBaseUrl() + passwardChecking;
            try {
                result = NetTool.sendPostRequest(url, param, "utf-8");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hideProgressBar();
            if (result != null) {
                if (Integer.parseInt(result) == 1) {
                    AsyDrawing asyDrawing = new AsyDrawing(ActivityDrawMoney.this);
                    asyDrawing.execute();
                    showProgressBar();
                }
                if (Integer.parseInt(result) == 0) {
                    CustomDialog dialog = new CustomDialog.Builder(context).setTitle("提示").setMessage("密码输入错误，请重新输入！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            et_passward.setHint("");
                        }
                    }).create();
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);
                }
            } else {
                Toast.makeText(context, "服务器请求失败，请重试...", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private class AsyDrawing extends AsyncTask<Void, Void, String> {

        private Context context;
        private long userId;
        private String takecash = "/mywallet/takecash";
        private String result = "";

        public AsyDrawing(Context context) {
            this.context = context;
            UtilsSharedData.initDataShare(context);// ////////
            userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        }

        @Override
        protected String doInBackground(Void... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userID", userId + "");
            param.put("userType", "1");
            param.put("outAccount", et_gatherAcount.getText().toString());
            param.put("money", et_withdrawalAmount.getText().toString());
            param.put("realName", et_realName.getText().toString());
            param.put("realName", et_realName.getText().toString());
            String url = AbsParam.getBaseUrl() + takecash;
            try {
                result = NetTool.sendPostRequest(url, param, "utf-8");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                hideProgressBar();
                if (result != null) {
                    JSONObject job = new JSONObject(result);
                    if (job.getInt("result") == 1) {
//						Toast.makeText(context, "提款请求成功，处理中...", Toast.LENGTH_SHORT).show();
                        CustomDialog dialog = new CustomDialog.Builder(context).setTitle("提示").setMessage("提款请求成功，处理中...").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                backTo(ActivityCoin.class.getName());
                            }
                        }).create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                    }
                    if (job.getInt("result") == 0) {
//						Toast.makeText(context, "提款请求失败...", Toast.LENGTH_SHORT).show();
                        CustomDialog dialog = new CustomDialog.Builder(context).setTitle("提示").setMessage("提款请求失败...").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        dialog.show();
                        dialog.setCanceledOnTouchOutside(false);
                    }
                } else {
                    Toast.makeText(context, "服务器请求失败...", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
