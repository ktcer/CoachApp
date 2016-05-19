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
public class DiaryItem extends LinearLayout {
    public Context context;
    public View view;
    public TextView content, time, num_fans;
    private String contentStr, timeStr, num;
    public LinearLayout linearlayout;
//	private OnClick listener;

    public DiaryItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.listitem_historydiary_left, this, true);
        content = (TextView) findViewById(R.id.health_tips);
        time = (TextView) findViewById(R.id.health_time);
        num_fans = (TextView) findViewById(R.id.num_fans);

        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);

        TypedArray localTypedArray = context.obtainStyledAttributes(attrs, R.styleable.diary_info);
        setTime(localTypedArray.getString(R.styleable.diary_info_item_content));
        setContent(localTypedArray.getString(R.styleable.diary_info_item_time));
        localTypedArray.recycle();
    }

    public int getLayout() {
        return 0;
    }

    public void setTime(String string) {
        // TODO Auto-generated method stub
        contentStr = string;
        if (contentStr == null) {
            content.setText("");
            return;
        }
        content.setText(contentStr);
    }

    public void setContent(String string) {
        // TODO Auto-generated method stub
        timeStr = string;
        if (timeStr == null) {
            time.setText("");
            return;
        }
        time.setText(timeStr);
    }

    public void setNum(String string) {
        // TODO Auto-generated method stub
        num = string;
        if (num == null) {
            num_fans.setText("");
            return;
        }
        num_fans.setText(num);
    }

    public void setLinearClickable(boolean flag) {
        this.setClickable(flag);
        if (flag) {
            content.setTextColor(context.getResources().getColor(android.R.color.background_dark));
        } else {
            content.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }
    }

}
