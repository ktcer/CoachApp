package com.cn.coachs.ui.patient.others.mytest;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.basic.ActivityBasic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的测评的具体信息
 *
 * @author kuangtiecheng
 */
public class ActivityMyTestInfo extends ActivityBasic {
    private ListView listView;
    private String[] date = new String[]{"2015-4-12", "2015-4-13", "2015-4-14"};
    private String[] score = new String[]{"测评得分：85", "测评得分：85", "测评得分：85"};
    private String[] result = new String[]{"测评结论：不容易跌倒。", "测评结论：不容易跌倒。", "测评结论：不容易跌倒。"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytestinfo);
        initial();
        List<Map<String, Object>> Items = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < date.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("date", date[i]);
            item.put("score", score[i]);
            item.put("result", result[i]);
            Items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, Items,
                R.layout.listitem_mytestinfo, new String[]{"date", "score", "result"},
                new int[]{R.id.date_myTestInfo, R.id.score_myTestInfo, R.id.result_myTestInfo});
        listView.setAdapter(adapter);
    }

    private void initial() {
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("健康测评详情");
        listView = (ListView) this.findViewById(R.id.lv_mytestinfo);
    }
}

