package com.cn.coachs.ui.chat.ui.chatting;

import java.util.HashMap;

import android.os.AsyncTask;
import android.util.Log;

import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.nurse.BeanUserChat;
import com.cn.coachs.util.AbsParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 专家回复消息
 *
 * @author chenkeliang
 */
public class AscynUserChat extends AsyncTask<Integer, Integer, BeanUserChat> {

    String result = "";
    private static String requestAddInfo = "/chat/reply";
    private String userId;
    private String eText;
    private String fansID;
    private BeanUserChat beenUserChat;

    public AscynUserChat(String userId, String pText, String expertID) {
        this.userId = userId;
        this.eText = pText;
        this.fansID = expertID;
    }

    @Override
    protected BeanUserChat doInBackground(Integer... params) {
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("expertID", userId);
        param.put("patientID", fansID);
        param.put("eText", eText);
        try {
            String url = AbsParam.getBaseUrl() + requestAddInfo;
            Log.i("input", url + param.toString());
            result = NetTool.sendPostRequest(url, param, "utf-8");
            Log.i("result", result);
            beenUserChat = JsonArrayToList(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beenUserChat;
    }

    @Override
    protected void onPostExecute(BeanUserChat result) {

    }

    /**
     * 解析返回来的Json数组
     *
     * @param jsonString
     * @return
     * @throws Exception
     */
    private BeanUserChat JsonArrayToList(String jsonString) throws Exception {
        Gson gson = new Gson();
        BeanUserChat beenUserChatq = null;

        // 添加我自己的信息
        if (jsonString != null) {
            if (!(jsonString.equals(-1))) {
                beenUserChatq = gson.fromJson(jsonString,
                        new TypeToken<BeanUserChat>() {
                        }.getType());
            }
        }
        return beenUserChatq;
    }

}
