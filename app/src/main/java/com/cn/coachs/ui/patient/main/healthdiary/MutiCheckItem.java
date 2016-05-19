package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.coachs.R;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/20 下午3:36:30
 * @parameter
 * @return
 */
public class MutiCheckItem extends LinearLayout implements Checkable {
    private TextView contentTxt;
    private CheckBox selectBtn;

    public MutiCheckItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.activity_muti_checked_item, this, true);
        contentTxt = (TextView) v.findViewById(R.id.content);
        selectBtn = (CheckBox) v.findViewById(R.id.check_box);
    }

    public void setContent(String text) {
        contentTxt.setText(text);
    }

    @Override
    public boolean isChecked() {
        return selectBtn.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        selectBtn.setChecked(checked);
    }

    @Override
    public void toggle() {
        selectBtn.toggle();
    }
}
