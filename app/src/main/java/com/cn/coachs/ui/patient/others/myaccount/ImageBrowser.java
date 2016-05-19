/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.dialog.ECProgressDialog;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author kuangtiecheng 点击查看图像
 */
public class ImageBrowser extends ActivityBasic {
    private ImageView headpic_browser;
    private String uri;// 服务器头像路径

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        setContentView(R.layout.myaccount_imagebrowser);

//		final LoadingDialog mLoadingDialog = new LoadingDialog(this);
//		mLoadingDialog.setCanceledOnTouchOutside(false);
//		mLoadingDialog.show();
        final ECProgressDialog mPostingdialog = new ECProgressDialog(this, "");
        mPostingdialog.show();

        headpic_browser = (ImageView) findViewById(R.id.headpic_browser);

//		final LoadingDialog dialog = new LoadingDialog(this);
//		dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DisplayImageOptions option = AppMain.initImageOptions(R.drawable.default_user_icon, true);

//            	mLoadingDialog.dismiss();
                mPostingdialog.dismiss();

                ImageLoader imageLoader = ImageLoader.getInstance();
                uri = AbsParam.getBaseUrl() + UtilsSharedData.getValueByKey(Constant.USER_IMAGEURL);
                imageLoader.displayImage(uri, headpic_browser, option);

//                dialog.dismiss();
            }
        }, 1000 * 2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        System.out.println("=-=-=-=  屏幕被点击 =-=-=");
        finish();
        return true;
    }
}
