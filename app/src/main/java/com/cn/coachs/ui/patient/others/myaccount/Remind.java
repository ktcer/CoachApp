package com.cn.coachs.ui.patient.others.myaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.broadcast.AlarmManagerReceiver;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.coachs.ui.chat.common.utils.ECPreferences;
import com.cn.coachs.ui.chat.common.view.SettingItem;
import com.cn.coachs.ui.patient.main.healthdiary.alarm.AlarmDBHelper;
import com.cn.coachs.ui.patient.main.healthdiary.alarm.AlarmModel;
import com.cn.coachs.util.UtilsSharedData;

import org.json.JSONObject;

import java.io.InvalidClassException;

public class Remind extends ActivityBasic {
    private AlarmDBHelper dbHelper = new AlarmDBHelper(this);

    private AlarmModel alarmDetails;
    private SettingItem frequency, remindRecord, timeLength;
    private LinearLayout linearLayoutOne, linearLayoutTwo;
    private TextView remindRate;
    private String[] times = {"1分钟", "5分钟", "手动关闭"};
    private ListView timeLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    /**
     * 初始化新消息声音设置参数
     */
    private void initRemind() {
        if (remindRecord == null) {
            return;
        }
        remindRecord.setVisibility(View.VISIBLE);
        boolean shakeSetting = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND.getId(),
                (Boolean) ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND.getDefaultValue());
//        remindRecord.setChecked(shakeSetting);
        if (remindRecord.isChecked()) {
            linearLayoutOne.setVisibility(View.VISIBLE);
            linearLayoutTwo.setVisibility(View.VISIBLE);
            remindRate.setVisibility(View.VISIBLE);
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
                    dbHelper.createAlarm(alarmDetails);
                }
                AlarmManagerReceiver.cancelAlarms(this);
                AlarmManagerReceiver.setAlarms(this);
            }

        } else {
            linearLayoutOne.setVisibility(View.INVISIBLE);
            linearLayoutTwo.setVisibility(View.INVISIBLE);
            remindRate.setVisibility(View.INVISIBLE);
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

    private void init() {
        setContentView(R.layout.myaccount_my_remind);
        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        linearLayoutOne = (LinearLayout) findViewById(R.id.layoutOne);
        linearLayoutTwo = (LinearLayout) findViewById(R.id.layoutTwo);
        remindRate = (TextView) findViewById(R.id.tvRemindRate);
        midTV.setText("我的提醒");
        remindRecord = (SettingItem) findViewById(R.id.remind_record);
        timeLength = (SettingItem) findViewById(R.id.time_length);
        String tlength = UtilsSharedData.getValueByKey("time_length");
        if (tlength == "") {
            tlength = "手动关闭";
        }
        timeLength.setTitleText(tlength);
        LinearLayout lay = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.list_times, null);
        timeLists = (ListView) lay.findViewById(R.id.list_time);
        timeLength.setOnClickListener(this);
        alarmDetails = new AlarmModel();
        initRemind();
        remindRecord.getCheckedTextView().setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (remindRecord.isChecked()) {
                    linearLayoutOne.setVisibility(View.INVISIBLE);
                    linearLayoutTwo.setVisibility(View.INVISIBLE);
                    remindRate.setVisibility(View.INVISIBLE);
                    AlarmManagerReceiver.cancelAlarms(getApplicationContext());
                } else {
                    linearLayoutOne.setVisibility(View.VISIBLE);
                    linearLayoutTwo.setVisibility(View.VISIBLE);
                    remindRate.setVisibility(View.VISIBLE);

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
                            dbHelper.createAlarm(alarmDetails);
                        }
                        AlarmManagerReceiver.cancelAlarms(getApplicationContext());
                        AlarmManagerReceiver.setAlarms(getApplicationContext());
                    }
                }
                updateNewMsgNotification(0);
            }
        });
        frequency = (SettingItem) findViewById(R.id.frequency);
        frequency.setOnClickListener(this);

        AsyncRemind mAsyncRemind = new AsyncRemind(this) {
            @Override
            protected void onPostExecute(JSONObject result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                //解析JSON数据...
            }
        };
        mAsyncRemind.execute();
    }

    protected void updateNewMsgNotification(int type) {
        try {
            if (type == 0) {
                if (remindRecord == null) {
                    return;
                }
                remindRecord.toggle();
                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_NEW_MSG_SOUND, remindRecord.isChecked(), true);
                return;
            }
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.frequency:
                break;
            case R.id.remind_record:
                startActivity(RemindDetail.class);
                break;
            case R.id.time_length:
                new AlertDialog.Builder(this).setAdapter(new ListAdapter() {

                    @Override
                    public void unregisterDataSetObserver(DataSetObserver observer) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void registerDataSetObserver(DataSetObserver observer) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public boolean isEmpty() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public boolean hasStableIds() {
                        // TODO Auto-generated method stub
                        return false;
                    }

                    @Override
                    public int getViewTypeCount() {
                        // TODO Auto-generated method stub
                        return 1;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if (convertView == null) {
                            convertView = getLayoutInflater().inflate(
                                    R.layout.time_items, null);
                        }
                        TextView tvTimes = (TextView) convertView.findViewById(R.id.time_item);
                        tvTimes.setText(times[position]);
                        return convertView;
                    }

                    @Override
                    public int getItemViewType(int position) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public long getItemId(int position) {
                        // TODO Auto-generated method stub
                        return 0;
                    }

                    @Override
                    public Object getItem(int position) {
                        // TODO Auto-generated method stub
                        return times[position];
                    }

                    @Override
                    public int getCount() {
                        // TODO Auto-generated method stub
                        return 3;
                    }

                    @Override
                    public boolean isEnabled(int position) {
                        // TODO Auto-generated method stub
                        return true;
                    }

                    @Override
                    public boolean areAllItemsEnabled() {
                        // TODO Auto-generated method stub
                        return true;
                    }
                }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        timeLength.setTitleText(times[which]);
                        UtilsSharedData.saveKeyMustValue("time_length", times[which]);
                        dialog.dismiss();

                    }
                }).create().show();
                break;
            default:
                break;
        }
    }
}
