package com.cn.coachs.ui.patient.others.mytest;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.ui.basic.ActivityBasic;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 我的测评
 *
 * @author kuangtiecheng
 */
public class ActivityMyTest extends ActivityBasic implements
        PopupWindow.OnDismissListener, OnScrollListener, OnItemClickListener {
    private ListView listView;
    private int[] pic = {R.drawable.setting_myaccount, R.drawable.setting_helpcenter,
            R.drawable.setting_aboutus, R.drawable.testcenter, R.drawable.setting_familyremind,
            R.drawable.setting_infocenter};
    private String[] array_name = new String[]{"跌倒概率测评", "心理健康测评", "心脏能力测评", "肺部测评", "肺部测评", "肺部测评"};
    private List<TestList> testList;
    private TextView search_btn;
    /*
     *搜索部分
     */
    private LinearLayout mainLayout;
    private RelativeLayout titleBarLayout;
    private int moveHeight;
    private int statusBarHeight;
    private PopupWindow popupWindow;
    private View searchView;
    private EditText searchEditText;
    private TextView searchTextView;
    private ListView resultListView;
    private View alphaView;
    private ImageView cancelEditTextIV;

    // 网络请求列表刷新部分
    private Thread mThread;
    private TestListAdapter lvAdapter;
    private TestListAdapter resultListViewAdapter;
    // private ListViewAdapter resultAdapter;
    private View loadingLayout;
    private int listTotal = 24;// 列表项总数(后台传来),不然不能提前取消载入动画


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mytest);
        initial();
        initSearchView();
        load();

    }

    protected void initial() {
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        titleText.setText("健康测评");
        listView = (ListView) this.findViewById(R.id.lv_myTest);
        testList = new ArrayList<TestList>();
        search_btn = (TextView) findViewById(R.id.right_tv);
        search_btn.setVisibility(View.GONE);
        //	search_btn.setVisibility(View.VISIBLE);
        search_btn.setBackgroundResource(R.drawable.search_icon);
        search_btn.setText("");
        search_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                showSearchBar();
                break;
            case R.id.cancel_edittext_iv:
                searchEditText.setText("");
                onDismiss();
            case R.id.popup_window_tv_search:
                setResultList();
                alphaView.setVisibility(View.GONE);
                resultListView.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void load() {
        writeData(pic, array_name);
        listView.setAdapter(new TestListAdapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
//				Map<String,Object> item=(Map<String,Object>)MyTest.this.adapter.getItem(position);
//				String Name=item.get("name").toString();
//				Toast.makeText(MyTest.this,Name, 1).show();
//				Intent intent=new Intent(MyTest.this,MyTestInfo.class);
                startActivity(ActivityMyTestInfo.class);
            }

        });
    }

    private void writeData(int[] pic, String[] array) {
        for (int i = 0; i < pic.length; i++) {
            TestList content = new TestList();
            content.setBitmap(BitmapFactory.decodeResource(
                    getResources(), pic[i]));
            content.setTestItem(array[i]);
            testList.add(content);
        }
    }

    private class TestListAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public TestListAdapter(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        @Override
        public int getCount() {
            return testList.size();
        }

        @Override
        public Object getItem(int position) {
            return testList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.list_item, null);
                holder.Test_Item = (TextView) convertView
                        .findViewById(R.id.list_item_title);
                holder.Test_Bitmap = (ImageView) convertView
                        .findViewById(R.id.list_item_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.Test_Item.setText(testList.get(position).getTestItem());
            holder.Test_Bitmap.setImageBitmap(testList.get(position).getBitmap());
            return convertView;
        }

    }

    private class ViewHolder {
        TextView Test_Item;
        /**
         * 预约日期
         */
        ImageView Test_Bitmap;/**预约类型*/
    }

    /**
     * 搜索部分初始化
     */

    private void initSearchView() {
        ////搜索框部分
        LayoutInflater mInflater = LayoutInflater.from(this);
        mainLayout = (LinearLayout) findViewById(R.id.linearLayout_myTest);
        titleBarLayout = (RelativeLayout) findViewById(R.id.top_title);
        searchView = mInflater.inflate(R.layout.popup_window_search, null);
        cancelEditTextIV = (ImageView) searchView.findViewById(R.id.cancel_edittext_iv);
        cancelEditTextIV.setOnClickListener(this);
        searchEditText = (EditText) searchView.findViewById(R.id.popup_window_et_search);
        searchEditText.setFocusable(true);
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    alphaView.setVisibility(View.VISIBLE);
                    resultListView.setVisibility(View.GONE);
                    cancelEditTextIV.setVisibility(View.GONE);
                } else {
                    cancelEditTextIV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchTextView = (TextView) searchView.findViewById(R.id.popup_window_tv_search);
        searchTextView.setOnClickListener(this);
        resultListView = (ListView) searchView.findViewById(R.id.popup_window_lv);
        resultListView.setTag("resultListView");
        //阴影View
        alphaView = searchView.findViewById(R.id.popup_window_v_alpha);
        alphaView.setOnClickListener(this);

        popupWindow = new PopupWindow(searchView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(this);

        loadingLayout = mInflater.inflate(R.layout.loading, null);
    }

    /**
     * 健康方案列表填充数据及设置点击、下拉刷新事件
     */
    private void setList() {

        // 添加到脚页显示
        listView.addFooterView(loadingLayout);
        // 给ListView添加适配器
        lvAdapter = new TestListAdapter(this);
        listView.setAdapter(lvAdapter);
        // 给ListView注册滚动监听
        listView.setOnScrollListener(this);
        // 给ListView注册点击监听
        listView.setOnItemClickListener(this);
    }

    private void setResultList() {

        // 添加到脚页显示
        resultListView.addFooterView(loadingLayout);
        // 给ListView添加适配器
        resultListViewAdapter = new TestListAdapter(this);
        resultListView.setAdapter(resultListViewAdapter);
        // 给ListView注册滚动监听
        resultListView.setOnScrollListener(this);
        // 给ListView注册点击监听
        resultListView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

//		Intent intent = new Intent(MyTest.this, ActivityMyOrderInfo.class);
//		intent.putExtra("state", State[position]);
//		startActivity(intent);

    }

    private void showSearchBar() {
        getStatusBarHeight();
        moveHeight = titleBarLayout.getHeight();
        Animation translateAnimation = new TranslateAnimation(0, 0, 0,
                -moveHeight);
        translateAnimation.setDuration(300);
        mainLayout.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
                mainLayout.setAnimation(anim);
                titleBarLayout.setVisibility(View.GONE);
                titleBarLayout.setPadding(0, -moveHeight, 0, 0);

                popupWindow.showAtLocation(mainLayout, Gravity.CLIP_VERTICAL,
                        0, statusBarHeight);
                openKeyboard();
            }
        });
    }

    private void getStatusBarHeight() {
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
    }

    private void openKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    public void onDismiss() {
        resetUI();
    }

    private void resetUI() {
        searchEditText.setText("");
        titleBarLayout.setPadding(0, 0, 0, 0);
        titleBarLayout.setVisibility(View.VISIBLE);
        Animation translateAnimation = new TranslateAnimation(0, 0, -moveHeight, 0);
        translateAnimation.setDuration(300);
        mainLayout.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {

            }


            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 0);
                mainLayout.setAnimation(anim);
                // titleBarLayout.setPadding(0, 0, 0, 0);

            }
        });
    }


    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        if (view.getTag().equals("listView")) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                // 开线程去下载网络数据
                if (mThread == null || !mThread.isAlive()) {
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                // 这里放你网络数据请求的方法，我在这里用线程休眠5秒方法来处理
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    };
                    mThread.start();
                }
            }
        } else if (view.getTag().equals("resultListView")) {
            if (firstVisibleItem + visibleItemCount == totalItemCount) {
                // 开线程去下载网络数据
                if (mThread == null || !mThread.isAlive()) {
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                // 这里放你网络数据请求的方法，我在这里用线程休眠5秒方法来处理
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    };
                    mThread.start();
                }
            }
        }

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 1:
                    if (lvAdapter.count <= 20) {
                        lvAdapter.count += 10;
                        int currentPage = lvAdapter.count / 10;
                        Toast.makeText(getApplicationContext(),
                                "第" + currentPage + "页", Toast.LENGTH_LONG).show();
                    } else {
                        listView.removeFooterView(loadingLayout);
                    }
                    // 重新刷新Listview的adapter里面数据
                    lvAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    if (resultListViewAdapter.count <= 20) {
                        resultListViewAdapter.count += 10;
                        int currentPage = resultListViewAdapter.count / 10;
                        Toast.makeText(getApplicationContext(),
                                "第" + currentPage + "页", Toast.LENGTH_LONG).show();
                    } else {
                        resultListView.removeFooterView(loadingLayout);
                    }
                    // 重新刷新Listview的adapter里面数据
                    resultListViewAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
}
