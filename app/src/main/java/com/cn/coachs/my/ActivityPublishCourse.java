package com.cn.coachs.my;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.baidumap.AcitivityBaiduMap;
import com.cn.coachs.coach.model.BeanAddress;
import com.cn.coachs.coach.model.BeanCourseType;
import com.cn.coachs.coach.model.BeanSubmitCourse;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.utils.ToastUtil;
import com.cn.coachs.ui.chat.common.view.SettingItem;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.DownloadHeadPic;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.MultiChoicePopWindow;
import com.cn.coachs.util.StringUtil;
import com.cn.coachs.util.UtilsImage;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.util.superdatepicker.CustomNumberPicker;
import com.cn.coachs.util.superdatepicker.DatePicker;
import com.cn.coachs.util.superdatepicker.TimePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 专家信息修改页面
 *
 * @author kuangtiecheng
 */

public class ActivityPublishCourse extends ActivityBasic implements
        OnClickListener {
    private String price, time1, courseName, courseaim, serverTypeStr, courseFunction, courseIntroduction, userAccount, courseType, numMax, teacheraddress, teacherTime,
            courseStartTime, timeyearmonth, userRegion, userGoodat,
            userPositon;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String imageUrl = "";
    private int resultdata;
    // private static String path = AppDisk.diskLocal + "myHead/";// sd路径
    private EditText mMyCoueseName;
    private EditText mMyFinalPrice;
    private CustomNumberPicker mMyCourseTimeEnd, mMyCourseType, mMyCourseFunction, mMyMaxNumber, mMyServeType;
    private EditText mMyIntroduction;
    private DatePicker mMyCourseTime;
    private TimePicker mMCouursrTime1;
    private SettingItem mMyAddress, mMyAim;
    private FButton submitButton;
    private List<BeanCourseType> tempInfoList;
    private String programTypeID = null;
//    private SettingItem myTwoDimensionCode;
//    private CustomNumberPicker mMyWeight;
//    private CustomNumberPicker etSection;
//    private List<BeanRegion> RegionList = new ArrayList<BeanRegion>();
//    private String basicFolder, headName;
//    private long userId;

    public static int type1;
    private MultiChoicePopWindow mMultiChoicePopWindow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_course);
        tempInfoList = new ArrayList<BeanCourseType>();
        setdata();
        initial();
        initDataPopWindow();

    }

    private void setdata() {
        UtilsSharedData.initDataShare(this);
        options = AppMain.initImageOptions(R.drawable.default_user_icon, true);// 构建完成
        imageLoader = ImageLoader.getInstance();
    }

    private void initial() {
        ((TextView) findViewById(R.id.middle_tv)).setText("发布课程");
//        userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
        mRootView = findViewById(R.id.rootView);
        submitButton = (FButton) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        mMyCoueseName = (EditText) findViewById(R.id.course_name_txt);
        mMyCoueseName.addTextChangedListener(new CustomTextWacher(3));
        mMyCoueseName.setHintTextColor(getResources().getColor(R.color.font_gray));
        mMyCoueseName.setOnClickListener(this);

        mMyFinalPrice = (EditText) findViewById(R.id.item_finalPrice);
        mMyFinalPrice.addTextChangedListener(new CustomTextWacher(0));
        mMyFinalPrice.setHintTextColor(getResources().getColor(R.color.font_gray));
        mMyFinalPrice.setOnClickListener(this);

        mMyCourseType = (CustomNumberPicker) findViewById(R.id.item_course_type);
        mMyCourseType.addTextChangedListener(new CustomTextWacher(1));
//        mMyCourseType.setHint(userGender);
//        List<String> list = Arrays.asList(getResources().getStringArray(
//                R.array.selection));
//        List<String> arrayList = new ArrayList<String>(list);
//        mMyCourseType.setList(arrayList);
        mMyCourseType.setTips("课程类别");

        mMyCourseFunction = (CustomNumberPicker) findViewById(R.id.item_course_function);
        mMyCourseFunction.addTextChangedListener(new CustomTextWacher(13));
//        mMyCourseType.setHint(userGender);
        List<String> list = Arrays.asList(getResources().getStringArray(
                R.array.coursefonction));
        List<String> arrayList = new ArrayList<String>(list);
        mMyCourseFunction.setList(arrayList);
        mMyCourseFunction.setTips("课程功效");

        mMyMaxNumber = (CustomNumberPicker) findViewById(R.id.item_max_number);
        mMyMaxNumber.addTextChangedListener(new CustomTextWacher(8));
        mMyMaxNumber.setHint(numMax);
//        List<String> list1 = Arrays.asList(getResources().getStringArray(
//                R.array.selection));
        List<String> arrayList1 = new ArrayList<String>();
        for (int i = 1; i < 210; i++) {
            String num = i + "人";
            arrayList1.add(num);
            if (i > 9) {
                i += 9;
            }
//            if(i>98){
//                i +=20;
//            }
        }
        mMyMaxNumber.setList(arrayList1);
        mMyMaxNumber.setTips("人数上限");

        mMyServeType = (CustomNumberPicker) findViewById(R.id.item_serve_type);
        mMyServeType.addTextChangedListener(new CustomTextWacher(9));
        mMyServeType.setHint(teacherTime);
        List<String> list2 = Arrays.asList(getResources().getStringArray(
                R.array.teacherway));
        List<String> arrayList2 = new ArrayList<String>(list2);
        mMyServeType.setList(arrayList2);
        mMyServeType.setTips("服务类型");
        mMyAddress = (SettingItem) findViewById(R.id.item_address);
        mMyAddress.setOnClickListener(this);
        mMyAddress.setCheckText("定位");
        mMyAim = (SettingItem) findViewById(R.id.item_aim);
        mMyAim.setOnClickListener(this);
        mMyAim.setCheckText("选择");


        mMyCourseTime = (DatePicker) findViewById(R.id.item_course_time_1);
        mMyCourseTime.addTextChangedListener(new CustomTextWacher(2));
        mMyCourseTime.setHint(courseStartTime);
        mMCouursrTime1 = (TimePicker) findViewById(R.id.item_course_time_1_time);
        mMCouursrTime1.addTextChangedListener(new CustomTextWacher(15));

        mMyCourseTimeEnd = (CustomNumberPicker) findViewById(R.id.item_course_time_2);
        mMyCourseTimeEnd.addTextChangedListener(new CustomTextWacher(12));
        mMyCourseTimeEnd.setHint(teacherTime);
        List<String> listCourseTimeEnd = Arrays.asList(getResources().getStringArray(
                R.array.coursetime));
        List<String> listCourseTimeEnd2 = new ArrayList<String>(listCourseTimeEnd);
        mMyCourseTimeEnd.setList(listCourseTimeEnd2);
        mMyCourseTimeEnd.setTips("请选择课程时长");


//        myTwoDimensionCode = (SettingItem) findViewById(R.id.item_myposition);
//        myTwoDimensionCode.setOnClickListener(this);
//        myTwoDimensionCode.setIndicatePic(R.drawable.iconfont_erweima);

        mMyIntroduction = (EditText) findViewById(R.id.item_myhospital);
        mMyIntroduction.addTextChangedListener(new CustomTextWacher(5));
        mMyIntroduction.setHint(courseIntroduction);
        mMyIntroduction.setHintTextColor(getResources().getColor(
                R.color.font_gray));
        mMyIntroduction.setOnClickListener(this);
//        imageUrl = UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
//        imageLoader.displayImage(AbsParam.getBaseUrl() + imageUrl, iv, options);
        submitButton.setVisibility(View.VISIBLE);
        // path=AppDisk.appInursePath+userAccount+File.separator+AppDisk.MYHEAD;
        new MyThread().start();

    }

    public class MyThread extends Thread {
        public void run() {
            DownloadHeadPic downpic = new DownloadHeadPic(
                    ActivityPublishCourse.this, "head.jpg",
                    AbsParam.getBaseUrl() + imageUrl);// 保存头像
            try {
                downpic.CreateFilede();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private List<String> mMultiDataList;

    public void initDataPopWindow() {
        List<String> list2 = Arrays.asList(getResources().getStringArray(
                R.array.courseaim));
        mMultiDataList = new ArrayList<String>(list2);
        boolean booleans[] = new boolean[mMultiDataList.size()];
        initPopWindow(booleans);

    }

    private View mRootView;

    public void initPopWindow(boolean[] booleans) {

        mMultiChoicePopWindow = new MultiChoicePopWindow(this, mRootView,
                mMultiDataList, booleans);
        mMultiChoicePopWindow.setTitle("目的(最多选3个)");
        mMultiChoicePopWindow.setOnOKButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean[] selItems = mMultiChoicePopWindow.getSelectItem();
                int size = selItems.length;
                StringBuffer stringBufferAim = new StringBuffer();
                for (int i = 0; i < size; i++) {
                    if (selItems[i]) {
                        stringBufferAim.append(mMultiDataList.get(i) + ",");

                    }

                }
                courseaim = stringBufferAim.toString().substring(0, stringBufferAim.toString().length() - 1);
                mMyAim.setCheckText(courseaim);
//                showToastDialog("selItems = " + stringBuffer.toString());
            }
        });
    }

    /*
     * 修改信息弹窗
     */
    private void changeDialog(final String hint, String content, String title,
                              final EditText et) {

        final EditText inputServer = new EditText(this);
        if (courseIntroduction == "") {
            inputServer.setText(hint);
        } else {
            inputServer.setText(content);
        }
        inputServer.setMinLines(8);
        inputServer.setGravity(Gravity.TOP);
        inputServer.setSingleLine(false);
        inputServer
                .setHintTextColor(getResources().getColor(R.color.font_gray));
        inputServer.setTextColor(Color.BLACK);
        inputServer.setHorizontalScrollBarEnabled(false);
        if (fixtype == 1) {
            inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        inputServer.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int editStart;
            private int editEnd, count;

            private int cursorPos = 0;
            // //输入表情前EditText中的文本
            private String tmp;
            // 是否重置了EditText的内容
            private boolean resetText = false;
            private int editStart1;
            private int editEnd1;

            @Override
            public void afterTextChanged(Editable arg0) {
                switch (fixtype) {
                    case 0:
                        editStart = inputServer.getSelectionStart();
                        editEnd = inputServer.getSelectionEnd();
                        String name1 = temp.toString();
                        if (StringUtil.isEmpty(name1)) {
                            ToastUtil.showMessage("输入不能为空");
                        } else {
                            if (StringUtil.isName1(name1)) {
                            } else {
                                arg0.delete(editEnd - count, editEnd);
                                inputServer.setText(arg0);
                                inputServer.setSelection(arg0.toString().length());
                                ToastUtil
                                        .showMessage("请输入1-4位字母或数字或者中文或三者组合,请重新输入");
                            }

                        }
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;

                    default:
                        break;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                switch (fixtype) {
                    case 0:
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        if (!resetText) {
                            cursorPos = inputServer.getSelectionEnd();
                            tmp = arg0.toString();// 这里用s.toString()而不直接用s是因为如果用s，那么，tmp和s在内存中指向的是同一个地址，s改变了，tmp也就改变了，那么表情过滤就失败了
                        }
                        break;

                    default:
                        break;
                }

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                switch (fixtype) {
                    case 0:
                        temp = arg0;
                        count = arg3;
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        editEnd1 = inputServer.getSelectionEnd();
                        if (!resetText) {
                            if (arg3 >= 0) {// 表情符号的字符长度最小为3
                                // 提取输入的长度大于3的文本
                                CharSequence input = arg0.subSequence(editEnd1
                                        - arg3, editEnd1);
                                // 正则匹配是否是表情符号
                                String in = input.toString();
                                if (!StringUtil.isText(in)) {
                                    resetText = true;
                                    // 是表情符号就将文本还原为输入表情符号之前的内容
                                    inputServer.setText(tmp);
                                    inputServer.setSelection(tmp.length());
                                    Toast.makeText(ActivityPublishCourse.this,
                                            "不支持输入", Toast.LENGTH_SHORT).show();

                                }
                            }
                        } else {
                            resetText = false;
                        }
                        break;

                    default:
                        break;
                }

            }

        });
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle(title);
        builder.setContentView(inputServer);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (!TextUtils.isEmpty(inputServer.getText())
                        && !(inputServer.getText().toString().equals(hint))) {
                    et.setText(inputServer.getText().toString());
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

    private class CustomTextWacher implements TextWatcher {
        int type;

        public CustomTextWacher(int type) {
            this.type = type;

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
            type1 = type;
            // TODO Auto-generated method stub
            switch (type) {
                case 0:
                    price = arg0.toString();
                    break;
                case 1:
                    courseType = arg0.toString();
                    break;
                case 8:
                    numMax = arg0.toString();
                    numMax = numMax.substring(0, numMax.length() - 1);
                    break;
                case 9:
                    serverTypeStr = arg0.toString();
                    break;
                case 11:
                    teacheraddress = arg0.toString();
                    break;
                case 2:
                    courseStartTime = arg0.toString();
                    break;
                case 3:
                    courseName = arg0.toString();
                    break;
                case 5://课程简介
                    courseIntroduction = arg0.toString();
                    break;
                case 7:
                    userPositon = arg0.toString();
                    break;
                case 12:
                    teacherTime = arg0.toString();
                    teacherTime = teacherTime.substring(0, teacherTime.length() - 2);//去掉分钟
                    break;
                case 13:
                    courseFunction = arg0.toString();
                    break;
                case 15:
                    time1 = arg0.toString();
                default:
                    break;
            }

        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        showProgressBar();
        QueryCourseType task = new QueryCourseType();
        task.execute();

    }

    /***
     * 0:我的姓名1:我的简介2:我的医院3:我的擅长4:我的职称
     */
    private int fixtype = 0;

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.item_aim:
                showMultiChoiceWindow();
                break;
            case R.id.item_address:
                intent = new Intent(ActivityPublishCourse.this, AcitivityBaiduMap.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.course_name_txt: /* 课程名称 */
                fixtype = 0;
                changeDialog("", courseName, "课程名称", mMyCoueseName);

                // showDialog();
                break;
            case R.id.item_finalPrice: /* 课程价格*/
                fixtype = 1;
                changeDialog("", price, "课程价格", mMyFinalPrice);

                break;
            case R.id.item_myhospital: /* 课程简介 */
                fixtype = -1;
                changeDialog("暂无简介", courseIntroduction, "课程简介", mMyIntroduction);

                break;
            case R.id.pic:
                UtilsImage.displayBigPic(ActivityPublishCourse.this, imageUrl);
                break;
            case R.id.submit_button:

                if (time1 != null && courseIntroduction != null && teacherTime != null && price != null && courseaim != null && numMax != null && courseType != null && serverTypeStr != null && courseStartTime != null && courseFunction != null && courseName != null) {
                    timeyearmonth = courseStartTime + " " + time1 + ":00";
                    updateData();
                } else {
                    showToastDialog("请填写完整信息");
                }
                break;
            default:
                break;
        }
    }

    public void showMultiChoiceWindow() {
        mMultiChoicePopWindow.show(true);

    }

    /**
     * 提交修改的信息数据
     */
    private void updateData() {
        for (BeanCourseType bean : tempInfoList) {
            if (courseType.equals(bean.getProgramName())) {
                programTypeID = bean.getProgramID() + "";
            }

        }

        long userid = UtilsSharedData.getLong(Constant.USER_ID, 1);
        showProgressBar();
        AscyncSubmitCourseInfo tasksci = new AscyncSubmitCourseInfo(this, "", userid, courseIntroduction, teacherTime, price, courseaim, beanAddress.getLatitude(), beanAddress.getAddress(), beanAddress.getLongitude(), numMax, programTypeID, serverTypeStr, timeyearmonth, courseFunction, courseName) {
            @Override
            protected void onPostExecute(BeanSubmitCourse beanSubmitCourse) {
                super.onPostExecute(beanSubmitCourse);
                hideProgressBar();
                if (beanSubmitCourse == null) {
                    return;
                }
                if (beanSubmitCourse.getResult() == 1) {
                    Intent intent = new Intent(ActivityPublishCourse.this, ActivityPublishCourseImage.class);
                    intent.putExtra("classid", beanSubmitCourse.getClassID());
                    startActivity(intent);

                } else {
                    showToastDialog("信息有误请重新填写");
                }
            }
        };
        tasksci.execute();
    }

    BeanAddress beanAddress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        beanAddress = new BeanAddress();
        if (data != null) {
            if (requestCode == 0) {

                beanAddress = (BeanAddress) data.getSerializableExtra("beanAddress");
                mMyAddress.setCheckText(beanAddress.getAddress());
                Log.i("address", "address" + beanAddress.toString());
            }

        }

//        if (requestCode == GalleryHelper.GALLERY_REQUEST_CODE) {
//            if (resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS) {
//                BeanPhotoInfo photoInfo = data
//                        .getParcelableExtra(GalleryHelper.RESULT_DATA);
//                List<BeanPhotoInfo> photoInfoList = (List<BeanPhotoInfo>) data
//                        .getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);
//
//
//            }
//        }
    }

    private class QueryCourseType extends AsyncTask<Integer, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            try {
                String url = AbsParam.getBaseUrl() + "/ad/app/getprogramlist";
                Log.i("result", url + param.toString());
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                jsonToArray(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            List<String> arrayList = new ArrayList<String>();
            for (BeanCourseType bean : tempInfoList) {
                arrayList.add(bean.getProgramName());
            }
            mMyCourseType.setList(arrayList);

        }
    }


    private void jsonToArray(String json) {
        Gson gson = new Gson();
        if (json != "") {
            tempInfoList = gson.fromJson(json, new TypeToken<List<BeanCourseType>>() {
            }.getType());
        }

    }


}
