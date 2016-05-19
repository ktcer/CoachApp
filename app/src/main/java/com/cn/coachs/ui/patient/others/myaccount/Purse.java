package com.cn.coachs.ui.patient.others.myaccount;

import android.os.Bundle;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.basic.ActivityBasic;

public class Purse extends ActivityBasic {
    public static float balance;
    private TextView rest_draw_money;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.myaccount_my_purse);
        rest_draw_money = (TextView) findViewById(R.id.rest_draw_money);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("我的钱包");
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AsyncGetMoney gmTask = new AsyncGetMoney(this) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                rest_draw_money.setText("还可以提款" + balance + "元");
            }
        };
        gmTask.execute();
    }

}
