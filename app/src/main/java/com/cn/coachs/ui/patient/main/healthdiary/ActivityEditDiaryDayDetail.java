package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.model.nurse.BeanEditDiaryNode;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.base.CCPCustomViewPager;
import com.cn.coachs.ui.chat.common.base.CCPLauncherUITabView;
import com.cn.coachs.ui.chat.common.utils.LogUtil;
import com.cn.coachs.ui.chat.ui.BaseFragment;
import com.cn.coachs.ui.chat.ui.ContactListFragment;
import com.cn.coachs.ui.chat.ui.ConversationListFragment;
import com.cn.coachs.ui.chat.ui.TabFragment;
import com.cn.coachs.ui.chat.ui.TabFragmentListView;
import com.cn.coachs.util.Constant;
import com.yuntongxun.ecsdk.ECDevice;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/12 下午11:34:22
 * @parameter
 * @return
 */
public class ActivityEditDiaryDayDetail extends ActivityBasic implements View.OnClickListener {

    /**
     * 当前ECLauncherUI 实例
     */
    public static ActivityEditDiaryDayDetail mLauncherUI;

    /**
     * 当前主界面RootView
     */
    public View mLauncherView;

    /**
     * LauncherUI 主界面导航控制View ,包含三个View Tab按钮
     */
    private DiaryLauncherUITabView mLauncherUITabView;
    /**
     * 三个TabView所对应的三个页面的适配器
     */
    private CCPCustomViewPager mCustomViewPager;

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
    public static final int TAB_CONVERSATION = 0;

    /**
     * 通讯录界面(联系人)
     */
    public static final int TAB_ADDRESS = 1;

    /**
     * 群组界面
     */
    public static final int TAB_GROUP = 2;

    /**
     * {@link CCPLauncherUITabView} 是否已经被初始化
     */
    private boolean mTabViewInit = false;

    /**
     * 缓存三个TabView
     */
    private final HashMap<Integer, Fragment> mTabViewCache = new HashMap<Integer, Fragment>();

    public ArrayList<BeanEditDiaryNode> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (mLauncherUI != null) {
            LogUtil.i(LogUtil.getLogUtilsTag(ActivityEditDiaryDayDetail.class),
                    "finish last LauncherUI");
            mLauncherUI.finish();
        }
        mLauncherUI = this;
        super.onCreate(savedInstanceState);
        // 设置页面默认为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initLauncherUIView();
    }

    /**
     * 初始化主界面UI视图
     */
    private void initLauncherUIView() {
        mLauncherView = getLayoutInflater().inflate(R.layout.activity_edit_diary_day, null);
        setContentView(mLauncherView);

        int day = initBeanEditDiaryNode();

        TextView tittle = (TextView) findViewById(R.id.middle_tv);
        tittle.setText("编辑第" + (day + 1) + "天日记");

        mTabViewInit = true;
        mCustomViewPager = (CCPCustomViewPager) findViewById(R.id.pager);
        mCustomViewPager.setOffscreenPageLimit(3);

        if (mLauncherUITabView != null) {
            mLauncherUITabView.setOnUITabViewClickListener(null);
            mLauncherUITabView.setVisibility(View.VISIBLE);
        }
        mLauncherUITabView = (DiaryLauncherUITabView) findViewById(R.id.laucher_tab_top);
        mCustomViewPager.setSlideEnabled(true);

        mLauncherViewPagerAdapter = new LauncherViewPagerAdapter(this,
                mCustomViewPager);
        mLauncherUITabView
                .setOnUITabViewClickListener(mLauncherViewPagerAdapter);
        ctrlViewTab(0);
    }

    /**
     * @author 丑旦
     * 初始化beanEditDiaryNode,用于请求数据
     */
    public int initBeanEditDiaryNode() {
        int day = 0;
        list = getIntent().getParcelableArrayListExtra(Constant.LIST_DIARY_NODE_DATA);
        Log.e("=-=-=", "=-=-=list.size()=-=-=" + list.size());
        if (list.size() > 0)
            day = list.get(0).getDay();
        return day;
    }

    /**
     * 根据TabFragment Index 查找Fragment
     *
     * @param tabIndex
     * @return
     */
    public final BaseFragment getTabView(int tabIndex) {
        LogUtil.d(LogUtil.getLogUtilsTag(ActivityEditDiaryDayDetail.class),
                "get tab index " + tabIndex);
        if (tabIndex < 0) {
            return null;
        }

        if (mTabViewCache.containsKey(Integer.valueOf(tabIndex))) {
            return (BaseFragment) mTabViewCache.get(Integer.valueOf(tabIndex));
        }

        BaseFragment mFragment = null;
        Bundle bundle = new Bundle();
        switch (tabIndex) {
            case TAB_CONVERSATION:
                mFragment = (TabFragmentListView) Fragment.instantiate(this,
                        MorningFrag.class.getName(), null);
                break;
            case TAB_ADDRESS:
                mFragment = (TabFragmentListView) Fragment.instantiate(this,
                        AfternoonFrag.class.getName(), null);
                break;
            case TAB_GROUP:
                mFragment = (TabFragmentListView) Fragment.instantiate(this,
                        NightFrag.class.getName(), null);
                break;

            default:
                break;
        }

        if (mFragment != null) {
            mFragment.setActionBarActivity(this);
//			//传递数据
            bundle.putParcelableArrayList(Constant.BEAN_EDIT_DIARY_NODE, list);
            mFragment.setArguments(bundle);
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

        LogUtil.d(LogUtil.getLogUtilsTag(ActivityEditDiaryDayDetail.class),
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
//	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		LogUtil.d(LogUtil.getLogUtilsTag(ActivityEditDiaryDayDetail.class), " onKeyDown");
//
//		try {
//
//			return super.dispatchKeyEvent(event);
//		} catch (Exception e) {
//			LogUtil.e(LogUtil.getLogUtilsTag(ActivityEditDiaryDayDetail.class),
//					"dispatch key event catch exception " + e.getMessage());
//		}
//
//		return false;
//	}


    /**
     * TabView 页面适配器
     *
     * @author 容联•云通讯
     * @version 4.0
     * @date 2014-12-4
     */
    private class LauncherViewPagerAdapter extends FragmentStatePagerAdapter
            implements ViewPager.OnPageChangeListener,
            DiaryLauncherUITabView.OnUITabViewClickListener {
        /**
         *
         */
        private int mClickTabCounts;
        private ContactListFragment mContactUI;
        private TabFragmentListView mGroupListFragment;

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

//		public void addTab(String tabSpec, Class<?> clss, Bundle args) {
//			String tag = tabSpec;
//
//			TabInfo info = new TabInfo(tag, clss, args);
//			mTabs.add(info);
//			notifyDataSetChanged();
//		}

        @Override
        public int getCount() {
            return 3;
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
//				mGroupListFragment.onGroupFragmentVisible(true);
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
                    mGroupListFragment = (TabFragmentListView) getTabView(CCPLauncherUITabView.TAB_VIEW_THIRD);
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
                TabFragment item = (TabFragment) getItem(tabIndex);
                item.onTabFragmentClick();
                return;
            }

            mClickTabCounts += mClickTabCounts;
            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                    "onUITabView Click count " + mClickTabCounts);
            mViewPager.setCurrentItem(tabIndex);
        }
    }

    /**
     * 网络注册状态改变
     *
     * @param connect
     */
    public void onNetWorkNotify(ECDevice.ECConnectState connect) {
        BaseFragment tabView = getTabView(TAB_CONVERSATION);
        if (tabView instanceof ConversationListFragment && tabView.isAdded()) {
            ((ConversationListFragment) tabView).updateConnectState();
        }
    }

}
