/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.coachs.R;

/**
 * @author kuangtiecheng
 */
public class IncomeDetailItem extends RelativeLayout {
    private TextView left_top, left_bottom, right_top, right_bottom;
    private LinearLayout item;

    public IncomeDetailItem(Context context) {
        super(context);
    }

    public IncomeDetailItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        LayoutInflater.from(context).inflate(R.layout.myaccount_income_detail_item, this, true);

        left_top = (TextView) findViewById(R.id.left_top);
        left_bottom = (TextView) findViewById(R.id.left_bottom);
        right_top = (TextView) findViewById(R.id.right_top);
        right_bottom = (TextView) findViewById(R.id.right_bottom);
        item = (LinearLayout) findViewById(R.id.item);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.income_item);
        setLeftTop(mTypedArray.getString(R.styleable.income_item_left_top));
        setLeftBottom(mTypedArray.getString(R.styleable.income_item_left_bottom));
        setRightTop(mTypedArray.getString(R.styleable.income_item_right_top));
        setRightBottom(mTypedArray.getString(R.styleable.income_item_right_bottom));
        setBackColor(mTypedArray.getColor(R.styleable.income_item_background,
                this.getResources().getColor(R.color.white)));

        mTypedArray.recycle();
    }

    public void setBackColor(int color) {
        if (!isInEditMode())
            item.setBackgroundColor(color);

    }

    public void setLeftTop(String string) {
        // TODO Auto-generated method stub
        if (!isInEditMode()) {
            if (string == null) {
                left_top.setText("");
                return;
            }
            left_top.setText(string);
        }
    }

    public void setLeftBottom(String string) {
        // TODO Auto-generated method stub
        if (!isInEditMode()) {
            if (string == null) {
                left_bottom.setText("");
                return;
            }
            left_bottom.setText(string);
        }
    }

    public void setRightTop(String string) {
        // TODO Auto-generated method stub
        if (!isInEditMode()) {
            if (string == null) {
                right_top.setText("");
                return;
            }
            right_top.setText(string);
        }
    }

    public void setRightBottom(String string) {
        // TODO Auto-generated method stub
        if (!isInEditMode()) {
            if (string == null) {
                right_bottom.setText("");
                return;
            }
            right_bottom.setText(string);
        }
    }

}
