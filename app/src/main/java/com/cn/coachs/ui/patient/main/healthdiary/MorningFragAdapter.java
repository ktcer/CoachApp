//package com.cn.aihuexpert.ui.patient.main.healthdiary;
//
//import java.util.ArrayList;
//
//import com.cn.coachs.R;
//import com.cn.coachs.ui.patient.main.healthdiary.MyGridAdapter.OnMyClick;
//import com.cn.coachs.ui.patient.main.healthdiary.MyGridAdapter.ViewHolder;
//import com.cn.coachs.util.FButton;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
///**
// * @author  kuangtiecheng
// * @date 创建时间：2015/10/13 下午5:23:44
// * @version 1.0 
// * @parameter  
// * @since  
// * @return  
// *
// */
//public class MorningFragAdapter extends BaseAdapter{
//	public OnMyClick myClick;
//	public Context context;
//	public ArrayList<String> list;
//		
//	public MorningFragAdapter(Context context, ArrayList<String> list) {
//		super();
//		this.context = context;
//		this.list = list;
//	}
//
//	public void setMyClick(OnMyClick myClick) {
//		this.myClick = myClick;
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return 13;
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		ViewHolder holder = null;
//		if (convertView == null) {
//			holder = new ViewHolder();
//			convertView = LayoutInflater.from(context).inflate(R.layout.activity_fbutton_text,null);
//			
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		holder.week.setText("第"+position+"周");
//				
//		return null;
//	}
//	
//	private class ViewHolder {
//		TextView vidoName;
//		TextView vidoDescription;
//	}
//}
