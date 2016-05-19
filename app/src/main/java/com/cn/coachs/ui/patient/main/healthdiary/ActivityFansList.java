package com.cn.coachs.ui.patient.main.healthdiary;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.myaccount.BeanFans;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasicListView;
import com.cn.coachs.ui.chat.ui.chatting.base.EmojiconTextView;
import com.cn.coachs.ui.chat.ui.contact.ContactDetailActivity;
import com.cn.coachs.ui.patient.others.myaccount.AsyncFansList;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.CircleImageView;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.cn.coachs.util.refreshlistview.XListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//public class ActivityFansList extends ActivityBasic implements OnItemClickListener,IXListViewListener{
//	private XListView mList;
//	private ArrayList<BeanFans> fansList;
//	private FansItemAdapter mAdapter;
//	public int flag;//1全部粉丝；0新增粉丝
//	public int pageNum = 1;
//	public int pageSize = 5;
//	public AsynMemberList asynMemberList;
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		init();
//	}
//	
//	protected void init(){
//		setContentView(R.layout.myaccount_fans);
//		TextView midTV = (TextView) findViewById(R.id.middle_tv);
//		midTV.setText("我的粉丝");
//		
//		flag = getIntent().getIntExtra(Constant.FANS_LIST_FLAG, -1);
//		System.out.println("=-=-=flag=-=-="+flag);
//		fansList = getIntent().getParcelableArrayListExtra(ActivityHealthDiary.LIST);
//
//		mList = (XListView)findViewById(R.id.mList);
//		
//		if (flag == 1) {
//			fansList = new ArrayList<BeanFans>();
//			requestNext();
//		} else {		
//			mAdapter = new Fan+sItemAdapter(this,fansList);
//			mList.setAdapter(mAdapter);
//			mList.setPullLoadEnable(false);		
//		}
//		
//		mAdapter = new FansItemAdapter(this,fansList);
//		mList.setAdapter(mAdapter);
//		mList.setXListViewListener(this);
//		mList.setPullRefreshEnable(false);										
//		mList.setOnItemClickListener(this);				
//	} 
//
//	protected long getUserId(int role) {
//		UtilsSharedData.initDataShare(this);
//		long userId = UtilsSharedData.getLong(Constant.USER_ID, role);
//		return userId;
//	}
//	
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO Auto-generated method stub
//		System.out.println("=-=-=position=-=-="+position);
//		if(position != fansList.size()+1){
//			BeanFans mBeanFans = fansList.get(position-1);
//			Intent intent = new Intent(getApplicationContext(), ContactDetailActivity.class);
//			
//			intent.putExtra(ContactDetailActivity.MOBILE, mBeanFans.getPaitentID()+"0");
//			intent.putExtra(Constant.FANS_NAME, mBeanFans.getName());
//			intent.putExtra(Constant.fANS_IMAGEURL, mBeanFans.getHeadImgUrl());
//			intent.putExtra(Constant.FANS_GENDER, mBeanFans.getSex());
//			intent.putExtra(Constant.FANS_AGE, ""+mBeanFans.getAge());
//			intent.putExtra(Constant.FANS_HEIGHT, mBeanFans.getHeight());
//			intent.putExtra(Constant.FANS_WEIGHT, mBeanFans.getWeight());
//			
//			//@丑旦，勿动！！！！！
////        intent.putExtra(Constant.FANS_BEANS, mBeanFans);
//			startActivity(intent);		
//		}
//	}
//
//	@Override
//	public void onRefresh() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void onLoadMore() {
//		// TODO Auto-generated method stub
//		requestNext();
//		System.out.println("=-=-=fansList.size()=-=-="+fansList.size());
//	}	
//	
//	public void requestNext(){
//		System.out.println("=-=-=pageNum=-=-="+pageNum);
//		System.out.println("=-=-=pageSize=-=-="+pageSize);
//		System.out.println("=-=-=getUserId(1)=-=-="+getUserId(1));
//		asynMemberList = new AsynMemberList(pageNum,pageSize,getUserId(1)){
//			@Override
//			protected void onPostExecute(JSONArray result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				if(result.length() == 0){
//					mList.stopLoadMore();
//				}else{				
//					for (int i = 0; i < result.length(); i++) {
//						try {
//							JSONObject jsonObject = result.getJSONObject(i);
//							BeanFans beanFans = new BeanFans();
//							beanFans.setContactid(jsonObject.getString("mobile"));
//							beanFans.setMemberName(jsonObject.getString("memberName"));
//							beanFans.setName(jsonObject.getString("memberName"));
//							beanFans.setImgUrl(jsonObject.getString("imgUrl"));
//							beanFans.setHeadImgUrl(jsonObject.getString("imgUrl"));
//							beanFans.setSex(jsonObject.getString("sex"));
//							beanFans.setAge(jsonObject.getString("age"));
//							beanFans.setHeight(jsonObject.getString("height"));
//							beanFans.setWeight(jsonObject.getString("weight"));
//							beanFans.setPaitentID(Long.parseLong(jsonObject.getString("patientID")));
//							fansList.add(beanFans);						
//							mAdapter = new FansItemAdapter(getApplicationContext(),fansList);
//							mList.setAdapter(mAdapter);
//							mList.stopLoadMore();
//						} catch (JSONException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//				}
//				pageNum ++;
//			}		
//		};	
//		asynMemberList.execute();
//	}
//}

/**
 * @author kuangtiecheng
 * @version 1.0
 * @date 创建时间：2015/10/19 上午9:59:54
 * @parameter
 * @return
 */

public class ActivityFansList extends ActivityBasicListView {

    public int flag;// 1全部粉丝；0新增粉丝
    public ArrayList<BeanFans> listNewFans, listUntalkFans;
    private List<BeanFans> infoList, tempInfoList;
    private TestReportListAdapter testReportListAdapter;
    private int type;
    protected int pageNum = 1;
    public String dateStr;// AsyncFansList中使用的日期，请求该日期下的爱护数和粉丝数

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlyaxlistview);
        initial();

    }

    /**
     * 基本组件初始化
     */
    private void initial() {
        infoList = new ArrayList<BeanFans>();
        tempInfoList = new ArrayList<BeanFans>();
        listNewFans = new ArrayList<BeanFans>();
        listUntalkFans = new ArrayList<BeanFans>();
        dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date());// 初始化日期参数，今天
        listView = (XListView) this.findViewById(R.id.xlist);
        ((TextView) findViewById(R.id.middle_tv)).setText("我的粉丝");
        initFansItem();
        // if(flag==0){
        // // infoList =
        // getIntent().getParcelableArrayListExtra(ActivityHealthDiary.LIST);
        // type = getIntent().getIntExtra("listFans", 0);
        // switch (type) {
        // case 1:
        // infoList = listNewFans;
        // break;
        // case 2:
        // infoList = listUntalkFans;
        // break;
        // case 3:
        // infoList = listUntalkFans;
        // break;
        // case 4:
        // infoList = listNewFans;
        // break;
        //
        // default:
        // break;
        // }
        //
        // }

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // if(flag==1){
        // showProgressBar();
        // QueryAllFansList taskQNL = new QueryAllFansList();
        // taskQNL.execute();
        // }else{
        showProgressBar();
        initFansItem();
        // switch (type) {
        // case 1:
        // infoList = listNewFans;
        // break;
        // case 2:
        // infoList = listUntalkFans;
        // break;
        // case 3:
        // infoList = listUntalkFans;
        // break;
        // case 4:
        // infoList = listNewFans;
        // break;
        //
        // default:
        // break;
        // }
        // listView.setAdapter(testReportListAdapter);
        // }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            // case R.id.fab_addtest:
            // Intent intent = new Intent(this, ActivityEvaluationSecond.class);
            // startActivity(intent);
            // break;

        }
    }

    /**
     * 查询粉丝列表（新增、所有）
     *
     * @author
     */
    private class QueryAllFansList extends AsyncTask<Integer, Integer, String> {

        public QueryAllFansList() {
            super();
        }

        String result = "";

        @Override
        protected String doInBackground(Integer... params) {
            UtilsSharedData.initDataShare(ActivityFansList.this);
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("userId",
                    "" + UtilsSharedData.getLong(Constant.USER_ID, 0));

            param.put("pageSize", 100 + "");
            param.put("pageNum", pageNum + "");
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/member/healthcare/list", param, "utf-8");
                Log.i("result", result);
                tempInfoList.clear();
                JsonArrayToList(result);
                if (tempInfoList.size() < 100) {
                    canLoadMore = false;
                } else {
                    canLoadMore = true;
                }
            } catch (Exception e) {
                canLoadMore = false;
                e.printStackTrace();
                hideProgressBar();

            }
            return null;
        }

        /**
         * 解析返回来的Json数组
         *
         * @param jsonString
         * @return
         * @throws Exception
         */
        private void JsonArrayToList(String jsonString) throws Exception {
            Gson gson = new Gson();
            if (jsonString != null) {
                if (!(jsonString.equals(-1))) {
                    // tempInfoList = gson.fromJson(jsonString,
                    // new TypeToken<List<BeanFans>>() {
                    // }.getType());
                    JSONArray jsonArray = new JSONArray(jsonString);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = jsonArray.getJSONObject(i);
                        BeanFans beanFans = new BeanFans();
                        if (json.getString("duration").equals("已过期")) {
                            continue;
                        }
                        beanFans.setContactid(json.getString("mobile"));
                        beanFans.setMemberName(json.getString("memberName"));
                        beanFans.setName(json.getString("memberName"));
                        beanFans.setImgUrl(json.getString("imgUrl"));
                        beanFans.setHeadImgUrl(json.getString("imgUrl"));
                        beanFans.setSex(json.getString("sex"));
                        beanFans.setAge(json.getString("age"));
                        beanFans.setHeight(json.getString("height"));
                        beanFans.setWeight(json.getString("weight"));
                        beanFans.setPaitentID(Long.parseLong(json
                                .getString("patientID")));

                        tempInfoList.add(beanFans);
                    }

                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (pageNum == 1) {
                infoList.clear();
            }
            for (BeanFans tmp : tempInfoList) {
                infoList.add(tmp);
            }
            if (canLoadMore) {
                listView.setPullLoadEnable(true);
            } else {
                listView.setPullLoadEnable(false);
            }
            if (infoList.size() == 0) {
                findViewById(R.id.empty_testdata_tv)
                        .setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.empty_testdata_tv))
                        .setText("暂无粉丝");
            } else {
                findViewById(R.id.empty_testdata_tv).setVisibility(View.GONE);
            }
            testReportListAdapter.notifyDataSetChanged();
            hideProgressBar();
            onLoad();

        }
    }

    private class TestReportListAdapter extends BaseAdapter {
        private Context context;
        public int count = 10;

        public TestReportListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
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
                convertView = LayoutInflater.from(ActivityFansList.this)
                        .inflate(R.layout.myaccount_fans_item, parent, false);
                holder.fans_name = (EmojiconTextView) convertView.findViewById(R.id.fans_name);
                holder.fans_sex = (TextView) convertView
                        .findViewById(R.id.fans_sex);
                holder.fans_age = (TextView) convertView
                        .findViewById(R.id.fans_age);
                holder.fans_head_image = (CircleImageView) convertView
                        .findViewById(R.id.fans_head_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.fans_name.setText(infoList.get(position).getName());
            holder.fans_sex.setText("性别： " + infoList.get(position).getSex());
            holder.fans_age.setText("年龄： " + infoList.get(position).getAge());
            System.out.println("=-=-=list.get(position).getImgUrl()=-=-="
                    + infoList.get(position).getHeadImgUrl());
            ImageLoader.getInstance().displayImage(
                    AbsParam.getBaseUrl()
                            + infoList.get(position).getHeadImgUrl(),
                    holder.fans_head_image,
                    AppMain.initImageOptions(R.drawable.default_user_icon,
                            false));
            // holder.fans_head_image.setOnClickListener(new OnClickListener(){
            // @Override
            // public void onClick(View v) {
            // // TODO Auto-generated method stub
            // if(mOnClickHeadImgListener!=null)
            // mOnClickHeadImgListener.onClickHeadImg(position);
            // }
            // });
            return convertView;
        }
    }

    private class ViewHolder {
        EmojiconTextView fans_name;
        //		TextView fans_name;
//		TextView fans_sex_age;
        TextView fans_sex;
        TextView fans_age;
        CircleImageView fans_head_image;
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        super.onRefresh();
        if (flag == 1) {
            pageNum = 1;
            // showProgressBar();
            QueryAllFansList taskQNL = new QueryAllFansList();
            taskQNL.execute();

        }
    }

    QueryAllFansList taskQNL;

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        // TODO Auto-generated method stub
        if (flag == 1) {

            if (canLoadMore) {
                if (taskQNL != null
                        && taskQNL.getStatus() == AsyncTask.Status.RUNNING) {
                    taskQNL.cancel(true); // 如果Task还在运行，则先取消它
                } else {
                    pageNum++;
                }
                taskQNL = new QueryAllFansList();
                taskQNL.execute();
            }
        }
    }

    public ArrayList<BeanFans> getList(JSONArray array)
            throws NumberFormatException, JSONException {
        ArrayList<BeanFans> list = new ArrayList<BeanFans>();
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                BeanFans bean = new BeanFans();
                JSONObject json = array.getJSONObject(i);
                bean.setAge(json.getString("age"));
                bean.setName(json.getString("memberName"));
                bean.setHeadImgUrl(json.getString("imgUrl"));
                bean.setSex(json.getString("sex"));
                bean.setContactid(json.getString("phone"));
                bean.setPaitentID(json.getLong("patientID"));
                bean.setHeight(json.getString("height"));
                bean.setWeight(json.getString("weight"));
                list.add(bean);
            }
            return list;
        }
        return null;
    }

    // 初始化日记项目，9点之后前两条不能点击
    public void initFansItem() {
        final int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        System.out.println("=-=-=hour=-=-=" + hour);

        AsyncFansList asyncFanList = new AsyncFansList(this, dateStr) {
            @Override
            protected void onPostExecute(JSONObject result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                // hideProgressBar();
                if (result != null) {
                    try {
                        flag = getIntent().getIntExtra(Constant.FANS_LIST_FLAG,
                                0);
                        type = getIntent().getIntExtra("listFans", 0);
                        if (flag == 0) {
                            String newFansInfo = result.getString("newFansInfo");
                            System.out.println("=-=-=newFansInfo=-=-="
                                    + newFansInfo);
                            String unTalkpatientInfo = result
                                    .getString("unTalkpatientInfo");
                            System.out.println("=-=-=unTalkpatientInfo=-=-="
                                    + unTalkpatientInfo);
                            if (!newFansInfo.equals("null")) {
                                JSONArray newFansarray = new JSONArray(newFansInfo);
                                System.out.println("=-=-=newFansarray=-=-="
                                        + newFansarray);
                                listNewFans = getList(newFansarray);
                            }
                            if (!unTalkpatientInfo.equals("null")) {
                                JSONArray untalkFansarray = new JSONArray(
                                        unTalkpatientInfo);
                                System.out.println("=-=-=untalkFansarray=-=-="
                                        + untalkFansarray);
                                listUntalkFans = getList(untalkFansarray);
                            }
                        }

                        if (flag == 0) {
                            switch (type) {
                                case 1:
                                    infoList = listNewFans;
                                    break;
                                case 2:
                                    infoList = listUntalkFans;
                                    break;
                                case 3:
                                    infoList = listUntalkFans;
                                    break;
                                case 4:
                                    infoList = listNewFans;
                                    break;

                                default:
                                    break;
                            }

                        } else {
                            QueryAllFansList taskQNL = new QueryAllFansList();
                            taskQNL.execute();
                        }

                        testReportListAdapter = new TestReportListAdapter(
                                ActivityFansList.this);
                        listView.setTag("listView");
                        if (flag == 0) {
                            listView.setPullLoadEnable(false);
                            listView.setPullRefreshEnable(false);
                            if (Integer.parseInt(result
                                    .getString("unTalkCounts")) == 0
                                    && type != 1 && type != 4) {
                                infoList = new ArrayList<BeanFans>();
                            }
                        } else if (flag == 1) {
                            listView.setPullLoadEnable(false);
                            listView.setPullRefreshEnable(true);
                        }
                        listView.setXListViewListener(ActivityFansList.this);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int position, long arg3) {
                                if (position != infoList.size() + 1) {
                                    BeanFans mBeanFans = infoList
                                            .get(position - 1);
                                    Intent intent = new Intent(
                                            getApplicationContext(),
                                            ContactDetailActivity.class);

                                    intent.putExtra(
                                            ContactDetailActivity.MOBILE,
                                            mBeanFans.getPaitentID() + "0");
                                    intent.putExtra(Constant.FANS_NAME,
                                            mBeanFans.getName());
                                    intent.putExtra(Constant.fANS_IMAGEURL,
                                            mBeanFans.getHeadImgUrl());
                                    intent.putExtra(Constant.FANS_GENDER,
                                            mBeanFans.getSex());
                                    intent.putExtra(Constant.FANS_AGE, ""
                                            + mBeanFans.getAge());
                                    intent.putExtra(Constant.FANS_HEIGHT,
                                            mBeanFans.getHeight());
                                    intent.putExtra(Constant.FANS_WEIGHT,
                                            mBeanFans.getWeight());

                                    // @丑旦，勿动！！！！！
                                    // intent.putExtra(Constant.FANS_BEANS,
                                    // mBeanFans);
                                    startActivity(intent);
                                }

                            }
                        });
                        if (flag == 0) {
                            hideProgressBar();
                        }
                        listView.setAdapter(testReportListAdapter);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        };
        asyncFanList.execute();
        // showProgressBar();
    }

}
