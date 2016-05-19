package com.cn.coachs.ui.chat.core;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cn.coachs.R;
import com.cn.coachs.http.NetTool;
import com.cn.coachs.model.nurse.BeanExpert;
import com.cn.coachs.ui.chat.common.CCPAppManager;
import com.cn.coachs.ui.chat.storage.ContactSqlManager;
import com.cn.coachs.ui.chat.storage.ConversationSqlManager;
import com.cn.coachs.ui.chat.ui.contact.ContactLogic;
import com.cn.coachs.ui.chat.ui.contact.ECContacts;
import com.cn.coachs.util.AbsParam;
import com.cn.coachs.util.Constant;
import com.cn.coachs.util.UtilsSharedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * com.cn.aihu.ui.chat.core in ECDemo_Android
 * Created by Jorstin on 2015/3/21.
 */
public class ContactsCache {
    // 初始化联系人
    public static final String ACTION_ACCOUT_INIT_CONTACTS = "com.cn.aihu.ui.chat.intent.ACCOUT_INIT_CONTACTS";

    private InterfaceGetContactsListener l;

    private static ContactsCache instance;

    private ArrayLists<ECContacts> contacts = new ArrayLists<ECContacts>();

    private LoadingTask asyncTask;
    public ArrayList<BeanExpert> allExpertList = new ArrayList<BeanExpert>();

    private ContactsCache() {

    }

    public void setOnGetContactsDoneListener(InterfaceGetContactsListener ml) {
        this.l = ml;
    }

    public static ContactsCache getInstance() {
        if (instance == null) {
            instance = new ContactsCache();
        }

        return instance;
    }

    /*
     * 获取当前用户的保健专家
     */
    private class LoadingTask extends AsyncTask<Integer, Integer, ArrayList<BeanExpert>> {

        String result;
        private static final String family = "/my/app/myuserlist";
        private long userId;

        public LoadingTask() {
            if (contacts == null) {
                contacts = new ArrayLists<ECContacts>();
            }
//    		else{
//    			contacts.clear();
//    		}
            allExpertList = new ArrayList<BeanExpert>();
            UtilsSharedData.initDataShare(CCPAppManager.getContext());// ////////
            userId = UtilsSharedData.getLong(Constant.USER_ID, 1);
        }


        @Override
        protected ArrayList<BeanExpert> doInBackground(Integer... params) {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("coachID", userId + "");
//    		param.put("pageSize", 1000+ "");
//    		param.put("pageNum", 1 + "");
            try {
                String url = AbsParam.getBaseUrl() + "/my/app/myuserlist";
                result = NetTool.sendPostRequest(url, param, "utf-8");
                Log.i("result", result);
                JsonArrayToList(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return allExpertList;
        }

        @Override
        protected void onPostExecute(ArrayList<BeanExpert> result) {
            if (result != null && result.size() > 0) {
                contacts.clear();
//    			int i = 0;
                for (BeanExpert bean : result) {
                    ECContacts beanContact = new ECContacts();
                    beanContact.setContactid(bean.getUserID() + "0");
                    beanContact.setNickname(bean.getName());
                    beanContact.setSex(bean.getSex());
                    beanContact.setAge(bean.getAge() + "");
                    beanContact.setImgUrl(bean.getPicUrl());
//    				beanContact.setHeight(bean.getHeight()+"");
//    				beanContact.setWeight(bean.getWeight()+"");
//    				beanContact.setIll(bean.getDisease());
                    beanContact.setPaitentID(bean.getUserID());
//    				beanContact.setIsMaster(bean.getIsMaster());
                    ContactLogic.pyFormat(beanContact);
                    contacts.add(beanContact);
                }
            } else if (result == null || result.size() <= 0 || contacts.size() < 0) {
                Toast.makeText(CCPAppManager.getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
            }
            ContactLogic.sortContacts(contacts);
            ContactSqlManager.insertContacts(contacts);
            if ((result.size()) != 0) {
                Cursor cursor = ConversationSqlManager.getConversationCursor();
                while (cursor.moveToNext()) {
                    if (deleteOrNot(cursor, result)) {
                        ConversationSqlManager.delSession(cursor.getString(4));
                    }

                }
            }
            l.getContactsDone();
            asyncTask = null;
        }

        private boolean deleteOrNot(Cursor cursor, ArrayList<BeanExpert> result) {
            for (BeanExpert expert : result) {
                String sessionId = cursor.getString(4);
                String patientId = expert.getUserID() + "0";
                if (sessionId.equals(patientId)) {
                    return false;
                }
            }
            return true;
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
            allExpertList = gson.fromJson(jsonString, new TypeToken<List<BeanExpert>>() {
            }.getType());
            //新加一个空值，用于添加专家
        }

    }
//    private class LoadingTask extends AsyncTask<Intent, Void, Long> {
//        ArrayLists<ECContacts> contactList = null;
//
//        public LoadingTask() {
//        }
//
//        @Override
//        protected Long doInBackground(Intent... intents) {
//            try {
//                LogUtil.d("contatsCache:开始加载联系人");
    //contactList = ContactLogic.getPhoneContacts(CCPAppManager.getContext());
//                contactList = ContactLogic.getContractList(true);
//                ContactLogic.getMobileContactPhoto(contactList);
//            } catch (Exception ce) {
//                ce.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Long result) {
//            if (contactList != null) {
//                //PinyinHelper.release();
//                ArrayLists<ECContacts> oldContacts = contacts;
//                contacts = contactList;
//                //added
//                ArrayList<String> phones = new ArrayList<String>();
//                for (ECContacts o : contacts) {
//                    List<Phone> phoneList = o.getPhoneList();
//                    if (phoneList == null) {
//                        continue;
//                    }
//                    for (Phone phone : phoneList) {
//                        if (!TextUtils.isEmpty(phone.getPhoneNum()))
//                            phones.add(phone.getPhoneNum());
//                    }
//                }
//                String[] array = phones.toArray(new String[]{});
//                Intent intent = new Intent(ACTION_ACCOUT_INIT_CONTACTS);
//                intent.putExtra("array", array);
//
//                CCPAppManager.getContext().sendBroadcast(intent);
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//        }
//    }

    public synchronized void load() {
        try {
            if (asyncTask == null) {
                asyncTask = new LoadingTask();
            }
            asyncTask.execute();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

//    public void reload() {
//        try {
//            stop();
//            load();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void stop() {
//        try {
//            if (asyncTask != null && !asyncTask.isCancelled()) {
//                asyncTask.cancel(true);
//                asyncTask = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * @return the contacts
     */
    public synchronized ArrayLists<ECContacts> getContacts() {
        return contacts;
    }

    //获取秘书数据的接口
    public interface InterfaceGetContactsListener {

        void getContactsDone();

    }

}
