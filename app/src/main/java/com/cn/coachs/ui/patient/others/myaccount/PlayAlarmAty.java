package com.cn.coachs.ui.patient.others.myaccount;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.broadcast.AlarmManagerReceiver;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.util.UtilsSharedData;

public class PlayAlarmAty extends Activity {

    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_player_aty);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        TextView tipsTV = (TextView) findViewById(R.id.Btn_textrimend_fb);
        Button loginBtn = (Button) findViewById(R.id.gotoProgram_text);
        midTV.setText("我的闹铃");
        getAlarmTime();
        System.out.println("进入我的闹铃！");
        mp = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mp.setLooping(true);// 设置循环
        try {
            mp.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        TimeThread myThread = new TimeThread();
        myThread.start();
        tipsTV.setText(TipsString());

        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishAlarm();
            }
        });

    }

    private void finishAlarm() {
        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
        }

        if (!AppMain.isAppRunning) {
            // 启动应用
            Intent intent1 = new Intent();
            PackageManager packageManager = PlayAlarmAty.this
                    .getPackageManager();
            intent1 = packageManager
                    .getLaunchIntentForPackage("com.cn.coachs");
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AlarmManagerReceiver.cancelAlarms(getApplicationContext());
            AlarmManagerReceiver.setAlarms(getApplicationContext());
            PlayAlarmAty.this.startActivity(intent1);
        }
        finish();
    }

    private void getAlarmTime() {
        String time_length = UtilsSharedData.getValueByKey("time_length");
        if (time_length.equals("1分钟")) {
            type = 1;
        } else if (time_length.equals("5分钟")) {
            type = 2;
        }
    }

    class TimeThread extends Thread {
        @Override
        public void run() {
            switch (type) {
                case 0:

                    break;
                case 1:
                    try {
                        System.out.println("睡眠时间2秒");
                        Thread.currentThread().sleep(1000 * 60);
                        finishAlarm();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        System.out.println("睡眠时间10秒");
                        Thread.currentThread().sleep(1000 * 60 * 5);
                        finishAlarm();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private String TipsString() {
        Calendar calendar = Calendar.getInstance();
        int nowHour = calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String Tips = "";
        switch (nowHour) {
            case 8:
                Tips = "08:30\n查看新增粉丝";
                break;
            case 9:
                Tips = "09:00\n进行爱护";
                break;
            case 14:
                Tips = "14:30\n进行爱护";
                break;
            case 15:
                Tips = "15:30\n查看新增粉丝";
                break;

            default:
                break;
        }
        return Tips;
    }

    @Override
    protected void onPause() {
        super.onPause();
//		if(mp.isPlaying()){
//			mp.stop();
//			mp.release();
//			finish();
//		}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }

    private MediaPlayer mp;
}
