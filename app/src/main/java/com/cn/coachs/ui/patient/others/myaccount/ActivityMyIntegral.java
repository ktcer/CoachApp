package com.cn.coachs.ui.patient.others.myaccount;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.model.personinfo.BeanOverage;
import com.cn.coachs.ui.basic.ActivityBasicListView;
import com.cn.coachs.util.refreshlistview.XListView;

public class ActivityMyIntegral extends ActivityBasicListView {
    private List<BeanOverage> infoList, tempInfoList;
    private AdapterMyIntegral adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_integral);
        initView();
    }

    private void initView() {
        ((TextView) this.findViewById(R.id.middle_tv)).setText("我的金币");
        listView = (XListView) findViewById(R.id.lv_overage);
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
//				String url = infoList.get(position-1).getVedioUrl();
//				 Intent intent =new Intent(getActivity(),VideoPlay.class);
//				 Bundle mBundle = new Bundle();
//				 mBundle.putString(Constant.VEDIO_URL, url);
//				 intent.putExtras(mBundle);
//				 startActivity(intent);
            }
        });
        infoList = new ArrayList<BeanOverage>();
        tempInfoList = new ArrayList<BeanOverage>();
        BeanOverage bo = new BeanOverage();
        bo.setConsumptionName("第三方支付消费");
        bo.setConsumptionTime("2015-10-12");
        bo.setDetailedMoney("+16");
        bo.setOverages("余额:2.00元");
        infoList.add(bo);
        BeanOverage bo1 = new BeanOverage();
        bo1.setConsumptionName("成功下单");
        bo1.setConsumptionTime("2015-10-13");
        bo1.setDetailedMoney("+160");
        bo1.setOverages("2015-9-10");
        infoList.add(bo1);


        adapter = new AdapterMyIntegral(infoList, this);
        listView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AsyncGetMyCoins mTask = new AsyncGetMyCoins(this) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                adapter.notifyDataSetChanged();
            }

        };
        mTask.execute();
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        super.onLoadMore();
    }


}
