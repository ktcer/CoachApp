/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.cn.coachs.ui.patient.main.healthclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cn.coachs.R;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.base.CCPCustomViewPager;
import com.cn.coachs.ui.chat.common.base.CCPLauncherUITabView;
import com.cn.coachs.ui.chat.common.utils.LogUtil;
import com.cn.coachs.ui.chat.ui.BaseFragment;
import com.cn.coachs.ui.chat.ui.ContactListFragment;
import com.cn.coachs.ui.chat.ui.ConversationListFragment;
import com.cn.coachs.ui.chat.ui.GroupListFragment;
import com.cn.coachs.ui.chat.ui.TabFragmentListView;
import com.cn.coachs.util.UtilsSharedData;

/**
 * 主界面（消息会话界面、联系人界面、群组界面）
 */
public class ActivityClassRoom extends ActivityBasic implements
        View.OnClickListener, ConversationListFragment.OnUpdateMsgUnreadCountsListener {

    private static final String TAG = "ActivityHealth";
    /**
     * 当前ECLauncherUI 实例
     */
    public static ActivityClassRoom mLauncherUI;

    /**
     * 当前ECLauncherUI实例产生个数
     */
    public static int mLauncherInstanceCount = 0;

    /**
     * 当前主界面RootView
     */
    public View mLauncherView;

    /**
     * LauncherUI 主界面导航控制View ,包含三个View Tab按钮
     */
    private CCPLauncherUITabView mLauncherUITabView;
    /**
     * 三个TabView所对应的三个页面的适配器
     */
    private CCPCustomViewPager mCustomViewPager;

    /**
     * 联系人列表
     */
    // public List<ECContacts> contacts = new ArrayList<ECContacts>();
    // public HashMap<String,ECContacts> contacts = new HashMap<String,
    // ECContacts>();

    /**
     * 沟通、联系人、群组适配器
     */
    public LauncherViewPagerAdapter mLauncherViewPagerAdapter;

    /**
     * 当前显示的TabView Fragment
     */
    private int mCurrentItemPosition = -1;

    /**
     * 会话界面(沟通)
     */
    private static final int TAB_CONVERSATION = 0;

    /**
     * 通讯录界面(联系人)
     */
    private static final int TAB_ADDRESS = 1;

    /**
     * 群组界面
     */
//	private static final int TAB_GROUP = 2;

    /**
     * {@link CCPLauncherUITabView} 是否已经被初始化
     */
    private boolean mTabViewInit = false;

    /**
     * 缓存三个TabView
     */
    private final HashMap<Integer, Fragment> mTabViewCache = new HashMap<Integer, Fragment>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        int pid = android.os.Process.myPid();
        UtilsSharedData.initDataShare(this);
        if (mLauncherUI != null) {
            LogUtil.i(LogUtil.getLogUtilsTag(ActivityClassRoom.class),
                    "finish last LauncherUI");
            mLauncherUI.finish();
        }
        mLauncherUI = this;
        mLauncherInstanceCount++;
        super.onCreate(savedInstanceState);
        // initWelcome();

        // 设置页面默认为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		ECContentObservers.getInstance().initContentObserver();
        mLauncherView = getLayoutInflater().inflate(R.layout.main_tab, null);
        setContentView(mLauncherView);
        initLauncherUIView();
    }


    /**
     * 初始化主界面UI视图
     */
    private void initLauncherUIView() {


        mTabViewInit = true;
        mCustomViewPager = (CCPCustomViewPager) findViewById(R.id.pager);
        mCustomViewPager.setOffscreenPageLimit(2);

        if (mLauncherUITabView != null) {
            mLauncherUITabView.setOnUITabViewClickListener(null);
            mLauncherUITabView.setVisibility(View.VISIBLE);
        }
        mLauncherUITabView = (CCPLauncherUITabView) findViewById(R.id.laucher_tab_top);
        List<Integer> tabTile = new ArrayList<Integer>();
        tabTile.add(R.string.healthpost);
        tabTile.add(R.string.expertspeek);
        mLauncherUITabView.setTabViewText(tabTile, 2);

        mCustomViewPager.setSlideEnabled(true);
        mLauncherViewPagerAdapter = new LauncherViewPagerAdapter(this,
                mCustomViewPager);
        mLauncherUITabView
                .setOnUITabViewClickListener(mLauncherViewPagerAdapter);


        ctrlViewTab(0);

    }


    /**
     * 根据TabFragment Index 查找Fragment
     *
     * @param tabIndex
     * @return
     */
    public final BaseFragment getTabView(int tabIndex) {
        LogUtil.d(LogUtil.getLogUtilsTag(ActivityClassRoom.class),
                "get tab index " + tabIndex);
        if (tabIndex < 0) {
            return null;
        }

        if (mTabViewCache.containsKey(Integer.valueOf(tabIndex))) {
            return (BaseFragment) mTabViewCache.get(Integer.valueOf(tabIndex));
        }

        BaseFragment mFragment = null;
        switch (tabIndex) {
            case TAB_CONVERSATION:
                mFragment = (TabFragmentListView) Fragment.instantiate(this,
                        FragmentExpertVideo.class.getName(), null);//DoctorInterviewFragment、、TabFragment/GroupListFragment
                break;
            case TAB_ADDRESS:
                mFragment = (TabFragmentListView) Fragment
                        .instantiate(this,
                                FragmentHealthKnowledge.class//HearthPostFragment
                                        .getName(), null);
                break;
//		case TAB_GROUP:
//			mFragment = (TabFragment) Fragment.instantiate(this,
//					GroupListFragment.class.getName(), null);
//			break;

            default:
                break;
        }

        if (mFragment != null) {
            mFragment.setActionBarActivity(this);
        }
        mTabViewCache.put(Integer.valueOf(tabIndex), mFragment);
        return mFragment;
    }

    /**
     * 根据提供的子Fragment index 切换到对应的页面
     *
     * @param index 子Fragment对应的index
     */
    public void ctrlViewTab(int index) {

        LogUtil.d(LogUtil.getLogUtilsTag(ActivityClassRoom.class),
                "change tab to " + index + ", cur tab " + mCurrentItemPosition
                        + ", has init tab " + mTabViewInit
                        + ", tab cache size " + mTabViewCache.size());
        if ((!mTabViewInit || index < 0)
                || (mLauncherViewPagerAdapter != null && index > mLauncherViewPagerAdapter
                .getCount() - 1)) {
            return;
        }

        if (mCurrentItemPosition == index) {
            return;
        }
        mCurrentItemPosition = index;

        if (mLauncherUITabView != null) {
            mLauncherUITabView.doChangeTabViewDisplay(mCurrentItemPosition);
        }

        if (mCustomViewPager != null) {
            mCustomViewPager.setCurrentItem(mCurrentItemPosition, false);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        LogUtil.i(LogUtil.getLogUtilsTag(ActivityClassRoom.class),
                "onResume start");
        super.onResume();


    }

    /**
     * TabView 页面适配器
     *
     * @author 容联•云通讯
     * @version 4.0
     * @date 2014-12-4
     */
    private class LauncherViewPagerAdapter extends FragmentStatePagerAdapter
            implements ViewPager.OnPageChangeListener,
            CCPLauncherUITabView.OnUITabViewClickListener {
        /**
         *
         */
        private int mClickTabCounts;
        private ContactListFragment mContactUI;
        private GroupListFragment mGroupListFragment;

        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        public LauncherViewPagerAdapter(FragmentActivity fm, ViewPager pager) {
            super(fm.getSupportFragmentManager());
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(String tabSpec, Class<?> clss, Bundle args) {
            String tag = tabSpec;

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return mLauncherUI.getTabView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onPageScrollStateChanged state = " + state);

            if (state != ViewPager.SCROLL_STATE_IDLE
                    || mGroupListFragment == null) {
                return;
            }
            if (mGroupListFragment != null) {
                mGroupListFragment.onGroupFragmentVisible(true);
                mGroupListFragment = null;
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onPageScrolled " + position + " " + positionOffset + " "
                            + positionOffsetPixels);
            if (mLauncherUITabView != null) {
                mLauncherUITabView.doTranslateImageMatrix(position,
                        positionOffset);
            }
            if (positionOffset != 0.0F) {
                if (mGroupListFragment == null) {
//					mGroupListFragment = (GroupListFragment) getTabView(CCPLauncherUITabView.TAB_VIEW_THIRD);
//					mGroupListFragment.onGroupFragmentVisible(false);
                }
                return;
            }
        }

        @Override
        public void onPageSelected(int position) {
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onPageSelected");
            if (mLauncherUITabView != null) {
                mLauncherUITabView.doChangeTabViewDisplay(position);
                mCurrentItemPosition = position;
            }
        }

        @Override
        public void onTabClick(int tabIndex) {
            if (tabIndex == mCurrentItemPosition) {
                LogUtil.d(
                        LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                        "on click same index " + tabIndex);
                // Perform a rolling
                TabFragmentListView item = (TabFragmentListView) getItem(tabIndex);
                item.onTabFragmentClick();
                return;
            }

            mClickTabCounts += mClickTabCounts;
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onUITabView Click count " + mClickTabCounts);
            mViewPager.setCurrentItem(tabIndex);
        }

    }

    @Override
    public void OnUpdateMsgUnreadCounts() {
        // TODO Auto-generated method stub

    }

}
