package com.cn.coachs.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.view.SettingItem;
import com.cn.coachs.ui.patient.main.healthdiary.ActivityFansList;
import com.cn.coachs.ui.patient.others.message.ActivityMessageCenter;
import com.cn.coachs.ui.patient.others.myaccount.ActivityCoin;
import com.cn.coachs.ui.patient.others.myaccount.ActivityMyCode;
import com.cn.coachs.ui.patient.others.myaccount.ActivityPersonalInfo;
import com.cn.coachs.ui.patient.others.myaccount.AsyGetPatientNum;
import com.cn.coachs.ui.patient.others.myaccount.AsyncGetMoney;
import com.cn.coachs.ui.patient.others.myaccount.AsyncGetMyCoins;
import com.cn.coachs.ui.patient.others.myaccount.Purse;
import com.cn.coachs.ui.patient.others.myaccount.Remind;
import com.cn.coachs.ui.patient.others.myaccount.Setting;
import com.cn.coachs.ui.patient.others.myaccount.performance.Performance;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.CircleImageView;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.FButton;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.coach.ascync.AsyGetUserInfo;
import com.cn.coachs.coach.model.BeanCoachInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author kuangtiecheng
 */
public class ActivityMyAccountCenter extends ActivityBasic implements
        OnClickListener {
    private ImageView iv;
    private TextView tv_name, tv_score, tv_money, tv_mymoney, tv_myfans;
    private String userName;
    private String staff;
    private String beGoodAt;
    private long userId;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String imageUrl = "";
    private Bitmap head;// 头像Bitmap
    public static float myCoins;
    private int resultdata;
    public static float money;
    public static int fans;
    private FButton btnRemark;

    private SettingItem mMyMsg;
    private SettingItem mMyFamily;
    private SettingItem mMyOrders;
    private SettingItem setting;
    private SettingItem mMyCode;
    private LinearLayout myMoney;
    private LinearLayout myscore, layout_mybonus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccountcenter);
        setdata();
        initial();
    }

    private void setdata() {
        UtilsSharedData.initDataShare(this);
        options = AppMain.initImageOptions(R.drawable.default_user_icon, true);// 构建完成
        imageLoader = ImageLoader.getInstance();
    }

    /**
     *
     */
    private void initial() {
        userName = UtilsSharedData.getValueByKey(Constant.USER_NAME);
        staff = UtilsSharedData.getValueByKey(Constant.USER_POSITION);
        beGoodAt = UtilsSharedData.getValueByKey(Constant.USER_GOODAT);
        userId = UtilsSharedData.getLong(Constant.USER_ID, 0);
        tv_name = (TextView) findViewById(R.id.name_informationManage);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_money = (TextView) findViewById(R.id.name_informationManage);
        tv_mymoney = (TextView) findViewById(R.id.tv_mymoney);
        tv_myfans = (TextView) findViewById(R.id.tv_bonus);
        iv = (ImageView) findViewById(R.id.image_informationManage);
        tv_name.setText(userName);
        imageLoader.displayImage(imageUrl, iv, options);

        btnRemark = (FButton) findViewById(R.id.btn_remark);
        btnRemark.setCornerRadius(3);
        btnRemark.setOnClickListener(this);

        findViewById(R.id.setting).setOnClickListener(this);

        myscore = (LinearLayout) findViewById(R.id.layout_myscore);
        myscore.setOnClickListener(this);

        myMoney = (LinearLayout) findViewById(R.id.layout_mymoney);
        myMoney.setOnClickListener(this);

        layout_mybonus = (LinearLayout) findViewById(R.id.layout_mybonus);
        layout_mybonus.setOnClickListener(this);

        mMyMsg = (SettingItem) findViewById(R.id.txt_mymsg);
        mMyMsg.setOnClickListener(this);

        mMyFamily = (SettingItem) findViewById(R.id.txt_myfamily);
        mMyFamily.setOnClickListener(this);

        mMyOrders = (SettingItem) findViewById(R.id.txt_myorders);
        mMyOrders.setOnClickListener(this);

        mMyCode = (SettingItem) findViewById(R.id.txt_mycode);
        mMyCode.setOnClickListener(this);

        setting = (SettingItem) findViewById(R.id.setting);
        setting.setOnClickListener(this);

        iv.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        userName = UtilsSharedData.getValueByKey(Constant.USER_NAME);
        tv_name.setText(userName);
        imageUrl = AbsParam.getBaseUrl() + UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
        imageLoader.displayImage(imageUrl, iv, options);
        showProgressBar();
        AsyGetUserInfo userTask = new AsyGetUserInfo(this) {

            @Override
            protected void onPostExecute(BeanCoachInfo result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                hideProgressBar();
                userName = result.getName();
                tv_name.setText(userName);
                imageUrl = AbsParam.getBaseUrl() + result.getPicUrl();
                imageLoader.displayImage(imageUrl, iv, options);
            }
        };
        userTask.execute();
        AsyncGetMyCoins mTask = new AsyncGetMyCoins(this) {

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                tv_score.setText((int) myCoins + "个");
            }
        };
        mTask.execute();

        AsyGetPatientNum gpTask = new AsyGetPatientNum(this) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                tv_myfans.setText(fans + "个");
            }
        };
        gpTask.execute();

        AsyncGetMoney gmTask = new AsyncGetMoney(this) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                tv_mymoney.setText(money + "元");
            }
        };
        gmTask.execute();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent;
        super.onClick(v);
        switch (v.getId()) {
            case R.id.setting: /* 设置 */
                intent = new Intent();

                intent.setClass(this, Setting.class);
                startActivity(intent);
                break;
            case R.id.txt_mymsg: /*我的消息  */
                intent = new Intent();
                intent.setClass(this, ActivityMessageCenter.class);
                startActivity(intent);
                break;
            case R.id.txt_myfamily: /* 我的提醒 */
                intent = new Intent();
                intent.setClass(this, Remind.class);
                startActivity(intent);
                break;
            case R.id.txt_myorders: /* 我的绩效 */
                intent = new Intent();
                intent.setClass(this, Performance.class);
                startActivity(intent);
                break;
            case R.id.txt_mycode:/*  我的二维码*/
                intent = new Intent();
                intent.setClass(this, ActivityMyCode.class);
                startActivity(intent);
                break;
            case R.id.image_informationManage://修改个人信息
                intent = new Intent();
                intent.setClass(this, ActivityPersonalInfo.class);
                startActivity(intent);
                break;

            case R.id.layout_mymoney:
                startActivity(Purse.class);// 我的钱包
                break;
            case R.id.layout_mybonus:
//			intent = new Intent(ActivityMyAccountCenter.this,ActivityFans.class);// 我的粉丝
                intent = new Intent(ActivityMyAccountCenter.this, ActivityFansList.class);// 我的粉丝
                intent.putExtra(Constant.FANS_LIST_FLAG, 1);
                startActivity(intent);
                break;
            case R.id.layout_myscore:
                startActivity(ActivityCoin.class);// 我的金币
                break;
            case R.id.btn_remark:
                // //与后台交互...
//			remarkData();
                break;
//		case R.id.my_two_dimension_codeing:
//			getMyTDC();
//		break;
            default:
                break;
        }
    }


    private void getMyTDC() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setTitle("我的二维码");
        builder.setContentView(formTipDialog());
        builder.setPositiveButton(null, null);//设置了这个下面的button就消失啦
        builder.create().show();
    }

    private ScrollView formTipDialog() {
        LayoutInflater inflaterDl = LayoutInflater.from(this);
        ScrollView layout = (ScrollView) inflaterDl.inflate(
                R.layout.dialog_mycode
                , null);
        CircleImageView myHeadImage = (CircleImageView) layout
                .findViewById(R.id.my_head_image);

        TextView myname = (TextView) layout
                .findViewById(R.id.my_name);
        TextView mystaff = (TextView) layout
                .findViewById(R.id.my_staff);
        TextView mygoodat = (TextView) layout
                .findViewById(R.id.my_good_at);

        ImageView mytwodimentioncode = (ImageView) layout
                .findViewById(R.id.my_twodimentioncode);
        myname.setText(userName);
        mystaff.setText(staff);
        mygoodat.setText("擅长:" + beGoodAt);
        imageLoader.displayImage(imageUrl, myHeadImage, options);
        return layout;
    }
//	/**
//	 * 获取钱包余额
//	 */
//	private class AsyncPurse extends AsyncBasic {
//
//		public AsyncPurse(Context mContext) {
//			super(mContext);
//			// TODO Auto-generated constructor stub
//			setPath("/mywallet/querymymoney");
//		}
//
//		@Override
//		protected HashMap<String, String> getMap() {
//			// TODO Auto-generated method stub
////			HashMap<String, String> map = new HashMap<String, String>();
////			map.put("userID", "" + getUserId(0));
////			map.put("userType", "" + 0);
//			return super.getMap();
//		}
//	}

    /**
     * 签到
     */
//	public synchronized void remarkData(){		
//		AsyncRemark asyncRemark = new AsyncRemark(this){
//			@Override
//			protected  void onPostExecute(JSONObject result) {
//				// TODO Auto-generated method stub
//				if(result==null){
//					Toast.makeText(getApplicationContext(), "网络出问题了", Toast.LENGTH_SHORT).show();
//					setBtnRemark(false,getApplicationContext().getString(R.string.remark_ready));
//				}else{
//					try {
//						if(result.getInt("result") == 1){
//							setBtnRemark(true,getApplicationContext().getString(R.string.remark_finish));
//							int count = remarkUi(3,tv_score);
//							tv_score.setText(""+ (Integer.parseInt(tv_score.getText().toString().replace("个", "")) + count) + "个");
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}				
//				}				
//			}		
//		};
//		asyncRemark.execute();
//	}
//	
//	/**
//	 * @author kuangtiecheng
//	 * 签到，设置btnRemark状态
//	 * @param isRemark 是否已经签到
//	 */
//	public void setBtnRemark(boolean isRemark,String state){
//		if(isRemark){
//			btnRemark.setText(state);
//			btnRemark.setEnabled(false);
//			btnRemark.setBackgroundColor(this.getResources().getColor(android.R.color.darker_gray));	
//		}else
//			return;
//	}
//	
//
//	/**
//	 * @author kuangtiecheng 签到效果，待修改
//	 * @param 一次签到送的金币数
//	 */
//	public int remarkUi(int count, TextView textView) {
//		AnimationSet mAnimationSet;
//		ImageView image = (ImageView) findViewById(R.id.image);
//		image.setVisibility(View.VISIBLE);
//		mAnimationSet = getAnimationSet(textView);
//		image.startAnimation(mAnimationSet);
//
//		TextView add_num = (TextView) findViewById(R.id.add_num);
//		mAnimationSet = getAddAnimationSet();
//		mAnimationSet.setStartOffset(1000);
//		add_num.setVisibility(View.VISIBLE);
//		add_num.setText("+" + count);
//		add_num.startAnimation(mAnimationSet);
//		image.setVisibility(View.INVISIBLE);
//		add_num.setVisibility(View.INVISIBLE);
//
//		return count;
//	}
//
//	/**
//	 * @author kuangtiecheng 签到金币动画，待修改
//	 */
//	public AnimationSet getAnimationSet(TextView textView) {
//		AnimationSet mAnimationSet = new AnimationSet(false);
//
//		int[] location = new int[2];
//		textView.getLocationOnScreen(location);
//		System.out.println("location[0]: " + location[0] + " location[1]： "
//				+ location[1]);
//		int height = textView.getHeight();
//		int width = textView.getWidth();
//		TranslateAnimation mTranslateAnimation = new TranslateAnimation(
//				Animation.RELATIVE_TO_SELF, 0f, Animation.ABSOLUTE,
//				location[0]+width, Animation.RELATIVE_TO_SELF, 0f,
//				Animation.ABSOLUTE, location[1] - height);
//
//		mTranslateAnimation.setInterpolator(new AccelerateInterpolator());
//		AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, 0);
//
//		ScaleAnimation mScaleAnimation = new ScaleAnimation(1f, 0.05f, 1f,
//				0.05f, Animation.RELATIVE_TO_SELF, 1f,
//				Animation.RELATIVE_TO_SELF, 1f);
//		mScaleAnimation.setInterpolator(new LinearInterpolator());
//
//		RotateAnimation mRotateAnimation = new RotateAnimation(0.0f, 15.0f,
//				Animation.ABSOLUTE, location[0], Animation.ABSOLUTE,
//				location[1] - height);
//		;
//		mAnimationSet.addAnimation(mScaleAnimation);
//		mAnimationSet.addAnimation(mAlphaAnimation);
//		mAnimationSet.addAnimation(mTranslateAnimation);
//		mAnimationSet.addAnimation(mRotateAnimation);
//
//		mAnimationSet.setDuration(2000);
//		return mAnimationSet;
//	}
//
//	/**
//	 * @author kuangtiecheng 签到加积分动画，待修改
//	 */
//	public AnimationSet getAddAnimationSet() {
//		AnimationSet mAnimationSet = new AnimationSet(true);
//		AlphaAnimation mAlphaAnimation = new AlphaAnimation(1, 0);
//		ScaleAnimation mScaleAnimation = new ScaleAnimation(2f, 0.25f, 2f,
//				0.25f, Animation.RELATIVE_TO_SELF, 0.25f,
//				Animation.RELATIVE_TO_SELF, 0.25f);
//
//		mAnimationSet.addAnimation(mScaleAnimation);
//		mAnimationSet.addAnimation(mAlphaAnimation);
//
//		mAnimationSet.setDuration(1000);
//		return mAnimationSet;
//	}


}
