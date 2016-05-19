package com.cn.coachs.ui.patient.others.myaccount.choosepicture;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cn.coachs.R;
import com.cn.coachs.model.myaccount.BeanPicture;


public class pictureAdapter extends BaseAdapter {
    private List<BeanPicture> mList;
    private Context mContext;

    public pictureAdapter(Context pContext, List<BeanPicture> pList) {
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
        BeanPicture picture = mList.get(position);
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.pictureitem, null);
        if (convertView != null) {
            holder = new ViewHolder();
            holder.name = (ImageView) convertView.findViewById(R.id.uploadPicture);
        }
        holder.name.setImageBitmap(picture.getBitmap());
        return convertView;
    }

    class ViewHolder {
        ImageView name;
    }
}

