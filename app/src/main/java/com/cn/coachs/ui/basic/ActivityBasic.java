package com.cn.coachs.ui.basic;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppPool;
import com.cn.coachs.ui.Loger;
import com.cn.coachs.ui.chat.common.utils.ToastUtil;
import com.cn.coachs.util.CustomProgressDialog;
import com.cn.coachs.util.RegexNormal;
import com.umeng.analytics.MobclickAgent;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityBasic extends FragmentActivity implements IfaceDialog, OnClickListener {

    protected RelativeLayout layout;
    private boolean activityStatus = false;    //当前Activity的状态（是否退出），供方法 closeActivity 调用
    protected UtilDialog utilDialog;
    protected Handler handler = new Handler();
    protected ImageView tx_Back;
    protected CustomProgressDialog dialogProgress;
    protected Loger log = new Loger("[" + getClass().getSimpleName() + "]");
    protected Animation animation;
    protected LinearLayout mainLayout;
    protected Dialog progressDialog;

    //显示载入弹窗
    public void showProgressBar() {
        hideProgressBar();
//		dialogProgress =new CustomProgressDialog(this, "正在加载中");
//		dialogProgress.show();
        progressDialog = new Dialog(this, R.style.progress_dialog);
        progressDialog.setContentView(R.layout.progress_dialog_ios);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
        msg.setText("卖力加载中");
        progressDialog.show();
    }

    //隐藏载入弹窗
    public void hideProgressBar() {
//		if(dialogProgress!=null)
//		dialogProgress.dismiss();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    /**
     * 登录注册专用
     *
     * @param enabled
     */
    public void setInputEnabled(boolean enabled) {

    }
//	/**
//	 * 隐藏软键盘
//	 */
//	
//	protected void inputHidden(IBinder binder) {
//		if (null == imm){
//			imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//		}
//		imm.hideSoftInputFromWindow(binder, InputMethodManager.RESULT_UNCHANGED_SHOWN);
//	}

//	private InputMethodManager imm;
//	
//	/**
//	 * 显示软键盘
//	 */
//	protected void inputShow(View view) {
//		if (null == imm){
//			imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//		}
//		imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
//	}
//	
//	/**
//	 * 输入法适应屏幕
//	 */
//	protected void inputAutoResize() {
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//	}

    protected void setAnimation(float startX, float endX, float startY, float endY) {
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, startX,
                Animation.RELATIVE_TO_SELF, endX, Animation.RELATIVE_TO_SELF,
                startY, Animation.RELATIVE_TO_SELF, endY);
//		animation = new AlphaAnimation(0.0f,1.0f); 
        animation.setDuration(200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppPool.destroyActivity(this);
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			setTranslucentStatus(true);
//		}
//
//		SystemBarTintManager tintManager = new SystemBarTintManager(this);
//		tintManager.setStatusBarTintEnabled(true);
//		tintManager.setStatusBarTintResource(R.color.statusbar_bg);

        AppPool.createActivity(this);
        utilDialog = new UtilDialog(this);
//		msgDialogMustExit = new MessageDialog(this, "", "");
//		msgDialogMustExit.setMiddleButton("确定", new OnClick() {
//			
//			@Override
//			public void onClick() {
//				msgDialogMustExit.toClose();
//				AppPool.logout();
//			}
//		});
//		
//		msgDialogNextFinish = new MessageDialog(this, "", "");
//		msgDialogNextFinish.setMiddleButton("确定", new OnClick() {
//			
//			@Override
//			public void onClick() {
//				AppPool.backHome();
//			}
//		});
    }

    public void finishEvent() {

    }

    //设置页面返回事件
    private void setBackBtnEvent() {
        tx_Back = (ImageView) findViewById(R.id.left_tv);
        if (tx_Back != null) {
            tx_Back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    inputControl(v);
                    v.startAnimation(AnimationUtils.loadAnimation(ActivityBasic.this,
                            R.anim.icon_scale));
                    finishEvent();
                    finish();
                }
            });
        }
    }

    public void inputControl(View v) {
        // 隐藏输入法
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        // 显示或者隐藏输入法
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //友盟的统计接口
    public void onResume() {
        super.onResume();
//		if (null == layout){
//			layout = (RelativeLayout)findViewById(R.id.common_title);
//		}
//		if (null != layout){
////			layout.setBackgroundResource(Skin.getHeadDrawable());
//		}
        activityStatus = true;
        setBackBtnEvent();
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        if (mainLayout != null) {
            mainLayout.setOnClickListener(this);
        }
        MobclickAgent.onResume(this);
    }

    //友盟的统计接口
    public void onPause() {
        super.onPause();
        activityStatus = false;
        MobclickAgent.onPause(this);
//		GpsGet.stopListener();
    }

    public void showNetErrorInfo() {
        ToastUtil.showMessage("您的网络好像有点问题哦，请稍后重试！");
    }


    /**
     * 用户登录
     *
     * @param v
     */
    public void userLogin(View v) {
//		startActivity(UserLogin.class);
    }

    /**
     * 返回上一级页面
     *
     * @param view View
     */
    public void previousActivity(View view) {
        finish();
    }

    /**
     * 返回到指定页面
     *
     * @param name
     */
    public void backTo(String name) {
        AppPool.backToSpecificActivity(name);
    }

    public void finishAllAct() {
        AppPool.finishAllAct();
    }

    protected static boolean flagReturn = false;

    /**
     * 启动新的Activity控件
     *
     * @param cls   Class<?>
     * @param key   String
     * @param value String
     */
    protected void startActivity(Class<?> cls, String key, String value) {
        Intent inte = new Intent(this, cls);
        inte.putExtra(key, value);
        startActivity(inte);
    }

    /**
     * 启动新的Activity控件
     *
     * @param cls Class
     */
    protected void startActivity(Class<?> cls) {
        Intent inte = new Intent(this, cls);
//		inte.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(inte);
    }

    /**
     * 启动新的Activity控件
     *
     * @param cls    Class
     * @param params Hashtable<String, String> 传递的参数
     */
    protected void startActivity(Class<?> cls, Hashtable<String, String> params) {
        Intent inte = new Intent(this, cls);
        Enumeration<String> enus = params.keys();
        while (enus.hasMoreElements()) {
            String key = enus.nextElement();
            String value = params.get(key);
            inte.putExtra(key, value);
        }
        startActivity(inte);
    }

    /**
     * 启动新的Activity控件
     *
     * @param cls    Class<?>
     * @param keys   String[]
     * @param values String[]
     */
    protected void startActivity(Class<?> cls, String[] keys, String[] values) {
        Intent inte = new Intent(this, cls);
        for (int i = 0; i < keys.length && i < values.length; i++) {
            inte.putExtra(keys[i], values[i]);
        }
        startActivity(inte);
    }

    /**
     * 关闭调用等待窗口
     */
    public void dismiss() {
        utilDialog.dismiss();
    }

    @Override
    public void showAlertDialog(String title, String message, int drawable) {
        utilDialog.showAlertDialog(title, message, drawable);
    }

    @Override
    public void showAlertDialog(String title, String message) {
        utilDialog.showAlertDialog(title, message);
    }

    @Override
    public void showProgressDialog(String title, String message) {
        utilDialog.showProgressDialog(title, message);
    }

    @Override
    public void showProgressDialog(String message, Runnable run) {
        utilDialog.showProgressDialog(message, run);
    }

    @Override
    public void showProgressDialog(String title, String message, Runnable run) {
        utilDialog.showProgressDialog(title, message, run);
    }

    @Override
    public void showProgressDialog(String title, String message, Runnable run, long delayedTime) {
        utilDialog.showProgressDialog(title, message, run, delayedTime);
    }

    @Override
    public void showToastDialog(String message) {
        utilDialog.showToastDialog(message);
    }

    @Override
    public void showToastDialogLongTime(String message) {
        utilDialog.showToastDialogLongTime(message);
    }

    /**
     * 获取Activity之间传递的数据
     *
     * @param key String
     * @return String
     */
    protected String getStringDataFromIntent(String key) {
        String value = getIntent().getStringExtra(key);
        if (null == value) {
            value = new String();
        }
        return value;
    }

    @Override
    public void showNextExitDialog(String title, String msg) {

    }

    @Override
    public void showDialogAndReturnMainPageReLogin(String title, String msg) {

    }

    //字符串验证接口
    protected RegexNormal validate = new RegexNormal();

    /**
     * 输入框输入字符验证是否通过
     *
     * @param input EditText 输入框
     * @return boolean 是否通过
     */
    protected boolean validate(EditText input) {
        String value = input.getText().toString().trim();
        if (value.equals("")) {
            showAlertDialog("提示信息", "请输入查询内容。");
            return false;
        }
        boolean illegal = validate.judge(value);
        if (illegal) {
            value = validate.correct(value);
            input.setText(value);
            input.setSelection(value.length());
            showAlertDialog("提示信息", "您好，暂不支持特殊字符查询！");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean closeActivity() {
        return activityStatus;
    }

    /**
     * 判断用户输入的是否为空或空格
     *
     * @param value 判断的字符串
     * @return boolean
     */
    protected boolean isEmpty(String value) {
        if (value.trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户输入内容是否合法
     *
     * @param value String 输入内容
     * @return boolean
     */
    protected boolean patternJudge(String value) {
        Pattern pattern = Pattern.compile("\\p{Punct}+");
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
//		v.startAnimation(AnimationUtils.loadAnimation(this,
//				R.anim.icon_scale));
        switch (v.getId()) {
            case R.id.main_layout:
                inputControl(v);
                break;

            default:
                break;
        }
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }
}