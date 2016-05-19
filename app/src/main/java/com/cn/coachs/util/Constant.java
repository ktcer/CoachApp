package com.cn.coachs.util;

import android.os.Environment;

public class Constant {
    // 用户端的appId，用于云通讯跨应用通讯
    public static String AIHU_APPID = "8a48b551521b87bc01522a76e3421815";
    // 存储ini的路径
    public static String INI_ROUTE = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/param.ini";
    public static String HASUSED = "hasused";
    // public static String IS_JUMP_TEST = "is_jump_test";

    // public static String INI_ROUTE="/mnt/sdcard/param.ini";
    // 查询返回结果
    public static final int FAIL = 0;
    public static final int COMPLETE = 1;
    public static final String TAB_SETTING = "tab_setting";
    public static final String TAB_DIARY = "tab_diary";
    public static final String TAB_CONSULT = "tab_consult";
    public static final String TAB_ACTIVITY = "tab_activity";
    // 界面传值标识
    // 监测中心
    public static String DETECTION_CENTER = "detection_center";
    public static String DETECTION_XUEYA = "detection_xueya";
    public static String DETECTION_TIWEN = "detection_tiwen";
    public static String DETECTION_XUETANG = "detection_xuetang";
    public static String DETECTION_XUEYANG = "detection_xueyang";
    public static String DETECTION_TIZHONG = "detection_tizhong";
    public static String DETECTION_TIZHI = "detection_tizhi";

    // Accont信息
    public static String USER_ACCOUNT = "useraccount";
    public static String USER_NAME = "username";
    public static String REMARK_NAME = "remarkname";
    public static String USER_IDENTIFY = "useridentify";
    public static String USER_PASS = "userpass";
    public static String LOGIN_STATUS = "loginstatus";
    public static String USER_ID = "userid";
    public static String IFEDITINFO = "ifeditinfo";
    public static String USER_MID = "usermid";
    public static String USER_NUBE = "usernube";
    public static String USER_INTRODUCTION = "userintroduction";
    public static String FLAG = "ishave";
    public static String NURSE_ID = "nurseid";

    public static String USER_REGION = "user_region";
    public static String USER_HOSPITAL = "user_hospital";
    public static String USER_GOODAT = "user_goodat";
    public static String USER_TWOCODE = "user_twocode";
    public static String USER_GENDER = "user_gender";
    public static String USER_BIRTHDAY = "user_birthday";
    public static String USER_GKMUM = "user_gknum";
    public static String USER_IMAGEURL = "user_imagurl";
    public static String USER_PHOTOS = "user_photos";
    public static String USER_CREDITURL = "user_crediturl";
    public static String REMIND_USER_OR_PAITENT = "remind_flag";
    public static String USER_POSITION = "user_position";
    public static String TEACHTIME = "teacherTime";
    public static String TEACHWAY = "teacherway";
    public static String TEACHSDDRESS = "teacheraddress";
    public static String ATHLETICSTYPE = "athleticsType";
    public static String PROGRAMTYPEID = "programTypeID";


    // 界面之间传递图像uri使用
    public static String PHOTO_URI = "photo_uri";
    public static String PHOTO_BMP = "photo_bmp";

    // 专家主页
    public static String PAGE_TYPE = "page_type";
    // public static String[] timeList = {
    // "0:00-0:15","0:15-0:30","0:30-0:45","0:45-1:00","1:00-1:15"
    // ,"1:15-1:30","1:30-1:45","1:45-2:00","2:00-2:15","2:15-2:30",
    // "2:30-2:45","2:45-3:00","3:00-3:15","3:15-3:30","3:30-3:45",
    // "3:45-4:00","4:00-4:15","4:15-4:30","4:30-4:45","4:45-5:00",
    // "5:00-5:15","5:15-5:30","5:30-5:45","5:45-6:00","6:00-6:15",
    // "6:15-6:30","6:30-6:45","6:45-7:00",":00-7:15","7:15-7:30",
    // "7:30-7:45","7:45-8:00","8:00-8:15","8:15-8:30","8:30-8:45",
    // "8:45-9:00","9:00-9:15","9:15-9:30","9:30-9:45","9:45-10:00",
    // "10:00-10:15","10:15-10:30","10:30-10:45","10:45-11:00",
    // "11:00-11:15","11:15-11:30","11:30-11:45","11:45-12:00",
    // "12:00-12:15","12:15-12:30","12:30-12:45","12:45-13:00",
    // "13:00-13:15","13:15-13:30","13:30-13:45","13:45-14:00",
    // "14:00-14:15","14:15-14:30","14:30-14:45","14:45-15:00",
    // "15:00-15:15","15:15-15:30","15:30-15:45","15:45-16:00",
    // "16:00-16:15","16:15-16:30","16:30-16:45","16:45-17:00",
    // "17:00-17:15","17:15-17:30","17:30-17:45","17:45-18:00",
    // "18:00-18:15","18:15-18:30","18:30-18:45","18:45-19:00",
    // "19:00-19:15","19:15-19:30","19:30-19:45","19:45-20:00",
    // "20:00-20:15","20:15-20:30","20:30-20:45","20:45-21:00",
    // "21:00-21:15","21:15-21:30","21:30-21:45","21:45-22:00",
    // "22:00-22:15","22:15-22:30","22:30-22:45","22:45-23:00",
    // "23:00-23:15","23:15-23:30","23:30-23:45","23:45-0:00"};

    // 病情自述
    public static String RESERVE_ID = "reserveID";
    /**
     * 是否是第一从朋友圈跳转到登录页面
     */
    public static Boolean firstEnterLoginFromMoment = true;

    // 视频播放
    public static String VEDIO_URL = "vedio_url";

    // 健康问答
    public static String HEALTH_QA = "health_qa";

    // 康复提醒
    public static String HEALTH_REMIND = "health_remind";

    // 专家访谈
    public static String DOCTOR_INTERVIEW = "doctor_interview";

    // 我的家庭,选中照片位置
    public static String FAMILY_SELECTNUM = "family_selectnum";

    // 是否显示检测提示
    public static String DISPLAY_DETECT_TIP = "display_detect_tip";

    public static String SELECT_MEMBER = "select_member";

    public static String APK_DOWNLOAD_URL = "download_url";

    // 旅游相关
    public static String TOUR_BEAN = "tour_bean";
    // 订单相关
    public static String ORDER_BEAN = "order_bean";
    public static String ORDER_NO = "order_no";
    public static String ORDER_STATUS = "order_status";
    public static String ORDER_PRICE = "order_price";

    // 粉丝相关
    public static String FANS_GENDER = "fans_gender";
    public static String FANS_AGE = "fans_age";
    public static String FANS_HEIGHT = "fans_height";
    public static String FANS_WEIGHT = "fans_weight";
    public static String fANS_IMAGEURL = "fans_imageurl";
    public static String FANS_NAME = "fans_name";
    public static String FANS_ID = "fans_id";
    public static String FANS_ISMASTER = "fans_ismaster";
    // 测评相关
    public static String TESTREPORTDETAIL = "test_report_detail";
    // 健康日记
    public static String START_DATE = "startDate";
    public static String PLAN_LENGTH = "planLength";
    public static String FANS_BEANS = "fansBean";
    public static String FANS_LIST_FLAG = "fansListFlag";
    public static String FAN_SESSIONID = "fan_sessionId";
    public static String NUM_OF_DAYS = "numOfDays";
    public static String BEAN_EDIT_DIARY_NODE = "beanEditDiaryNode";
    public static String LIST_DIARY_NODE_DATA = "listDiaryNodeData";
    public static String PATIENT_ID = "patientId";

    // 医院
    public final static int HOSPITAL_RESULT_CODE = 1;
    public final static int HOSPITAL_RESULT_CODE_EDIT = 2;
    public static final String HOSPITAL_NAME = "hospitalName";
    public static final String HOSPITAL_ID = "hospitalId";
    public static final String HOSPITAL_EDIT = "hospitalEdit";
}
