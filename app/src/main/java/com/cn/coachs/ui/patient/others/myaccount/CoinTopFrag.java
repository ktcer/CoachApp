/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cn.coachs.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author kuangtiecheng
 */
public class CoinTopFrag extends BaseFrag {
    private TextView coin_number;
    private Button exchange;

    @Override
    public int getLayout() {
        // TODO Auto-generated method stub
        return R.layout.myaccount_coin_top_frag;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        coin_number = (TextView) view.findViewById(R.id.coin_number);
        exchange = (Button) view.findViewById(R.id.exchange);
        exchange.setVisibility(View.GONE);
        AsyncGetMyCoins asyncGetMyCoins = new AsyncGetMyCoins(getActivity()) {
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                JSONObject json;
                try {
                    json = new JSONObject(result);
                    coin_number.setText(json.getString("coins") + "个");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        asyncGetMyCoins.execute();
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        if (v.getId() == R.id.exchange) {
//			请求数据线程

        }
    }
}
