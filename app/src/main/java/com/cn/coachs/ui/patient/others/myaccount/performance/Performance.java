/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount.performance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.detect.BeanHistoricDetectionList;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;

/**
 * @author kuangtiecheng
 */
public class Performance extends ActivityBasic {

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
    public final static String[] label = new String[]{"粉丝数", "人/月数",
            "发展会员", "发展秘书", "金币数", "总绩效"};
    public final static String[] days = new String[]{"周一", "周二", "周三", "周四",
            "周五", "周六", "周日",};
    private List<BeanHistoricDetectionList> historicList;
    private static String dataCode = "BP";
    private static String nowChooseType = Constant.DETECTION_XUEYA;

    private String dataNum = "7";// 获取历史数据个数
//	private View rootView;// 缓存Fragment view

    private UtilsSharedData sharedData;

    private ColumnChartView chart;
    private ColumnChartData data;
    private LineChartView chartTop;
    private LineChartData lineData;
    private boolean hasAxes = true;
    private boolean hasAxesNames = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean isFilled = true;
    private boolean hasLabels = false;
    private boolean isCubic = false;
    private boolean hasLabelForSelected = true;
    private int numberOfPoints = 7;
    private CharSequence[] detectType;
    private ListView listView;
    private List<Statistic> statistic = new ArrayList<Statistic>();
    private staticsticAdapter staAdapter;
    private Statistic addStatistic;
    private String mumbernum;
    private String mymembernum;
    private String serviceMonth;
    private String mishunum;
    private String gold;
    private String grade;
    private String[] achievementData = new String[]{};
    private PopMenu mpop;

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		if (rootView == null) {
//			rootView = inflater.inflate(R.layout.myaccount_my_performance, container,
//					false);
//		}
//		// 缓存的rootView需要判断是否已经被加过parent，
//		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
//		ViewGroup parent = (ViewGroup) rootView.getParent();
//		if (parent != null) {
//			parent.removeView(rootView);
//		}
//		return rootView;
//	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_my_performance);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("我的绩效");

        initial();
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex,
                                    SubcolumnValue value) {
            generateLineData(value.getColor(), 10);
        }

        @Override
        public void onValueDeselected() {

            generateLineData(ChartUtils.COLOR_GREEN, 0);

        }
    }

    private void generateLineData(int color, float range) {
        // Cancel last animation if not finished.
        chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = lineData.getLines().get(0);// For this example there is
        // always only one line.
        line.setColor(color);
        for (PointValue value : line.getValues()) {
            // Change target only for Y value.
            value.setTarget(value.getX(), (float) Math.random() * range);
        }
        chartTop.setValueSelectionEnabled(hasLabelForSelected);
        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(300);
    }

    private void resetViewport() {
        // Reset viewport height range to (0,100)
        Viewport v = new Viewport(0, 180, numberOfPoints - 1, 0);
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);

        if (data != null) {
            data.getColumns().clear();
            chart.setColumnChartData(data);
        }
    }

//	@Override
//	public void onViewCreated(View view, Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//		initial(view);
//	}

    private void initial() {
        // 将上次的旋转值缓存下来
        chart = (ColumnChartView) findViewById(R.id.menulist);
        chartTop = (LineChartView) findViewById(R.id.chart_top);
        listView = (ListView) findViewById(R.id.statistics);
        // Generate and set data for line chart
        generateInitialLineData();
        historicList = new ArrayList<BeanHistoricDetectionList>();
        showProgressBar();
        getHistoricDetection task_hisD = new getHistoricDetection();
        task_hisD.execute();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        AscyncGetAchievement getAchievement = new AscyncGetAchievement();
        getAchievement.execute();
    }

    private void generateInitialLineData() {
        int numValues = 7;

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0; i < numValues; ++i) {
            values.add(new PointValue(i, 0));
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set
        // viewports after data.
        Viewport v = new Viewport(0, 11, 6, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
        }
    }

    // 更新图表数据
    private void updateChartData() {

        resetViewport();
        if (historicList.size() == 0) {
            return;
        }

        int numSubcolumns = 1;
        // int numColumns = historicList.size();
        int numColumns = statistic.size();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        int[] color = new int[]{ChartUtils.COLOR_BLUE, ChartUtils.COLOR_GREEN, ChartUtils.COLOR_ORANGE, ChartUtils.COLOR_RED,
                ChartUtils.COLOR_VIOLET, ChartUtils.DEFAULT_DARKEN_COLOR};
        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < numSubcolumns; ++j) {
                // values.add(new
                // SubcolumnValue(Float.parseFloat(historicList.get(
                // historicList.size() - i - 1).getValueTWorSSY()),
                // ChartUtils.pickColor()));
//				values.add(new SubcolumnValue(Integer
//						.parseInt(achievementData[i] == null ? "0"
//								: achievementData[i]), ChartUtils.pickColor()));
                values.add(new SubcolumnValue(Integer
                        .parseInt(achievementData[i]), color[i]));

            }

            axisValues.add(new AxisValue(i).setLabel(label[i]));

            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        List<AxisValue> axisValues1 = new ArrayList<AxisValue>();
        for (int i = 0; i < getUpperLimit(achievementData); i++) {
            axisValues1.add(new AxisValue(i).setLabel(i + ""));
        }

        data = new ColumnChartData(columns);

        data.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3)
                .setValues(axisValues1));

        chart.setColumnChartData(data);

        // Set value touch listener that will trigger changes for chartTop.
        chart.setOnValueTouchListener(new ValueTouchListener());

        // Set selection mode to keep selected month column highlighted.
        chart.setValueSelectionEnabled(true);

        // chart.setZoomType(ZoomType.HORIZONTAL);

    }

    /**
     * 获取数据的平均数
     */
    private int getAverage(String[] arr) {
        if (arr != null) {
            if (arr.length < 1)
                return 0;
            int Sum = 0;
            for (String i : arr) {
                int item = Integer.parseInt(i);
                Sum = Sum + item;
            }
            return Sum / arr.length;
        }
        return -1;
    }

    /**
     * 获取数据的最大值
     */
    private int getMax(String[] arr) {
        int temp;
        int max;
        if (arr != null) {
            if (arr.length < 1)
                return 0;
            max = Integer.parseInt(arr[0]);
            for (int i = 1; i < arr.length; i++) {
                temp = Integer.parseInt(arr[i]);
                if (temp > max) {
                    max = temp;
                }
            }
            return max;
        }
        return -1;
    }

    /**
     * 获取纵坐标的上限
     */
    private int getUpperLimit(String[] arr) {
        if (arr != null) {
            if (arr.length < 1)
                return 10;
            return getMax(arr) > 2 * getAverage(arr) ? getMax(arr) : getAverage(arr);
        }
        return -1;
    }

    private void generateColumnData() {

        // chartBottom.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // SelectedValue sv = chartBottom.getSelectedValue();
        // if (!sv.isSet()) {
        // generateInitialLineData();
        // }
        //
        // }
        // });

    }

    private String pickerLeftValue, pickerRightValue;

    /**
     * 获得历史数据的类
     */
    private class getHistoricDetection extends
            AsyncTask<Integer, Integer, String> {
        String result;

        @Override
        protected String doInBackground(Integer... arg0) {
            // TODO Auto-generated method stub

            HashMap<String, String> param = new HashMap<String, String>();
            // UtilsSharedData.initDataShare(getActivity());// ////////
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            // 获取选中人员的历史数据
            // long userId =
            // AppMain.allFamilyMemberList.get(MainActivityFragme.selectDetetcMember).getMemberId();
            param.put("patientId", userId + "");
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
                    // for(int i=0;i<array_SSY.length();i++){
                    // JSONObject object_SSY=array_SSY.getJSONObject(i);
                    // JSONObject object_SZY=array_SZY.getJSONObject(i);
                    // JSONObject object_HR=array_HR.getJSONObject(i);
                    // historicDetectionList hDList=new historicDetectionList();
                    // hDList.setDate(object_SSY.getString("date"));
                    // hDList.setTime(object_SSY.getString("time"));
                    // hDList.setValueTWorSSY(object_SSY.getString("value"));
                    // hDList.setValueSZY(object_SZY.getString("value"));
                    // hDList.setValueHR(object_HR.getString("value"));
                    // historicList.add(hDList);
                    // }
                    for (int i = 0; i < 7; i++) {

                        BeanHistoricDetectionList hDList = new BeanHistoricDetectionList();
                        hDList.setDate(Math.random() * 110 + "");
                        hDList.setTime(Math.random() * 110 + "");
                        hDList.setValueTWorSSY(Math.random() * 110 + "");
                        hDList.setValueSZY(Math.random() * 110 + "");
                        hDList.setValueHR(Math.random() * 110 + "");
                        historicList.add(hDList);
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
        }

    }

    private class AscyncGetAchievement extends
            AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            // TODO Auto-generated method stub
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("expertId", sharedData.getLong(Constant.USER_ID, 0) + "");
            try {
                String url = AbsParam.getBaseUrl() + "/performance/detail";
                String retString = NetTool.sendPostRequest(url, param,
                        "utf-8");
                JSONObject json = new JSONObject(retString);
                mymembernum = json.getString("mymembernum");
                mumbernum = json.getString("membernum");
                mishunum = json.getString("mishunum");
                serviceMonth = json.getString("servicemonth");
                gold = json.getString("gold");

                grade = json.getString("grade");

            } catch (Exception e) {
                // TODO: handle exception
                mymembernum = nullToZero(mymembernum);
                mumbernum = nullToZero(mumbernum);
                mishunum = nullToZero(mishunum);
                serviceMonth = nullToZero(serviceMonth);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hideProgressBar();
            gold = stringHandle(gold);
            grade = stringHandle(grade);
            if (!mymembernum.equals("0")) {
                statistic.clear();
            }
            addStatistic = new Statistic("粉丝数", mymembernum + "人");
            statistic.add(addStatistic);
            addStatistic = new Statistic("人/月数", serviceMonth + "人/月");
            statistic.add(addStatistic);
            addStatistic = new Statistic("发展会员", mumbernum + "人");
            statistic.add(addStatistic);
            addStatistic = new Statistic("发展秘书", mishunum + "人");
            statistic.add(addStatistic);
            addStatistic = new Statistic("金币数", gold + "个");
            statistic.add(addStatistic);
            addStatistic = new Statistic("总绩效", grade);
            statistic.add(addStatistic);
            achievementData = new String[]{mymembernum, serviceMonth,
                    mumbernum, mishunum, gold, grade};
            if (staAdapter == null) {
                staAdapter = new staticsticAdapter(getApplicationContext(), statistic);
                listView.setAdapter(staAdapter);
            } else {
                staAdapter.notifyDataSetChanged();
            }
            updateChartData();
        }

        /**
         * 对字符串化的整数进行四舍五入取整运算
         *
         * @param String
         * @return String
         */
        private String stringHandle(String str) {
            if (str == null)
                return "0";
            String[] arr = str.split("\\.");
            if (arr.length > 0) {
                String tempNum = arr[0];
                //四舍五入
                if (Double.parseDouble(str) - Double.parseDouble(tempNum) < 0.5) {
                    return tempNum;
                }
                return String.valueOf(Integer.parseInt(tempNum) + 1);
            }
            return "-1";
        }

        private String nullToZero(String str) {
            if (str == null)
                return "0";
            return str;
        }
    }
}
