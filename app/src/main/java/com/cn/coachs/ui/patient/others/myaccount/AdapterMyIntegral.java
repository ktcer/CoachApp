package com.cn.coachs.ui.patient.others.myaccount;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.model.personinfo.BeanOverage;
import com.cn.coachs.my.ActivityMyAccountCenter;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.chat.common.dialog.ECListDialog;
import com.cn.coachs.ui.paychoose.ActivityPayChoose;
import com.cn.coachs.util.FButton;

public class AdapterMyIntegral extends BaseAdapter {
    private List<BeanOverage> list;
    private Context mContext;
    private LayoutInflater inflater;
    /**
     * 第一个后面positiong
     */
    private final int TYPE_1 = 0; // 类型1
    /**
     * 第一个positiong
     */
    private final int TYPE_2 = 1;// 类型第一个positiong
    private final int VIEW_TYPE = 2; // 总布局数
    private float[] money1 = {(float) 0.01, (float) 0.1};
    private String[] money = {"10个金币", "100个金币"};
    public static int gold = 0;

    public AdapterMyIntegral() {
        super();
        // TODO Auto-generated constructor stub
    }

    public AdapterMyIntegral(List<BeanOverage> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
        // beanOverageHead = new BeanOverageHead();
        // list = new ArrayList<BeanOverage>();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        // return super.getItemViewType(position);
        if (position == 0) {
            return TYPE_2;
        } else {
            return TYPE_1;
        }
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        // return super.getViewTypeCount();
        return VIEW_TYPE;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        inflater = LayoutInflater.from(mContext);

        viewHolderHead1 head = null;
        viewHolder1 holderList = null;
        int type = getItemViewType(position);

        if (convertView == null) {
            Log.e("convertView = ", " NULL");

            switch (type) {
                case TYPE_2:

                    head = new viewHolderHead1();
                    convertView = inflater.inflate(R.layout.my_integral_head,
                            parent, false);
                    //
                    head.takeMoney = (FButton) convertView
                            .findViewById(R.id.take_money);
                    head.fillMoney = (FButton) convertView
                            .findViewById(R.id.fill_money);
                    head.currentMoneyNum = (TextView) convertView
                            .findViewById(R.id.current_money_num);
                    convertView.setTag(head);
                    break;
                case TYPE_1:
                    convertView = inflater.inflate(R.layout.my_integral, parent,
                            false);

                    holderList = new viewHolder1();
                    holderList.consumptionName = (TextView) convertView
                            .findViewById(R.id.consumption_name);
                    holderList.overages = (TextView) convertView
                            .findViewById(R.id.overage);
                    holderList.detailedMoney = (TextView) convertView
                            .findViewById(R.id.detailed_money);
                    Log.e("convertView = ", "NULL TYPE_SEEKBAR");
                    convertView.setTag(holderList);
                    break;
            }
        } else {
            // 有convertView，按样式，取得不用的布局
            switch (type) {
                case TYPE_2:
                    head = (viewHolderHead1) convertView.getTag();
                    head.currentMoneyNum.setText((int) ActivityMyAccountCenter.myCoins
                            + "个");
                    head.takeMoney.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // Intent intent =new
                            // Intent(mContext,ActivityHealthPostDetail.class);
                            // Bundle mBundle = new Bundle();
                            // mBundle.putSerializable(Constant.TOUR_BEAN,mList.get(i));
                            // intent.putExtras(mBundle);
                            // mContext.startActivity(intent);
                        }

                    });
                    head.fillMoney.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            AppMain.kindsPay = 1;
                            ECListDialog dialog = new ECListDialog(mContext, money);
                            dialog.setOnDialogItemClickListener(new ECListDialog.OnDialogItemClickListener() {
                                @Override
                                public void onDialogItemClick(Dialog d, int position) {
                                    // handleContentMenuClick(itemPosition
                                    // ,position);
                                    // selectServeType = position;
                                    // AscyncSetExpert acsy = new AscyncSetExpert();
                                    // acsy.execute();
                                    if (position == 0) {
                                        gold = (int) money1[position] + 10;
                                    } else {
                                        gold = (int) money1[position] + 100;
                                    }

                                    Intent intent = new Intent(mContext, ActivityPayChoose.class);
                                    intent.putExtra("money", money1[position]);
                                    intent.putExtra("description", "充值" + money[position]);
                                    mContext.startActivity(intent);
                                }
                            });
                            dialog.setTitle("请您选择要充值的金币");
                            dialog.show();
                        }
                    });
                    break;
                case TYPE_1:
                    holderList = (viewHolder1) convertView.getTag();
                    BeanOverage beanOverage = list.get(position);
                    holderList.consumptionName.setText(beanOverage
                            .getConsumptionName());
                    holderList.overages.setText(beanOverage.getOverages());
                    float money = Float.parseFloat(beanOverage.getDetailedMoney());
                    if (money > 0) {
                        holderList.detailedMoney.setTextColor(mContext
                                .getResources().getColor(R.color.lightgreen));
                    } else {
                        holderList.detailedMoney.setTextColor(mContext
                                .getResources().getColor(R.color.orange));
                    }
                    holderList.detailedMoney
                            .setText(beanOverage.getDetailedMoney());
                    Log.e("convertView !!!!!!= ", "NULL TYPE_SEEKBAR");
                    break;
            }
        }
        return convertView;
    }
}

class viewHolderHead1 {
    FButton takeMoney;
    FButton fillMoney;
    TextView currentMoneyNum;
}

class viewHolder1 {
    TextView consumptionName;
    TextView consumptionTime;
    TextView overages;
    TextView detailedMoney;
}
