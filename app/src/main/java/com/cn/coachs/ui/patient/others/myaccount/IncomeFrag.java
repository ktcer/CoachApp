/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.myaccount.BeanIncomeDetail;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author kuangtiecheng
 *         总收入、总结算、最近四笔收支明细
 */
public class IncomeFrag extends BaseFrag implements OnItemClickListener {
    private TextView total_income, total_settlement, income_detail;
    //	private XListView list_income;
//	private IncomeDetailItem incomeContent1,incomeContent2,incomeContent3,incomeContent4;
//	private IncomeDetailAdapter mAdapter;
    private ArrayList<BeanIncomeDetail> list = null;
    public static float allIncome;
    public static float allCash;
    private int pageNum;
    private int pageSize;

    public void init() {
        total_income = (TextView) view.findViewById(R.id.total_income);
        total_settlement = (TextView) view.findViewById(R.id.total_settlement);

//		incomeContent1 = (IncomeDetailItem)view.findViewById(R.id.item_one);
//		incomeContent2 = (IncomeDetailItem)view.findViewById(R.id.item_two);
//		incomeContent3 = (IncomeDetailItem)view.findViewById(R.id.item_three);
//		incomeContent4 = (IncomeDetailItem)view.findViewById(R.id.item_four);

        income_detail = (TextView) view.findViewById(R.id.income_detail);
        income_detail.setOnClickListener(this);

//		list_income = (XListView)view.findViewById(R.id.list_income);
//		list_income.setPullRefreshEnable(false);
//		list_income.setXListViewListener(this);


        pageNum = 1;
        pageSize = 4;

        initData();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.income_detail)
            startIntent(IncomeDetail.class);
//			Toast.makeText(getActivity(), "开发中...", Toast.LENGTH_SHORT).show();
    }

    protected long getUserId(int role) {
        UtilsSharedData.initDataShare(getActivity());
        long userId = UtilsSharedData.getLong(Constant.USER_ID, role);
        return userId;
    }

    public String setUrl(String path) {
        return AbsParam.getBaseUrl() + path;
        // this.url = "http://192.168.202.108:8080/serviceplatform"+path;
    }

    public void initData() {

        AsyncTask asyncTask = new AsyncTask<Integer, Void, String>() {

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                System.out.println("result =" + result);
                Type type = new TypeToken<ArrayList<BeanIncomeDetail>>() {
                }.getType();
                Gson gson = new Gson();
                list = gson.fromJson(result, type);
                if (list == null) {
                    return;
                }
                IncomeDetailItem incomeDetailItem = null;
                for (int i = 0; i < list.size(); i++) {
                    switch (i) {
                        case 0:
                            incomeDetailItem = (IncomeDetailItem) view.findViewById(R.id.item_one);
                            break;
                        case 1:
                            incomeDetailItem = (IncomeDetailItem) view.findViewById(R.id.item_two);
                            break;
                        case 2:
                            incomeDetailItem = (IncomeDetailItem) view.findViewById(R.id.item_three);
                            break;
                        case 3:
                            incomeDetailItem = (IncomeDetailItem) view.findViewById(R.id.item_four);
                            break;
                        default:
                            break;
                    }
                    System.out.println("result是否=" + list);
                    incomeDetailItem.setLeftBottom(list.get(i).getCreatetime());
                    incomeDetailItem.setRightTop(list.get(i).getAmount() + "元");
                    String drawTips = "";
                    switch (list.get(i).getState()) {
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
                    incomeDetailItem.setRightBottom(list.get(i).getDetail() + " " + drawTips);
                    if ((int) list.get(i).getType() == 1) {
//						incomeDetailItem.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                        StringBuilder stringBuilder = new StringBuilder(list.get(i).getInAccount());
                        stringBuilder.replace(3, 6, "****");
                        incomeDetailItem.setLeftTop(stringBuilder.toString());
                    } else {
//						incomeDetailItem.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_second));
                        StringBuilder stringBuilder = new StringBuilder(list.get(i).getOutAccount());
                        stringBuilder.replace(3, 6, "****");
                        incomeDetailItem.setLeftTop(stringBuilder.toString());
                    }
                    if (i % 2 != 0) {
                        incomeDetailItem.setBackColor(getActivity().getResources()
                                .getColor(R.color.gray_second));
                    }

                }
            }

            @Override
            protected String doInBackground(Integer... params) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("userID", "" + getUserId(1));
                map.put("userType", "" + 1);
                map.put("pageNum", "" + pageNum);
                map.put("pageSize", "" + pageSize);
                String result = "";
                try {
                    result = NetTool.sendPostRequest(setUrl("/mywallet/moneyhistory"), map, "utf-8");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return result;
            }

        }.execute();
//		AsyncIncomeDetail  mAsyncIncomeDetail = new AsyncIncomeDetail(getActivity(),pageNum,pageSize){
//			@Override
//			protected void onPostExecute(ArrayList<BeanIncomeDetail> result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				IncomeDetailItem incomeDetailItem = null;
//				for (int i = 0; i < result.size(); i++) {
//					switch(i){
//						case 0 :incomeDetailItem = (IncomeDetailItem)view.findViewById(R.id.item_one);
//							break;
//						case 1 :incomeDetailItem = (IncomeDetailItem)view.findViewById(R.id.item_two);
//							break;
//						case 2 :incomeDetailItem = (IncomeDetailItem)view.findViewById(R.id.item_three);
//							break;
//						case 3 :incomeDetailItem = (IncomeDetailItem)view.findViewById(R.id.item_four);
//							break;
//						default:
//							break;
//					}
//					System.out.println("result="+result);
//					incomeDetailItem.setLeftBottom(result.get(i).getCreatetime());
//					incomeDetailItem.setRightTop(result.get(i).getAmount()+"元");
//					incomeDetailItem.setRightBottom(result.get(i).getDetail());
//					if(result.get(i).isType()){
//						incomeDetailItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//						incomeDetailItem.setLeftTop(result.get(i).getInAccount());
//					}else{
//						incomeDetailItem.setBackgroundColor(mContext.getResources().getColor(R.color.gray_second));
//						incomeDetailItem.setLeftTop(result.get(i).getOutAccount());
//					}
//					
//				}
////				mAdapter = new IncomeDetailAdapter(getActivity(),result);
////				list_income.setAdapter(mAdapter);
//			}	
//		};
//		mAsyncIncomeDetail.execute();

    }

    @Override
    public void onResume() {
        super.onResume();
        AsyncGetMonthInfo gmTask = new AsyncGetMonthInfo(getActivity()) {
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                total_income.setText(allIncome + "");
                total_settlement.setText(allCash + "");
            }
        };
        gmTask.execute();
    }


    @Override
    public int getLayout() {
        // TODO Auto-generated method stub
        return R.layout.myaccount_income_frag;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub

    }

//	@Override
//	public int getLayout() {
//		// TODO Auto-generated method stub
//		return R.layout.myaccount_income_frag;
//	}
//
//	@Override
//	public void onRefresh() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onLoadMore() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO Auto-generated method stub
//		System.out.println("=-=-=position=-=-="+position);
//		
//	}	
//	
//	public class IncomeDetailAdapter extends BaseAdapter {
//		private Context mContext;
//		private ArrayList<BeanIncomeDetail> list;
//
//		public IncomeDetailAdapter(Context mContext,
//				ArrayList<BeanIncomeDetail> list) {
//			super();
//			this.mContext = mContext;
//			this.list = list;
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return list.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return list.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder viewHolder = null;
//			if (convertView == null){
//				viewHolder = new ViewHolder();
//				convertView = LayoutInflater.from(mContext).inflate(
//						R.layout.myaccount_income_detail_item, parent, false);
//				viewHolder.item = (LinearLayout)convertView.findViewById(R.id.item);
//				viewHolder.left_top = (TextView)convertView.findViewById(R.id.left_top);
//				viewHolder.left_bottom = (TextView)convertView.findViewById(R.id.left_bottom);
//				viewHolder.right_top = (TextView)convertView.findViewById(R.id.right_top);
//				viewHolder.right_bottom = (TextView)convertView.findViewById(R.id.right_bottom);
//				convertView.setTag(viewHolder);
//			}else
//				viewHolder = (ViewHolder)convertView.getTag();
//			viewHolder.left_bottom.setText(list.get(position).getCreatetime());
//			viewHolder.right_top.setText(list.get(position).getAmount()+"元");
//			viewHolder.right_bottom.setText(list.get(position).getDetail());
//			if(list.get(position).isType()){//true 来白，false 出灰
//				viewHolder.item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//				viewHolder.left_top.setText(list.get(position).getInAccount());				
//			}else{
//				viewHolder.item.setBackgroundColor(mContext.getResources().getColor(R.color.gray_second));
//				viewHolder.left_top.setText(list.get(position).getOutAccount());								
//			}
//			return convertView;
//		}
//
//		class ViewHolder {
//			TextView left_top, left_bottom, right_top, right_bottom;
//			LinearLayout item;
//		}
//	}
}
