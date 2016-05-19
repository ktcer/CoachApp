/*
 * 订单列表fragment
 */

package com.cn.coachs.my;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.cn.coachs.R;
import com.cn.coachs.coach.model.BeanDiscovery;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.shara.SharaSocia;
import com.cn.coachs.ui.basic.FragmentBasicListView;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("ResourceType")
public class FragmentCoursList extends FragmentBasicListView implements AdaperMyCollectss.InterfaceIsfavoriteListener {

    private static final String ARG_POSITION = "position";
    private List<BeanDiscovery> infoList, tempInfoList;
    private int type;
    private int pageNum = 1;
    private AdaperMyCollectss listViewAdapter;
    private SharaSocia sharaSocia;
//    private String[] l;
//    private double latitude= 39.959833; //纬度 number double，6位小数
//    private double longitude = 116.355626;
//    private   BaiduLacationUtil location ;
//
//    private LocationClient mLocationClient;
//    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
//    private String tempcoor="gcj02";
//    private String LocationResult= "";

    public static FragmentCoursList newInstance(int position) {
        FragmentCoursList f = new FragmentCoursList();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position - 1);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getInt(ARG_POSITION);
        infoList = new ArrayList<BeanDiscovery>();
        tempInfoList = new ArrayList<BeanDiscovery>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        FrameLayout fl = new FrameLayout(getActivity());
        fl.setLayoutParams(params);

//		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
//				.getDisplayMetrics());
        listViewAdapter = new AdaperMyCollectss(getActivity().getApplicationContext(), infoList, true);
        listViewAdapter.setIsfavoriteListener(this);
        listView = new XListView(getActivity());
//		params.setMargins(margin, margin, margin, margin);
        params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        listView.setLayoutParams(params);
        listView.setCacheColorHint(R.color.black);
        listView.setBackgroundResource(R.color.white);
        listView.setDividerHeight(1);
        listView.setDivider(getResources().getDrawable(R.color.lightgray));
        listView.setSelector(R.color.transparent);
        listView.setTag("listView");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Intent intent = new Intent(getActivity(), ActivityMyOrdersList.class);
                intent.putExtra("classId", infoList.get(position - 1).getClassID());
                startActivity(intent);
            }
        });

        listView.setAdapter(listViewAdapter);
        fl.addView(listView);
        return fl;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        QueryMyCoursTask task = new QueryMyCoursTask();
        task.execute(type);
    }

    @Override
    public void getDone(int positione) {
        int a = positione;
        sharaSocia = new SharaSocia();
        sharaSocia.init(getActivity(), AbsParam.getBaseUrl() + infoList.get(positione).getCover(), infoList.get(positione).getTitle(), infoList.get(positione).getTag(), "http://101.200.90.103/ad/web/detail?classID=" + infoList.get(positione).getClassID());

    }

    private class QueryMyCoursTask extends AsyncTask<Integer, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            UtilsSharedData.initDataShare(getActivity());
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("coachID", userId + "");
            param.put("state", "" + params[0]);//
            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                String url = AbsParam.getBaseUrl() + "/my/app/mypublishlist";
                Log.i("result", url + param.toString());
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                jsonToArray(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            } catch (Exception e) {
                canLoadMore = false;
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanDiscovery tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }

            listViewAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
        }
    }


    private void jsonToArray(String json) {
        Gson gson = new Gson();
        String a = json;
        if (json != "") {
            tempInfoList = gson.fromJson(json, new TypeToken<List<BeanDiscovery>>() {
            }.getType());
        }

    }

    /*
     * 订单列表adapter
     */
//    private class OrdersListAdapter extends BaseAdapter {
//        private Context context;
//        public int count = 10;
//
//        public OrdersListAdapter(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public int getCount() {
//            return infoList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return infoList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = LayoutInflater.from(context).inflate(
//                        R.layout.listitem_myorder, null);
//                holder.orderCover = (ImageView) convertView
//                        .findViewById(R.id.type_myappointment);
//                holder.orderDetail = (TextView) convertView
//                        .findViewById(R.id.order_title);
//                holder.orderPrice = (TextView) convertView
//                        .findViewById(R.id.order_price);
//                holder.orderStatus = (TextView) convertView
//                        .findViewById(R.id.order_status);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + infoList.get(position).getCover(),
//                    holder.orderCover, AppMain.initImageOptions(R.drawable.default_life_icon, false));
//
//            holder.orderDetail.setText("【" + infoList.get(position).getServeType() + "】" + infoList.get(position).getStartTime() + " " + infoList.get(position).getTitle());
//            holder.orderPrice.setText("¥" + infoList.get(position)
//                    .getMoney());
//
//            switch (infoList.get(position).getState()) {
//                case 0:
//                    holder.orderStatus
//                            .setText("待支付");
//                    holder.orderStatus.setTextColor(getResources().getColor(R.color.green));
//                    break;
//                case 1:
//                    holder.orderStatus
//                            .setText("已付款");
//                    holder.orderStatus.setTextColor(getResources().getColor(R.color.orangered));
//                    break;
//                case 2:
//                    holder.orderStatus
//                            .setText("已退款");
//                    holder.orderStatus.setTextColor(getResources().getColor(R.color.lightgray));
//                    break;
//                case 3:
//                    holder.orderStatus
//                            .setText("退款中");
//                    holder.orderStatus.setTextColor(getResources().getColor(R.color.red));
//                    break;
//                case 4:
//                    holder.orderStatus
//                            .setText("已过期");
//                    holder.orderStatus.setTextColor(getResources().getColor(R.color.lightgray));
//                    break;
//            }
//
//            return convertView;
//        }
//    }
//
//    private class ViewHolder {
//        /**
//         * 订单图片
//         */
//        ImageView orderCover;
//        /**
//         * 订单详情
//         */
//        TextView orderDetail;
//        /**
//         * 订单价格
//         */
//        TextView orderPrice;
//        /**
//         * 订单状态
//         */
//        TextView orderStatus;
//
//    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        QueryMyCoursTask task = new QueryMyCoursTask();
        task.execute(type);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            sharaSocia.onsharaActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        sharaSocia.destroy();
    }

    QueryMyCoursTask achTask;

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            if (achTask != null
                    && achTask.getStatus() == AsyncTask.Status.RUNNING) {
                achTask.cancel(true); // 如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }
            achTask = new QueryMyCoursTask();
            achTask.execute(type);
        }
    }

}