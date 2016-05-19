//package com.cn.aihuexpert.ui.patient.main.healthdiary;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.DatePicker.OnDateChangedListener;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.cn.coachs.R;
//import com.cn.coachs.ui.basic.ActivityBasic;
//import com.cn.coachs.util.Constant;
//import com.cn.coachs.util.CustomDialog;
//import com.cn.coachs.util.PickerView;
//import com.cn.coachs.util.PickerView.onSelectListener;
//
///**
// * @author  kuangtiecheng
// *
// */
//public class ActivityEditDiary extends ActivityBasic{
//	public String startDate;
//	public AsynQueryPlan asynQueryPlan;
//	public String planLength,fansId;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		
//		fansId = getIntent().getStringExtra(Constant.FANS_ID);
//		System.out.println("=-=-=传过来的fansId=-=-="+fansId);
//		
//		setContentView(R.layout.activity_make_plan);
//		Button make_plan = (Button)findViewById(R.id.make_plan);
//		make_plan.setOnClickListener(this);
//		Button day = (Button)findViewById(R.id.day);
//		day.setOnClickListener(this);
//		Button make_day = (Button)findViewById(R.id.make_day);
//		make_day.setOnClickListener(this);	
//	}
//
//	public void getStatus(){
//		AsynGetcpStatus asynGetcpStatus = new AsynGetcpStatus(this,fansId){			
//			@Override
//			protected void onPostExecute(JSONObject result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				System.out.println("=-=-=result=-=-="+result);
//				try {
//					int cpStatus = result.getInt("cpstatus");
//					System.out.println("=-=-=cpStatus=-=-="+cpStatus);
//					if(cpStatus==6||cpStatus==5){
//						AsynGetDiaryStartAndLength asynGetDiaryStartAndLength = new AsynGetDiaryStartAndLength(fansId){
//							@Override
//							protected void onPostExecute(JSONArray result) {
//								// TODO Auto-generated method stub
//								super.onPostExecute(result);
//								try {
//									startDate = result.getJSONObject(0).getString("startdate");
//									System.out.println("=-=-=AsynGetDiaryStartAndLength  startDate=-=-="+startDate);
//									int days = result.getJSONObject(0).getInt("days");
//									System.out.println("=-=-=AsynGetDiaryStartAndLength  days=-=-="+days);
//									planLength = String.valueOf(days/7);
//									System.out.println("=-=-=AsynGetDiaryStartAndLength  planLength=-=-="+planLength);
//									
//									switchActivity();
//								} catch (JSONException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}								
//							}						
//						};
//						asynGetDiaryStartAndLength.execute();
//					}else{
//						showChooseDialog();
//					}
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}		
//		};		
//		asynGetcpStatus.execute();
//	}
//	
//	
//	public void switchActivity(){
//		Intent intent = new Intent(ActivityEditDiary.this,ActivityEditDiaryDay.class);
//		intent.putExtra(Constant.START_DATE, startDate);
//		intent.putExtra(Constant.PLAN_LENGTH, planLength);
//		intent.putExtra(Constant.FANS_ID, fansId);
//		startActivity(intent);		
//	}
//	
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		super.onClick(v);
//		if(v.getId()==R.id.make_plan){
//			getStatus();		
//		}else if(v.getId()==R.id.day){
//			startActivity(new Intent().setClass(getApplicationContext(), ActivityEditDiaryDay.class));
//		}else if(v.getId()==R.id.make_day){
//			startActivity(new Intent().setClass(getApplicationContext(), ActivityEditDiaryDayDetail.class));			
//		}
//	}
//	
//	private void showChooseDialog() {
//		// TODO Auto-generated method stub
//		CustomDialog.Builder builder = new CustomDialog.Builder(ActivityEditDiary.this);
//		builder.setContentView(formChooseDialog());
//		builder.setTitle("制定方案");
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				if(startDate!=null&&planLength!=null){
//					dialog.dismiss();
//					asynQueryPlan = new AsynQueryPlan(getApplicationContext(),startDate,""+planLength,fansId){
//						@Override
//						protected void onPostExecute(JSONObject result) {
//							// TODO Auto-generated method stub
//							super.onPostExecute(result);
//							System.out.println("=-=-=asynQueryPlan=-=-="+result);
//							try {
//								if(result.getInt("resultID")==1)						
//									switchActivity();
//								else
//									Toast.makeText(getApplicationContext(), "创建失败...", Toast.LENGTH_SHORT).show();
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}		
//					};	
//					asynQueryPlan.execute();				
//				}
//			}
//		});
//		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}	
//	
//	// 构建选择弹窗
//	private LinearLayout formChooseDialog() {
//		LayoutInflater inflaterDl = LayoutInflater.from(ActivityEditDiary.this);
//		LinearLayout layout = (LinearLayout) inflaterDl.inflate(R.layout.activity_edit_diary, null);
//		PickerView item_mytall = (PickerView)layout.findViewById(R.id.item_mytall);		
//		DatePicker date_start = (DatePicker)layout.findViewById(R.id.date_start);
//		
//		List<String> list = new ArrayList<String>();
//		for(int i=1;i<9;i++)
//			list.add(""+i);
//		item_mytall.setData(list);
//		item_mytall.setOnSelectListener(new onSelectListener() {
//			@Override
//			public void onSelect(String text) {
//				// TODO Auto-generated method stub
//				planLength = text;
//			}
//		});
//		
//		final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//		startDate = sf.format(new Date());//初始化startDate，日记默认为从今天开始
//		
//		Calendar calendar = Calendar.getInstance();	
//		int year = calendar.get(Calendar.YEAR);
//		int monthOfYear = calendar.get(Calendar.MONTH);
//		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//		
//		calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
//		long time = calendar.getTimeInMillis();
//		date_start.setMinDate(time);	
//		
//		date_start.init(year, monthOfYear, dayOfMonth, new OnDateChangedListener(){
//            public void onDateChanged(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
//            	Calendar calendar = Calendar.getInstance();
//            	calendar.set(year, monthOfYear, dayOfMonth);
//            	String date = sf.format(calendar.getTime());
//                startDate = date;
//            }           
//        });
//		return layout;
//	}
//}
