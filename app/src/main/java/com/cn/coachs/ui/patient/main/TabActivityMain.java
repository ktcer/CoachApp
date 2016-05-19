package com.cn.coachs.ui.patient.main;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.broadcast.AlarmManagerReceiver;
import com.cn.coachs.my.ActivityMyPage;
import com.cn.coachs.service.ServiceGps;
import com.cn.coachs.ui.AppPool;
import com.cn.coachs.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.coachs.ui.chat.common.utils.ECPreferences;
import com.cn.coachs.ui.chat.ui.ActivityHealthAssist;
import com.cn.coachs.ui.patient.main.healthclass.ActivityClassRoom;
import com.cn.coachs.ui.patient.main.healthdiary.ActivityHealthDiary;
import com.cn.coachs.ui.patient.main.healthdiary.alarm.AlarmDBHelper;
import com.cn.coachs.ui.patient.main.healthdiary.alarm.AlarmModel;
import com.cn.coachs.ui.welcome.AscycUpData;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.SystemBarTintManager;
import com.cn.coachs.util.UtilsSharedData;

public class TabActivityMain extends TabActivity implements
        View.OnClickListener {
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
    private AlarmModel alarmDetails;
    private SystemBarTintManager tintManager;
    private static LinearLayout layoutTop;
    private long mExitTime;
    private Button rightMenu;
    private static TabHost mTabHost;
    public static int selectDetetcMember = 0;
    private String userID, userAccount;
    private Intent intentLocation;
    private Intent iDiary;
    private Intent iAssist;
    private Intent iPost;
    private Intent iMe;
    private static ImageButton mBt0;
    private static ImageButton mBt2;
    private static ImageButton mBt3;
    private static View mBt1;
    public static View mRedMark; // 秘书消息的红点

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UtilsSharedData.initDataShare(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.statusbar_bg);

        AppPool.createActivity(this);
        AscycUpData ascycUpdate = new AscycUpData(this);
        ascycUpdate.execute();
        setContentView(R.layout.activity_main);

        getData();
        setUpMenu();
        setupTabs(savedInstanceState);
        setListener();
//		setRemind();
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void getData() {
        UtilsSharedData.initDataShare(this);// ////////
        userID = UtilsSharedData.getLong(Constant.USER_ID, 1) + "";
        userAccount = UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT);
    }

    private void startLocationService() {
        initGPS();
        intentLocation = new Intent(this, ServiceGps.class);
        intentLocation.putExtra(Constant.USER_ID, userID);
        intentLocation.putExtra(Constant.USER_ACCOUNT, userAccount);
        startService(intentLocation);
    }

    private void setupTabs(Bundle savedInstanceState) {

        mTabHost = getTabHost();
        iAssist = new Intent(this, ActivityHealthAssist.class);
        iAssist.putExtra("launcher_from", 1);
        mTabHost.addTab(mTabHost
                .newTabSpec(Constant.TAB_CONSULT)
                .setIndicator(
                        getResources().getString(R.string.action_settings),
                        getResources().getDrawable(R.drawable.icon))
                .setContent(iAssist));

        iDiary = new Intent(this, ActivityHealthDiary.class);
        mTabHost.addTab(mTabHost
                .newTabSpec(Constant.TAB_DIARY)
                .setIndicator(
                        getResources().getString(R.string.action_settings),
                        getResources().getDrawable(R.drawable.icon))
                .setContent(iDiary));
        iPost = new Intent(this, ActivityClassRoom.class);// ActivityHealthPostList
        mTabHost.addTab(mTabHost
                .newTabSpec(Constant.TAB_ACTIVITY)
                .setIndicator(
                        getResources().getString(R.string.action_settings),
                        getResources().getDrawable(R.drawable.icon))
                .setContent(iPost));
        iMe = new Intent(this, ActivityMyPage.class);//  ActivityMyAccountCenter
        mTabHost.addTab(mTabHost
                .newTabSpec(Constant.TAB_SETTING)
                .setIndicator(
                        getResources().getString(R.string.action_settings),
                        getResources().getDrawable(R.drawable.icon))
                .setContent(iMe));

        mBt0 = (ImageButton) findViewById(R.id.tab_iv_0);
        mBt1 = findViewById(R.id.tab_iv_1);
        mBt2 = (ImageButton) findViewById(R.id.tab_iv_2);
        mBt3 = (ImageButton) findViewById(R.id.tab_iv_3);
        mRedMark = findViewById(R.id.red_mark);
        mBt0.setSelected(true);
        mBt1.setSelected(false);
        mBt2.setSelected(false);
        mBt3.setSelected(false);
        mBt0.setOnClickListener(this);
        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
        mBt3.setOnClickListener(this);
    }

    private static void switchTabChoosed(int tab) {
        // mCurrntTabInt = tab;
        switch (tab) {
            case 0:
                layoutTop.setVisibility(View.VISIBLE);
                mBt0.setSelected(true);
                mBt1.setSelected(false);
                mBt2.setSelected(false);
                mBt3.setSelected(false);
                break;
            case 1:
                layoutTop.setVisibility(View.VISIBLE);
                mBt0.setSelected(false);
                mBt1.setSelected(true);
                mBt2.setSelected(false);
                mBt3.setSelected(false);
                break;
            case 2:
                layoutTop.setVisibility(View.VISIBLE);
                mBt0.setSelected(false);
                mBt1.setSelected(false);
                mBt2.setSelected(true);
                mBt3.setSelected(false);
                break;
            case 3:
                layoutTop.setVisibility(View.GONE);
                mBt0.setSelected(false);
                mBt1.setSelected(false);
                mBt2.setSelected(false);
                mBt3.setSelected(true);
                break;

            default:
                break;
        }
    }


    @SuppressWarnings("deprecation")
    private void setUpMenu() {
        layoutTop = (LinearLayout) findViewById(R.id.layout_top);
        rightMenu = (Button) findViewById(R.id.title_bar_right_menu);
    }

    private void setListener() {
        rightMenu.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.icon_scale));
        changePage(view.getId());
        switch (view.getId()) {

            case R.id.title_bar_right_menu:

                break;

            default:
                // Log.e(TAG, "tabs 5 or -1");
                break;
        }

        // }
    }

    private void setRemind() {
        boolean shakeSetting = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND.getId(),
                (Boolean) ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND.getDefaultValue());
        if (shakeSetting) {
            for (int i = 1; i < 5; i++) {
                if ((alarmDetails = dbHelper.getAlarm(i)) == null) {
                    alarmDetails = new AlarmModel();
                    switch (i) {
                        case 1:
                            updateAlarmModel(8, 30);
                            break;
                        case 2:
                            updateAlarmModel(9, 0);
                            break;
                        case 3:
                            updateAlarmModel(14, 30);
                            break;
                        case 4:
                            updateAlarmModel(15, 30);
                            break;

                        default:
                            break;
                    }
//	        			updateAlarmModel(8,30);
                    dbHelper.createAlarm(alarmDetails);
                }
                AlarmManagerReceiver.cancelAlarms(this);
                AlarmManagerReceiver.setAlarms(this);
            }
        }
    }

    private void updateAlarmModel(int hour, int minute) {
        alarmDetails.timeMinute = minute;
        alarmDetails.timeHour = hour;
        // alarmDetails.name = edtName.getText().toString();
        alarmDetails.setRepeatingDay(AlarmModel.SUNDAY, true);
        alarmDetails.setRepeatingDay(AlarmModel.MONDAY, true);
        alarmDetails.setRepeatingDay(AlarmModel.TUESDAY, true);
        alarmDetails.setRepeatingDay(AlarmModel.WEDNESDAY, true);
        alarmDetails.setRepeatingDay(AlarmModel.THURSDAY, true);
        alarmDetails.setRepeatingDay(AlarmModel.FRDIAY, true);
        alarmDetails.setRepeatingDay(AlarmModel.SATURDAY, true);
        alarmDetails.isEnabled = true;
    }

    public static void changePage(int id) {
        switch (id) {

            case R.id.tab_iv_0:
                mTabHost.setCurrentTabByTag(Constant.TAB_CONSULT);
                switchTabChoosed(0);
                break;
            case R.id.tab_iv_1:
                mTabHost.setCurrentTabByTag(Constant.TAB_DIARY);
                switchTabChoosed(1);
                break;
            case R.id.tab_iv_2:
                mTabHost.setCurrentTabByTag(Constant.TAB_ACTIVITY);
                switchTabChoosed(2);
                break;
            case R.id.tab_iv_3:
                mTabHost.setCurrentTabByTag(Constant.TAB_SETTING);
                switchTabChoosed(3);
                break;
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                // 判断菜单是否关闭
                // if (is_closed) {
                // 判断两次点击的时间间隔（默认设置为2秒）
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();

                    mExitTime = System.currentTimeMillis();
                } else {
                    // stopService(intentLocation);
                    // MakeCall.logout(getApplicationContext());
                    AppPool.finishAllAct();
                    finish();
                    System.exit(0);
                    super.onBackPressed();
                }
                // }
                // else {
                // resideMenu.closeMenu();
                // }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("为了我们能为您提供更好的服务，您需要打开设备的定位功能，是否继续？");
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                            dialog.dismiss();

                        }
                    });
            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();

        }
    }

}
