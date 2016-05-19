package com.cn.coachs.baidumap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.cn.coachs.R;
import com.cn.coachs.coach.model.BeanAddress;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;

public class AcitivityBaiduMap extends ActivityBasic {

    public AppMain app;
    static MyMapView mMapView = null;
    static TextView showAddr;

    public MKMapViewListener mMapListener = null;
    MyLocationOverlay myLocationOverlay = null;
    // 定位相关
    LocationClient mLocClient;
    public NotifyLister mNotifyer = null;
    public MyLocationListenner myListener = new MyLocationListenner();
    LocationData locData = null;
    private MapController mMapController = null;
    private MKAddrInfo infoAddress;
    private BeanAddress bean = new BeanAddress();

    static MKSearch mkSerach;

    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

//			Toast.makeText(AcitivityBaiduMap.this, "msg:" + msg.what,
//					Toast.LENGTH_SHORT).show();
        }

        ;
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        showAddr = (TextView) findViewById(R.id.showAddr);
        ((TextView) findViewById(R.id.middle_tv)).setText("位置");
        TextView rightTv = (TextView) findViewById(R.id.right_tv);
        rightTv.setVisibility(View.VISIBLE);
        rightTv.setOnClickListener(this);
        rightTv.setText("完成");
        MyIcon mi = new MyIcon(this);

        //在屏幕中心点添加接我图标
        getWindow().addContentView(
                mi,
                new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
        MyIcon2 m2 = new MyIcon2(this);
        getWindow().addContentView(
                m2,
                new LayoutParams(LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));

        mMapView = (MyMapView) findViewById(R.id.bmapsView);
        mMapController = mMapView.getController();

        initMapView();
        app = AppMain.getInstance().getInstance();
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);


        //搜索初始化
        mkSerach = new MKSearch();
        mkSerach.init(app.mBMapManager, new MKSearchListener() {

            @Override
            public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
                                                int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onGetTransitRouteResult(MKTransitRouteResult arg0,
                                                int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onGetPoiDetailSearchResult(int arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
                                                int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onGetAddrResult(MKAddrInfo info, int arg1) {
                infoAddress = info;
                bean.setAddress(info.strAddr);
                showAddr.setText(info.strAddr);
            }
        });


        //设置地图相关
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(300000);
        mLocClient.setLocOption(option);
        option.setLocationNotify(true);
        mLocClient.start();
        mMapView.getController().setZoom(19);
        mMapView.getController().enableClick(true);
        mMapView.displayZoomControls(true);
        mMapListener = new MKMapViewListener() {

            public void onMapMoveFinish() {

            }

            public void onClickMapPoi(MapPoi mapPoiInfo) {
                // TODO Auto-generated method stub
                String title = "";
                if (mapPoiInfo != null) {
                    title = mapPoiInfo.strText;
                    Toast.makeText(AcitivityBaiduMap.this, title, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        };
        mMapView.regMapViewListener(AppMain.getInstance().mBMapManager,
                mMapListener);
        myLocationOverlay = new MyLocationOverlay(mMapView);
        locData = new LocationData();
        myLocationOverlay.setData(locData);
        mMapView.getOverlays().add(myLocationOverlay);
        myLocationOverlay.enableCompass();
        mMapView.refresh();
    }

    private void initMapView() {
        mMapView.setLongClickable(true);
        // mMapController.setMapClickEnable(true);
        // mMapView.setSatellite(false);
    }

    /**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        public void onReceiveLocation(BDLocation location) {
            Log.i("================", " null ============ null" + location);
            if (location == null)
                return;
            Log.i("================", "not null ============not null");
            // 31.192695,121.42712,3000
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.direction = 2.0f;
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            Log.d("loctest",
                    String.format("before: lat: %f lon: %f",
                            location.getLatitude(), location.getLongitude()));
            // GeoPoint p = CoordinateConver.fromGcjToBaidu(new
            // GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *
            // 1e6)));
            // Log.d("loctest",String.format("before: lat: %d lon: %d",
            // p.getLatitudeE6(),p.getLongitudeE6()));
            myLocationOverlay.setData(locData);
            mMapView.refresh();
            bean.setLatitude(location.getLatitude());
            bean.setLongitude(location.getLongitude());
            mMapController.setCenter(new GeoPoint((int) (locData.latitude *
                    1e6),
                    (int) (locData.longitude * 1e6)));
            mMapController
                    .animateTo(new GeoPoint((int) (locData.latitude * 1e6),
                            (int) (locData.longitude * 1e6)), mHandler
                            .obtainMessage(1));
        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    public class NotifyLister extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance) {
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.right_tv:
                //添加给第一个Activity的返回值，并设置resultCode
                if (bean.getAddress() == null || bean.getLongitude() == 0.00) {
                    showToastDialog("请再次选择位置");
                } else {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("beanAddress", bean);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }


                break;
        }
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    public static void getPosition(GeoPoint g) {
        mkSerach.reverseGeocode(g);
        showAddr.setText("获取位置中...");
    }

}
