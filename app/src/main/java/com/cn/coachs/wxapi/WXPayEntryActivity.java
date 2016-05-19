//package com.cn.aihuexpert.wxapi;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.cn.coachs.R;
//import com.cn.coachs.model.nurse.BeanPayDone;
//import com.cn.coachs.model.nurse.BeanPayGold;
//import com.cn.coachs.ui.AppMain;
//import com.cn.coachs.ui.basic.ActivityBasic;
//import com.cn.coachs.ui.patient.main.TabActivityMain;
//import com.cn.coachs.ui.patient.main.healthdiary.ActivityHealthDiary;
//import com.cn.coachs.ui.patient.main.mynurse.asynctask.AscyncPayDone;
//import com.cn.coachs.ui.patient.main.mynurse.asynctask.AscyncPayGold;
//import com.cn.coachs.ui.patient.others.myaccount.AdapterMyIntegral;
//import com.cn.coachs.ui.patient.others.myorders.ActivityMyOrderInfo;
//import com.cn.coachs.weixin.pay.Constants;
//import com.tencent.mm.sdk.constants.ConstantsAPI;
//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//
//public class WXPayEntryActivity extends ActivityBasic implements
//		IWXAPIEventHandler {
//
//	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
//
//	private IWXAPI api;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.pay_result);
//
//		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//
//		api.handleIntent(getIntent(), this);
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//		api.handleIntent(intent, this);
//	}
//
//	@Override
//	public void onReq(BaseReq req) {
//	}
//
//	@Override
//	public void onResp(BaseResp resp) {
//		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
//
//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			int code = resp.errCode;
//			String msg = "";
//			switch (code) {
//			case 0:
//				if(AppMain.kindsPay == 0){
//					payTravel();
//				}
//				if(AppMain.kindsPay == 1){
//					payMyGold();
//				}
//				msg = "支付成功，请到单订单列表查看详情";
//				Toast.makeText(WXPayEntryActivity.this, msg, 1000).show();
//				
//				break;
//			case -1:
//				msg = "支付失败，您可以在单订单列表重试支付";
//				Toast.makeText(WXPayEntryActivity.this, msg, 1000).show();
//				String oldPayChooseActivity = ActivityHealthDiary.class.getName();
//				backTo(oldPayChooseActivity);
//				break;
//			case -2:
//				msg = "支付取消，您可以在单订单列表继续支付";
//				Toast.makeText(WXPayEntryActivity.this, msg, 1000).show();
//				String oldPayChooseActivity1 = ActivityHealthDiary.class
//						.getName();
//				backTo(oldPayChooseActivity1);
//				break;
//
//			default:
//				msg = "支付失败，您可以在单订单列表重试支付";
//				Toast.makeText(WXPayEntryActivity.this, msg, 1000).show();
//				String oldPayChooseActivity2 = ActivityHealthDiary.class
//						.getName();
//				backTo(oldPayChooseActivity2);
//				break;
//			}
//
//		}
//	}
//
//	private void payMyGold() {
//		showProgressBar();
//		AscyncPayGold ascypgTask= new AscyncPayGold(this,AdapterMyIntegral.gold+""){
//
//			@Override
//			protected void onPostExecute(BeanPayGold result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				if(result.getResult().equals("success")){
//					hideProgressBar();
//	            	String old=TabActivityMain.class.getName();
//	            	backTo(old);
//				}else{
//					Toast.makeText(WXPayEntryActivity.this,"失败"+ result.getNowcoins(), 1000).show();
//				}
//			}
//			
//		};
//		ascypgTask.execute();
//		
//	}
//
//	private void payTravel() {
//
//		showProgressBar();
//		AscyncPayDone apd = new AscyncPayDone(WXPayEntryActivity.this,
//				ActivityMyOrderInfo.orderNo, ActivityMyOrderInfo.price) {
//
//			@Override
//			protected void onPostExecute(BeanPayDone result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				if (result.getResult() == 1) {
//
//				} else {
//					// Toast.makeText(act, result.getDetail(),
//					// 1000).show();
//				}
//				hideProgressBar();
//				String old = ActivityHealthDiary.class.getName();
//				backTo(old);
//
//			}
//
//		};
//		apd.execute();
//		
//	}
//
//}