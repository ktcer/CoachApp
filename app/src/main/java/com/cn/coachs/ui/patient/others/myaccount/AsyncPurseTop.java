/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.content.Context;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 */
public class AsyncPurseTop extends AsyncBasic {

    public AsyncPurseTop(Context mContext) {
        super(mContext);
        // TODO Auto-generated constructor stub
        setPath("/mywallet/querymymoney");
    }

    @Override
    protected HashMap<String, String> getMap() {
        // TODO Auto-generated method stub
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userID", "" + getUserId(0));
        map.put("userType", "" + 0);
        return map;
    }
}
