/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.cn.coachs.R;

/**
 * @author kuangtiecheng 点击查看图像
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.ImageloadingDialogStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        setContentView(R.layout.myaccount_loading_dialog);
    }


}
