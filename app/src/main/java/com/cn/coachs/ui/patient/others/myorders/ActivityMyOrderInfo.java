package com.cn.coachs.ui.patient.others.myorders;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.com.alipay.pay.Pays;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.healthpost.BeanHealthPostOrderDetail;
import com.cn.coachs.ui.AppMain;
import com.cn.coachs.ui.basic.ActivityBasic;
import com.cn.coachs.ui.paychoose.PayRadioGroup;
import com.cn.coachs.ui.paychoose.PayRadioGroup.OnCheckedChangeListener;
import com.cn.coachs.ui.paychoose.PayRadioPurified;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.CustomDialog;
import com.cn.coachs.util.FButton;
import com.cn.coachs.weixin.pay.Constants;
import com.cn.coachs.weixin.pay.MD5;
import com.cn.coachs.weixin.pay.Util;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 我的订单详情
 *
 * @author kuangtiecheng
 */
public class ActivityMyOrderInfo extends ActivityBasic {
    //	private static String queryServerInfo = "queryreserveInfo";
    private static String cancelReserve = "cancelreserve";
    private Dialog alterDialog;
    private BeanHealthPostOrderDetail beanHealthPostOrderDetail;
    public static String orderNo = "";
    public static float price;
    private Byte status;
    private TextView tvOrderNo;
    private TextView tvOrderPeople;
    private TextView tvOrderTime;
    private TextView tvOrderDetail;
    private ImageView imgOrderCover;
    private TextView tvOrderPrice;
    private FButton buyOrCancelBtn;
    private PayRadioGroup group;
    private String desc = "";
    private String flag = "微信支付";

    //微信支付
    private static final String TAG = "MicroMsg.SDKSample.PayActivity";

    PayReq req;
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    //	TextView show;
    Map<String, String> resultunifiedorder;
    StringBuffer sb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorderinfo);
        initial();
    }

    private void initial() {
        AppMain.kindsPay = 0;
        TextView titleText = (TextView) findViewById(R.id.middle_tv);
        group = (PayRadioGroup) findViewById(R.id.genderGroup);
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(PayRadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                int radioButtonId = group.getCheckedRadioButtonId();
//				PayRadioButton rb = (PayRadioButton)MainActivity.this.findViewById(radioButtonId);
//				Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();

                PayRadioPurified rl = (PayRadioPurified) ActivityMyOrderInfo.this.findViewById(radioButtonId);
                for (int i = 0; i < group.getChildCount(); i++) {
                    ((PayRadioPurified) group.getChildAt(i)).setChangeImg(checkedId);
                }
                flag = rl.getTextTitle().toString();
                if (flag == null) {

                } else {
//					payTectView.setVisibility(View.VISIBLE);
                }

//				Toast.makeText(ActivityPayChoose.this, rl.getTextTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        orderNo = getIntent().getStringExtra(Constant.ORDER_NO);
        status = getIntent().getByteExtra(Constant.ORDER_STATUS, Byte.valueOf("0"));
        price = getIntent().getFloatExtra(Constant.ORDER_PRICE, 0.0f);
        tvOrderNo = (TextView) findViewById(R.id.order_no);
        tvOrderPeople = (TextView) findViewById(R.id.apart_people);
        tvOrderTime = (TextView) findViewById(R.id.order_time);
        tvOrderDetail = (TextView) findViewById(R.id.order_detail);
        tvOrderPrice = (TextView) findViewById(R.id.order_price);
        imgOrderCover = (ImageView) findViewById(R.id.order_cover);
        buyOrCancelBtn = (FButton) findViewById(R.id.btn_buyorcancel);
        buyOrCancelBtn.setCornerRadius(3);
        switch (status) {
            case 0:
                //待支付
                group.setVisibility(View.VISIBLE);
                titleText.setText("确认购买");
                buyOrCancelBtn.setButtonColor(getResources().getColor(R.color.blue_second));
                buyOrCancelBtn.setText("去支付");
                buyOrCancelBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        pay();
                    }
                });
                break;
            case 1:
                //执行中
                group.setVisibility(View.GONE);
                titleText.setText("订单详情");
                buyOrCancelBtn.setButtonColor(getResources().getColor(R.color.red));
                buyOrCancelBtn.setText("取消订单");
                buyOrCancelBtn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        cancel();
                    }
                });
                break;
            case 2:
                //已退款
                group.setVisibility(View.GONE);
                titleText.setText("订单详情");
                buyOrCancelBtn.setVisibility(View.INVISIBLE);
                break;
            case 3:
                //退款中
                group.setVisibility(View.GONE);
                titleText.setText("订单详情");
                buyOrCancelBtn.setButtonColor(getResources().getColor(R.color.orangered));
                buyOrCancelBtn.setText("取消退款");
                break;
            case 4:
                //已过期
                group.setVisibility(View.GONE);
                titleText.setText("订单详情");
                buyOrCancelBtn.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        showProgressBar();
        QueryOrderInfo task = new QueryOrderInfo();
        task.execute();
    }


    private void pay() {
        // TODO Auto-generated method stub
        if (flag.equals("微信支付")) {
            req = new PayReq();
            sb = new StringBuffer();

            msgApi.registerApp(Constants.APP_ID);

            String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

            GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
            getPrepayId.execute();
        } else if (flag.equals("支付宝支付")) {
            Pays pays = new Pays(ActivityMyOrderInfo.this, desc, desc, "0.01", orderNo, price, 0);//mHandler
            pays.check();
            pays.pay();

        } else if (flag.equals("银联支付")) {
            Toast.makeText(ActivityMyOrderInfo.this, "银联支付暂时还没开通，嘿嘿", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 取消订单对话框
     *
     * @author Hubert
     */
    private void cancel() {
        alterDialog = new CustomDialog.Builder(this).setTitle("提示")
                .setMessage("您确认要取消当前订单么？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        CancelTask task = new CancelTask();
                        task.execute();
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        alterDialog.dismiss();
                    }
                }).create();
        alterDialog.show();
    }

    /*
     * 查询订单详情
     */
    private class QueryOrderInfo extends AsyncTask<Integer, Integer, String> {
        String result = "";

        @Override
        protected String doInBackground(Integer... params) {

            HashMap<String, String> param = new HashMap<String, String>();
            param.put("ddh", orderNo);
            try {
                result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/travel/paydeail", param, "utf-8");
                Log.i("result", result);
                jsonToArray(result);
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            tvOrderNo.setText(orderNo);
            if (beanHealthPostOrderDetail != null) {
                tvOrderPeople.setText("60岁以上老人 " + beanHealthPostOrderDetail.getOlder() + " 位\n" +
                        "成人 " + beanHealthPostOrderDetail.getNormal() + " 位\n" +
                        "10岁以下小孩 " + beanHealthPostOrderDetail.getKids() + " 位");
                tvOrderTime.setText(beanHealthPostOrderDetail.getActivetime());
                tvOrderDetail.setText("【" + beanHealthPostOrderDetail.getTag() + "】" + beanHealthPostOrderDetail.getTitle());
                ImageLoader.getInstance().displayImage(AbsParam.getBaseUrl() + beanHealthPostOrderDetail.getCover(), imgOrderCover, AppMain.initImageOptions(R.drawable.default_life_icon, true));
                desc = "【" + beanHealthPostOrderDetail.getTag() + "】" + beanHealthPostOrderDetail.getActivetime() + " " + beanHealthPostOrderDetail.getTitle();
                tvOrderPrice.setText("¥" + price + "");
            }

            hideProgressBar();
        }
    }

    private void jsonToArray(String json) {
        Gson gson = new Gson();
        beanHealthPostOrderDetail = gson.fromJson(json, BeanHealthPostOrderDetail.class);
    }

    /*
     * 取消订单
     */
    private class CancelTask extends AsyncTask<Integer, Integer, String> {
        int resultID;
        String detail;

        @Override
        protected String doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("ddh", orderNo);
            try {
                String result = NetTool.sendPostRequest(AbsParam.getBaseUrl()
                        + "/travel/paycancel", param, "utf-8");
                Log.i("result", result);
                JSONObject jsObject = new JSONObject(result);
                resultID = jsObject.getInt("result");
                detail = jsObject.getString("detail");
            } catch (Exception e) {
                hideProgressBar();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            hideProgressBar();
            String resultContent = "";
            if (resultID == 0) {
                resultContent = "取消失败";
            } else if (resultID == 1) {
                resultContent = "取消成功";
            }
            Toast.makeText(ActivityMyOrderInfo.this,
                    "提交" + resultContent + "!" + detail, Toast.LENGTH_SHORT)
                    .show();
        }
    }


    /**
     * 下面是微信支付
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);


        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        ActivityMyOrderInfo.this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");


            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        return sb.toString();

    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ActivityMyOrderInfo.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

            resultunifiedorder = result;

            genPayReq();
            sendPayReq();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            Log.e("orion", entity);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            Log.e("orion", content);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }


    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //瀹炰緥鍖杝tudent瀵硅薄
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }


    //
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();
            String Allprice = new String();
            Allprice = (int) (price * 100) + "";//Allprice不能有小数点，不然微信调不起来


            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
            packageParams.add(new BasicNameValuePair("body", desc));//
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", "http://121.40.35.3/test"));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", Allprice));//String.valueOf(price*100)));//
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));


            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));


            String xmlstring = toXml(packageParams);

            return new String(xmlstring.getBytes(), "ISO8859-1");

//			return xmlstring;

        } catch (Exception e) {
            Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }


    }

    private void genPayReq() {

        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

//		show.setText(sb.toString());

        Log.e("orion", signParams.toString());

    }

    private void sendPayReq() {


        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }

}
