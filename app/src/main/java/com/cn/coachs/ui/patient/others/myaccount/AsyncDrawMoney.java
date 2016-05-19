/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kuangtiecheng
 */
public class AsyncDrawMoney extends AsyncBasic {

    public AsyncDrawMoney(Context mContext) {
        super(mContext);
        // TODO Auto-generated constructor stub
        setPath("/mywallet/takecash");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = super.getMap();
        map.put("outAccount", "outAccount");
        map.put("money", "money");
        for (Map.Entry<String, String> entry : map.entrySet())
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        return map;
    }

}
