package com.cn.coachs.ui.patient.main.healthdiary;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.healthdiary.BeanIntervNode;
import com.cn.coachs.model.healthdiary.BeanInterventionLogResult;
import com.cn.coachs.model.healthdiary.BeanResultOfEvaluation;
import com.cn.coachs.model.healthdiary.BeanSummaryResult;
import com.cn.coachs.model.myaccount.BeanFans;
import com.cn.coachs.ui.AppPool;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.basic.IfaceDialog;
import com.cn.coachs.ui.chat.common.utils.ToastUtil;
import com.cn.coachs.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.coachs.ui.patient.main.healthdiary.datepicker.SliderContainer;
import com.cn.coachs.ui.patient.main.healthdiary.datepicker.SliderContainer.OnTimeChangeListener;
import com.cn.coachs.ui.patient.others.myaccount.AsyncFansList;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ActivityHealthDiary extends ActivityBasic implements IfaceDialog,
        OnClickListener {
    Calendar mInitialTime;
    Calendar minTime, maxTime;
    static SliderContainer mContainer;
    TextView date;
    // ListView contentList;
    // MyAdapter adapter;
    // ImageView todaySummarize,todayRemind,todayLocation;
    int minuteInterval = 1;
    String dataData = "2015-04-24";
    String tomorrowData = "";
    boolean firstIn = true;
    private String summary;
    private ImageView increaseMonthBtn, decreaseMonthBtn;
    private ImageView increaseMember, decreaseMember;
    private TextView person;// ,introduction;
    private EmojiconTextView tv_Communication;
    // private static int selectMember = 0;
    // private LinearLayout noDiaryLayout;//,noDiaryLayoutContent;
    // private RelativeLayout noDiaryLayoutContent;
    // private FButton btnStartTest;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    public static Long valueId, nodeItemId, valueId_DBP, valueId_HR,
            nodeItemId_DBP, nodeItemId_HR;
    public static String detectionType;
    private int pageType;
    /**
     * true表示选择成功，false表示选择失败
     */
    // private Typeface typeface;
    String[] listTips;
    Random random;
    public DiaryItem diary_item_one, diary_item_three;
    public DiaryItemRight diary_item_two, diary_item_four;
    // public ArrayList<String> listId;
    public final static String LIST = "listFans";
    // public ArrayList<BeanFans> listNewFans,listUntalkFans;
    public SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    public String dateStr;// AsyncFansList中使用的日期，请求该日期下的爱护数和粉丝数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_healthdiary);
        imageInit();
        initView();
        lastChooseTime = dataData;
        new Thread(runnableHealthDiary).start();
    }

    private void imageInit() {
        options = new DisplayImageOptions.Builder()
                // .showStubImage(R.drawable.my_videos)
                // .showImageForEmptyUri(R.drawable.my_videos)
                // .showImageOnFail(R.drawable.my_videos)
                .cacheInMemory(true).cacheOnDisc(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // showProgressBar();
        // new Thread(runnableHealthDiary).start();
        // GetMyStausAsyncTask mTask = new GetMyStausAsyncTask();
        // mTask.execute();
        initDiaryItem();
    }

    private void initView() {
        // typeface = Typeface.createFromAsset(getAssets(), "fangzheng.ttf");
        UtilsSharedData.initDataShare(this);
        AppPool.createActivity(ActivityHealthDiary.this);

        // tv_Communication=(EmojiconTextView)findViewById(R.id.CommunicationWithSecretary);
        // tv_Communication.setTypeface(typeface);
        // 设置今天的日期
        // btnStartTest = (FButton)findViewById(R.id.add_diary_btn);
        // btnStartTest.setCornerRadius(3);
        // noDiaryLayout = (LinearLayout)findViewById(R.id.no_diary_layout);
        // noDiaryLayoutContent =
        // (RelativeLayout)findViewById(R.id.no_diary_layout_content);

        Calendar c = Calendar.getInstance();
        mInitialTime = c;
        mContainer = (SliderContainer) findViewById(R.id.dateSliderContainer);
        date = (TextView) findViewById(R.id.health_date);
        person = (TextView) findViewById(R.id.person);
        // mContainer.setBackgroundColor(getResources().getColor(R.color.whitesmoke));
        mContainer.setOnTimeChangeListener(onTimeChangeListener);
        mContainer.setMinuteInterval(minuteInterval);
        if (mContainer.getTime() == null) {
            mContainer.setTime(mInitialTime);
            mContainer.setMaxTime(mInitialTime);
        }

        increaseMonthBtn = (ImageView) findViewById(R.id.increasemonth);
        decreaseMonthBtn = (ImageView) findViewById(R.id.decreasemonth);
        increaseMember = (ImageView) findViewById(R.id.increaseperson);
        decreaseMember = (ImageView) findViewById(R.id.decreaseperson);
        increaseMonthBtn.setOnClickListener(this);
        decreaseMonthBtn.setOnClickListener(this);
        increaseMember.setOnClickListener(this);
        decreaseMember.setOnClickListener(this);

        dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());// 初始化日期参数，今天
        // btnStartTest.setOnClickListener(this);
        // todaySummarize =
        // (ImageView)findViewById(R.id.healthy_dairy_summarize);
        // todaySummarize.setOnClickListener(this);
        // todayRemind = (ImageView)findViewById(R.id.healthy_dairy_remind);
        // todayRemind.setOnClickListener(this);
        // todayLocation = (ImageView)findViewById(R.id.healthy_dairy_location);
        // todayLocation.setOnClickListener(this);

        // contentList = (ListView) findViewById(R.id.health_content);
        // adapter = new MyAdapter(getApplicationContext());
        // contentList.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> parent, View view,
        // int position, long id) {
        // view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
        // R.anim.icon_scale));
        // // if(intervNodeList.get(position).getContentList().size()!=0){
        // // Intent intent = new
        // // Intent(HealthDiaryActivity.this,ActivityHealthTraningGuide.class);
        // // intent.putExtra(Constant.PAGE_TYPE, position);
        // // startActivity(intent);
        // // }else
        // // Toast.makeText(getActivity(), position, 1).show();
        // if (intervNodeList.get(position).getIntervItemsList().size() != 0) {
        // if ((Calendar.getInstance().getTimeInMillis() / (24 * 60 * 1000 *
        // 60)) == (mContainer
        // .getTime().getTimeInMillis() / (24 * 60 * 1000 * 60))) {
        // //设置选中的人员为当前用户
        // // if(selectMember == 0){
        // //只有当前用户自己才可以进行测量
        // // TabActivityMain.selectDetetcMember = 0;
        // // ActivityDetectionCenter.whetherFromDiary=true;
        // //
        // detectionType=intervNodeList.get(position).getIntervItemsList().get(0).getCode();
        // //
        // valueId=Long.valueOf(intervNodeList.get(position).getIntervItemsList().get(0).getId());
        // //
        // nodeItemId=Long.valueOf(intervNodeList.get(position).getIntervItemsList().get(0).getNodeItemId());
        // // if(detectionType.endsWith("SSY")){
        // //
        // valueId_DBP=Long.valueOf(intervNodeList.get(position).getIntervItemsList().get(position+1).getId());
        // //
        // nodeItemId_DBP=Long.valueOf(intervNodeList.get(position).getIntervItemsList().get(position+1).getNodeItemId());
        // //
        // valueId_HR=Long.valueOf(intervNodeList.get(position).getIntervItemsList().get(position+2).getId());
        // //
        // nodeItemId_HR=Long.valueOf(intervNodeList.get(position).getIntervItemsList().get(position+2).getNodeItemId());
        // // }else{
        // // valueId_DBP=0l;
        // // valueId_HR=0l;
        // // nodeItemId_DBP=0l;
        // // nodeItemId_HR=0l;
        // // }
        // // ((TabActivityMain)getActivity()).mTabHost.setCurrentTab(0);
        // // }
        // Intent intent1 = new Intent();
        // intent1.setClass(getApplicationContext(),
        // ActivityDetectionCenter.class);
        // startActivity(intent1);
        // } else if (intervNodeList.get(position).getContentList()
        // .size() > 0) {
        // Intent intent = new Intent(getApplicationContext(),
        // ActivityHealthTraningGuide.class);
        // intent.putExtra(Constant.PAGE_TYPE, position);
        // startActivity(intent);
        // }
        // } else {
        // Intent intent = new Intent(getApplicationContext(),
        // ActivityHealthTraningGuide.class);
        // intent.putExtra(Constant.PAGE_TYPE, position);
        // startActivity(intent);
        // }
        // }
        // });

        // listTips =
        // getResources().getStringArray(R.array.health_diary_random_tips);
        // random = new Random(listTips.length-1);
        initDiaryItemWiget();
        // initDiaryItem();
    }

    public void initDiaryItemWiget() {
        diary_item_one = (DiaryItem) findViewById(R.id.diary_item_one);
        diary_item_one.setOnClickListener(this);

        diary_item_two = (DiaryItemRight) findViewById(R.id.diary_item_two);
        diary_item_two.setOnClickListener(this);

        diary_item_three = (DiaryItem) findViewById(R.id.diary_item_three);
        diary_item_three.setOnClickListener(this);

        diary_item_four = (DiaryItemRight) findViewById(R.id.diary_item_four);
        diary_item_four.setOnClickListener(this);

        // listNewFans = new ArrayList<BeanFans>();
        // listUntalkFans = new ArrayList<BeanFans>();
    }

    // 初始化日记项目，9点之后前两条不能点击
    public void initDiaryItem() {
        final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        System.out.println("=-=-=hour=-=-=" + hour);

        AsyncFansList asyncFanList = new AsyncFansList(this, dateStr) {
            @Override
            protected void onPostExecute(JSONObject result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                // hideProgressBar();
                if (result != null) {
                    try {
                        System.out.println(mContainer.getTime().getTimeInMillis() + "?" + Calendar.getInstance());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        String showDate = sdf.format(mContainer.getTime().getTime());
                        String today = sdf.format(Calendar.getInstance().getTime());
                        System.out.println(showDate + "?" + today);

                        if (Integer.parseInt(showDate) < Integer.parseInt(today)) {
                            setItemsUnClickable(false);
                        } else {
                            setItemsUnClickable(true);// 恢复可点击性
                        }
                        Log.e(TAG,
                                "=-=-=unTalkCounts=-=-="
                                        + result.getString("unTalkCounts"));
                        Log.e(TAG,
                                "=-=-=newFansCounts=-=-="
                                        + result.getString("newFansCounts"));
                        Log.e(TAG, "=-=-=result=-=-=" + result);
                        diary_item_three.setNum(result
                                .getString("unTalkCounts"));
                        diary_item_four.setNum(result
                                .getString("newFansCounts"));
                        diary_item_two.setNum(result.getString("unTalkCounts"));
                        diary_item_one
                                .setNum(result.getString("newFansCounts"));
                        if (hour > 9) {
                            diary_item_one.setLinearClickable(false);
                            diary_item_two.setLinearClickable(false);
                            if (result.getString("newFansCounts").equals("0")) {
                                diary_item_four.setLinearClickable(false);
                            }
                            if (result.getString("unTalkCounts").equals("0")) {
                                diary_item_three.setLinearClickable(false);
                            }
                        } else {
                            diary_item_three.setLinearClickable(false);
                            diary_item_four.setLinearClickable(false);
                            if (result.getString("unTalkCounts").equals("0")) {
                                diary_item_two.setLinearClickable(false);
                            }
                            if (result.getString("newFansCounts").equals("0")) {
                                diary_item_one.setLinearClickable(false);
                            }
                        }

                        String newFansInfo = result.getString("newFansInfo");
                        System.out.println("=-=-=newFansInfo=-=-="
                                + newFansInfo);
                        String unTalkpatientInfo = result
                                .getString("unTalkpatientInfo");
                        System.out.println("=-=-=unTalkpatientInfo=-=-="
                                + unTalkpatientInfo);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    setItemsUnClickable(false);
                }
            }
        };
        asyncFanList.execute();
        // showProgressBar();
    }

    public void setItemsUnClickable(boolean flag) {
        diary_item_four.setLinearClickable(flag);
        diary_item_three.setLinearClickable(flag);
        diary_item_two.setLinearClickable(flag);
        diary_item_one.setLinearClickable(flag);
    }

    public ArrayList<BeanFans> getList(JSONArray array)
            throws NumberFormatException, JSONException {
        ArrayList<BeanFans> list = new ArrayList<BeanFans>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                BeanFans bean = new BeanFans();
                JSONObject json = array.getJSONObject(i);
                bean.setAge(json.getString("age"));
                bean.setName(json.getString("memberName"));
                bean.setHeadImgUrl(json.getString("imgUrl"));
                bean.setSex(json.getString("sex"));
                bean.setContactid(json.getString("phone"));
                bean.setPaitentID(json.getLong("patientID"));
                bean.setHeight(json.getString("height"));
                bean.setWeight(json.getString("weight"));
                list.add(bean);
            }
            return list;
        }
        return null;
    }

    String lastChooseTime = "";
    // 时间改变提示类
    private OnTimeChangeListener onTimeChangeListener = new OnTimeChangeListener() {
        public void onTimeChange(Calendar time) {
            if (date != null) {
                final Calendar c = mContainer.getTime();
                date.setText(String.format("%tY" + "年" + " %tB", c, c));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dataData = sdf.format(c.getTime());
                // long t=c.getTimeInMillis();
                // t=t+86400;
                // Date date=new Date(t);
                // tomorrowData=sdf.format(date);
                System.out.println("时间：" + dataData);
                if (mContainer.isFinishScroller()) {
                    dateStr = dataData;// 停止滑动是读出当前日期
                    System.out.println("=-=-=dateStr=-=-=" + dateStr);
                    initDiaryItem();
                }
            }
        }
    };

    private boolean threadIsRunning = false;

    int lastScrollX = 0;
    Runnable checkScrollFinished = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            threadIsRunning = true;
            while (true) {
                if (mContainer.getCurrentX() == lastScrollX) {
                    // if (mContainer.isFinishScroller()) {
                    Message msg = scrollHandler.obtainMessage();
                    msg.what = 1;
                    scrollHandler.sendMessage(msg);
                    break;
                    // }
                } else {
                    lastScrollX = mContainer.getCurrentX();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            threadIsRunning = false;
        }
    };

    Handler scrollHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            judgeCanSummaryOrNot();
            lastChooseTime = dataData;
            showProgressBar();
            new Thread(runnableHealthDiary).start();
            super.handleMessage(msg);
        }

    };

    private void judgeCanSummaryOrNot() {
        if ((Calendar.getInstance().getTimeInMillis() / (24 * 60 * 1000 * 60)) < (mContainer
                .getTime().getTimeInMillis() / (24 * 60 * 1000 * 60))) {
            // 今天之后的日期不允许提交总结
            // todaySummarize.setVisibility(View.INVISIBLE);
        } else {
            // if(selectMember!=0){
            // //非当前人员不能填写总结
            // todaySummarize.setVisibility(View.INVISIBLE);
            // }else{
            // todaySummarize.setVisibility(View.VISIBLE);
            // }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        // TODO Auto-generated method stub
        Intent intent;
        switch (v.getId()) {
            case R.id.diary_item_one:
                System.out.println("=-=-=1");
                intent = new Intent(getApplicationContext(), ActivityFansList.class);
                // intent.putParcelableArrayListExtra(LIST, listNewFans);
                intent.putExtra(LIST, 1);
                startActivity(intent);
                break;
            case R.id.diary_item_two:
                intent = new Intent(getApplicationContext(), ActivityFansList.class);
                // intent.putParcelableArrayListExtra(LIST, listUntalkFans);
                intent.putExtra(LIST, 2);
                startActivity(intent);
                System.out.println("=-=-=1");
                break;
            case R.id.diary_item_three:
                // diary_item_three.setLinearClickable(false);
                intent = new Intent(getApplicationContext(), ActivityFansList.class);
                // intent.putParcelableArrayListExtra(LIST, listUntalkFans);
                intent.putExtra(LIST, 3);
                startActivity(intent);
                System.out.println("=-=-=1");
                break;
            case R.id.diary_item_four:
                intent = new Intent(getApplicationContext(), ActivityFansList.class);
                // intent.putParcelableArrayListExtra(LIST, listNewFans);
                intent.putExtra(LIST, 4);
                startActivity(intent);
                System.out.println("=-=-=1");
                break;

            // case R.id.healthy_dairy_summarize:
            // showSummarizeDialog();
            // break;
            // case R.id.healthy_dairy_location:
            // intent = new Intent(this, ActivityMyFamilyMemberPage.class);
            // intent.putExtra(Constant.SELECT_MEMBER, 0);
            // startActivity(intent);
            // break;
            // case R.id.healthy_dairy_remind:
            // // intent = new Intent(getActivity(), ActivityRecoveryRemind.class);
            // // intent = new Intent(getActivity(), ChooseRecovery.class);
            // // startActivity(intent);
            // showRemindDialog();
            // break;
            case R.id.increasemonth:
                // 增加月份
                if (calendarEnd == null)
                    return;
                Calendar c = mContainer.getTime();
                c.add(Calendar.MONTH, 1);
                if (c.compareTo(calendarEnd) == 1) {
                    // 比干预方案最大日期还大了，就不再增加了
                    c.add(Calendar.MONTH, -1);
                    return;
                }
                mContainer.setTime(c);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                dataData = sdf.format(c.getTime());
                judgeCanSummaryOrNot();
                showProgressBar();
                new Thread(runnableHealthDiary).start();
                break;
            case R.id.decreasemonth:
                // 增加月份
                if (calendarStart == null)
                    return;
                Calendar c1 = mContainer.getTime();
                c1.add(Calendar.MONTH, -1);
                if (c1.compareTo(calendarStart) == -1) {
                    // 比干预方案最小日期还小了，就不再减少了
                    c1.add(Calendar.MONTH, 1);
                    return;
                }
                mContainer.setTime(c1);
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                dataData = sdf1.format(c1.getTime());
                judgeCanSummaryOrNot();
                showProgressBar();
                new Thread(runnableHealthDiary).start();
                // 减少月份
                break;
            // case R.id.increaseperson:
            // if(selectMember >= AppMain.allFamilyMemberList.size()-1){
            // selectMember = 0;
            // }else{
            // selectMember++;
            // }
            // judgeCanSummaryOrNot();
            // mContainer.setTime(Calendar.getInstance());
            // person.setText(StringUtil.displayName(AppMain.allFamilyMemberList.get(selectMember)));
            // showProgressBar();
            // new Thread(runnableHealthDiary).start();
            // break;
            // case R.id.decreaseperson:
            // if(selectMember == 0){
            // selectMember = AppMain.allFamilyMemberList.size()-1;
            // }else{
            // selectMember--;
            // }
            // judgeCanSummaryOrNot();
            // mContainer.setTime(Calendar.getInstance());
            // person.setText(StringUtil.displayName(AppMain.allFamilyMemberList.get(selectMember)));
            // showProgressBar();
            // new Thread(runnableHealthDiary).start();
            // break;
            // case R.id.add_diary_btn:
            // judgeJumpToWhere();
            // break;
            default:
                break;
        }
    }

    // private void judgeJumpToWhere(){
    // Intent intent1;
    // System.out.println("=-=-=pageType=-=-="+pageType);
    // switch (pageType) {
    // case 0:
    // //完成注册
    // intent1 = new Intent(getApplicationContext(),
    // ActivityTestPersonInfo.class);
    // startActivity(intent1);
    // break;
    // case 1:
    // intent1 = new Intent(getApplicationContext(),
    // ActivityEvaluationCenter.class);
    // startActivity(intent1);
    // //完成个人信息填写
    // break;
    // case 2:
    // //完成初始测评
    // //选择专家
    // startActivity(ActivityHealthSecretaryList.class);
    // break;
    // case 3:
    // //选完专家,进行二次测评
    // // Intent intent = new Intent(this, ActivityEvaluationSecond.class);
    // // startActivity(intent);
    // // break;
    // case 4:
    // // TabActivityMain.changePage(R.id.tab_iv_2);
    // //专家测评完，点击逛逛驿站
    // break;
    // case 5:
    // //方案开始执行
    // showProgressBar();
    // new Thread(runnableHealthDiary).start();
    // break;
    //
    // default:
    // break;
    // }
    // }

    /**
     * @return 嵌有datapicker的对话框，用于弹出一个datapicker，让用户选择日期；
     * @author Hubert_WZP
     */
    // private void choseStartDayDialog() {
    // LayoutInflater inflater = LayoutInflater.from(this);
    // LinearLayout layout =
    // (LinearLayout)inflater.inflate(R.layout.datapicker,null);
    // DatePicker DP=(DatePicker)layout.findViewById(R.id.datapicker_dialog);
    // Calendar calendar=Calendar.getInstance();
    // startDate = calendar.get(Calendar.YEAR) + "-"
    // + calendar.get(Calendar.MONTH) + "-"
    // + calendar.get(Calendar.DAY_OF_MONTH);
    // DP.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
    // calendar.get(Calendar.DAY_OF_MONTH),
    // new OnDateChangedListener() {
    //
    // @Override
    // public void onDateChanged(DatePicker view, int year,
    // int monthOfYear, int dayOfMonth) {
    // // TODO Auto-generated method stub
    // monthOfYear=monthOfYear+1;
    // startDate=year+"-"+monthOfYear+"-"+dayOfMonth;
    // Toast.makeText(getActivity(), startDate, 1).show();
    // }
    // });
    // CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
    // builder.setTitle("提示");
    // builder.setContentView(layout);
    // builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // // Toast.makeText(context, text, duration)
    // // AscyncChoseHealthDiary choseHD=new AscyncChoseHealthDiary(planId);
    // // choseHD.execute();
    // dialog.dismiss();
    // }
    // });
    // builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(DialogInterface dialog, int which) {
    // // AscyncChoseHealthDiary choseHD=new AscyncChoseHealthDiary(planId);
    // // choseHD.execute();
    // dialog.dismiss();
    // }
    // });
    // builder.create().show();
    //
    //
    // }
    // 弹出总结弹窗
    @SuppressLint("ResourceAsColor")
    private void showSummarizeDialog() {
        final EditText inputServer = new EditText(this);
        inputServer.setHint("请填写今日小结");
        inputServer.setMinLines(8);
        if (healthDiaryBean != null) {
            if (healthDiaryBean.getNodes().size() != 0) {
                inputServer.setText(healthDiaryBean.getNodes().get(0)
                        .getSummary());
            }
        }
        inputServer.setGravity(Gravity.TOP);
        inputServer.setSingleLine(false);
        inputServer.setHintTextColor(R.color.font_gray);
        inputServer.setTextColor(Color.BLACK);
        inputServer.setHorizontalScrollBarEnabled(false);
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("提示");
        builder.setContentView(inputServer);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                summary = inputServer.getText().toString();
                new Thread(runnableSummaryResult).start();
                showProgressBar();
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

    /*******************************************************
     * 获取当天康复日记内容
     */
    private Map<String, String> map;
    private String retStr;// 获取到返回的json数据
    public static BeanInterventionLogResult healthDiaryBean;
    // public static BeanIntervItem detectionNodeBean;
    // 网络请求
    Runnable runnableHealthDiary = new Runnable() {
        private String url = AbsParam.getBaseUrl() + "/interv/plan/show";

        @Override
        public void run() {
            try {
                long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
                map = new HashMap<String, String>();
                map.put("patientId", userId + "");// 表示患者用户id
                map.put("planId", "");// 查找最新的干预方案
                map.put("days", "1");// 查询一天的干预方案
                map.put("startDate", dataData);// 查询开始日期
                retStr = NetTool.sendPostRequest(url, map, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
                System.out.println("=-=-=retStr=-=-=" + retStr);
                getHealthDiaryInfo(retStr);
                // getDetectionNode(retStr);
                // Toast.makeText(getActivity(), detectionNodeBean.getCode(),
                // Toast.LENGTH_SHORT).show();
                Message msg = new Message();
                if (retStr == null) {
                    msg.what = Constant.FAIL;
                } else {
                    msg.what = Constant.COMPLETE;
                    pageType = 6;
                }
                healthDiaryHandler.sendMessage(msg);

            } catch (Exception e) {
                healthDiaryHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        hideProgressBar();
                        showNetErrorInfo();
                    }
                }, 1000);
            }
        }
    };

    private Handler healthDiaryHandler = new Handler() {

        public void handleMessage(Message m) {
            switch (m.what) {
                case Constant.COMPLETE:
                    // 展示获取的数据bean
                    hideProgressBar();
                    firstIn = false;
                    if (healthDiaryBean != null) {
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                        String end = healthDiaryBean.getPlanEndDate();
                        String request = healthDiaryBean.getStartDate();
                        long compare = 0;
                        try {
                            compare = sf.parse(end).getTime()
                                    - sf.parse(request).getTime();
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        if (compare < 0) {
                            // 请求时间大于结束时间也不显示数据界面
                            displayOrNotIndicate(true);
                        } else {
                            // 有健康档案数据，则显示健康日记界面
                            displayOrNotIndicate(false);
                            updateUI();
                        }
                    } else {
                        displayOrNotIndicate(true);
                    }
                    break;

                default:

                    break;
            }
        }
    };

    private void displayOrNotIndicate(boolean visible) {
        if (visible) {
            // noDiaryLayout.setVisibility(View.VISIBLE);
            // noDiaryLayoutContent.setVisibility(View.VISIBLE);
            // date.setVisibility(View.INVISIBLE);
            // increaseMonthBtn.setVisibility(View.INVISIBLE);
            // decreaseMonthBtn.setVisibility(View.INVISIBLE);
            // if(selectMember==0){
            // btnStartTest.setVisibility(View.VISIBLE);
            // }else{
            // btnStartTest.setVisibility(View.INVISIBLE);
            // }
        } else {
            // noDiaryLayout.setVisibility(View.GONE);
            // noDiaryLayoutContent.setVisibility(View.GONE);
            date.setVisibility(View.VISIBLE);
            increaseMonthBtn.setVisibility(View.VISIBLE);
            decreaseMonthBtn.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void getHealthDiaryInfo(String jsonString) throws Exception {
        Gson gson = new Gson();
        healthDiaryBean = gson.fromJson(jsonString,
                BeanInterventionLogResult.class);
    }

    // private void getDetectionNode(String jsonString) throws Exception {
    // Gson gson=new Gson();
    // detectionNodeBean=gson.fromJson(jsonString, BeanIntervItem.class);
    // }

    Calendar calendarStart, calendarEnd;

    private void updateTimePicker() {
        // 设置最大时间
        String startTime, endTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        startTime = healthDiaryBean.getPlanStartDate();
        endTime = healthDiaryBean.getPlanEndDate();
        Date dateStart = null;
        Date dateEnd = null;
        try {
            dateStart = sdf.parse(startTime);
            dateEnd = sdf.parse(endTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        calendarStart = Calendar.getInstance();
        calendarStart.setTime(dateStart);
        calendarEnd = Calendar.getInstance();
        calendarEnd.setTime(dateEnd);
        if (calendarStart != null)
            mContainer.setMinTime(calendarStart);
        if (calendarEnd != null)
            mContainer.setMaxTime(calendarEnd);
        mContainer.refreshDrawableState();
    }

    /**
     * 更新界面
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    List<BeanIntervNode> intervNodeList;

    private void updateUI() {
        updateTimePicker();
        if (healthDiaryBean.getNodes().size() != 0) {
            intervNodeList = healthDiaryBean.getNodes().get(0).getNodes();

            // contentList.setAdapter(adapter);
            // adapter.notifyDataSetChanged();
        } else {
            intervNodeList = null;
            // contentList.setAdapter(adapter);
            // adapter.notifyDataSetChanged();
        }
    }

    /*******************************************************
     * 提交当日总结
     */
    private Map<String, String> map1;
    private String retStr1;// 获取到返回的json数据
    private BeanSummaryResult beanSummaryResult;
    // 网络请求
    Runnable runnableSummaryResult = new Runnable() {
        private String url = AbsParam.getBaseUrl()
                + "/interv/plan/dailysummary";

        // private String
        // url="http://192.168.12.112:8080/serviceplatform/interv/plan/dailysummary";
        @Override
        public void run() {
            try {
                map1 = new HashMap<String, String>();
                map1.put("patientPlanId",
                        String.valueOf(healthDiaryBean.getPatientPlanId()));// 干预方案Id
                map1.put("summary", summary);// 查询一天的干预方案
                retStr1 = NetTool.sendPostRequest(url, map1, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
                getSummaryResult(retStr1);

                Message msg = new Message();
                if (retStr1 == null) {
                    msg.what = Constant.FAIL;
                } else {
                    msg.what = Constant.COMPLETE;
                }
                summaryHandler.sendMessage(msg);

            } catch (Exception e) {
                summaryHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        hideProgressBar();
                        showNetErrorInfo();
                    }
                }, 1000);
            }
        }
    };

    private Handler summaryHandler = new Handler() {

        public void handleMessage(Message m) {
            switch (m.what) {
                case Constant.COMPLETE:
                    // 展示获取的数据bean
                    hideProgressBar();
                    ToastUtil.showMessage("提交成功");
                    break;

                default:

                    break;
            }
        }
    };

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void getSummaryResult(String jsonString) throws Exception {
        Gson gson = new Gson();
        beanSummaryResult = gson.fromJson(jsonString, BeanSummaryResult.class);
    }

    public final class ViewHolder {
        public ImageView img;
        public TextView time;
        public TextView title;
        public TextView tips;
        private FrameLayout pic_layout;
    }

	/*
     * public class MyAdapter extends BaseAdapter {
	 * 
	 * private LayoutInflater mInflater;
	 * 
	 * public MyAdapter(Context healthDiaryFragment) { this.mInflater =
	 * LayoutInflater.from(healthDiaryFragment); }
	 * 
	 * @Override public int getCount() { // TODO Auto-generated method stub int
	 * count = 0; if (intervNodeList != null) { count = intervNodeList.size(); }
	 * return count; }
	 * 
	 * @Override public Object getItem(int arg0) { // TODO Auto-generated method
	 * stub return intervNodeList.get(arg0); }
	 * 
	 * @Override public long getItemId(int arg0) { // TODO Auto-generated method
	 * stub return arg0; }
	 * 
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) {
	 * 
	 * ViewHolder holder = null; // if (convertView == null) { holder = new
	 * ViewHolder(); if (position % 2 == 0) { convertView = mInflater.inflate(
	 * R.layout.listitem_historydiary_left, null); } else { convertView =
	 * mInflater.inflate( R.layout.listitem_historydiary_right, null);
	 * 
	 * } holder.img = (ImageView) convertView.findViewById(R.id.health_pic);
	 * holder.time = (TextView) convertView.findViewById(R.id.health_time);
	 * holder.title = (TextView) convertView .findViewById(R.id.health_desc);
	 * holder.tips = (TextView) convertView .findViewById(R.id.health_tips);
	 * holder.pic_layout =
	 * (FrameLayout)convertView.findViewById(R.id.pic_layout);
	 * holder.img.setScaleType(ScaleType.FIT_XY); if (intervNodeList != null) {
	 * if (intervNodeList.get(position).getState() == 7) {
	 * holder.title.setText("今日训练(已完成)"); } if
	 * (intervNodeList.get(position).getContentList().size() != 0) { String
	 * imgURL = AbsParam.getBaseUrl()+intervNodeList.get(position)
	 * .getContentList().get(0).getThumbnail(); imageLoader.displayImage(imgURL,
	 * holder.img, options); } else if
	 * (intervNodeList.get(position).getIntervItemsList() .size() != 0) {
	 * List<BeanIntervItem> intervItemsList = intervNodeList.get(
	 * position).getIntervItemsList(); //隐藏视频界面
	 * holder.pic_layout.setVisibility(View.GONE); if
	 * (intervNodeList.get(position).getState() == 7) { String display = ""; for
	 * (int i = 0; i < intervItemsList.size(); i++) { display +=
	 * intervItemsList.get(i).getName() + ":" +
	 * intervItemsList.get(i).getValue() + intervItemsList.get(i).getUnit() +
	 * ((i == intervItemsList.size() - 1) ? "" : "\n"); }
	 * holder.title.setText(display); } else {
	 * holder.title.setText(intervNodeList.get(position).getContentStr()); //
	 * holder.title.setText("请进行测量"); } }
	 * holder.time.setText(intervNodeList.get(position).getTime()); //
	 * holder.tips
	 * .setText("温馨提示:\n"+(intervNodeList.get(position).getRemindContent()));
	 * holder.tips.setText(Html.fromHtml(
	 * "<h5><font color='#33cccc'>温馨提示</font></h5><h9><font color='#888888'>"
	 * +listTips[random.nextInt(listTips.length-1)]+"</font></h9>"));
	 * convertView.setTag(holder); } // }else { // // holder =
	 * (ViewHolder)convertView.getTag(); // }
	 * 
	 * //
	 * holder.img.setBackgroundResource((Integer)mData.get(position).get("img"
	 * )); // holder.title.setText((String)mData.get(position).get("title")); //
	 * holder.info.setText((String)mData.get(position).get("info")); // //
	 * holder.viewBtn.setOnClickListener(new View.OnClickListener() { // //
	 * 
	 * @Override // public void onClick(View v) { // showInfo(); // } // });
	 * 
	 * return convertView; }
	 * 
	 * }
	 */

    // public static int getSelectMember() {
    // // TODO Auto-generated method stub
    // return selectMember;
    // }

    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 逻辑处理
            // 当手指离开的时候
            if (!lastChooseTime.equals(dataData)) {
                if (!threadIsRunning) {
                    new Thread(checkScrollFinished).start();
                }
            }
            // 开启线程监视滑动是否停止
        }

        return super.onTouchEvent(event);
    }

    /**
     * 获取当前的用户状态， 0 完成注册 1完成个人信息填写 2完成初始测评 3选完专家 4专家测评完 5方案开始执行
     *
     * @author kuangtiecheng
     *
     */
    // private class GetMyStausAsyncTask extends
    // AsyncTask<Integer, Integer, String> {
    // String result = "";
    //
    // private GetMyStausAsyncTask() {
    // // showProgressBar();
    // }
    //
    // @Override
    // protected String doInBackground(Integer... params) {
    // HashMap<String, String> param = new HashMap<String, String>();
    // param.put("patientID", UtilsSharedData.getLong(Constant.USER_ID, 0)
    // + "");
    // try {
    // result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
    // + "/base/app/getcpstatus", param, "utf-8");
    // Log.i("result", result);
    // } catch (Exception e) {
    // hideProgressBar();
    // e.printStackTrace();
    // }
    // return result;
    // }
    //
    // @Override
    // protected void onPostExecute(String result) {
    // JSONObject json = null;
    // try {
    // json = new JSONObject(result);
    // pageType = json.getInt("cpstatus");
    // } catch (JSONException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // switch (pageType) {
    // case 0:
    // //完成注册
    // hideProgressBar();
    // displayOrNotIndicate(true);
    // break;
    // case 1:
    // //完成个人信息填写
    // hideProgressBar();
    // displayOrNotIndicate(true);
    // break;
    // case 2:
    // //完成初始测评
    // //需要继续请求测评结果
    // GetMyTestResultAsyncTask mTask = new GetMyTestResultAsyncTask();
    // mTask.execute();
    // break;
    // case 3:
    // //选完专家
    // // hideProgressBar();
    // // displayOrNotIndicate(true);
    // // break;
    // case 4:
    // //专家测评完
    // hideProgressBar();
    // displayOrNotIndicate(true);
    // break;
    // case 5:
    // //方案开始执行
    // hideProgressBar();
    // displayOrNotIndicate(true);
    // break;
    // case 6:
    // //已经有方案了，直接显示健康日记界面
    // new Thread(runnableHealthDiary).start();
    // break;
    // default:
    // break;
    // }
    // disPlayResult(pageType,null);
    // }
    //
    // }

	/*
	 * 根据状态判断当前需要显示什么内容
	 */

    // private void disPlayResult(int status,BeanResultOfEvaluation result){
    // String tempText = "";
    // switch (status) {
    // case 0:
    // //完成注册
    // tempText = "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>" +
    // "<big><big>·</big></big> 《我的秘书》帮您选择多个专科的资深护士<br>" +
    // "<big><big>·</big></big> 再由他们为您量身定制《健康日记》<br>" +
    // "<big><big>·</big></big> 在《健康驿站》您和著名专家欢聚一堂<br>亲，点击“开始”，让我们为您的健康之旅保驾护航";
    // // btnStartTest.setText("开始");
    // if(!UtilsSharedData.getBoolean(Constant.HASUSED)){
    // Intent intent1 = new Intent(getApplicationContext(),
    // ActivityTestPersonInfo.class);
    // startActivity(intent1);
    // UtilsSharedData.saveKeyMustValue(Constant.HASUSED, true);
    // }
    // break;
    // case 1:
    // tempText =
    // "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，我们已经收到您填写的基本信息<br>现在您可以进行一次身体状况的测评<br>让我们可以为您提供更为贴心的服务<br>还可以赢取 <font color='#B8860B'>30金币</font>哦<br>快点击“开始测评”进行测评吧！";
    // // btnStartTest.setText("开始测评");
    // //完成个人信息填写
    // break;
    // case 2:
    // //完成初始测评
    // //需要继续请求测评结果
    // if(result!=null){
    // String textResult = "";
    // if(result.getDetail().equals("高危")){
    // textResult = "<font color='#FF4500'><big><big>高危</big></big></font>";
    // }else if(result.getDetail().equals("极高危")){
    // textResult = "<font color='#FF0000'><big><big>极高危</big></big></font>";
    // }else if(result.getDetail().equals("中危")){
    // textResult = "<font color='#FFA500'><big><big>中危</big></big></font>";
    // }else if(result.getDetail().equals("低危")){
    // textResult = "<font color='#FFFF00'><big><big>低危</big></big></font>";
    // }else if(result.getDetail().equals("健康")){
    // textResult = "<font color='#45c01a'><big><big>健康</big></big></font>";
    // }
    // tempText =
    // "<font color='#e6e5e5'>————————   测评结果       ————————</font><br>您的测评结果为："+textResult+"<br><font color='#e6e5e5'>————————   温馨提示       ————————</font><br>"+result.getWarming();
    // // btnStartTest.setText("选择健康秘书");
    // }
    // break;
    // case 3:
    // // //选完专家
    // // tempText =
    // "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，你已经有私人健康秘书为您服务啦！<br>为了您的私人健康秘书能够更全面的了解您的身体状况<br>请完善一下您的健康状况信息吧！";
    // // btnStartTest.setText("完善健康信息");
    //
    // // case 1:
    // // tempText =
    // "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，我们已经收到您填写的基本信息<br>现在您可以进行一次身体状况的测评<br>让我们可以为您提供更为贴心的服务<br>还可以赢取 <font color='#B8860B'>30金币</font>哦<br>快点击“开始测评”进行测评吧！";
    // //// btnStartTest.setText("开始测评");
    // // //完成个人信息填写
    // // break;
    // // case 2:
    // // //完成初始测评
    // // //需要继续请求测评结果
    // // if(result!=null){
    // // String textResult = "";
    // // if(result.getDetail().equals("高危")){
    // // textResult = "<font color='#FF4500'><big><big>高危</big></big></font>";
    // // }else if(result.getDetail().equals("极高危")){
    // // textResult = "<font color='#FF0000'><big><big>极高危</big></big></font>";
    // // }else if(result.getDetail().equals("中危")){
    // // textResult = "<font color='#FFA500'><big><big>中危</big></big></font>";
    // // }else if(result.getDetail().equals("低危")){
    // // textResult = "<font color='#FFFF00'><big><big>低危</big></big></font>";
    // // }else if(result.getDetail().equals("健康")){
    // // textResult = "<font color='#45c01a'><big><big>健康</big></big></font>";
    // // }
    // // tempText =
    // "<font color='#e6e5e5'>————————   测评结果       ————————</font><br>您的测评结果为："+textResult+"<br><font color='#e6e5e5'>————————   温馨提示       ————————</font><br>"+result.getWarming();
    // //// btnStartTest.setText("选择健康秘书");
    // // }
    // // break;
    // // case 3:
    // //// //选完专家
    // //// tempText =
    // "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，你已经有私人健康秘书为您服务啦！<br>为了您的私人健康秘书能够更全面的了解您的身体状况<br>请完善一下您的健康状况信息吧！";
    // //// btnStartTest.setText("完善健康信息");
    // //// break;
    // // case 4:
    // // tempText =
    // "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，您的私人健康秘书正在为您量身定制健康方案！<br>请您耐心等待<br>你也可以到“驿站”逛逛哦！";
    // //// btnStartTest.setText("去驿站逛逛");
    // // //专家测评完
    // // break;
    // // case 5:
    // // //方案开始执行
    // // tempText =
    // "<h3>关爱自己  呵护亲人</h3><h4>“爱护”是您随时随地的保健医！</h4>亲，您的私人健康秘书已经为您制定好了健康方案！<br>点击“开启健康之旅”来查看吧！";
    // //// btnStartTest.setText("开启健康之旅");
    // // break;
    // //
    // // default:
    // // break;
    // // }
    // //// tv_Communication.setText(Html
    // //// .fromHtml(tempText, StringUtil.getImageGetterInstance(this), null));
    // // }
    //
    // }

    /**
     * 请求当前测评结果
     *
     * @author kuangtiecheng
     */
    private class GetMyTestResultAsyncTask extends
            AsyncTask<Integer, Integer, BeanResultOfEvaluation> {
        String result = "";
        private BeanResultOfEvaluation beanResultOfEvaluation;

        private GetMyTestResultAsyncTask() {
            // showProgressBar();
        }

        @Override
        protected BeanResultOfEvaluation doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("patientID", UtilsSharedData.getLong(Constant.USER_ID, 0)
                    + "");
            try {
                result = NetTool
                        .sendPostRequest(AbsParam.getBaseUrl()
                                        + "/webapp/questionnaire/getquesresult", param,
                                "utf-8");
                Log.i("result", result);
                beanResultOfEvaluation = jsonToBean(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return beanResultOfEvaluation;
        }

        @Override
        protected void onPostExecute(BeanResultOfEvaluation result) {
            super.onPostExecute(result);
            hideProgressBar();
            if (result == null) {
                return;
            }
            if (result.getStatus().equals("success")) {
                displayOrNotIndicate(true);
                // disPlayResult(2,result);
                // Toast.makeText(getBaseContext(), "submit success!",
                // 1).show();
            } else {
                Toast.makeText(getBaseContext(), "获取身体评测结果失败！", 1).show();
            }
        }

        private BeanResultOfEvaluation jsonToBean(String str) {
            BeanResultOfEvaluation beanROE = new BeanResultOfEvaluation();
            Gson gson = new Gson();
            if (str != null && !str.equals("-1")) {
                beanROE = gson.fromJson(str,
                        new TypeToken<BeanResultOfEvaluation>() {
                        }.getType());
            }
            return beanROE;
        }

    }

}

/*
 * package com.cn.coachs.ui.patient.main.healthdiary;
 * 
 * import java.text.ParseException; import java.text.SimpleDateFormat; import
 * java.util.Calendar; import java.util.Date; import java.util.HashMap; import
 * java.util.List; import java.util.Map;
 * 
 * import android.annotation.SuppressLint; import android.app.Dialog; import
 * android.content.Context; import android.content.DialogInterface; import
 * android.content.Intent; import android.graphics.Bitmap; import
 * android.graphics.Color; import android.os.Bundle; import android.os.Handler;
 * import android.os.Message; import android.util.Log; import
 * android.view.Gravity; import android.view.LayoutInflater; import
 * android.view.MotionEvent; import android.view.View; import
 * android.view.View.OnClickListener; import android.view.ViewGroup; import
 * android.view.ViewGroup.LayoutParams; import android.view.Window; import
 * android.view.WindowManager; import android.view.animation.AnimationUtils;
 * import android.widget.AdapterView; import
 * android.widget.AdapterView.OnItemClickListener; import
 * android.widget.BaseAdapter; import android.widget.EditText; import
 * android.widget.FrameLayout; import android.widget.ImageView; import
 * android.widget.ImageView.ScaleType; import android.widget.LinearLayout;
 * import android.widget.ListView; import android.widget.RelativeLayout; import
 * android.widget.TextView;
 * 
 * import com.cn.coachs.R; import com.cn.coachs.http.NetTool; import
 * com.cn.coachs.model.healthdiary.BeanIntervItem; import
 * com.cn.coachs.model.healthdiary.BeanIntervNode; import
 * com.cn.coachs.model.healthdiary.BeanInterventionLogResult; import
 * com.cn.coachs.model.healthdiary.BeanSummaryResult; import
 * com.cn.coachs.patient.others.myfamily.ActivityMyFamilyMemberPage; import
 * com.cn.coachs.ui.AppMain; import com.cn.coachs.ui.AppPool; import
 * com.cn.coachs.ui.basic.ActivityBasic; import
 * com.cn.coachs.ui.basic.IfaceDialog; import
 * com.cn.coachs.ui.chat.common.utils.ToastUtil; import
 * com.cn.coachs.ui.patient.main.TabActivityMain; import
 * com.cn.coachs.ui.patient.main.detectioncenter.ActivityDetectionCenter;
 * import
 * com.cn.coachs.ui.patient.main.healthdiary.alarm.ActivitySetNotificationTime
 * ; import
 * com.cn.coachs.ui.patient.main.healthdiary.datepicker.SliderContainer;
 * import
 * com.cn.coachs.ui.patient.main.healthdiary.datepicker.SliderContainer
 * .OnTimeChangeListener; import
 * com.cn.coachs.ui.patient.main.healthdiary.test.ActivityTestPersonInfo;
 * import com.cn.coachs.ui.record.audio.ActivityRecordAudio; import
 * com.cn.coachs.ui.record.video.ActivityVideoRecord; import
 * com.cn.coachs.util.AbsParam; import com.cn.coachs.util.Constant;
 * import com.cn.coachs.util.CustomDialog; import
 * com.cn.coachs.util.FButton; import com.cn.coachs.util.StringUtil;
 * import com.cn.coachs.util.UtilsSharedData; import com.google.gson.Gson;
 * import com.nostra13.universalimageloader.core.DisplayImageOptions; import
 * com.nostra13.universalimageloader.core.ImageLoader; import
 * com.nostra13.universalimageloader.core.assist.ImageScaleType;
 * 
 * public class ActivityHealthDiary extends ActivityBasic implements
 * IfaceDialog, OnClickListener{ Calendar mInitialTime; Calendar minTime,
 * maxTime; static SliderContainer mContainer; TextView date; ListView
 * contentList; MyAdapter adapter; ImageView
 * todaySummarize,todayRemind,todayLocation; int minuteInterval = 1; String
 * dataData = "2015-04-24"; String tomorrowData=""; boolean firstIn = true;
 * private String summary; private ImageView increaseMonthBtn, decreaseMonthBtn;
 * private ImageView increaseMember, decreaseMember; private TextView
 * person,communication;//,introduction; private static int selectMember = 0;
 * private LinearLayout noDiaryLayout;//,noDiaryLayoutContent; private
 * RelativeLayout noDiaryLayoutContent; private FButton btnStartTest; private
 * DisplayImageOptions options; private ImageLoader imageLoader; public static
 * Long valueId,nodeItemId,valueId_DBP,valueId_HR,nodeItemId_DBP,nodeItemId_HR;
 * public static String detectionType; // private long planId;
 *//**
 * true表示选择成功，false表示选择失败
 *
 * @author kuangtiecheng
 * @return 嵌有datapicker的对话框，用于弹出一个datapicker，让用户选择日期；
 * <p/>
 * 获取当天康复日记内容
 * <p/>
 * 解析返回来的Json数组
 * @param jsonString
 * @return
 * @throws Exception
 * <p/>
 * 更新界面
 * @param jsonString
 * @return
 * @throws Exception
 * <p/>
 * 提交当日总结
 * <p/>
 * 解析返回来的Json数组
 * @param jsonString
 * @return
 * @throws Exception
 * <p/>
 * 0:音频，1：视频，2：文字
 */
/*
 * 
 * @Override public void onCreate(Bundle savedInstanceState) { // TODO
 * Auto-generated method stub super.onCreate(savedInstanceState);
 * setContentView(R.layout.home_healthdiary); imageInit(); initView();
 * lastChooseTime = dataData; }
 * 
 * private void imageInit() { options = new DisplayImageOptions.Builder() //
 * .showStubImage(R.drawable.my_videos) //
 * .showImageForEmptyUri(R.drawable.my_videos) //
 * .showImageOnFail(R.drawable.my_videos) .cacheInMemory(true)
 * .cacheOnDisc(true) .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
 * .bitmapConfig(Bitmap.Config.RGB_565).build(); imageLoader =
 * ImageLoader.getInstance(); }
 * 
 * @Override public void onResume() { // TODO Auto-generated method stub
 * super.onResume(); showProgressBar(); new Thread(runnableHealthDiary).start();
 * }
 * 
 * private void initView() { UtilsSharedData.initDataShare(this);
 * AppPool.createActivity(ActivityHealthDiary.this);
 * communication=(TextView)findViewById(R.id.CommunicationWithSecretary); //
 * 设置今天的日期 btnStartTest = (FButton)findViewById(R.id.add_diary_btn);
 * btnStartTest.setCornerRadius(3); noDiaryLayout =
 * (LinearLayout)findViewById(R.id.no_diary_layout); noDiaryLayoutContent =
 * (RelativeLayout)findViewById(R.id.no_diary_layout_content); Calendar c =
 * Calendar.getInstance(); mInitialTime = c; mContainer =
 * (SliderContainer)findViewById(R.id.dateSliderContainer); date = (TextView)
 * findViewById(R.id.health_date); person = (TextView)
 * findViewById(R.id.person); //
 * mContainer.setBackgroundColor(getResources().getColor(R.color.whitesmoke));
 * mContainer.setOnTimeChangeListener(onTimeChangeListener);
 * mContainer.setMinuteInterval(minuteInterval); if(mContainer.getTime()==null){
 * mContainer.setTime(mInitialTime); } increaseMonthBtn =
 * (ImageView)findViewById(R.id.increasemonth); decreaseMonthBtn =
 * (ImageView)findViewById(R.id.decreasemonth); increaseMember =
 * (ImageView)findViewById(R.id.increaseperson); decreaseMember =
 * (ImageView)findViewById(R.id.decreaseperson);
 * increaseMonthBtn.setOnClickListener(this);
 * decreaseMonthBtn.setOnClickListener(this);
 * increaseMember.setOnClickListener(this);
 * decreaseMember.setOnClickListener(this);
 * btnStartTest.setOnClickListener(this); todaySummarize =
 * (ImageView)findViewById(R.id.healthy_dairy_summarize);
 * todaySummarize.setOnClickListener(this); todayRemind =
 * (ImageView)findViewById(R.id.healthy_dairy_remind);
 * todayRemind.setOnClickListener(this); todayLocation =
 * (ImageView)findViewById(R.id.healthy_dairy_location);
 * todayLocation.setOnClickListener(this);
 * 
 * contentList = (ListView) findViewById(R.id.health_content); adapter = new
 * MyAdapter(getApplicationContext()); contentList.setOnItemClickListener(new
 * OnItemClickListener() {
 * 
 * @Override public void onItemClick(AdapterView<?> parent, View view, int
 * position, long id) {
 * view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
 * R.anim.icon_scale)); //
 * if(intervNodeList.get(position).getContentList().size()!=0){ // Intent intent
 * = new // Intent(HealthDiaryActivity.this,ActivityHealthTraningGuide.class);
 * // intent.putExtra(Constant.PAGE_TYPE, position); // startActivity(intent);
 * // }else // Toast.makeText(getActivity(), position, 1).show(); if
 * (intervNodeList.get(position).getIntervItemsList().size() != 0) { if
 * ((Calendar.getInstance().getTimeInMillis() / (24 * 60 * 1000 * 60)) ==
 * (mContainer .getTime().getTimeInMillis() / (24 * 60 * 1000 * 60))) {
 * //设置选中的人员为当前用户 if(selectMember == 0){ //只有当前用户自己才可以进行测量
 * TabActivityMain.selectDetetcMember = 0;
 * ActivityDetectionCenter.whetherFromDiary=true;
 * detectionType=intervNodeList.get
 * (position).getIntervItemsList().get(0).getCode();
 * valueId=Long.valueOf(intervNodeList
 * .get(position).getIntervItemsList().get(0).getId());
 * nodeItemId=Long.valueOf(intervNodeList
 * .get(position).getIntervItemsList().get(0).getNodeItemId());
 * if(detectionType.endsWith("SSY")){
 * valueId_DBP=Long.valueOf(intervNodeList.get
 * (position).getIntervItemsList().get(position+1).getId());
 * nodeItemId_DBP=Long.
 * valueOf(intervNodeList.get(position).getIntervItemsList().
 * get(position+1).getNodeItemId());
 * valueId_HR=Long.valueOf(intervNodeList.get(position
 * ).getIntervItemsList().get(position+2).getId());
 * nodeItemId_HR=Long.valueOf(intervNodeList
 * .get(position).getIntervItemsList().get(position+2).getNodeItemId()); }else{
 * valueId_DBP=0l; valueId_HR=0l; nodeItemId_DBP=0l; nodeItemId_HR=0l; } //
 * ((TabActivityMain)getActivity()).mTabHost.setCurrentTab(0); } } else if
 * (intervNodeList.get(position).getContentList().size() > 0) { Intent intent =
 * new Intent(getApplicationContext(), ActivityHealthTraningGuide.class);
 * intent.putExtra(Constant.PAGE_TYPE, position); startActivity(intent); } }
 * else { Intent intent = new
 * Intent(getApplicationContext(),ActivityHealthTraningGuide.class);
 * intent.putExtra(Constant.PAGE_TYPE, position); startActivity(intent); } } });
 * }
 * 
 * String lastChooseTime = ""; // 时间改变提示类 private OnTimeChangeListener
 * onTimeChangeListener = new OnTimeChangeListener() { public void
 * onTimeChange(Calendar time) { if (date != null) { final Calendar c =
 * mContainer.getTime(); date.setText(String.format("%tY" + "年" + " %tB", c,
 * c)); SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); dataData =
 * sdf.format(c.getTime()); // long t=c.getTimeInMillis(); // t=t+86400; // Date
 * date=new Date(t); // tomorrowData=sdf.format(date); System.out.println("时间："
 * + dataData); } } };
 * 
 * private boolean threadIsRunning = false;
 * 
 * 
 * 
 * int lastScrollX = 0; Runnable checkScrollFinished = new Runnable() {
 * 
 * @Override public void run() { // TODO Auto-generated method stub
 * threadIsRunning = true; while (true) { if(mContainer.getCurrentX() ==
 * lastScrollX){ // if (mContainer.isFinishScroller()) { Message msg =
 * scrollHandler.obtainMessage(); msg.what = 1; scrollHandler.sendMessage(msg);
 * break; // } }else{ lastScrollX = mContainer.getCurrentX(); try {
 * Thread.sleep(200); } catch (InterruptedException e) { // TODO Auto-generated
 * catch block e.printStackTrace(); } } } threadIsRunning = false; } };
 * 
 * Handler scrollHandler = new Handler() {
 * 
 * @Override public void handleMessage(Message msg) { // TODO Auto-generated
 * method stub judgeCanSummaryOrNot(); lastChooseTime = dataData;
 * showProgressBar(); new Thread(runnableHealthDiary).start();
 * super.handleMessage(msg); }
 * 
 * };
 * 
 * private void judgeCanSummaryOrNot(){ if
 * ((Calendar.getInstance().getTimeInMillis() / (24 * 60 * 1000 * 60)) <
 * (mContainer .getTime().getTimeInMillis() / (24 * 60 * 1000 * 60))) { //
 * 今天之后的日期不允许提交总结 todaySummarize.setVisibility(View.INVISIBLE); } else {
 * if(selectMember!=0){ //非当前人员不能填写总结
 * todaySummarize.setVisibility(View.INVISIBLE); }else{
 * todaySummarize.setVisibility(View.VISIBLE); } } }
 * 
 * @Override public void onClick(View v) { super.onClick(v); // TODO
 * Auto-generated method stub Intent intent; switch (v.getId()) { case
 * R.id.healthy_dairy_summarize: showSummarizeDialog(); break; case
 * R.id.healthy_dairy_location: intent = new Intent(this,
 * ActivityMyFamilyMemberPage.class); intent.putExtra(Constant.SELECT_MEMBER,
 * selectMember); startActivity(intent); break; case R.id.healthy_dairy_remind:
 * // intent = new Intent(getActivity(), ActivityRecoveryRemind.class); //
 * intent = new Intent(getActivity(), ChooseRecovery.class); //
 * startActivity(intent); showRemindDialog(); break; case R.id.increasemonth: //
 * 增加月份 if (calendarEnd == null) return; Calendar c = mContainer.getTime();
 * c.add(Calendar.MONTH, 1); if (c.compareTo(calendarEnd) == 1) { //
 * 比干预方案最大日期还大了，就不再增加了 c.add(Calendar.MONTH, -1); return; }
 * mContainer.setTime(c); SimpleDateFormat sdf = new
 * SimpleDateFormat("yyyy-MM-dd"); dataData = sdf.format(c.getTime());
 * judgeCanSummaryOrNot(); showProgressBar(); new
 * Thread(runnableHealthDiary).start(); break; case R.id.decreasemonth: // 增加月份
 * if (calendarStart == null) return; Calendar c1 = mContainer.getTime();
 * c1.add(Calendar.MONTH, -1); if (c1.compareTo(calendarStart) == -1) { //
 * 比干预方案最小日期还小了，就不再减少了 c1.add(Calendar.MONTH, 1); return; }
 * mContainer.setTime(c1); SimpleDateFormat sdf1 = new
 * SimpleDateFormat("yyyy-MM-dd"); dataData = sdf1.format(c1.getTime());
 * judgeCanSummaryOrNot(); showProgressBar(); new
 * Thread(runnableHealthDiary).start(); // 减少月份 break; case R.id.increaseperson:
 * if(selectMember >= AppMain.allFamilyMemberList.size()-1){ selectMember = 0;
 * }else{ selectMember++; } judgeCanSummaryOrNot();
 * mContainer.setTime(Calendar.getInstance());
 * person.setText(StringUtil.displayName
 * (AppMain.allFamilyMemberList.get(selectMember))); showProgressBar(); new
 * Thread(runnableHealthDiary).start(); break; case R.id.decreaseperson:
 * if(selectMember == 0){ selectMember = AppMain.allFamilyMemberList.size()-1;
 * }else{ selectMember--; } judgeCanSummaryOrNot();
 * mContainer.setTime(Calendar.getInstance());
 * person.setText(StringUtil.displayName
 * (AppMain.allFamilyMemberList.get(selectMember))); showProgressBar(); new
 * Thread(runnableHealthDiary).start(); break; case R.id.add_diary_btn: Intent
 * intent1 = new Intent(getApplicationContext(), ActivityTestPersonInfo.class);
 * startActivity(intent1); break; default: break; } }
 *//**
 * @author kuangtiecheng
 * @return 嵌有datapicker的对话框，用于弹出一个datapicker，让用户选择日期；
 */
/*
 * // private void choseStartDayDialog() { // LayoutInflater inflater =
 * LayoutInflater.from(this); // LinearLayout layout =
 * (LinearLayout)inflater.inflate(R.layout.datapicker,null); // DatePicker
 * DP=(DatePicker)layout.findViewById(R.id.datapicker_dialog); // Calendar
 * calendar=Calendar.getInstance(); // startDate = calendar.get(Calendar.YEAR) +
 * "-" // + calendar.get(Calendar.MONTH) + "-" // +
 * calendar.get(Calendar.DAY_OF_MONTH); // DP.init(calendar.get(Calendar.YEAR),
 * calendar.get(Calendar.MONTH), // calendar.get(Calendar.DAY_OF_MONTH), // new
 * OnDateChangedListener() { // // @Override // public void
 * onDateChanged(DatePicker view, int year, // int monthOfYear, int dayOfMonth)
 * { // // TODO Auto-generated method stub // monthOfYear=monthOfYear+1; //
 * startDate=year+"-"+monthOfYear+"-"+dayOfMonth; //
 * Toast.makeText(getActivity(), startDate, 1).show(); // } // }); //
 * CustomDialog.Builder builder = new CustomDialog.Builder(getActivity()); //
 * builder.setTitle("提示"); // builder.setContentView(layout); //
 * builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // //
 * 
 * @Override // public void onClick(DialogInterface dialog, int which) { ////
 * Toast.makeText(context, text, duration) //// AscyncChoseHealthDiary
 * choseHD=new AscyncChoseHealthDiary(planId); //// choseHD.execute(); //
 * dialog.dismiss(); // } // }); // builder.setNegativeButton("取消", new
 * DialogInterface.OnClickListener() { // // @Override // public void
 * onClick(DialogInterface dialog, int which) { //// AscyncChoseHealthDiary
 * choseHD=new AscyncChoseHealthDiary(planId); //// choseHD.execute(); //
 * dialog.dismiss(); // } // }); // builder.create().show(); // // // } //
 * 弹出总结弹窗
 * 
 * @SuppressLint("ResourceAsColor") private void showSummarizeDialog() { final
 * EditText inputServer = new EditText(this); inputServer.setHint("请填写今日小结");
 * inputServer.setMinLines(8); if (healthDiaryBean != null) { if
 * (healthDiaryBean.getNodes().size() != 0) {
 * inputServer.setText(healthDiaryBean.getNodes().get(0) .getSummary()); } }
 * inputServer.setGravity(Gravity.TOP); inputServer.setSingleLine(false);
 * inputServer.setHintTextColor(R.color.font_gray);
 * inputServer.setTextColor(Color.BLACK);
 * inputServer.setHorizontalScrollBarEnabled(false); CustomDialog.Builder
 * builder = new CustomDialog.Builder(this); builder.setTitle("提示");
 * builder.setContentView(inputServer); builder.setPositiveButton("确定", new
 * DialogInterface.OnClickListener() {
 * 
 * @Override public void onClick(DialogInterface dialog, int which) {
 * dialog.dismiss(); summary = inputServer.getText().toString(); new
 * Thread(runnableSummaryResult).start(); showProgressBar(); } });
 * builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
 * 
 * @Override public void onClick(DialogInterface dialog, int which) {
 * dialog.dismiss(); } }); builder.create().show();
 * 
 * }
 *//*******************************************************
 * 获取当天康复日记内容
 */
/*
 * private Map<String, String> map; private String retStr;// 获取到返回的json数据 public
 * static BeanInterventionLogResult healthDiaryBean; // public static
 * BeanIntervItem detectionNodeBean; // 网络请求 Runnable runnableHealthDiary = new
 * Runnable() { private String url = AbsParam.getBaseUrl() +
 * "/interv/plan/show";
 * 
 * @Override public void run() { try { long userId =
 * UtilsSharedData.getLong(Constant.USER_ID, 1); map = new HashMap<String,
 * String>(); map.put("patientId", userId + "");// 表示患者用户id map.put("planId",
 * "");// 查找最新的干预方案 map.put("days", "1");// 查询一天的干预方案 map.put("startDate",
 * dataData);// 查询开始日期 retStr = NetTool.sendPostRequest(url, map, "utf-8"); //
 * post方式提交，这一步执行后从后台获取到了返回的数据 Log.e("=-=-=retStr=-=-=",retStr);
 * getHealthDiaryInfo(retStr); // getDetectionNode(retStr); //
 * Toast.makeText(getActivity(), detectionNodeBean.getCode(),
 * Toast.LENGTH_SHORT).show(); Message msg = new Message(); if (retStr == null)
 * { msg.what = Constant.FAIL; } else { msg.what = Constant.COMPLETE; }
 * healthDiaryHandler.sendMessage(msg);
 * 
 * } catch (Exception e) { healthDiaryHandler.postDelayed(new Runnable() {
 * 
 * @Override public void run() { // TODO Auto-generated method stub
 * hideProgressBar(); showNetErrorInfo(); } }, 1000); } } };
 * 
 * private Handler healthDiaryHandler = new Handler() {
 * 
 * public void handleMessage(Message m) { switch (m.what) { case
 * Constant.COMPLETE: // 展示获取的数据bean hideProgressBar(); firstIn = false; if
 * (healthDiaryBean != null) { SimpleDateFormat sf = new
 * SimpleDateFormat("yyyy-MM-dd"); String end =
 * healthDiaryBean.getPlanEndDate(); String request =
 * healthDiaryBean.getStartDate(); long compare = 0; try { compare =
 * sf.parse(end).getTime()-sf.parse(request).getTime(); } catch (ParseException
 * e) { // TODO Auto-generated catch block e.printStackTrace(); } if(compare <
 * 0){ //请求时间大于结束时间也不显示数据界面 // --------------------------------- //
 * displayOrNotIndicate(true); displayOrNotIndicate(false); }else{
 * //有健康档案数据，则显示健康日记界面 displayOrNotIndicate(false); updateUI(); } }else{ //
 * --------------------------------- // displayOrNotIndicate(true);
 * displayOrNotIndicate(false); } break;
 * 
 * default:
 * 
 * break; } } };
 * 
 * private void displayOrNotIndicate(boolean visible){ if(visible){
 * noDiaryLayout.setVisibility(View.VISIBLE);
 * noDiaryLayoutContent.setVisibility(View.VISIBLE);
 * date.setVisibility(View.INVISIBLE);
 * increaseMonthBtn.setVisibility(View.INVISIBLE);
 * decreaseMonthBtn.setVisibility(View.INVISIBLE); if(selectMember==0){
 * btnStartTest.setVisibility(View.VISIBLE); }else{
 * btnStartTest.setVisibility(View.INVISIBLE); } }else{
 * noDiaryLayout.setVisibility(View.GONE);
 * noDiaryLayoutContent.setVisibility(View.GONE);
 * date.setVisibility(View.VISIBLE);
 * increaseMonthBtn.setVisibility(View.VISIBLE);
 * decreaseMonthBtn.setVisibility(View.VISIBLE); }
 * 
 * }
 *//**
 * 解析返回来的Json数组
 *
 * @param jsonString
 * @return
 * @throws Exception
 */
/*
 * private void getHealthDiaryInfo(String jsonString) throws Exception { Gson
 * gson = new Gson(); healthDiaryBean = gson .fromJson(jsonString,
 * BeanInterventionLogResult.class); }
 * 
 * // private void getDetectionNode(String jsonString) throws Exception { //
 * Gson gson=new Gson(); // detectionNodeBean=gson.fromJson(jsonString,
 * BeanIntervItem.class); // }
 * 
 * Calendar calendarStart, calendarEnd;
 * 
 * private void updateTimePicker() { // 设置最大时间 String startTime, endTime;
 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); startTime =
 * healthDiaryBean.getPlanStartDate(); endTime =
 * healthDiaryBean.getPlanEndDate(); Date dateStart = null; Date dateEnd = null;
 * try { dateStart = sdf.parse(startTime); dateEnd = sdf.parse(endTime); } catch
 * (ParseException e) { // TODO Auto-generated catch block e.printStackTrace();
 * } calendarStart = Calendar.getInstance(); Log.e("=-=-=calendarStart=-=-=",
 * ""+calendarStart.get(Calendar.YEAR)+calendarStart.get(Calendar.MONTH));
 * calendarStart.setTime(dateStart); calendarEnd = Calendar.getInstance();
 * Log.e("=-=-=calendarEnd=-=-=",
 * ""+calendarEnd.get(Calendar.YEAR)+calendarEnd.get(Calendar.MONTH));
 * calendarEnd.setTime(dateEnd); if (calendarStart != null)
 * mContainer.setMinTime(calendarStart); if (calendarEnd != null)
 * mContainer.setMaxTime(calendarEnd); mContainer.refreshDrawableState();
 * 
 * }
 *//**
 * 更新界面
 *
 * @param jsonString
 * @return
 * @throws Exception
 */
/*
 * List<BeanIntervNode> intervNodeList;
 * 
 * private void updateUI() { updateTimePicker(); if
 * (healthDiaryBean.getNodes().size() != 0) { intervNodeList =
 * healthDiaryBean.getNodes().get(0).getNodes();
 * 
 * contentList.setAdapter(adapter); adapter.notifyDataSetChanged(); } else {
 * intervNodeList = null; contentList.setAdapter(adapter);
 * adapter.notifyDataSetChanged(); } }
 *//*******************************************************
 * 提交当日总结
 */
/*
 * private Map<String, String> map1; private String retStr1;// 获取到返回的json数据
 * private BeanSummaryResult beanSummaryResult; // 网络请求 Runnable
 * runnableSummaryResult = new Runnable() { private String url =
 * AbsParam.getBaseUrl() + "/interv/plan/dailysummary"; // private String
 * url="http://192.168.12.112:8080/serviceplatform/interv/plan/dailysummary";
 * 
 * @Override public void run() { try { map1 = new HashMap<String, String>();
 * map1.put("patientPlanId",
 * String.valueOf(healthDiaryBean.getPatientPlanId()));// 干预方案Id
 * map1.put("summary", summary);// 查询一天的干预方案 retStr1 =
 * NetTool.sendPostRequest(url, map1, "utf-8"); // post方式提交，这一步执行后从后台获取到了返回的数据
 * getSummaryResult(retStr1);
 * 
 * Message msg = new Message(); if (retStr1 == null) { msg.what = Constant.FAIL;
 * } else { msg.what = Constant.COMPLETE; } summaryHandler.sendMessage(msg);
 * 
 * } catch (Exception e) { summaryHandler.postDelayed(new Runnable() {
 * 
 * @Override public void run() { // TODO Auto-generated method stub
 * hideProgressBar(); showNetErrorInfo(); } }, 1000); } } };
 * 
 * private Handler summaryHandler = new Handler() {
 * 
 * public void handleMessage(Message m) { switch (m.what) { case
 * Constant.COMPLETE: // 展示获取的数据bean hideProgressBar();
 * ToastUtil.showMessage("提交成功"); break;
 * 
 * default:
 * 
 * break; } } };
 *//**
 * 解析返回来的Json数组
 *
 * @param jsonString
 * @return
 * @throws Exception
 */
/*
 * private void getSummaryResult(String jsonString) throws Exception { Gson gson
 * = new Gson(); beanSummaryResult = gson.fromJson(jsonString,
 * BeanSummaryResult.class); }
 * 
 * public final class ViewHolder { public ImageView img; public TextView time;
 * public TextView title; public TextView tips; private FrameLayout pic_layout;
 * }
 * 
 * public class MyAdapter extends BaseAdapter {
 * 
 * private LayoutInflater mInflater; private Context healthDiaryFragment;
 * 
 * public MyAdapter(Context healthDiaryFragment) { this.mInflater =
 * LayoutInflater.from(healthDiaryFragment); this.healthDiaryFragment =
 * healthDiaryFragment; }
 * 
 * @Override public int getCount() { // TODO Auto-generated method stub int
 * count = 0; if (intervNodeList != null) { count = intervNodeList.size(); }
 * return count; }
 * 
 * @Override public Object getItem(int arg0) { // TODO Auto-generated method
 * stub return intervNodeList.get(arg0); }
 * 
 * @Override public long getItemId(int arg0) { // TODO Auto-generated method
 * stub return arg0; }
 * 
 * @Override public View getView(int position, View convertView, ViewGroup
 * parent) {
 * 
 * ViewHolder holder = null; // if (convertView == null) { holder = new
 * ViewHolder(); if (position % 2 == 0) { convertView = mInflater.inflate(
 * R.layout.listitem_historydiary_left, null); } else { convertView =
 * mInflater.inflate( R.layout.listitem_historydiary_right, null); }
 * 
 * // DisplayMetrics disply = new DisplayMetrics(); //
 * getWindowManager().getDefaultDisplay().getMetrics(disply); // int width =
 * disply.widthPixels; // convertView.setLayoutParams(new
 * LayoutParams(width/2,LayoutParams.WRAP_CONTENT));
 * 
 * holder.img = (ImageView) convertView.findViewById(R.id.health_pic);
 * holder.time = (TextView) convertView.findViewById(R.id.health_time);
 * holder.title = (TextView) convertView .findViewById(R.id.health_desc);
 * holder.tips = (TextView) convertView .findViewById(R.id.health_tips);
 * holder.pic_layout = (FrameLayout)convertView.findViewById(R.id.pic_layout);
 * holder.img.setScaleType(ScaleType.FIT_XY); if (intervNodeList != null) { if
 * (intervNodeList.get(position).getState() == 7) {
 * holder.title.setText("今日训练(已完成)"); } if
 * (intervNodeList.get(position).getContentList().size() != 0) { String imgURL =
 * AbsParam.getBaseUrl()+intervNodeList.get(position)
 * .getContentList().get(0).getThumbnail(); imageLoader.displayImage(imgURL,
 * holder.img, options); } else if
 * (intervNodeList.get(position).getIntervItemsList() .size() != 0) {
 * List<BeanIntervItem> intervItemsList = intervNodeList.get(
 * position).getIntervItemsList(); //隐藏视频界面
 * holder.pic_layout.setVisibility(View.GONE); if
 * (intervNodeList.get(position).getState() == 7) { String display = ""; for
 * (int i = 0; i < intervItemsList.size(); i++) { display +=
 * intervItemsList.get(i).getName() + ":" + intervItemsList.get(i).getValue() +
 * intervItemsList.get(i).getUnit() + ((i == intervItemsList.size() - 1) ? "" :
 * "\n"); } holder.title.setText(display); } else {
 * holder.title.setText("请进行测量"); } }
 * holder.time.setText(intervNodeList.get(position).getTime());
 * holder.tips.setText
 * ("温馨提示:\n"+intervNodeList.get(position).getRemindContent());
 * 
 * convertView.setTag(holder); } // }else { // // holder =
 * (ViewHolder)convertView.getTag(); // }
 * 
 * // holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
 * // holder.title.setText((String)mData.get(position).get("title")); //
 * holder.info.setText((String)mData.get(position).get("info")); // //
 * holder.viewBtn.setOnClickListener(new View.OnClickListener() { // //
 * 
 * @Override // public void onClick(View v) { // showInfo(); // } // });
 * 
 * return convertView; }
 * 
 * }
 * 
 * 
 * 提示用户选择提醒方式
 *//**
 * 0:音频，1：视频，2：文字
 */
/*
 * private int chooseFalg = 0; private void showRemindDialog() { View view =
 * getLayoutInflater().inflate(R.layout.photo_choose_dialog, null); Dialog
 * dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
 * dialog.setContentView(view, new LayoutParams(LayoutParams.FILL_PARENT,
 * LayoutParams.WRAP_CONTENT)); Window window = dialog.getWindow(); // 设置显示动画
 * window.setWindowAnimations(R.style.main_menu_animstyle);
 * WindowManager.LayoutParams wl = window.getAttributes(); wl.x = 0; wl.y =
 * getWindowManager().getDefaultDisplay().getHeight(); // 以下这两句是为了保证按钮可以水平满屏
 * wl.width = ViewGroup.LayoutParams.MATCH_PARENT; wl.height =
 * ViewGroup.LayoutParams.WRAP_CONTENT;
 * 
 * // 设置显示位置 dialog.onWindowAttributesChanged(wl); // 设置点击外围解散
 * dialog.setCanceledOnTouchOutside(true); dialog.show();
 * 
 * buttonEvent(dialog,view);
 * 
 * }
 * 
 * private void buttonEvent(final Dialog dialog,View view){
 * view.findViewById(R.id.videoremind).setOnClickListener(new OnClickListener()
 * {
 * 
 * @Override public void onClick(View arg0) { // TODO Auto-generated method stub
 * dialog.dismiss(); chooseFalg=1;
 * UtilsSharedData.saveKeyMustValue("chooseFalg", 1); Intent intent = new
 * Intent(ActivityHealthDiary.this,ActivityVideoRecord.class);
 * intent.putExtra("choose", chooseFalg); startActivity(intent); } });
 * 
 * view.findViewById(R.id.audioremind).setOnClickListener(new OnClickListener()
 * {
 * 
 * @Override public void onClick(View arg0) { // TODO Auto-generated method stub
 * dialog.dismiss(); chooseFalg=0;
 * UtilsSharedData.saveKeyMustValue("chooseFalg", 0); Intent intent1 = new
 * Intent(ActivityHealthDiary.this,ActivityRecordAudio.class);
 * intent1.putExtra("choose", chooseFalg); startActivity(intent1); } });
 * 
 * view.findViewById(R.id.textremind).setOnClickListener(new OnClickListener() {
 * 
 * @Override public void onClick(View arg0) { // TODO Auto-generated method stub
 * dialog.dismiss(); chooseFalg= 2;
 * UtilsSharedData.saveKeyMustValue("chooseFalg", 2); characterRecord(); } });
 * 
 * view.findViewById(R.id.cancelremind).setOnClickListener(new OnClickListener()
 * {
 * 
 * @Override public void onClick(View arg0) { // TODO Auto-generated method stub
 * dialog.dismiss(); } });
 * 
 * }
 * 
 * @SuppressLint("ResourceAsColor") private void characterRecord() {
 * 
 * final EditText inputServer = new EditText(this);
 * inputServer.setHint("请填写您要设置的提醒文字"); inputServer.setMinLines(8);
 * inputServer.setGravity(Gravity.TOP); inputServer.setSingleLine(false);
 * inputServer.setHintTextColor(R.color.font_gray);
 * inputServer.setTextColor(Color.BLACK);
 * inputServer.setHorizontalScrollBarEnabled(false); CustomDialog.Builder
 * builder = new CustomDialog.Builder(this); builder.setTitle("提示");
 * builder.setContentView(inputServer); builder.setPositiveButton("确定", new
 * DialogInterface.OnClickListener() {
 * 
 * @Override public void onClick(DialogInterface dialog, int which) {
 * dialog.dismiss(); UtilsSharedData.saveKeyMustValue("textremind",
 * inputServer.getText().toString()); Intent intent2 = new
 * Intent(ActivityHealthDiary.this, ActivitySetNotificationTime.class);
 * intent2.putExtra("choose", chooseFalg); startActivity(intent2); } });
 * builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
 * 
 * @Override public void onClick(DialogInterface dialog, int which) {
 * dialog.dismiss(); } }); builder.create().show(); }
 * 
 * public static int getSelectMember() { // TODO Auto-generated method stub
 * return selectMember; }
 * 
 * @Override public boolean onTouchEvent(MotionEvent event) { // TODO
 * Auto-generated method stub if (event.getAction() == MotionEvent.ACTION_UP) {
 * // 逻辑处理 //当手指离开的时候 if(!lastChooseTime.equals(dataData)){
 * if(!threadIsRunning){ new Thread(checkScrollFinished).start(); } }
 * //开启线程监视滑动是否停止 }
 * 
 * return super.onTouchEvent(event); }
 * 
 * }
 */