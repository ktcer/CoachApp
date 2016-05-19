package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cn.coachs.R;
import com.cn.coachs.model.personinfo.BeanHospital;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.chat.common.view.SettingItem;
import com.cn.coachs.ui.patient.others.myaccount.ClearEditText;
import com.cn.coachs.ui.patient.others.myaccount.ClearEditText.ClearListener;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.StringUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/11/13 下午5:16:29
 * @parameter
 * @return
 */
public class ActivitySearchHospital extends ActivityBasic implements
        OnItemClickListener, ClearListener, OnEditorActionListener {
    // public SearchView searchView;
    // public String currentSearchTip;
    public TextView middle_tv, right_tv;
    public ScheduledExecutorService scheduledExecutor = Executors
            .newScheduledThreadPool(10);
    public ArrayList<BeanHospital> list = new ArrayList<BeanHospital>();
    public ListView listHospital;
    public TextView noHospital;
    public SuggestionItemAdapter suggestionItemAdapter;
    public ClearEditText search;
    public BeanHospital beanHospital;
    public boolean hospitalEdit = false;// 标志位，为true，从编辑个人信息进入当前ActivitySearchHospital
    public String content = "";
    public int contentLength = 0;
    public int pageNum = 1;
    public int pageSize = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        setContentView(R.layout.activity_search_hospital);

        hospitalEdit = getIntent().getBooleanExtra(Constant.HOSPITAL_EDIT,
                false);
        System.out.println("=-=-=hospitalEdit=-=-=" + hospitalEdit);

        middle_tv = (TextView) findViewById(R.id.middle_tv);
        middle_tv.setText("填写医院");
        right_tv = (TextView) findViewById(R.id.right_tv);
        right_tv.setClickable(true);
        right_tv.setText("提交");
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setOnClickListener(this);

        noHospital = (TextView) findViewById(R.id.noHospital);

        listHospital = (ListView) findViewById(R.id.listHospital);
        suggestionItemAdapter = new SuggestionItemAdapter(this);
        startSearch(content);
        listHospital.setAdapter(suggestionItemAdapter);
        listHospital.setOnItemClickListener(this);

        search = (ClearEditText) findViewById(R.id.search);
        if (hospitalEdit) {
            String hospital = getIntent()
                    .getStringExtra(Constant.HOSPITAL_NAME);
            search.setHint(hospital);
        }
        search.setClearListener(this);
        search.setOnEditorActionListener(this);
        search.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                // TODO Auto-generated method stub
                contentLength = dstart;
                return null;
            }
        }});

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                noHospital.setVisibility(View.GONE);
                System.out.println("=-=-=onTextChanged=-=-=start...  " + start);
                System.out.println("=-=-=onTextChanged=-=-=before...  "
                        + before);
                System.out.println("=-=-=onTextChanged=-=-=count...  " + count);
                contentLength = contentLength + count;

                if (before >= count || (before == 0 & start > 0 & count == 1)) {
                    String content = StringUtil.stringFilter(search.getText()
                            .toString().substring(0, contentLength));
                    startSearch(content);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                Intent intent = new Intent();
                intent.putExtra(Constant.HOSPITAL_NAME, search.getText().toString());
                intent.putExtra(Constant.HOSPITAL_ID, "" + 0);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    public void startSearch(String newText) {
        if (newText != null)
            schedule(new SearchTipThread(newText), 500);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delayTimeMills) {
        return scheduledExecutor.schedule(command, delayTimeMills,
                TimeUnit.MILLISECONDS);
    }

    public class SearchTipThread implements Runnable {
        private String newText;

        public SearchTipThread(String newText) {
            super();
            this.newText = newText;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            // 开始搜索
            AsynSearchHospital asynSearchHospital = new AsynSearchHospital(
                    newText, pageNum, pageSize) {
                @Override
                protected void onPostExecute(JSONArray result) {
                    // TODO Auto-generated method stub
                    super.onPostExecute(result);
                    if (result != null) {
                        refresh(result);
                        Gson gson = new Gson();
                        list.clear();
                        for (int i = 0; i < result.length(); i++) {
                            try {
                                String object = result.getString(i);
                                BeanHospital beanHospital = gson.fromJson(
                                        object, BeanHospital.class);
                                list.add(beanHospital);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        suggestionItemAdapter.notifyDataSetChanged();
                    }
                }
            };
            asynSearchHospital.execute();
        }
    }

    public void refresh(JSONArray result) {
        if (result.length() == 0) {
            noHospital.setVisibility(View.VISIBLE);
            listHospital.setVisibility(View.GONE);
        } else {
            listHospital.setVisibility(View.VISIBLE);
            noHospital.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
        // TODO Auto-generated method stub
        System.out.println("=-=-=onEditorAction=-=-=..." + arg0.getText());
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        if (list.size() > 0) {
            beanHospital = list.get(arg2);
            System.out.println("=-=-=beanHospital=-=-=" + beanHospital);
            search.setText(beanHospital.getHospitalName());
            search.setSelection(search.getText().length());
        }
    }

    public class SuggestionItemAdapter extends BaseAdapter {
        private Context context;

        private SuggestionItemAdapter(Context context) {
            super();
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.activity_suggestion_item, parent, false);
                viewHolder.settingItem = (SettingItem) convertView
                        .findViewById(R.id.settingItem);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.settingItem.setTitleText(list.get(position)
                    .getHospitalName());
            return convertView;
        }

        class ViewHolder {
            SettingItem settingItem;
        }
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        hideSoftInput();
    }

    public void hideSoftInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View v = ActivitySearchHospital.this.getCurrentFocus();
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            search.clearFocus();
        }
    }

}
