/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.myaccount.BeanCoinRecord;
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
import java.util.List;

/**
 * @author kuangtiecheng
 */
public class CoinBottomFrag extends BaseFrag implements IXListViewListener, OnItemClickListener {
    private XListView list_coin_record;
    private CoinRecordAdapter mAdapter;
    private ArrayList<BeanCoinRecord> list = new ArrayList<BeanCoinRecord>();
    ;
    private int pageNum = 1;
    private AsyncTask asyncTask;

    @Override
    public int getLayout() {
        // TODO Auto-generated method stub
        return R.layout.myaccount_coin_bottom_frag;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        list_coin_record = (XListView) view.findViewById(R.id.list_coin_record);
        list_coin_record.setPullRefreshEnable(false);
        list_coin_record.setXListViewListener(this);
//		mAdapter = new CoinRecordAdapter(getData(),getActivity());
//		list_coin_record.setAdapter(mAdapter);
        asyncTask = new AscynCoinHistory().execute();
    }

//	public ArrayList<BeanCoinRecord> getData(){
//		list = new ArrayList<BeanCoinRecord>();
//		BeanCoinRecord mBeanCoinRecord = new BeanCoinRecord();
//		mBeanCoinRecord.setContent("签到奖励");
//		mBeanCoinRecord.setDate("2015-09-10");
//		mBeanCoinRecord.setIncrement("3");
//		list.add(mBeanCoinRecord);
//		return list;
//	}

    private class AscynCoinHistory extends AsyncTask<Integer, Integer, String> {

        String result = "";
        //		private ActivityBasic act;
        private String requestAddInfo = "/goldcoins/coinhistory";
        private List<BeanCoinRecord> beanOveragelist;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userID", "" + getUserId(1));
            param.put("userType", 1 + "");
            param.put("pageNum", pageNum + "");
            param.put("pageSize", 7 + "");
//			if (pageNum == 1) {
//				param.put("pageSize", 3 + "");
//			} else {
//				param.put("pageSize", 10 + "");
//			}

            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            try {
                result = NetTool.sendPostRequest(url, param, "utf-8");
                if (result != null) {
                    pageNum++;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Log.i("result", result);
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("result =" + result);
            Type type = new TypeToken<ArrayList<BeanCoinRecord>>() {
            }.getType();
//			try {
//				JSONArray jsonArray = new JSONArray(result);
//				for (int i = 0; i < jsonArray.length(); i++) {
//					JSONObject jsonObject = jsonArray.getJSONObject(i);
//					jsonObject.get("amount");
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

            Gson gson = new Gson();
            ArrayList<BeanCoinRecord> tempList = new ArrayList<BeanCoinRecord>();
            tempList = gson.fromJson(result, type);
            if (tempList == null) {
                return;
            }
            for (int i = 0; i < tempList.size(); i++) {
                BeanCoinRecord mBeanCoinRecord = new BeanCoinRecord();
                mBeanCoinRecord.setDetail(tempList.get(i).getDetail());
                mBeanCoinRecord.setCreatetime(tempList.get(i).getCreatetime());
                mBeanCoinRecord.setAmount(tempList.get(i).getAmount());
                list.add(tempList.get(i));
            }
            if (list.size() < 7) {
                list_coin_record.setPullLoadEnable(false);
            } else {
                list_coin_record.setPullLoadEnable(true);
            }
            if (pageNum == 2) {
                mAdapter = new CoinRecordAdapter(list, getActivity());
                list_coin_record.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
            list_coin_record.stopLoadMore();
        }
    }

    protected long getUserId(int role) {
        UtilsSharedData.initDataShare(getActivity());
        long userId = UtilsSharedData.getLong(Constant.USER_ID, role);
        return userId;
    }


    public class CoinRecordAdapter extends BaseAdapter {
        private ArrayList<BeanCoinRecord> list;
        private Context mContext;

        public CoinRecordAdapter(ArrayList<BeanCoinRecord> list,
                                 Context mContext) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.myaccount_coin_item, parent, false);
                viewHolder.detail = (TextView) convertView.findViewById(R.id.content);
                viewHolder.creattime = (TextView) convertView.findViewById(R.id.date);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.increase);
                convertView.setTag(viewHolder);
            } else
                viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.detail.setText(list.get(position).getDetail());
            viewHolder.creattime.setText(list.get(position).getCreatetime());
            viewHolder.amount.setText("+" + list.get(position).getAmount());

            return convertView;
        }

        class ViewHolder {
            TextView detail, creattime, amount;
        }
    }


    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoadMore() {
        asyncTask = new AscynCoinHistory().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }
}
