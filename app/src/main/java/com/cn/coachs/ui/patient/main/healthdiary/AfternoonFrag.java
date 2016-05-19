package com.cn.coachs.ui.patient.main.healthdiary;

import android.view.View;

import com.cn.coachs.R;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/13 下午5:13:01
 * @parameter
 * @return
 */
public class AfternoonFrag extends NightFrag {
    public EditDiaryDayItem thiteen;

    @Override
    public void init() {
        super.init();

        twelve = (EditDiaryDayItem) view.findViewById(R.id.twelve);
        twelve.setOnClickListener(this);
        listTextView.add(twelve);

        initData(list, listTextView);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        super.onClick(arg0);
        if (arg0.getId() == R.id.twelve)
            showChooseDialog(twelve);
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_edit_diary_day_total_pm;
    }
}
