package com.cn.coachs.ui.patient.others.myaccount;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.baidumap.AcitivityBaiduMap;
import com.cn.coachs.coach.model.BeanAddress;
import com.cn.coachs.coach.model.BeanCourseType;
import com.cn.coachs.coach.model.BeandituserUIinfo;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.utils.ToastUtil;
import com.cn.coachs.ui.chat.common.view.SettingItem;
import com.cn.coachs.ui.patient.main.TabActivityMain;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.CircleImageView;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.DownloadHeadPic;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.FileUtils;
import com.cn.coachs.util.StringUtil;
import com.cn.coachs.util.UtilsImage;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.util.customgallery.GalleryHelper;
import com.cn.coachs.util.customgallery.model.BeanPhotoInfo;
import com.cn.coachs.util.superdatepicker.CustomNumberPicker;
import com.cn.coachs.util.superdatepicker.DatePicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

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

public class ActivityPersonalInfo extends ActivityBasic implements
        OnClickListener {
    private ImageView iv;
    private String userName, userIntroduction, userAccount, userGender, athleticsType, teacheraddress, teacherTime,
            userBirth, userRegion, userGoodat,
            userPositon;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String imageUrl = "";
    private int resultdata;
    // private static String path = AppDisk.diskLocal + "myHead/";// sd路径
    private RelativeLayout mMyPic;
    private SettingItem mMyAccount;
    private EditText mMyName;
    private CustomNumberPicker mMySex, mMysectionis, mMyTime;
    private EditText mMyIntroduction, mMyGoodat;
    private DatePicker mMyBirth;
    private FButton submitButton;
    private SettingItem mMyAddress;
//    private SettingItem myTwoDimensionCode;
//    private CustomNumberPicker mMyWeight;
//    private CustomNumberPicker etSection;
//    private List<BeanRegion> RegionList = new ArrayList<BeanRegion>();
//    private String basicFolder, headName;
//    private long userId;

    public static int type1;
    private long ifedeitinfo;
    private String latitude;
    private String longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);
        setdata();
        initial();
    }

    private void setdata() {
        UtilsSharedData.initDataShare(this);
        options = AppMain.initImageOptions(R.drawable.default_user_icon, true);// 构建完成
        imageLoader = ImageLoader.getInstance();
    }

    private void initial() {
        ((TextView) findViewById(R.id.middle_tv)).setText("编辑信息");
        userName = UtilsSharedData.getValueByKey(Constant.USER_NAME);
        userIntroduction = UtilsSharedData
                .getValueByKey(Constant.USER_INTRODUCTION);
        userAccount = UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT);
        userGender = UtilsSharedData.getValueByKey(Constant.USER_GENDER);
        userBirth = UtilsSharedData.getValueByKey(Constant.USER_BIRTHDAY);
//        userRegionName = UtilsSharedData.getValueByKey(Constant.USER_REGION);
        userGoodat = UtilsSharedData.getValueByKey(Constant.USER_GOODAT);
        userPositon = UtilsSharedData.getValueByKey(Constant.USER_POSITION);
        athleticsType = UtilsSharedData.getValueByKey(Constant.ATHLETICSTYPE);
        teacherTime = UtilsSharedData.getValueByKey(Constant.TEACHTIME);
        teacheraddress = UtilsSharedData.getValueByKey(Constant.TEACHSDDRESS);
//        userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
        iv = (ImageView) findViewById(R.id.pic);
        iv.setOnClickListener(this);
        submitButton = (FButton) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(this);

        mMyPic = (RelativeLayout) findViewById(R.id.item_pic);
        mMyPic.setOnClickListener(this);

        mMyAccount = (SettingItem) findViewById(R.id.item_mytel);
        mMyAccount.setCheckText(userAccount);

        mMyName = (EditText) findViewById(R.id.item_myname);
        mMyName.addTextChangedListener(new CustomTextWacher(0));
        mMyName.setHint(userName);
        mMyName.setHintTextColor(getResources().getColor(R.color.font_gray));
        mMyName.setOnClickListener(this);

        mMySex = (CustomNumberPicker) findViewById(R.id.item_mysex);
        mMySex.addTextChangedListener(new CustomTextWacher(1));
        mMySex.setHint(userGender);
        List<String> list = Arrays.asList(getResources().getStringArray(
                R.array.gender));
        List<String> arrayList = new ArrayList<String>(list);
        mMySex.setList(arrayList);
        mMySex.setTips("请选择您的性别");

        mMysectionis = (CustomNumberPicker) findViewById(R.id.item_mysection);
        mMysectionis.addTextChangedListener(new CustomTextWacher(8));
        mMysectionis.setHint(athleticsType);
//        List<String> list1 = Arrays.asList(getResources().getStringArray(
//                R.array.selection));
//        List<String> arrayList1 = new ArrayList<String>(list1);
//        mMysectionis.setList(arrayList1);
        mMysectionis.setTips("请选择您的运动类型");

        mMyTime = (CustomNumberPicker) findViewById(R.id.item_mytime);
        mMyTime.addTextChangedListener(new CustomTextWacher(9));
        mMyTime.setHint(teacherTime);
        List<String> list2 = Arrays.asList(getResources().getStringArray(
                R.array.teachertime));
        List<String> arrayList2 = new ArrayList<String>(list2);
        mMyTime.setList(arrayList2);
        mMyTime.setTips("请选择您的执照时间");
        mMyAddress = (SettingItem) findViewById(R.id.item_address);
        mMyAddress.setOnClickListener(this);
        if (teacheraddress == null) {
            mMyAddress.setCheckText("定位");
        } else {
            mMyAddress.setCheckText(teacheraddress);
        }


        mMyBirth = (DatePicker) findViewById(R.id.item_mybirth);
        mMyBirth.addTextChangedListener(new CustomTextWacher(2));
        mMyBirth.setHint(userBirth);

        mMyGoodat = (EditText) findViewById(R.id.item_myposition);
        mMyGoodat.addTextChangedListener(new CustomTextWacher(6));
        mMyGoodat.setHint(userGoodat);
        mMyGoodat.setHintTextColor(getResources().getColor(R.color.font_gray));
        mMyGoodat.setOnClickListener(this);


//        myTwoDimensionCode = (SettingItem) findViewById(R.id.item_myposition);
//        myTwoDimensionCode.setOnClickListener(this);
//        myTwoDimensionCode.setIndicatePic(R.drawable.iconfont_erweima);

        mMyIntroduction = (EditText) findViewById(R.id.item_myhospital);
        mMyIntroduction.addTextChangedListener(new CustomTextWacher(5));
        mMyIntroduction.setHint(userIntroduction);
        mMyIntroduction.setHintTextColor(getResources().getColor(
                R.color.font_gray));
        mMyIntroduction.setOnClickListener(this);
        imageUrl = UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
        imageLoader.displayImage(AbsParam.getBaseUrl() + imageUrl, iv, options);
        ifedeitinfo = UtilsSharedData.getLong(Constant.IFEDITINFO, 1);
        if (ifedeitinfo == 1) {
            submitButton.setVisibility(View.GONE);
        } else {
            submitButton.setVisibility(View.VISIBLE);
        }
        // path=AppDisk.appInursePath+userAccount+File.separator+AppDisk.MYHEAD;
        new MyThread().start();

    }

    public class MyThread extends Thread {
        public void run() {
            DownloadHeadPic downpic = new DownloadHeadPic(
                    ActivityPersonalInfo.this, "head.jpg",
                    AbsParam.getBaseUrl() + imageUrl);// 保存头像
            try {
                downpic.CreateFilede();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    /*
     * 修改信息弹窗
     */
    private void changeDialog(final String hint, String content, String title,
                              final EditText et) {

        final EditText inputServer = new EditText(this);
        if (userIntroduction == "") {
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
                            if (StringUtil.isName(name1)) {
                            } else {
                                arg0.delete(editEnd - count, editEnd);
                                inputServer.setText(arg0);
                                inputServer.setSelection(arg0.toString().length());
                                ToastUtil
                                        .showMessage("请输入1-15位字母或数字或者中文或三者组合,请重新输入");
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
                                    Toast.makeText(ActivityPersonalInfo.this,
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
                    userName = arg0.toString();
                    break;
                case 1:
                    userGender = arg0.toString();
                    break;
                case 8:
                    athleticsType = arg0.toString();
                    break;
                case 9:
                    teacherTime = arg0.toString();
                    break;
                case 2:
                    userBirth = arg0.toString();
                    break;
//                case 3:
//                    for (BeanRegion bean : RegionList) {
//                        if (bean.getRegionName().equals(arg0.toString())) {
//                            userRegion = bean.getRegionID() + "";
//                            userRegionName = arg0.toString();
//                            break;
//                        }
//                    }
//                    break;
                case 5:
                    userIntroduction = arg0.toString();
                    break;
                case 6:
                    userGoodat = arg0.toString();
                    break;
                case 7:
                    userPositon = arg0.toString();
                    break;
                default:
                    break;
            }
            if (ifedeitinfo == 1) {
                updateData();
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
            case R.id.item_address:
                intent = new Intent(ActivityPersonalInfo.this, AcitivityBaiduMap.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.item_pic: /* 设置 */
                GalleryHelper.openGallerySingle(ActivityPersonalInfo.this, true,
                        new GalleryImageLoader());
                // showDialog();
                break;
            case R.id.item_myname: /* 修改名称 */
                changeDialog("暂无姓名", userName, "我的姓名", mMyName);
                fixtype = 0;
                break;
            case R.id.item_myhospital: /* 修改简介 */
                changeDialog("暂无简介", userIntroduction, "我的简介", mMyIntroduction);
                fixtype = 1;
                break;
//            case R.id.item_myhospital: /* 修改医院 */
//                // changeDialog("暂无医院", userHospital, "我的医院", mMyHospital);
//                intent = new Intent(this, ActivitySearchHospital.class);
//                intent.putExtra(Constant.HOSPITAL_NAME, userHospital);
//                intent.putExtra(Constant.HOSPITAL_EDIT, true);
//                startActivityForResult(intent, Constant.HOSPITAL_RESULT_CODE_EDIT);
//                // fixtype = 2;
//                break;
            case R.id.pic:
                UtilsImage.displayBigPic(ActivityPersonalInfo.this, imageUrl);
                break;
            case R.id.item_myposition:
                changeDialog("暂无获奖经历", userGoodat, "获奖经历", mMyGoodat);
                fixtype = 3;
                break;
            case R.id.submit_button:

                if (userName != null &&
                        userGender != null && userBirth != null && teacherTime != null && athleticsType != null && teacheraddress != null && userGoodat != null && userIntroduction != null) {
                    updateData();

                } else {
                    showToastDialog("请填写完整信息");
                }

                break;
            default:
                break;
        }
    }

//    private void getMyTDC() {
//        imageUrl = UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
//        CustomDialog.Builder builder = new CustomDialog.Builder(this);
//        builder.setTitle("我的二维码");
//        builder.setContentView(formTipDialog());
//        builder.setPositiveButton(null, null);// 设置了这个下面的button就消失啦
//        builder.create().show();
//    }

    private ScrollView formTipDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        ScrollView layout = (ScrollView) inflaterDl.inflate(
                R.layout.dialog_mycode, null);
        CircleImageView myHeadImage = (CircleImageView) layout
                .findViewById(R.id.my_head_image);

        TextView myname = (TextView) layout.findViewById(R.id.my_name);
        TextView mystaff = (TextView) layout.findViewById(R.id.my_staff);
        TextView mygoodat = (TextView) layout.findViewById(R.id.my_good_at);

        ImageView mytwodimentioncode = (ImageView) layout
                .findViewById(R.id.my_twodimentioncode);
        myname.setText(userName);
        mystaff.setText(userPositon);
        mygoodat.setText("擅长" + userGoodat);
        imageLoader.displayImage(AbsParam.getBaseUrl() + imageUrl, myHeadImage,
                options);
        ImageLoader.getInstance().displayImage(
                AbsParam.getBaseUrl()
                        + UtilsSharedData.getValueByKey(Constant.USER_TWOCODE),
                mytwodimentioncode,
                AppMain.initImageOptions(R.drawable.default_healthdiary, true));
        // UtilsImage.getcodePic(mytwodimentioncode,"addexpert"
        // + "," + userId + "," + imageUrl,400);
        return layout;
    }

    /**
     * 提交修改的信息数据
     */
    private String programTypeID;

    private void updateData() {
        if (tempInfoList != null) {
            for (BeanCourseType bean : tempInfoList) {
                if (athleticsType.equals(bean.getProgramName())) {
                    programTypeID = bean.getProgramID() + "";
                }

            }
        } else {
            programTypeID = UtilsSharedData.getValueByKey(Constant.PROGRAMTYPEID);
        }
        if (beanAddress == null) {
            longitude = UtilsSharedData.getValueByKey("longitude");
            latitude = UtilsSharedData.getValueByKey("latitude");
            teacheraddress = UtilsSharedData.getValueByKey(Constant.TEACHSDDRESS);
        }


        long userid = UtilsSharedData.getLong(Constant.USER_ID, 1);
        // **提交注册信息*/
        try {
            AscyncSubmitPersonalInfo mTask = new AscyncSubmitPersonalInfo(userid, ActivityPersonalInfo.this, userName,
                    userGender, userBirth, teacherTime, programTypeID, beanAddress.getAddress(), userGoodat, userIntroduction, beanAddress.getLatitude(), beanAddress.getLongitude()) {
                @Override
                protected void onPostExecute(BeandituserUIinfo beandituserUIinfo) {
                    super.onPostExecute(beandituserUIinfo);

                    if (beandituserUIinfo == null) {
                        return;
                    }
                    if (beandituserUIinfo.getResult() == 1) {
                        if (ifedeitinfo == 1) {
                            switch (ActivityPersonalInfo.type1) {
                                case 0:
                                    UtilsSharedData.saveKeyMustValue(Constant.USER_NAME, userName);
                                    break;
                                case 1:
                                    UtilsSharedData.saveKeyMustValue(Constant.USER_GENDER, userGender);
                                    break;
                                case 8:
                                    UtilsSharedData.saveKeyMustValue(Constant.ATHLETICSTYPE, athleticsType);
                                    UtilsSharedData.saveKeyMustValue(Constant.PROGRAMTYPEID, programTypeID);
                                    break;
                                case 9:
                                    UtilsSharedData.saveKeyMustValue(Constant.TEACHTIME, teacherTime);
                                    break;
                                case 2:
                                    UtilsSharedData.saveKeyMustValue(Constant.USER_BIRTHDAY, userBirth);
                                case 5:
                                    UtilsSharedData.saveKeyMustValue(Constant.USER_INTRODUCTION, userIntroduction);
                                    break;
                                case 6:
                                    UtilsSharedData.saveKeyMustValue(Constant.USER_GOODAT, userGoodat);
                                    break;
                            }

                        } else {
                            UtilsSharedData.saveKeyMustValue(Constant.USER_NAME, userName);
                            UtilsSharedData.saveKeyMustValue(Constant.USER_GENDER, userGender);
                            UtilsSharedData.saveKeyMustValue(Constant.ATHLETICSTYPE, athleticsType);
                            UtilsSharedData.saveKeyMustValue(Constant.PROGRAMTYPEID, programTypeID);
                            UtilsSharedData.saveKeyMustValue(Constant.TEACHTIME, teacherTime);
                            UtilsSharedData.saveKeyMustValue(Constant.TEACHSDDRESS, teacheraddress);
                            UtilsSharedData.saveKeyMustValue(Constant.USER_BIRTHDAY, userBirth);
                            UtilsSharedData.saveKeyMustValue(Constant.USER_GOODAT, userGoodat);
                            UtilsSharedData.saveKeyMustValue(Constant.USER_INTRODUCTION, userIntroduction);
                            Intent intents = new Intent(getApplication(), TabActivityMain.class);
                            startActivity(intents);
                            finish();
                        }
                    }
                }
            };
            mTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    BeanAddress beanAddress = new BeanAddress();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 0) {

                beanAddress = (BeanAddress) data.getSerializableExtra("beanAddress");
                mMyAddress.setCheckText(beanAddress.getAddress());
                Log.i("address", "address" + beanAddress.toString());
                latitude = beanAddress.getLatitude() + "";
                longitude = beanAddress.getLongitude() + "";
                UtilsSharedData.saveKeyMustValue(Constant.TEACHSDDRESS, beanAddress.getAddress());
                UtilsSharedData.saveKeyMustValue("latitude", latitude);
                UtilsSharedData.saveKeyMustValue("longitude", longitude);
            }

        }

        if (requestCode == GalleryHelper.GALLERY_REQUEST_CODE) {
            if (resultCode == GalleryHelper.GALLERY_RESULT_SUCCESS) {
                BeanPhotoInfo photoInfo = data
                        .getParcelableExtra(GalleryHelper.RESULT_DATA);
                List<BeanPhotoInfo> photoInfoList = (List<BeanPhotoInfo>) data
                        .getSerializableExtra(GalleryHelper.RESULT_LIST_DATA);

                if (photoInfo != null) {
                    // 修改头像上传服务器
                    ImageLoader.getInstance().displayImage(
                            "file:/" + photoInfo.getPhotoPath(), iv);
                    UpPhotoTask upt = new UpPhotoTask(photoInfo.getPhotoPath());
                    upt.execute();
                }

                if (photoInfoList != null) {
                    Toast.makeText(this, "选择了" + photoInfoList.size() + "张",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String img;

    private class UpPhotoTask extends AsyncTask<Integer, Integer, String> {
        String result;
        String path;

        public UpPhotoTask(String path) {
            this.path = path;
        }

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            long userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
            param.put("userId", userId + "");
//            param.put("userType", 1 + "");// 0是患者，1是护士
            Log.i("input",
                    AbsParam.getBaseUrl() + "/common/imupload"
                            + param.toString());
            try {
                result = NetTool.uploadFile(AbsParam.getBaseUrl()
                        + "/base/app/uploadpicture", path, param, null);
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            if (resultdata == 1) {
                UtilsSharedData.saveKeyMustValue(Constant.USER_IMAGEURL, img);
                imageUrl = AbsParam.getBaseUrl()
                        + UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
                imageLoader.displayImage(imageUrl, iv, options);
                FileUtils.deleteDir();
            } else {
                showToastDialogLongTime("修改头像失败");
            }

        }
    }

    /**
     * @param result
     */
    private void JsonArrayToList(String result) {
        try {
            JSONObject jso = new JSONObject(result);
            resultdata = jso.getInt("result");
            jso.getString("detail");
            img = jso.getString("fileurl");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<BeanCourseType> tempInfoList;

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
            mMysectionis.setList(arrayList);

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
