package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.model.nurse.BeanEditDiaryNode;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.ui.contact.ContactDetailActivity;
import com.cn.coachs.ui.patient.main.healthdiary.MyGridAdapter.OnMyClick;
import com.cn.coachs.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/12 下午11:34:22
 * @parameter
 * @return
 */
public class ActivityEditDiaryDay extends ActivityBasic implements OnMyClick {
    public ListView list_days;
    public MyGridAdapter adapter;
    public ArrayList<String> listDate, listDateRelative;
    public int length;// 长度,单位是"天"
    public String planLength;// 长度,单位是"周"
    public String startDate;// 起始日期
    public String fansId;// 类型混乱
    public AsynQueryPlan asynQueryPlan;
    public ArrayList<BeanEditDiaryNode> list;
    public int turnGrey = 0;// 第n天控件变灰的个数

    public static final String TAG = ActivityEditDiaryDay.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public void finishEvent() {
        // TODO Auto-generated method stub
        super.finishEvent();
        String old = ContactDetailActivity.class.getName();
        backTo(old);
    }

    // @Override
    // protected void onDestroy() {
    // // TODO Auto-generated method stub
    // super.onDestroy();
    // String old = ContactDetailActivity.class.getName();
    // backTo(old);
    // }

    public void init() {
        setContentView(R.layout.activity_edit_diary_day_tab);

        startDate = getIntent().getStringExtra(Constant.START_DATE);
        planLength = getIntent().getStringExtra(Constant.PLAN_LENGTH);
        if (planLength != null) {
            length = Integer.parseInt(planLength) * 7 + 1;
        }

        fansId = getIntent().getStringExtra(Constant.PATIENT_ID);
        TextView middle_tv = (TextView) findViewById(R.id.middle_tv);
//		TextView left = (TextView) findViewById(R.id.left_tv);
//		left.setOnClickListener(this);
        middle_tv.setText("粉丝日记");
        listDateRelative = CalendarUtils.getListDateRelative(length);
        try {
            listDate = CalendarUtils.getListDate(startDate, length);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            turnGrey = CalendarUtils.getDateSpace(startDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        list_days = (ListView) findViewById(R.id.listview);
        adapter = new MyGridAdapter(this, listDate, listDateRelative, turnGrey);
        adapter.setOnMyClick(this);
        list_days.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String old = ContactDetailActivity.class.getName();
            backTo(old);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onMyClick(View v, int position) {
        // TODO Auto-generated method stub
        showProgressBar();
        requestData(position);
    }

    // dian ji 第position天,统一放在线程类中加
    public synchronized void requestData(final int num) {
        AsynGetDiaryItem asynGetDiaryItem = new AsynGetDiaryItem(fansId, num) {
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONArray result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                hideProgressBar();
                list = parseJSONArray(result, num);
                Intent intent = new Intent(ActivityEditDiaryDay.this,
                        ActivityEditDiaryDayDetail.class);
                intent.putParcelableArrayListExtra(
                        Constant.LIST_DIARY_NODE_DATA, list);
                startActivity(intent);
            }
        };
        asynGetDiaryItem.execute();
    }

    public ArrayList<BeanEditDiaryNode> parseJSONArray(JSONArray result,
                                                       int position) {
        ArrayList<BeanEditDiaryNode> list = new ArrayList<BeanEditDiaryNode>();
        if (result.length() > 0) {
            for (int i = 0; i < result.length(); i++) {
                BeanEditDiaryNode beanEditDiaryNode = new BeanEditDiaryNode();
                try {
                    beanEditDiaryNode.setId(result.getJSONObject(i).getLong(
                            "id"));
                    beanEditDiaryNode.setContent(result.getJSONObject(i)
                            .getString("content"));
                    beanEditDiaryNode.setTime(result.getJSONObject(i)
                            .getString("time"));
                    beanEditDiaryNode.setDay(result.getJSONObject(i).getInt(
                            "day"));
                    beanEditDiaryNode.setWeek(position / 7 + 1);
                    beanEditDiaryNode.setFansId(fansId);
                    list.add(beanEditDiaryNode);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            BeanEditDiaryNode beanEditDiaryNode = new BeanEditDiaryNode();
            beanEditDiaryNode.setFansId(fansId);
            beanEditDiaryNode.setWeek(position / 7 + 1);
            beanEditDiaryNode.setDay(position);
            beanEditDiaryNode.setId((long) -1);
            list.add(beanEditDiaryNode);
        }
        return list;
    }
}
