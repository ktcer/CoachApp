package com.cn.coachs.ui.patient.main.detectioncenter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.detect.BeanHistoricDetectionList;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.CircleImageView;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.HorizontalPicker;
import com.cn.coachs.util.HorizontalPicker.OnItemClicked;
import com.cn.coachs.util.HorizontalPicker.OnItemSelected;
import com.cn.coachs.util.segmentbutton.AndroidSegmentedControlView;
import com.cn.coachs.util.segmentbutton.AndroidSegmentedControlView.OnSelectionChangedListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 监测数据
 *
 * @author ktcer
 */
public class ActivityFansDetectionInfo extends ActivityBasic {

    private CircleImageView imag;
    private TextView name;
    private TextView sex;
    private TextView age;
    private TextView height;
    private TextView weight;
    // private TextView illness;
    /**
     * 测量值
     */
    private long monitorDataId;
    /**
     * 体温或者搜收缩压的最大值
     */
    private long refValueMax_TWorSSY;
    /**
     * 体温或者收缩压的最小值
     */
    private long refValueMin_TWorSSY;
    private long refValueMax_SZY;
    private long refValueMin_SZY;
    /**
     * 单位
     */
    private String unit;
    private String dataName;
    // private RecorderAndPlaybackInterface audioRecorderAndPlaybackInterface;

    private static final long TW = 1l;
    private static final long SSY = 2l;
    private static final long SZY = 3l;
    private static final long HR = 4l;
    private static final long XT = 5l;
    private static final long XY = 6l;
    private static final long TZ = 7l;
    private static final long TIZHONG = 8l;

    private static final long KFXT = 13l;
    private static final long ZCQXT = 14l;
    private static final long ZCHXT = 15;
    private static final long WUCQXT = 16l;
    private static final long WUCHXT = 17l;
    private static final long WANCQXT = 18l;
    private static final long WANCHXT = 19l;
    private static final long SQXT = 20l;
    private String DetectionValue, DetectionValue1;
    private List<BeanHistoricDetectionList> historicList;
    private String dataCode = "BP";
    private String nowChooseType = Constant.DETECTION_XUEYA;

    private String dataNum = "7";// 获取历史数据个数

    private LineChartView chart;
    private LineChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = false;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = true;
    private int numberOfPoints = 7;
    private HorizontalPicker picker;
    private CharSequence[] detectType;
    private LinearLayout indicateLayout;
    private TextView indicate1, indicate2, indicate3, indicate4, historyCenter;

    private String fansId;
    private String fansName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fansdetectioninfo);
        fansId = getIntent().getStringExtra(Constant.FANS_ID);
        fansName = getIntent().getStringExtra(Constant.FANS_NAME);
        ((TextView) findViewById(R.id.middle_tv)).setText("监测数据");
        initial();
    }

    /**
     * 重置图表
     */
    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(chart.getMaximumViewport());
        if (nowChooseType.equals(Constant.DETECTION_TIWEN)) {
            v.bottom = 31;
            v.top = 42;
        } else if (nowChooseType.equals(Constant.DETECTION_XUEYA)) {
            v.bottom = 0;
            v.top = 300;
        } else if (nowChooseType.equals(Constant.DETECTION_XUETANG)) {
            v.bottom = 0;
            v.top = 35;
        } else if (nowChooseType.equals(Constant.DETECTION_XUEYANG)) {
            v.bottom = 50;
            v.top = 100;
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHI)) {
            v.bottom = 1;
            v.top = 50;
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHONG)) {
            v.bottom = 1;
            v.top = 400;
        }

        v.left = 0;
        if (numberOfPoints == 1) {
            v.right = 1;
        } else {
            v.right = numberOfPoints - 1;
        }
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
        if (data != null) {
            data.getLines().clear();
            chart.setLineChartData(data);
        } else {
            //TODO很奇怪的处理方案
            //第一次进入，为了不让图表区域空白， 新建一张空白表
            ArrayList<Line> lines = new ArrayList<Line>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; j++) {
                values.add(new PointValue(Float.parseFloat(j + ""), -100));
            }
            initLines(lines, values, ChartUtils.COLORS[0]);
            data = new LineChartData(lines);
            data.setBaseValue(Float.NEGATIVE_INFINITY);
            Axis axisX = new Axis().setHasLines(true);
            Axis axisY = new Axis().setHasLines(true);
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
            chart.setLineChartData(data);

        }
    }

    private void initial() {
        // 将上次的旋转值缓存下来
        imag = (CircleImageView) findViewById(R.id.image_head);
        name = (TextView) findViewById(R.id.state_name);
        sex = (TextView) findViewById(R.id.state_sex);
        age = (TextView) findViewById(R.id.state_age);
        height = (TextView) findViewById(R.id.date_height);
        weight = (TextView) findViewById(R.id.weight);

        ImageLoader.getInstance().displayImage(
                AbsParam.getBaseUrl()
                        + getIntent().getStringExtra(Constant.fANS_IMAGEURL),
                imag,
                AppMain.initImageOptions(R.drawable.default_user_icon, true));
        sex.setText("性别:" + getIntent().getStringExtra(Constant.FANS_GENDER));
        age.setText("年龄:" + getIntent().getStringExtra(Constant.FANS_AGE));
        height.setText("身高:" + getIntent().getStringExtra(Constant.FANS_HEIGHT)
                + "cm");
        weight.setText("体重:" + getIntent().getStringExtra(Constant.FANS_WEIGHT)
                + "kg");
        // illness.setText("确诊疾病:"+MobileContactActivity.ill);
        name.setText("姓名:" + fansName);

        chart = (LineChartView) findViewById(R.id.menulist);
        chart.setViewportCalculationEnabled(false);
        chart.setValueSelectionEnabled(hasLabelForSelected);
        if (hasLabelForSelected) {
            hasLabels = false;
        }
        historyCenter = (TextView) findViewById(R.id.history_detectioncenter);
        indicateLayout = (LinearLayout) findViewById(R.id.indicate);
        indicate1 = (TextView) findViewById(R.id.indicate1);
        indicate2 = (TextView) findViewById(R.id.indicate2);
        indicate3 = (TextView) findViewById(R.id.indicate3);
        indicate4 = (TextView) findViewById(R.id.indicate4);
        indicate1.setOnClickListener(this);
        indicate2.setOnClickListener(this);
        indicate3.setOnClickListener(this);
        indicate4.setOnClickListener(this);

        historyCenter.setOnClickListener(this);
        historicList = new ArrayList<BeanHistoricDetectionList>();
        // 滑动选择测量类型
        chooseType();
        // 周选择
        choosePeriod();
        showProgressBar();
        getHistoricDetection task_hisD = new getHistoricDetection();
        task_hisD.execute();

    }

    private void chooseType() {
        picker = (HorizontalPicker) findViewById(R.id.picker);
        detectType = getResources().getTextArray(R.array.values);
        picker.setOnItemClickedListener(new OnItemClicked() {

            @Override
            public void onItemClicked(int index) {
                // TODO Auto-generated method stub
                judgeChooseType(index);
            }
        });
        picker.setOnItemSelectedListener(new OnItemSelected() {

            @Override
            public void onItemSelected(int index) {
                // TODO Auto-generated method stub
                judgeChooseType(index);
            }
        });
    }

    private void choosePeriod() {
        AndroidSegmentedControlView datePicker = (AndroidSegmentedControlView) findViewById(R.id.choosePeriod);
        datePicker
                .setOnSelectionChangedListener(new OnSelectionChangedListener() {

                    @Override
                    public void newSelection(String identifier, String value) {
                        // TODO Auto-generated method stub
                        // Toast.makeText(getActivity(), value, 1000).show();
                    }
                });
    }

    // 修改测试类型
    private void judgeChooseType(int index) {

        if (detectType[index].toString().equals("血压")) {
            nowChooseType = Constant.DETECTION_XUEYA;
            dataCode = "BP";
        } else {

            if (detectType[index].toString().equals("体温")) {
                nowChooseType = Constant.DETECTION_TIWEN;
                dataCode = "TW";
            } else if (detectType[index].toString().equals("血糖")) {
                nowChooseType = Constant.DETECTION_XUETANG;
                dataCode = "XT";
            } else if (detectType[index].toString().equals("血氧")) {
                nowChooseType = Constant.DETECTION_XUEYANG;
                dataCode = "XY";
            } else if (detectType[index].toString().equals("体脂")) {
                nowChooseType = Constant.DETECTION_TIZHI;
                dataCode = "TIZHI";
            } else if (detectType[index].toString().equals("体重")) {
                nowChooseType = Constant.DETECTION_TIZHONG;
                dataCode = "TIZHONG";
            } else {
                return;
            }
        }
        showProgressBar();
        getHistoricDetection task_hisD = new getHistoricDetection();
        task_hisD.execute();

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {

            case R.id.history_detectioncenter:
                Intent intent = new Intent(this, ActivityHistrionicDataCenter.class);
                intent.putExtra("dataCode", dataCode);
                intent.putExtra(Constant.FANS_ID, fansId);
                startActivity(intent);
                break;

        }
    }

    private ArrayList<String> xTimeList = new ArrayList<String>();

    // 更新图表数据
    private void updateChartData() {
        // resetViewport();
        if (nowChooseType.equals(Constant.DETECTION_XUEYA)) {
            indicate1.setText("收缩压(mmHg)");
            indicate2.setText("舒张压(mmHg)");
            indicate3.setText("心率(次/分)");
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.VISIBLE);
            indicate3.setVisibility(View.VISIBLE);
            indicate4.setVisibility(View.GONE);
        } else if (nowChooseType.equals(Constant.DETECTION_TIWEN)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.GONE);
            indicate3.setVisibility(View.GONE);
            indicate4.setVisibility(View.GONE);
            indicate1.setText("体温(摄氏度)");
        } else if (nowChooseType.equals(Constant.DETECTION_XUETANG)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.VISIBLE);
            indicate3.setVisibility(View.VISIBLE);
            indicate4.setVisibility(View.VISIBLE);
            indicate1.setText("凌晨");
            indicate2.setText("餐前");
            indicate3.setText("餐后");
            indicate4.setText("睡前");
        } else if (nowChooseType.equals(Constant.DETECTION_XUEYANG)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.GONE);
            indicate3.setVisibility(View.GONE);
            indicate4.setVisibility(View.GONE);
            indicate1.setText("血氧(百分比)");
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHONG)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.GONE);
            indicate3.setVisibility(View.GONE);
            indicate4.setVisibility(View.GONE);
            indicate1.setText("体重(千克)");
        } else if (nowChooseType.equals(Constant.DETECTION_TIZHI)) {
            indicate1.setVisibility(View.VISIBLE);
            indicate2.setVisibility(View.GONE);
            indicate3.setVisibility(View.GONE);
            indicate4.setVisibility(View.GONE);
            indicate1.setText("体脂(百分比)");
        }
        if (historicList.size() == 0) {
            resetViewport();
            return;
        }
        xTimeList.clear();
        List<Line> lines = null;
        if (nowChooseType.equals(Constant.DETECTION_XUEYA)) {
            lines = new ArrayList<Line>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < historicList.size(); j++) {
                if (historicList.get(historicList.size() - j - 1)
                        .getValueTWorSSY().length() != 0) {
                    values.add(new PointValue(Float.parseFloat(j + ""), Float
                            .parseFloat(historicList.get(
                                    historicList.size() - j - 1)
                                    .getValueTWorSSY())));
                    xTimeList.add(historicList.get(historicList.size() - j - 1)
                            .getTime().substring(0, 5));
                }

            }
            numberOfPoints = 7;// values.size();
            resetViewport();
            initLines(lines, values, ChartUtils.COLORS[0]);

            List<PointValue> values1 = new ArrayList<PointValue>();
            for (int j = 0; j < historicList.size(); j++) {
                values1.add(new PointValue(Float.parseFloat(j + ""), Float
                        .parseFloat(historicList.get(
                                historicList.size() - j - 1).getValueSZY())));
            }
            initLines(lines, values1, ChartUtils.COLORS[2]);

            List<PointValue> values2 = new ArrayList<PointValue>();
            for (int j = 0; j < historicList.size(); j++) {
                if (historicList.get(historicList.size() - j - 1).getValueHR()
                        .length() != 0) {
                    values2.add(new PointValue(Float.parseFloat(j + ""), Float
                            .parseFloat(historicList.get(
                                    historicList.size() - j - 1).getValueHR())));
                }

            }
            initLines(lines, values2, ChartUtils.COLORS[4]);

        } else if (nowChooseType.equals(Constant.DETECTION_XUETANG)) {
            lines = new ArrayList<Line>();
            List<PointValue> values = new ArrayList<PointValue>();
            int m1 = 0;
            for (int j = 0; j < historicList.size(); j++) {
                if (historicList.get(historicList.size() - j - 1)
                        .getMonitorDataId() == KFXT) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getValueTWorSSY().length() != 0) {
                        if (!historicList.get(historicList.size() - j - 1)
                                .getValueTWorSSY().equals("0")) {
                            values.add(new PointValue(
                                    Float.parseFloat(m1 + ""), Float
                                    .parseFloat(historicList
                                            .get(historicList.size()
                                                    - j - 1)
                                            .getValueTWorSSY())));
                        }
                    }

                    m1 += 3;
                    xTimeList.add("");
                    xTimeList.add("");
                    xTimeList.add(historicList.get(historicList.size() - j - 1)
                            .getDate().substring(5));
                }
            }
            numberOfPoints = 21;
            resetViewport();
            initLines(lines, values, ChartUtils.COLORS[0]);
            long[] tempZCQ = {ZCQXT, WUCQXT, WANCQXT};
            List<PointValue> values1 = new ArrayList<PointValue>();
            for (int n = 0; n < 3; n++) {
                int m2 = n;
                for (int j = 0; j < historicList.size(); j++) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getMonitorDataId() == tempZCQ[n]) {
                        if (!historicList.get(historicList.size() - j - 1)
                                .getValueTWorSSY().equals("0")) {
                            values1.add(new PointValue(Float
                                    .parseFloat(m2 + ""), Float
                                    .parseFloat(historicList.get(
                                            historicList.size() - j - 1)
                                            .getValueTWorSSY())));
                        }
                        m2 += 3;
                    }
                }
            }

            initLines(lines, values1, ChartUtils.COLORS[2]);

            List<PointValue> values2 = new ArrayList<PointValue>();
            long[] tempZCH = {ZCHXT, WUCHXT, WANCHXT};
            for (int n = 0; n < 3; n++) {
                int m3 = n;
                for (int j = 0; j < historicList.size(); j++) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getMonitorDataId() == tempZCH[n]) {
                        if (!historicList.get(historicList.size() - j - 1)
                                .getValueTWorSSY().equals("0")) {
                            values2.add(new PointValue(Float
                                    .parseFloat(m3 + ""), Float
                                    .parseFloat(historicList.get(
                                            historicList.size() - j - 1)
                                            .getValueTWorSSY())));

                        }
                        m3 += 3;
                    }
                }
            }
            initLines(lines, values2, ChartUtils.COLORS[4]);

            List<PointValue> values3 = new ArrayList<PointValue>();
            int m4 = 0;
            for (int j = 0; j < historicList.size(); j++) {
                if (historicList.get(historicList.size() - j - 1)
                        .getMonitorDataId() == SQXT) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getValueTWorSSY().length() != 0) {
                        if (!historicList.get(historicList.size() - j - 1)
                                .getValueTWorSSY().equals("0")) {
                            values3.add(new PointValue(Float
                                    .parseFloat(m4 + ""), Float
                                    .parseFloat(historicList.get(
                                            historicList.size() - j - 1)
                                            .getValueTWorSSY())));
                        }
                    }

                    m4 += 3;
                }
            }

            initLines(lines, values3, ChartUtils.COLORS[3]);
        } else {
            // 其他值获取的方式一致
            lines = new ArrayList<Line>();
            for (int i = 0; i < 1; ++i) {

                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < historicList.size(); j++) {
                    if (historicList.get(historicList.size() - j - 1)
                            .getValueTWorSSY().length() != 0) {
                        values.add(new PointValue(Float.parseFloat(j + ""),
                                Float.parseFloat(historicList.get(
                                        historicList.size() - j - 1)
                                        .getValueTWorSSY())));
                        xTimeList.add(historicList
                                .get(historicList.size() - j - 1).getTime()
                                .substring(0, 5));
                    }

                }
                numberOfPoints = 7;// values.size();
                resetViewport();
                initLines(lines, values, ChartUtils.COLORS[i]);
            }

        }

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        for (int i = 0; i < xTimeList.size(); ++i) {
            axisValues.add(new AxisValue(i).setLabel(xTimeList.get(i)));
        }
        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Axis X");
                axisY.setName("Axis Y");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));

        chart.setLineChartData(data);
    }


    /**
     * 初始化每一个表曲线
     *
     * @param lines
     * @param values
     * @param color
     */
    private void initLines(List<Line> lines, List<PointValue> values, int color) {
        Line line = new Line(values);
        line.setColor(color);
        line.setShape(shape);
        line.setCubic(isCubic);
        line.setFilled(isFilled);
        line.setHasLabels(hasLabels);
        line.setHasLabelsOnlyForSelected(hasLabelForSelected);
        line.setHasLines(hasLines);
        line.setHasPoints(hasPoints);
        lines.add(line);
    }

    private String pickerLeftValue, pickerRightValue, pickerRightMostValue;

    CustomDialog tempDialog;

    /**
     * 获得历史数据的类
     */
    private class getHistoricDetection extends
            AsyncTask<Integer, Integer, String> {
        String result = "";

        // boolean choose;

        public getHistoricDetection() {
            // TODO Auto-generated constructor stub
        }

        @Override
        protected String doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientId", fansId + "");
            param.put("dataCode", dataCode);
            param.put("num", dataNum);
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/monitordata/record/history", param, "utf-8");
                Log.i("result", result);
                JSONObject data = new JSONObject(result);
                JSONObject historicDection = new JSONObject(
                        data.getString("data"));
                if (dataCode.equals("BP")) {
                    JSONObject historicDection_SSY = new JSONObject(
                            historicDection.getString("SSY"));
                    JSONObject historicDection_SZY = new JSONObject(
                            historicDection.getString("SZY"));
                    JSONObject historicDection_HR = new JSONObject(
                            historicDection.getString("XL"));
                    monitorDataId = historicDection_SSY
                            .getLong("monitorDataId");
                    unit = historicDection_SSY.getString("unit");
                    // dataCode=historicDection_SSY.getString("dataCode");
                    dataName = historicDection_SSY.getString("dataName");
                    refValueMax_TWorSSY = historicDection_SSY
                            .getLong("refValueMax");
                    refValueMin_TWorSSY = historicDection_SSY
                            .getLong("refValueMin");
                    refValueMax_SZY = historicDection_SZY
                            .getLong("refValueMax");
                    refValueMin_SZY = historicDection_SZY
                            .getLong("refValueMin");
                    JSONArray array_SSY = new JSONArray(
                            historicDection_SSY.getString("value"));
                    JSONArray array_SZY = new JSONArray(
                            historicDection_SZY.getString("value"));
                    JSONArray array_HR = new JSONArray(
                            historicDection_HR.getString("value"));
                    historicList.clear();
                    for (int i = 0; i < array_SSY.length(); i++) {
                        JSONObject object_SSY = array_SSY.getJSONObject(i);
                        JSONObject object_SZY = array_SZY.getJSONObject(i);
                        JSONObject object_HR = array_HR.getJSONObject(i);
                        BeanHistoricDetectionList hDList = new BeanHistoricDetectionList();
                        hDList.setDate(object_SSY.getString("date"));
                        hDList.setTime(object_SSY.getString("time"));
                        // days[6-i]=object_SSY.getString("time").substring(0,
                        // 5);
                        hDList.setValueTWorSSY(object_SSY.getString("value"));
                        hDList.setValueSZY(object_SZY.getString("value"));
                        hDList.setValueHR(object_HR.getString("value"));
                        historicList.add(hDList);
                    }
                } else if (dataCode.equals("XT")) {
                    ArrayList<JSONObject> historicDection_XT = new ArrayList<JSONObject>();
                    String[] tempXtType = {"KFXT", "ZCQXT", "ZCHXT", "WUCQXT",
                            "WUCHXT", "WANCQXT", "WANCHXT", "SQXT"};
                    for (int m = 0; m < tempXtType.length; m++) {
                        if (!historicDection.getString(tempXtType[m]).equals(
                                "null")) {
                            historicDection_XT.add(new JSONObject(
                                    historicDection.getString(tempXtType[m])));
                        }
                    }
                    // monitorDataId=historicDection_SSY.getLong("monitorDataId");
                    // unit=historicDection_KFXT.getString("unit");
                    // dataCode=historicDection_SSY.getString("dataCode");
                    // dataName=historicDection_KFXT.getString("dataName");
                    // refValueMax_TWorSSY=historicDection_SSY.getLong("refValueMax");
                    // refValueMin_TWorSSY=historicDection_SSY.getLong("refValueMin");
                    // refValueMax_SZY=historicDection_SZY.getLong("refValueMax");
                    // refValueMin_SZY=historicDection_SZY.getLong("refValueMin");
                    ArrayList<JSONArray> historicDection_XTData = new ArrayList<JSONArray>();
                    for (int i = 0; i < historicDection_XT.size(); i++) {
                        historicDection_XTData.add(new JSONArray(
                                historicDection_XT.get(i).getString("value")));
                    }

                    historicList.clear();
                    for (int j = 0; j < historicDection_XTData.size(); j++) {
                        for (int k = 0; k < historicDection_XTData.get(j)
                                .length(); k++) {
                            JSONObject object_XT = historicDection_XTData
                                    .get(j).getJSONObject(k);
                            BeanHistoricDetectionList hDList = new BeanHistoricDetectionList();
                            hDList.setDate(object_XT.getString("date"));
                            hDList.setTime(object_XT.getString("time"));
                            hDList.setMonitorDataId(KFXT + j);
                            // days[6-k]=object_XT.getString("time").substring(0,
                            // 5);
                            hDList.setValueTWorSSY(object_XT.getString("value"));
                            // hDList.setValueSZY(object_SZY.getString("value"));
                            // hDList.setValueHR(object_HR.getString("value"));
                            historicList.add(hDList);
                        }
                    }

                } else {
                    // 其他的值获取历史的方式相同
                    JSONObject historicDection_TW = new JSONObject(
                            historicDection.getString(dataCode));
                    JSONArray array_tw = new JSONArray(
                            historicDection_TW.getString("value"));
                    monitorDataId = historicDection_TW.getLong("monitorDataId");
                    unit = historicDection_TW.getString("unit");
                    // dataCode=historicDection_TW.getString("dataCode");
                    dataName = historicDection_TW.getString("dataName");
                    refValueMax_TWorSSY = historicDection_TW
                            .getLong("refValueMax");
                    refValueMin_TWorSSY = historicDection_TW
                            .getLong("refValueMin");
                    historicList.clear();
                    for (int i = 0; i < array_tw.length(); i++) {
                        JSONObject object_tw = array_tw.getJSONObject(i);
                        BeanHistoricDetectionList hDList_tw = new BeanHistoricDetectionList();
                        hDList_tw.setDate(object_tw.getString("date"));
                        hDList_tw.setTime(object_tw.getString("time"));
                        // days[6-i]=object_tw.getString("time").substring(0,
                        // 5);
                        hDList_tw.setValueTWorSSY(object_tw.getString("value"));
                        historicList.add(hDList_tw);
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                hideProgressBar();
                // showNetErrorInfo();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hideProgressBar();
            updateChartData();
            picker.setEnabled(true);
        }

    }


}
