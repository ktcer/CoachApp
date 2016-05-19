package com.cn.coachs.ui.patient.others.myaccount.performance;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.coachs.R;

public class staticsticAdapter extends BaseAdapter {
    private List<Statistic> list;
    private Context mContext;

    public staticsticAdapter(Context p, List<Statistic> l) {
        mContext = p;
        list = l;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        Statistic statistic = list.get(position);
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.hosptial, null);
        if (convertView != null) {
            holder = new ViewHolder();
            holder.item = (TextView) convertView.findViewById(R.id.hosptial);
            holder.number = (TextView) convertView.findViewById(R.id.hosptial2);
            holder.number.setVisibility(View.VISIBLE);

        }
        holder.item.setText(statistic.getItem());
        holder.number.setText(statistic.getNumberOfItem());
        return convertView;

    }

    class ViewHolder {
        TextView item;
        TextView number;
    }

}
