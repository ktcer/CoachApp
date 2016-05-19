package com.cn.coachs.ui.basic;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.cn.coachs.R;
import com.cn.coachs.ui.AppPool;
import com.umeng.analytics.MobclickAgent;

/**
 * Tab页面基础接口
 *
 * @author kuangtiecheng
 */
public class TabActivityBasic extends TabActivity {

    /**
     * 默认显示的Tab页（第一个TabHost页面）
     */
    protected int curTab = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppPool.createActivity(this);
        setContentView(R.layout.tab_main);
    }

    /**
     * 添加一个TabHost页面
     *
     * @param tagName      String 页面标签名称
     * @param drawableIcon int 页面图标
     * @param context      Class<?> 实现Activity页面
     */
    protected void addTabHost(String tagName, int drawableIcon, Class<?> context) {
        TabHost tabHost = getTabHost();
        TabSpec spec_line = tabHost.newTabSpec(tagName);
        spec_line.setIndicator(getTabView(tagName, drawableIcon));//设置标题
        spec_line.setContent(new Intent(this, context));//设置内容
        tabHost.addTab(spec_line);
    }

    /**
     * 根据Tab的标签名称获取Tab组件
     *
     * @param label String 标签
     * @param id    int 资源文件编号（设置背景图片）
     * @return View
     */
    private View getTabView(String label, int id) {
        View localView = LayoutInflater.from(this).inflate(R.layout.tab_main_view, null);
        ImageView iconView = (ImageView) localView.findViewById(R.id.icon);
        iconView.setImageResource(id);
        return localView;
    }

    protected RelativeLayout layout;    //换肤

    //友盟的统计接口
    public void onResume() {
        super.onResume();
        getTabHost().setCurrentTab(curTab);
        if (null == layout) {
            layout = (RelativeLayout) findViewById(R.id.common_title);
        }
        if (null != layout) {
//			layout.setBackgroundResource(Skin.getHeadDrawable());
        }
        MobclickAgent.onResume(this);
    }

    public void onStop() {
        super.onStop();
        curTab = getTabHost().getCurrentTab();
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppPool.destroyActivity(this);
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