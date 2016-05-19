package com.cn.coachs.ui.patient.main.healthdiary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.cn.coachs.R;

@SuppressLint("NewApi")
public class CustomSwitch extends FrameLayout {

    private TextView label;
    private CompoundButton button;

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Initalise and configure view layout
        RelativeLayout layout = new RelativeLayout(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(layoutParams);

        layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                button.toggle();
            }
        });

        layout.setBackgroundResource(R.drawable.bg_item);

        //Initalise and configure compound button
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            button = new Switch(context);
        } else {
            button = new CheckBox(context);
        }
        button.setId(1);
        button.setText("");

        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.topMargin = 16;
        buttonParams.bottomMargin = 16;
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        buttonParams.addRule(RelativeLayout.ALIGN_RIGHT, 2);

        //Initalise and configure button label
        label = new TextView(context);
        label.setId(2);

        RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        labelParams.leftMargin = 8;
        labelParams.addRule(RelativeLayout.ALIGN_BASELINE, 1);

        //Empty view to force bottom margin
        View emptyView = new View(context);
        RelativeLayout.LayoutParams emptyViewParams = new RelativeLayout.LayoutParams(0, 0);
        emptyViewParams.addRule(RelativeLayout.BELOW, button.getId());

        //Add our components to the layout
        layout.addView(label, labelParams);
        layout.addView(button, buttonParams);
        layout.addView(emptyView, emptyViewParams);
        addView(layout);

        //Manage attributes
        int[] attributeSet = {
                android.R.attr.text,
                android.R.attr.checked
        };

        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, attributeSet, 0, 0);

        try {
            label.setText(a.getText(0));
            button.setChecked(a.getBoolean(1, false));
        } finally {
            a.recycle();
        }
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setChecked(boolean isChecked) {
        button.setChecked(isChecked);
    }

    public boolean isChecked() {
        return button.isChecked();
    }
}
