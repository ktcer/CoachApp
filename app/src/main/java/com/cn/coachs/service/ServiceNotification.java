package com.cn.coachs.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import com.cn.coachs.broadcast.AlarmManagerReceiver;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.patient.others.myaccount.PlayAlarmAty;
import com.cn.coachs.util.UtilsSharedData;

public class ServiceNotification extends Service {
    private WakeLock mWakeLock;
    /**
     * 0：语音1：视频2:文字
     */
    private long flag;

    @Override
    public void onCreate() {
        super.onCreate();
        //判断应用是否在运行
        ActivityManager am = (ActivityManager) ServiceNotification.this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        String MY_PKG_NAME = "com.cn.coachs";
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(MY_PKG_NAME)
                    || info.baseActivity.getPackageName().equals(MY_PKG_NAME)) {
                AppMain.isAppRunning = true;
                break;
            }
        }
        Log.d("FROM SERVICE", "FROM SERVICE");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("FROM SERVICE", "FROM SERVICE");
        UtilsSharedData.initDataShare(this);
        flag = UtilsSharedData.getLong("chooseFalg", 0);
        this.startActivity(PlayAlarmAty.class);
        AlarmManagerReceiver.setAlarms(this);
        stopSelf();
//		switch ((int)flag) {
//		case 1://视频
//			String url=AppDisk.appInursePath+UtilsSharedData.getValueByKey(Constant.USER_ACCOUNT)+File.separator+AppDisk.DCIM_VIDEO+"1.mp4";
//			this.startActivity(AlarmScreen.class,Constant.VEDIO_URL,url);
//			AlarmManagerReceiver.setAlarms(this);
//			stopSelf();
//			break;
//		case 0://语音
//			this.startActivity(AlarmScreenVoice.class);
//			AlarmManagerReceiver.setAlarms(this);
//			stopSelf();
//			break;
//		case 2://文字
//			this.startActivity(AlarmScreenText.class);
//			AlarmManagerReceiver.setAlarms(this);
//			stopSelf();
//			break;
//
//		default:
//			break;
//		}

        return super.onStartCommand(intent, flags, startId);

    }

    protected void startActivity(Class<?> cls, String key, String value) {
        Intent alarmIntent = new Intent(getBaseContext(), cls);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmIntent.putExtra(key, value);
        getApplication().startActivity(alarmIntent);
    }

    protected void startActivity(Class<?> cls) {
        Intent alarmIntent = new Intent(getBaseContext(), cls);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(alarmIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FROM SERVICE", "Service Destroyed");
    }


}
