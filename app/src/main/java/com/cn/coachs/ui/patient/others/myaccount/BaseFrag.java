/**
 *
 */
package com.cn.coachs.ui.patient.others.myaccount;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * @author kuangtiecheng
 */
public abstract class BaseFrag extends Fragment implements OnClickListener {
    public View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(getLayout(), container);
        init();
        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public void startIntent(Class<?> cls, HashMap<String, String> map) {
        Intent intent = new Intent(getActivity(), cls);
        if (map != null) {
            Bundle mbundle = new Bundle();
            for (HashMap.Entry<String, String> entry : map.entrySet()) {
                mbundle.putString(entry.getKey(), entry.getValue());
            }
            intent.putExtras(mbundle);
        }
        startActivity(intent);
    }

    public void startIntent(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    public abstract int getLayout();

    public abstract void init();
}
