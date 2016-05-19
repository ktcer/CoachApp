/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.myaccount.BeanIncomeDetail;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.util.refreshlistview.XListView;
import com.cn.coachs.util.refreshlistview.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author kuangtiecheng
 */
public class IncomeDetail extends Activity implements IXListViewListener,
        OnItemClickListener {
    private XListView list_income;
    private IncomeDetailAdapter mAdapter;
    private ArrayList<BeanIncomeDetail> list = new ArrayList<BeanIncomeDetail>();
    private ImageView tx_Back;
    private int pageNum;
    private int pageSize;
    private AsyncTask asyncTask;

    // 设置页面返回事件
    private void setBackBtnEvent() {
        tx_Back = (ImageView) findViewById(R.id.left_tv);
        if (tx_Back != null) {
            tx_Back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    v.startAnimation(AnimationUtils.loadAnimation(
                            IncomeDetail.this, R.anim.icon_scale));
                    finish();
                }
            });
        }
    }


    public void init() {
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("收支明细");
        list_income = (XListView) findViewById(R.id.list_income);
        list_income.setPullRefreshEnable(false);
        list_income.setXListViewListener(this);

        pageNum = 1;
        pageSize = 10;

        initData();
        // mAdapter = new IncomeDetailAdapter(IncomeDetail.this, list);
        // list_income.setAdapter(mAdapter);
    }

    protected long getUserId(int role) {
        UtilsSharedData.initDataShare(this);
        long userId = UtilsSharedData.getLong(Constant.USER_ID, role);
        return userId;
    }

    public String setUrl(String path) {
        return AbsParam.getBaseUrl() + path;
        // this.url = "http://192.168.202.108:8080/serviceplatform"+path;
    }

    public class IncomeDetailGet extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("userID", "" + getUserId(1));
            map.put("userType", "" + 1);
            map.put("pageNum", "" + pageNum);
            map.put("pageSize", "" + pageSize);
            String result = null;
            try {
                result = NetTool.sendPostRequest(
                        setUrl("/mywallet/moneyhistory"), map, "utf-8");
                if (result != null) {
                    pageNum++;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("result =" + result);
            Type type = new TypeToken<ArrayList<BeanIncomeDetail>>() {
            }.getType();
            Gson gson = new Gson();
            ArrayList<BeanIncomeDetail> tempList = new ArrayList<BeanIncomeDetail>();
            if (result != null && result != "") {
                tempList = gson.fromJson(result, type);
                for (int i = 0; i < tempList.size(); i++) {
                    list.add(tempList.get(i));
                }
                if (tempList.size() < 10) {
                    list_income.setPullLoadEnable(false);
                } else {
                    list_income.setPullLoadEnable(true);
                }
                if (pageNum == 2) {
                    mAdapter = new IncomeDetailAdapter(IncomeDetail.this, list);
                    list_income.setAdapter(mAdapter);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                list_income.stopLoadMore();
            } else {
                Toast.makeText(IncomeDetail.this, R.string.errormsg_server, Toast.LENGTH_SHORT).show();
            }

        }


    }

    public void initData() {
        asyncTask = new IncomeDetailGet().execute();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_detail);
        init();
        setBackBtnEvent();
    }


//	protected long getUserId(int role) {
//		UtilsSharedData.initDataShare(this);
//		long userId = UtilsSharedData.getLong(Constant.USER_ID, role);
//		return userId;
//	}
//
//	public String setUrl(String path) {
//		return AbsParam.getBaseUrl() + path;
//		// this.url = "http://192.168.202.108:8080/serviceplatform"+path;
//	}

//	public class IncomeDetailGet extends AsyncTask<Integer, Void, String>{
//
//		@Override
//		protected String doInBackground(Integer... params) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put("userID", "" + getUserId(1));
//			map.put("userType", "" + 1);
//			map.put("pageNum", "" + pageNum);
//			map.put("pageSize", "" + pageSize);
//			String result = null;
//			try {
//				result = NetTool.sendPostRequest(
//						setUrl("/mywallet/moneyhistory"), map, "utf-8");
//				if(result != null){
//					pageNum ++;
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return result;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			System.out.println("result =" + result);
//			Type type = new TypeToken<ArrayList<BeanIncomeDetail>>() {
//			}.getType();
//			Gson gson = new Gson();
//			ArrayList<BeanIncomeDetail> tempList = new ArrayList<BeanIncomeDetail>();
//			tempList = gson.fromJson(result, type);
//			for (int i = 0; i < tempList.size(); i++) {
//				list.add(tempList.get(i));
//			}
//			System.out.println("list.time=" + list.get(0).getCreatetime());
//			mAdapter = new IncomeDetailAdapter(IncomeDetail.this, list);
//			list_income.setAdapter(mAdapter);
//		}
//		
//		
//	}


    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoadMore() {
        asyncTask = new IncomeDetailGet().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
        System.out.println("=-=-=position=-=-=" + position);

    }

    public class IncomeDetailAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<BeanIncomeDetail> list;

        public IncomeDetailAdapter(Context mContext,
                                   ArrayList<BeanIncomeDetail> list) {
            super();
            this.mContext = mContext;
            this.list = list;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.myaccount_income_detail_item, parent, false);
                viewHolder.item = (LinearLayout) convertView
                        .findViewById(R.id.item);
                viewHolder.left_top = (TextView) convertView
                        .findViewById(R.id.left_top);
                viewHolder.left_bottom = (TextView) convertView
                        .findViewById(R.id.left_bottom);
                viewHolder.right_top = (TextView) convertView
                        .findViewById(R.id.right_top);
                viewHolder.right_bottom = (TextView) convertView
                        .findViewById(R.id.right_bottom);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.left_bottom.setText(list.get(position).getCreatetime());
            viewHolder.right_top.setText(list.get(position).getAmount() + "元");
            String drawTips = "";
            switch (list.get(position).getState()) {
                case 0:
                    drawTips = "审核中";
                    break;
                case 1:
                    drawTips = "已完成";
                    break;
                case 2:
                    drawTips = "失败";
                    break;

                default:
                    break;
            }
            viewHolder.right_bottom.setText(list.get(position).getDetail() + " " + drawTips);
            if ((int) list.get(position).getType() == 1) {// true 来白，false 出灰
                StringBuilder stringBuilder = new StringBuilder(list.get(position).getInAccount());
                stringBuilder.replace(3, 6, "****");
                viewHolder.left_top.setText(stringBuilder);
            } else {
                StringBuilder stringBuilder = new StringBuilder(list.get(position).getOutAccount());
                stringBuilder.replace(3, 6, "****");
                viewHolder.left_top.setText(stringBuilder);
            }
            if (position % 2 != 0) {
                viewHolder.item.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.gray_second));
            }
            return convertView;
        }

        class ViewHolder {
            TextView left_top, left_bottom, right_top, right_bottom;
            LinearLayout item;
        }
    }

}
