package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.coachs.R;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/10 下午7:03:01
 * @parameter
 * @return
 */
public class EditDiaryDayItem extends LinearLayout {
    public Context context;
    public View view;
    public TextView content, time;
    public View item_bottom_divider;
    private String contentStr, timeStr;

    public EditDiaryDayItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.activity_edit_diary_day_item, this, true);
        time = (TextView) findViewById(R.id.time);
        content = (TextView) findViewById(R.id.content);
        item_bottom_divider = (View) findViewById(R.id.item_bottom_divider);

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.diary_info);
        setContent(localTypedArray.getString(R.styleable.diary_info_item_content));
        setTime(localTypedArray.getString(R.styleable.diary_info_item_time));
        setBottomLineVisiable(localTypedArray.getBoolean(R.styleable.diary_info_line_visiable, true));
        localTypedArray.recycle();
    }

    public interface OnClick {
        public void click(View v);
    }

    public void setContent(String string) {
        // TODO Auto-generated method stub
        contentStr = string;
        if (contentStr == null) {
            content.setText("");
            return;
        }
        content.setText(contentStr);
    }

    public String getContent() {
        return content.getText().toString();
    }

    public void setTime(String string) {
        // TODO Auto-generated method stub
        timeStr = string;
        if (timeStr == null) {
            time.setText("");
            return;
        }
        time.setText(timeStr);
    }

    public String getTime() {
        return time.getText().toString();
    }

    public void setBottomLineVisiable(boolean visiable) {
        if (visiable)
            item_bottom_divider.setVisibility(View.VISIBLE);
        else
            item_bottom_divider.setVisibility(View.INVISIBLE);
    }
}
