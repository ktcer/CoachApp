package com.cn.coachs.util.horizontallistview.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.util.AbsParam;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HorizontalPicAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mList;
    private DisplayImageOptions options;
//	private ImageLoader imageLoader;

    public HorizontalPicAdapter(Context context, List<String> list) {
        this.mContext = context;
        this.mList = list;
        options = AppMain.initImageOptions(R.drawable.gerenfengcai, true);
    }

    @Override
    public int getCount() {

        return mList.size() == 6 ? 6 : mList.size() + 1;
    }

    @Override
    public String getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holderView = null;
        View view = convertView;

        if (view == null) {
            holderView = new HolderView();
            view = LayoutInflater.from(mContext).inflate(R.layout.match_league_round_item, parent, false);
            holderView.imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }

        if (mList.size() < 6) {
            if (position == mList.size()) {
                holderView.imageView.setImageResource(R.drawable.bt_add_pic);
            } else {
                ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + mList.get(position), holderView.imageView, options);
            }
        } else if (mList.size() == 6) {
            ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + mList.get(position), holderView.imageView, options);
        }
        return view;

    }

    private class HolderView {
        ImageView imageView;
    }

}
