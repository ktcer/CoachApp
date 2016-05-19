/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;

/**
 * @author kuangtiecheng
 *         提款、已提款项、可提款项
 */
public class SettlementFrag extends BaseFrag {
    private TextView present_month_income, draw_money, already_draw_money;
    public static float monthIncome;
    public static float monthCash;

    public void init() {
        present_month_income = (TextView) view.findViewById(R.id.present_month_income);
        already_draw_money = (TextView) view.findViewById(R.id.already_draw_money);

        draw_money = (TextView) view.findViewById(R.id.draw_money);
        draw_money.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        AsyncGetMonthInfo gmTask = new AsyncGetMonthInfo(getActivity()) {
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                present_month_income.setText(monthIncome + "");
                already_draw_money.setText("已经提款" + monthCash + "元");
            }
        };
        gmTask.execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.draw_money)
            if (Purse.balance == 0.0) {
                Toast.makeText(getActivity(), "余额不足", Toast.LENGTH_SHORT).show();
            } else {
                startIntent(ActivityDrawMoney.class);
            }
//			Toast.makeText(getActivity(), "开发中...", Toast.LENGTH_SHORT).show();
    }

    public void initData() {

    }

    @Override
    public int getLayout() {
        // TODO Auto-generated method stub
        return R.layout.myaccount_settlement_frag;
    }

}
