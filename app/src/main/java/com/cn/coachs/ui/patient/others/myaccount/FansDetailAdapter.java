package com.cn.coachs.ui.patient.others.myaccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.model.myaccount.BeanFans;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.CircleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author kuangtiecheng
 *         我的粉丝
 */
public class FansDetailAdapter extends BaseAdapter {
    public Context mContext;
    public ArrayList<BeanFans> list;
    private OnClickHeadImgListener mOnClickHeadImgListener;

    public FansDetailAdapter(Context mContext, ArrayList<BeanFans> list) {
        super();
        this.mContext = mContext;
        this.list = list;
    }

    public void setOnClickHeadImgListener(
            OnClickHeadImgListener mOnClickHeadImgListener) {
        this.mOnClickHeadImgListener = mOnClickHeadImgListener;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.myaccount_fans_item, parent, false);
            holder.fans_name = (EmojiconTextView) convertView.findViewById(R.id.fans_name);
            holder.fans_sex = (TextView) convertView.findViewById(R.id.fans_sex);
            holder.fans_age = (TextView) convertView.findViewById(R.id.fans_age);
            holder.fans_head_image = (CircleImageView) convertView.findViewById(R.id.fans_head_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.fans_name.setText(list.get(position).getName());
        holder.fans_sex.setText("性别： " + list.get(position).getSex());
        holder.fans_age.setText("年龄： " + list.get(position).getAge());
        ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + list.get(position).getImgUrl(), holder.fans_head_image, AppMain.initImageOptions(R.drawable.default_user_icon, false));
        holder.fans_head_image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mOnClickHeadImgListener != null)
                    mOnClickHeadImgListener.onClickHeadImg(position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        EmojiconTextView fans_name;
        TextView fans_sex;
        TextView fans_age;
        CircleImageView fans_head_image;
    }


    public interface OnClickHeadImgListener {
        public void onClickHeadImg(int position);
    }
}
