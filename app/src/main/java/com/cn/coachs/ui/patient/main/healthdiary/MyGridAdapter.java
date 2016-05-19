package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.util.FButton;

import java.util.ArrayList;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/13 上午9:38:42
 * @parameter
 * @return
 */
public class MyGridAdapter extends BaseAdapter {
    public ArrayList<String> listDate;
    public ArrayList<String> listDateRelative;// 第几天
    public Context context;
    public int interval;
    private OnMyClick myClick;

    public MyGridAdapter(Context context, ArrayList<String> listDate,
                         ArrayList<String> listDateRelative, int interval) {
        super();
        this.listDate = listDate;
        this.listDateRelative = listDateRelative;
        this.context = context;
        this.interval = interval;
    }

    public void setOnMyClick(OnMyClick myClick) {
        this.myClick = myClick;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listDate.size() / 7;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return listDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_fbutton_text, null);
            holder.week = (TextView) convertView.findViewById(R.id.week);

            holder.one_text = (TextView) convertView.findViewById(R.id.one_text);
            holder.two_text = (TextView) convertView.findViewById(R.id.two_text);
            holder.three_text = (TextView) convertView.findViewById(R.id.three_text);
            holder.four_text = (TextView) convertView.findViewById(R.id.four_text);
            holder.five_text = (TextView) convertView.findViewById(R.id.five_text);
            holder.six_text = (TextView) convertView.findViewById(R.id.six_text);
            holder.seven_text = (TextView) convertView.findViewById(R.id.seven_text);

            holder.one_btn = (FButton) convertView.findViewById(R.id.one_btn);
            holder.two_btn = (FButton) convertView.findViewById(R.id.two_btn);
            holder.three_btn = (FButton) convertView.findViewById(R.id.three_btn);
            holder.four_btn = (FButton) convertView.findViewById(R.id.four_btn);
            holder.five_btn = (FButton) convertView.findViewById(R.id.five_btn);
            holder.six_btn = (FButton) convertView.findViewById(R.id.six_btn);
            holder.seven_btn = (FButton) convertView.findViewById(R.id.seven_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.week.setText("第" + (position + 1) + "周");

        holder.one_text.setText(listDate.get(position * 7));
        holder.two_text.setText(listDate.get(position * 7 + 1));
        holder.three_text.setText(listDate.get(position * 7 + 2));
        holder.four_text.setText(listDate.get(position * 7 + 3));
        holder.five_text.setText(listDate.get(position * 7 + 4));
        holder.six_text.setText(listDate.get(position * 7 + 5));
        holder.seven_text.setText(listDate.get(position * 7 + 6));

        holder.one_btn.setText("第\n" + listDateRelative.get(position * 7) + "\n天");
        holder.two_btn.setText("第\n" + listDateRelative.get(position * 7 + 1) + "\n天");
        holder.three_btn.setText("第\n" + listDateRelative.get(position * 7 + 2) + "\n天");
        holder.four_btn.setText("第\n" + listDateRelative.get(position * 7 + 3) + "\n天");
        holder.five_btn.setText("第\n" + listDateRelative.get(position * 7 + 4) + "\n天");
        holder.six_btn.setText("第\n" + listDateRelative.get(position * 7 + 5) + "\n天");
        holder.seven_btn.setText("第\n" + listDateRelative.get(position * 7 + 6) + "\n天");

        if (!setUnClickable(holder.one_btn, interval)) {
            holder.one_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (myClick != null) {
                        myClick.onMyClick(v, position * 7);
                        System.out.println("=-=-=position=-=-=" + (position * 7));
                    }
                }
            });
        }

        if (!setUnClickable(holder.two_btn, interval)) {
            holder.two_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (myClick != null) {
                        myClick.onMyClick(v, position * 7 + 1);
                        System.out.println("=-=-=position=-=-=" + (position * 7 + 1));
                    }
                }
            });
        }

        if (!setUnClickable(holder.three_btn, interval)) {
            holder.three_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (myClick != null) {
                        myClick.onMyClick(v, position * 7 + 2);
                        System.out.println("=-=-=position=-=-=" + (position * 7 + 2));
                    }
                }
            });
        }

        if (!setUnClickable(holder.four_btn, interval)) {
            holder.four_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (myClick != null) {
                        myClick.onMyClick(v, position * 7 + 3);
                        System.out.println("=-=-=position=-=-=" + (position * 7 + 3));
                    }
                }
            });
        }

        if (!setUnClickable(holder.five_btn, interval)) {
            holder.five_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (myClick != null) {
                        myClick.onMyClick(v, position * 7 + 4);
                        System.out.println("=-=-=position=-=-=" + (position * 7 + 4));
                    }
                }
            });
        }

        if (!setUnClickable(holder.six_btn, interval)) {
            holder.six_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (myClick != null) {
                        myClick.onMyClick(v, position * 7 + 5);
                        System.out.println("=-=-=position=-=-=" + (position * 7 + 5));
                    }
                }
            });
        }

        if (!setUnClickable(holder.seven_btn, interval)) {
            holder.seven_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (myClick != null) {
                        myClick.onMyClick(v, position * 7 + 6);
                        System.out.println("=-=-=position=-=-=" + (position * 7 + 6));
                    }
                }
            });
        }
        return convertView;
    }

    public boolean setUnClickable(FButton btn, int interval) {
        Integer position = Integer.parseInt(btn.getText().toString().replace("第", "").replace("天", "").replaceAll("\n", ""));
        if (position <= interval) {
            Log.e("=-=-=position setUnClickable=-=-= 位置", "" + position);
//			btn.setBackgroundColor(context.getResources().getColor(R.color.color_gray));			
            btn.setButtonColor(context.getResources().getColor(R.color.color_gray));
            btn.setEnabled(false);
            return true;
        }
        return false;
    }

    class ViewHolder {
        public TextView one_text, two_text, three_text, four_text, five_text, six_text, seven_text, week;
        public FButton one_btn, two_btn, three_btn, four_btn, five_btn, six_btn, seven_btn;
    }

    interface OnMyClick {
        public void onMyClick(View v, int position);
    }
}
