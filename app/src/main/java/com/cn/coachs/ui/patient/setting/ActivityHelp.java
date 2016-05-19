package com.cn.coachs.ui.patient.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.basic.ActivityBasic;

public class ActivityHelp extends ActivityBasic {
    private ListView lv;
    private TextView tv;
    private String[] array_question = new String[]{"问：爱·护（秘书）有什么功能？", "问：如何为粉丝制定健康方案？",};
    private String[] array_answer = new String[]{"答：爱护（秘书）是一款能够帮助专家处理粉丝相关事务的软件，极大程度方便专家为粉丝制定健康方案并查看相应粉丝的相关数据。", "答：在粉丝界面的粉丝列表中点击相关粉丝记录进入粉丝详情界面，点击编辑方案即可为粉丝制定、编辑方案。"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_setting);
        initial();
        load();
    }

    private void initial() {
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("帮助中心");
        lv = (ListView) findViewById(R.id.lv_help);

    }

    private void load() {
        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < array_question.length; i++) {
            Map<String, Object> listItem = new HashMap<String, Object>();
            listItem.put("question", array_question[i]);
            listItem.put("answer", array_answer[i]);
            listItems.add(listItem);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.listitem_help, new String[]{"question", "answer"},
                new int[]{R.id.tv1_help, R.id.tv2_help});
        lv.setAdapter(simpleAdapter);
    }
}
