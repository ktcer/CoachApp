package com.cn.coachs.ui.patient.main.healthdiary;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.HeaderViewListAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.healthdiary.BeanResultOfEvaluation;
import com.cn.coachs.model.healthdiary.BeanResultOfEvaluationItem;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.utils.ToastUtil;
import com.cn.coachs.ui.patient.main.healthdiary.nlevelitem.NLevelAdapter;
import com.cn.coachs.ui.patient.main.healthdiary.nlevelitem.NLevelItem;
import com.cn.coachs.ui.patient.main.healthdiary.nlevelitem.NLevelView;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.PickerView;
import com.cn.coachs.util.PickerView.onSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author kuangtiecheng
 */
@SuppressLint("ResourceAsColor")
public class ActivityFansTestReportDetail extends ActivityBasic {

    private ListView listView;
    private BeanResultOfEvaluation mBeanResultOfEvaluation;
    private List<NLevelItem> list;
    public static String TESTREPORTTYPE = "test_report_type";// 0为查看评测详情，1为开始制定康复日记
    private int pageType;
    private String fansId;// 科良
    // 以下与日记有关
    public AsynQueryPlan asynQueryPlan;
    public String startDate;
    public String planLength;
    // public BeanFans mBeanFans;
    private int cpStatus;
    private String isMaster = "0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlyalistview);
        ((TextView) findViewById(R.id.middle_tv)).setText("制定方案");
        listView = (ListView) findViewById(R.id.list);
        listView.setDivider(null);
        pageType = getIntent().getIntExtra(TESTREPORTTYPE, 0);
        fansId = getIntent().getStringExtra(Constant.FANS_ID);// 科良
        // mBeanFans = getIntent().getParcelableExtra(Constant.FANS_BEANS);//丑旦
        isMaster();
    }

    private void isMaster() {
        showProgressBar();
        AsyncIsFansMaster isfansmaster = new AsyncIsFansMaster(ActivityFansTestReportDetail.this, fansId) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                hideProgressBar();
                if (result != null) {
                    isMaster = result;
                } else {
                    Toast.makeText(ActivityFansTestReportDetail.this, R.string.network_error, Toast.LENGTH_SHORT).show();
                    isMaster = "0";
                }
                if (pageType == 0) {
                    ((TextView) findViewById(R.id.middle_tv)).setText("评估详情");
//					if(isMaster.equals("1")){
                    mBeanResultOfEvaluation = (BeanResultOfEvaluation) getIntent()
                            .getSerializableExtra(Constant.TESTREPORTDETAIL);
                    initializeHeaderAndFooter();
                    initializeAdapter();
//					}else{
//						Toast.makeText(ActivityFansTestReportDetail.this, R.string.youcannotsee, Toast.LENGTH_SHORT).show();
//					}
                } else if (pageType == 1) {
                    // 查询当前粉丝是否已经有日记了
                    ((TextView) findViewById(R.id.middle_tv)).setText("制定方案");
                    if (isMaster.equals("1")) {
                        getLastTest();
                    } else {
                        ToastUtil.showMessage(R.string.youarenotnew);
                    }
                }
            }
        };
        isfansmaster.execute();
    }

    private void getLastTest() {
        showProgressBar();
        QueryTestReportList taskQNL = new QueryTestReportList();
        taskQNL.execute();
    }

    private void initializeHeaderAndFooter() {

        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout header = (LinearLayout) inflater.inflate(
                R.layout.fanstest_headerandfooter, listView, false);
        listView.addHeaderView(header, null, false);
        if (pageType == 1) {
            LinearLayout footer = (LinearLayout) inflater.inflate(
                    R.layout.fanstest_headerandfooter, listView, false);
            footer.findViewById(R.id.headerLayout).setVisibility(View.GONE);
            footer.findViewById(R.id.btn_startSetDiary).setVisibility(
                    View.VISIBLE);
            ((FButton) footer.findViewById(R.id.btn_startSetDiary))
                    .setCornerRadius(3);
            listView.addFooterView(footer, null, false);
            footer.findViewById(R.id.btn_startSetDiary)
                    .setOnClickListener(this);
        }
    }

    private void setDescColor(TextView tv, String desc) {
        if (desc == null) {
            return;
        }
        if (desc.equals("危险")) {
            tv.setTextColor(getResources().getColor(R.color.red));
        } else if (desc.equals("良好")) {
            tv.setTextColor(getResources().getColor(R.color.blue_second));
        } else if (desc.equals("理想")) {
            tv.setTextColor(getResources().getColor(R.color.lightgreen));
        }
    }

    private void initializeAdapter() {

        list = new ArrayList<NLevelItem>();
        final LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < mBeanResultOfEvaluation.getReport().size(); i++) {

            final NLevelItem grandParent = new NLevelItem(
                    mBeanResultOfEvaluation.getReport().get(i), null,
                    new NLevelView() {

                        @Override
                        public View getView(NLevelItem item) {
                            View view = inflater.inflate(
                                    R.layout.item_testresult_group, null);
                            TextView tv_title = (TextView) view
                                    .findViewById(R.id.txt_item_title);
                            // tv_title.setBackgroundColor(Color.GREEN);
                            TextPaint paint = tv_title.getPaint();
                            paint.setFakeBoldText(true);
                            String name = (String) ((BeanResultOfEvaluationItem) item
                                    .getWrappedObject()).getItem();
                            name = name.replaceAll("null", "");
                            tv_title.setText(name);

                            if (((BeanResultOfEvaluationItem) item
                                    .getWrappedObject()).getHaschild() == 0) {

                                TextView tv_value = (TextView) view
                                        .findViewById(R.id.txt_item_value);
                                String value = (String) (((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getValue() + " " + ((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getUtil());
                                value = value.replaceAll("null", "");
                                tv_value.setText(value);
                            }
                            return view;
                        }
                    });
            list.add(grandParent);
            if (mBeanResultOfEvaluation.getReport().get(i).getHaschild() == 0) {
                continue;
            }
            int numChildren = mBeanResultOfEvaluation.getReport().get(i)
                    .getChildren().size();
            for (int j = 0; j < numChildren; j++) {
                NLevelItem parent = new NLevelItem(mBeanResultOfEvaluation
                        .getReport().get(i).getChildren().get(j), grandParent,
                        new NLevelView() {

                            @Override
                            public View getView(NLevelItem item) {

                                View view = inflater.inflate(
                                        R.layout.item_testresult_group, null);
                                TextView tv_title = (TextView) view
                                        .findViewById(R.id.txt_item_title);
                                // tv_title.setBackgroundColor(Color.YELLOW);
                                String name = (String) ((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getItem();
                                name = name.replaceAll("null", "");
                                tv_title.setText("  " + name);

                                if (((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getHaschild() == 0) {

                                    TextView tv_value = (TextView) view
                                            .findViewById(R.id.txt_item_value);
                                    String value = (String) (((BeanResultOfEvaluationItem) item
                                            .getWrappedObject()).getValue()
                                            + " " + ((BeanResultOfEvaluationItem) item
                                            .getWrappedObject()).getUtil());
                                    value = value.replaceAll("null", "");
                                    tv_value.setText(value);

                                    TextView tv_desc = (TextView) view
                                            .findViewById(R.id.txt_item_desc);
                                    String desc = (String) ((BeanResultOfEvaluationItem) item
                                            .getWrappedObject()).getState();
                                    // desc = desc.replaceAll("null", "");
                                    tv_desc.setText(desc);
                                    setDescColor(tv_desc, desc);
                                }
                                return view;
                            }
                        });

                list.add(parent);
                if (mBeanResultOfEvaluation.getReport().get(i).getChildren()
                        .get(j).getHaschild() == 0) {
                    continue;
                }
                int grandChildren = mBeanResultOfEvaluation.getReport().get(i)
                        .getChildren().get(j).getChildren().size();
                for (int k = 0; k < grandChildren; k++) {
                    NLevelItem child = new NLevelItem(mBeanResultOfEvaluation
                            .getReport().get(i).getChildren().get(j)
                            .getChildren().get(k), parent, new NLevelView() {

                        @Override
                        public View getView(NLevelItem item) {

                            View view = inflater.inflate(
                                    R.layout.item_testresult_group, null);
                            TextView tv_title = (TextView) view
                                    .findViewById(R.id.txt_item_title);
                            // tv_title.setBackgroundColor(Color.GRAY);
                            String name = (String) ((BeanResultOfEvaluationItem) item
                                    .getWrappedObject()).getItem();
                            name = name.replaceAll("null", "");
                            tv_title.setText("    " + name);

                            if (((BeanResultOfEvaluationItem) item
                                    .getWrappedObject()).getHaschild() == 0) {

                                TextView tv_value = (TextView) view
                                        .findViewById(R.id.txt_item_value);
                                String value = (String) (((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getValue() + " " + ((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getUtil());
                                value = value.replaceAll("null", "");
                                tv_value.setText(value);

                                TextView tv_desc = (TextView) view
                                        .findViewById(R.id.txt_item_desc);
                                String desc = (String) ((BeanResultOfEvaluationItem) item
                                        .getWrappedObject()).getState();
                                // desc = desc.replaceAll("null", "");
                                tv_desc.setText(desc == null ? "" : desc);
                                setDescColor(tv_desc, desc);
                            }
                            return view;
                        }
                    });

                    list.add(child);
                }
            }
        }

        NLevelAdapter adapter = new NLevelAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) listView
                        .getAdapter();
                NLevelAdapter adapter = (NLevelAdapter) listAdapter
                        .getWrappedAdapter();
                adapter.toggle(arg2 - 1);
                adapter.getFilter().filter();
            }
        });
    }

    /**
     * 查询测评结果(对于初次制定日记请求最近一次测评结果)
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

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", fansId);
            param.put("wenjuanID", "3");

            param.put("pageSize", "1");
            param.put("pageNum", "1");
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                                + "/webapp/questionnaire/getquesresult2", param,
                        "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                e.printStackTrace();

            }
            hideProgressBar();
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
                    List<BeanResultOfEvaluation> tempResultOfEvaluations = gson
                            .fromJson(
                                    jsonString,
                                    new TypeToken<List<BeanResultOfEvaluation>>() {
                                    }.getType());
                    if (tempResultOfEvaluations != null) {
                        if (tempResultOfEvaluations.size() > 0) {
                            mBeanResultOfEvaluation = tempResultOfEvaluations
                                    .get(0);
                        }
                    }

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            if (mBeanResultOfEvaluation == null) {
                findViewById(R.id.noTestLayout).setVisibility(View.VISIBLE);
                ((FButton) findViewById(R.id.btn_setDiary)).setCornerRadius(3);
                ((FButton) findViewById(R.id.btn_setDiary))
                        .setOnClickListener(ActivityFansTestReportDetail.this);
                ((FButton) findViewById(R.id.btn_remindtest))
                        .setCornerRadius(3);
                ((FButton) findViewById(R.id.btn_remindtest))
                        .setOnClickListener(ActivityFansTestReportDetail.this);
            } else {
                findViewById(R.id.noTestLayout).setVisibility(View.GONE);
                initializeHeaderAndFooter();
                initializeAdapter();
            }
            // hideProgressBar();

        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_startSetDiary:
                // 开始设置方案
                showChooseDialog();
                break;
            case R.id.btn_setDiary:
                // 开始设置方案
                showChooseDialog();
                break;
            case R.id.btn_remindtest:
                // 提醒患者测评
                break;
            default:
                break;
        }
    }

    /**
     * @return 弹窗
     */
    private void showChooseDialog() {
        // TODO Auto-generated method stub
        if (isMaster.equals("0")) {
            Toast.makeText(ActivityFansTestReportDetail.this, R.string.youarenotnew, Toast.LENGTH_LONG).show();
            return;
        }
        CustomDialog.Builder builder = new CustomDialog.Builder(
                ActivityFansTestReportDetail.this);
        builder.setContentView(formChooseDialog());
        builder.setTitle("制定方案");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (startDate != null && planLength != null) {
                    dialog.dismiss();
                    asynQueryPlan = new AsynQueryPlan(getApplicationContext(),
                            startDate, "" + planLength, fansId) {
                        @Override
                        protected void onPostExecute(JSONObject result) {
                            // TODO Auto-generated method stub
                            super.onPostExecute(result);
                            hideProgressBar();
                            System.out.println("=-=-=asynQueryPlan=-=-="
                                    + result);
                            try {
                                if (result.getInt("resultID") == 1) {
                                    Intent intent = new Intent(
                                            ActivityFansTestReportDetail.this,
                                            ActivityEditDiaryDay.class);
                                    intent.putExtra(Constant.START_DATE,
                                            startDate);
                                    intent.putExtra(Constant.PLAN_LENGTH,
                                            planLength);
                                    intent.putExtra(Constant.PATIENT_ID, fansId);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "创建失败...", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    };
                    asynQueryPlan.execute();
                    showProgressBar();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * @return 构建选择弹窗
     */
    private LinearLayout formChooseDialog() {
        LayoutInflater inflaterDl = LayoutInflater
                .from(ActivityFansTestReportDetail.this);
        LinearLayout layout = (LinearLayout) inflaterDl.inflate(
                R.layout.activity_edit_diary, null);
        PickerView item_mytall = (PickerView) layout
                .findViewById(R.id.item_mytall);
        DatePicker date_start = (DatePicker) layout
                .findViewById(R.id.date_start);

        List<String> list = new ArrayList<String>();
        for (int i = 1; i < 9; i++)
            list.add("" + i);
        item_mytall.setData(list);
        planLength = 5 + "";//默认五天，不默认没法点击确定
        item_mytall.setOnSelectListener(new onSelectListener() {
            @Override
            public void onSelect(String text) {
                // TODO Auto-generated method stub
                planLength = text;
            }
        });

        final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        startDate = sf.format(new Date());// 初始化startDate，日记默认为从今天开始

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
        long time = calendar.getTimeInMillis();
        date_start.setMinDate(time);

        date_start.init(year, monthOfYear, dayOfMonth,
                new OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        String date = sf.format(calendar.getTime());
                        startDate = date;
                    }
                });
        return layout;
    }
}
