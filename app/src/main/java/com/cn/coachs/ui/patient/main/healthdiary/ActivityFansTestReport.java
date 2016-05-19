package com.cn.coachs.ui.patient.main.healthdiary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.healthdiary.BeanResultOfEvaluation;
import com.cn.coachs.ui.basic.ActivityBasicListView;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ActivityFansTestReport extends ActivityBasicListView {


    private List<BeanResultOfEvaluation> infoList, tempInfoList;
    private TestReportListAdapter testReportListAdapter;
    private String fansId;
    protected int pageNum = 1;

    //	private String isMaster;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlyaxlistview);
        fansId = getIntent().getStringExtra(Constant.FANS_ID);
        initial();

    }

    /**
     * 基本组件初始化
     */
    private void initial() {
        testReportListAdapter = new TestReportListAdapter(this);
        listView = (XListView) this.findViewById(R.id.xlist);
        listView.setTag("listView");
        ((TextView) findViewById(R.id.middle_tv)).setText("评估报告");
        listView.setPullLoadEnable(false);
        listView.setXListViewListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

//				 if(isMaster.equals("1")){
                Intent intent = new Intent(ActivityFansTestReport.this,
                        ActivityFansTestReportDetail.class);
                intent.putExtra(Constant.TESTREPORTDETAIL, infoList.get(position - 1));
                startActivity(intent);
//					}else{
//						Toast.makeText(ActivityFansTestReport.this, R.string.youcannotsee, Toast.LENGTH_SHORT).show();
//					}

            }
        });
        infoList = new ArrayList<BeanResultOfEvaluation>();
        tempInfoList = new ArrayList<BeanResultOfEvaluation>();
        listView.setAdapter(testReportListAdapter);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        QueryTestReportList taskQNL = new QueryTestReportList();
        taskQNL.execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
//		case R.id.fab_addtest:
//			Intent intent = new Intent(this, ActivityEvaluationSecond.class);
//	        startActivity(intent);
//			break;

        }
    }

    /**
     * 查询测评结果列表
     *
     * @author kuangtiecheng
     */
    private class QueryTestReportList extends
            AsyncTask<Integer, Integer, String> {

        public QueryTestReportList() {
            super();
        }

        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            UtilsSharedData.initDataShare(ActivityFansTestReport.this);
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", fansId);
            param.put("wenjuanID", "0");

            param.put("pageSize", 10 + "");
            param.put("pageNum", pageNum + "");
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                                + "/webapp/questionnaire/getquesresult2", param,
                        "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                JsonArrayToList(result);
                if (tempInfoList.size() < 10) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            } catch (Exception e) {
                canLoadMore = false;
                e.printStackTrace();
                hideProgressBar();

            }
            return null;
        }

        /**
         * 解析返回来的Json数组
         *
         * @param jsonString
         * @return
         * @throws Exception
         */
        private void JsonArrayToList(String jsonString) throws Exception {
            Gson gson = new Gson();
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    tempInfoList = gson.fromJson(jsonString,
                            new TypeToken<List<BeanResultOfEvaluation>>() {
                            }.getType());

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanResultOfEvaluation tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            if (infoList.size() == 0) {
                findViewById(R.id.empty_testdata_tv).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.empty_testdata_tv).setVisibility(View.GONE);
            }
            testReportListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();
//			AsyncIsFansMaster isfansmaster = new AsyncIsFansMaster(ActivityFansTestReport.this,fansId){
//				@Override
//				protected void onPostExecute(String result) {
//					// TODO Auto-generated method stub
//					super.onPostExecute(result);
//					hideProgressBar();
//					if(result!=null){
//						isMaster = result;
//					}else{
//						Toast.makeText(ActivityFansTestReport.this, R.string.network_error, Toast.LENGTH_SHORT).show();
//						isMaster = "0";
//					}
//				}
//			};
//			isfansmaster.execute();
        }
    }

    private class TestReportListAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public TestReportListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.listitem_testreport, null);
                holder.reportTime = (TextView) convertView
                        .findViewById(R.id.report_time);
                holder.reportTitle = (TextView) convertView
                        .findViewById(R.id.report_title);
                holder.reportResult = (TextView) convertView
                        .findViewById(R.id.report_result);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.reportTime.setText(infoList.get(position).getDate());// infoList.get(position)
            holder.reportTitle.setText(infoList.get(position).getWenjuanName());// infoList.get(position)
            if (infoList.get(position).getWenjuanName().equals("身体测评")) {
                holder.reportResult.setVisibility(View.GONE);
            } else {
                holder.reportResult.setVisibility(View.VISIBLE);
                holder.reportResult.setText("测评结果：" + infoList.get(position).getDetail());
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView reportTime;
        TextView reportTitle;
        TextView reportResult;

    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        pageNum = 1;
        // showProgressBar();
        QueryTestReportList taskQNL = new QueryTestReportList();
        taskQNL.execute();
    }

    QueryTestReportList taskQNL;

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (canLoadMore) {
            if (taskQNL != null && taskQNL.getStatus() == AsyncTask.Status.RUNNING) {
                taskQNL.cancel(true);  //  如果Task还在运行，则先取消它
            } else {
                pageNum++;
            }

            taskQNL = new QueryTestReportList();
            taskQNL.execute();
        }
    }

}
