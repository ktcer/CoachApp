package com.cn.coachs.ui.patient.others.myaccount;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.model.nurse.DepartmentInfo;

public class DepartmentAdapter extends BaseAdapter {
    private List<DepartmentInfo> mList;
    private Context mContext;

    public DepartmentAdapter(Context pContext, List<DepartmentInfo> pList) {
        // TODO Auto-generated constructor stub
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
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
        DepartmentInfo department = mList.get(position);
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.hosptial, null);
        if (convertView != null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.hosptial);
        }
        holder.name.setText(department.getDepartmentName());
        return convertView;
    }

    class ViewHolder {
        TextView name;
    }
}
