package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/20 下午3:29:29
 * @parameter
 * @return
 */
public class MyMutiCheckedAdapter extends BaseAdapter {
    public ArrayList<String> list;
    public Context mContext;

    public MyMutiCheckedAdapter(ArrayList<String> list, Context mContext) {
        super();
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        MutiCheckItem mutiCheckItem = null;
        if (convertView == null)
            mutiCheckItem = new MutiCheckItem(mContext, null);
        else
            mutiCheckItem = (MutiCheckItem) convertView;
        mutiCheckItem.setContent(list.get(position));
        return mutiCheckItem;
    }

}
