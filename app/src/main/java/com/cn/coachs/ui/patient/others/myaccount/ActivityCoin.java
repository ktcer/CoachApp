/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.os.Bundle;
import android.widget.TextView;

import com.cn.coachs.R;
import com.cn.coachs.model.personinfo.BeanOverage;
import com.cn.coachs.ui.basic.ActivityBasicListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collection;
import java.util.List;

/**
 * @author kuangtiecheng 我的-设置-请求个人信息、退出登录
 */
public class ActivityCoin extends ActivityBasicListView {
    private List<BeanOverage> infoList, tempInfoList;
    protected int pageNum = 1;
    private AdapterMyIntegral adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.myaccount_coin);

        TextView midTV = (TextView) findViewById(R.id.middle_tv);
        midTV.setText("我的金币");

    }


//	private void getMyCoins() {
//		// showProgressBar();
//		AsyncGetMyCoins mTask = new AsyncGetMyCoins(ActivityCoin.this) {
//
//			@Override
//			protected void onPostExecute(String result) {
//				// TODO Auto-generated method stub
//				super.onPostExecute(result);
//				hideProgressBar();
//				// adapter.notifyDataSetChanged();
//			}
//
//		};
//		mTask.execute();
//
//	}

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private void JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        // List<BeanOverage> beanOveragelist = new ArrayList<BeanOverage>();

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                tempInfoList.addAll((Collection<? extends BeanOverage>) gson
                        .fromJson(jsonString,
                                new TypeToken<List<BeanOverage>>() {
                                }.getType()));
            }
        }
    }

}
