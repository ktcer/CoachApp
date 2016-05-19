package com.cn.coachs.ui;

import android.app.Activity;
import android.content.Intent;

import com.cn.coachs.ui.patient.main.TabActivityMain;
import com.cn.coachs.ui.welcome.ActivityWelcomePage;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用页面队列
 *
 * @author kuangtiecheng
 */
public class AppPool {

    private static AppPool instance = new AppPool();

    private List<Activity> listActivity = new ArrayList<Activity>();

    // 存储Activity名称，以此种方式得出页面之间的调用关系
    private List<String> listActivityName = new ArrayList<String>();

    private AppPool() {
    }

    /**
     * 添加Activity到队列
     *
     * @param activity Activity
     */
    public static void createActivity(Activity activity) {
        if (null == AppMain.app) {
            exit();
            Intent inte = new Intent(activity, ActivityWelcomePage.class);
            activity.startActivity(inte);
            activity.finish();
            return;
        }
        if (null == instance) {
            instance = new AppPool();
        }
        instance.listActivityName.add(activity.getClass().getSimpleName());
        instance.listActivity.add(activity);
        Loger.print("ListActivityName >>> " + instance.listActivityName);
    }

    /**
     * 从列队中销毁Activity
     *
     * @param activity Activity
     */
    public static void destroyActivity(Activity activity) {
        if (null == instance) {
            instance = new AppPool();
        }
        instance.listActivity.remove(activity);
    }

    /**
     * 退出应用程序
     */
    public static void exit() {
        if (null == instance) {
            instance = new AppPool();
        }
        int size = instance.listActivity.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity act = instance.listActivity.remove(i);
            if (null != act) {
                act.finish();
            }
        }
        System.exit(0);
    }

    /**
     * 退回到导航页面（首页）
     */
    public static void backHome() {
        if (null == instance) {
            instance = new AppPool();
        }
        int size = instance.listActivity.size();
        String mPage = TabActivityMain.class.getName();
        for (int i = size - 1; i >= 0; i--) {
            Activity act = instance.listActivity.remove(i);
            if (null != act) {
                String cname = act.getClass().getName();
                if (cname.equals(mPage)) {
                    break;
                } else {
                    act.finish();
                }
            }
        }
    }

    /**
     * 销毁所有页面
     */
    public static void finishAllAct() {
        if (null == instance) {
            instance = new AppPool();
        }
        int size = instance.listActivity.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity act = instance.listActivity.remove(i);
            if (null != act) {
                act.finish();
            }
        }

    }

    /**
     * 退回到指定的页面
     *
     * @param name
     */
    public static void backToSpecificActivity(String name) {
        if (null == instance) {
            instance = new AppPool();
        }
        int size = instance.listActivity.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity act = instance.listActivity.remove(i);
            if (null != act) {
                String cname = act.getClass().getName();

                if (cname.equals("com.cn.coachs.ui.patient.main.TabActivityMain") ||
                        cname.equals("com.cn.coachs.ui.patient.main.healthdiary.ActivityHealthDiary") ||
                        cname.equals("com.cn.coachs.ui.chat.ui.ActivityHealthAssist") ||
                        cname.equals("com.cn.coachs.ui.patient.main.healthclass.ActivityClassRoom") ||
                        cname.equals("com.cn.coachs.my.ActivityMyPage") ||
                        cname.equals("com.cn.coachs.ui.chat.ui.contact.ContactDetailActivity")
                        ) {
                    continue;
                }

                if (cname.equals(name)) {
                    break;
                } else {
                    act.finish();
                }
            }
        }
    }

    /**
     * 拿到当前激活的页面
     *
     * @return PushMsg
     */
    public static IfacePush getPush() {
        if (null == instance) {
            instance = new AppPool();
        }
        if (instance.listActivity.isEmpty()) {
            return null;
        }
        int size = instance.listActivity.size();
        Object obj = instance.listActivity.get(size - 1);
        if (null != obj) {
            return (IfacePush) obj;
        }
        return null;
    }

    /**
     * 判断页面是否存在于栈中
     *
     * @return
     */
    public static boolean judgePageExists(String pageName) {
        if (null == instance) {
            instance = new AppPool();
        }
        if (instance.listActivityName.isEmpty()) {
            return false;
        }
        for (String actName : instance.listActivityName) {
            if (pageName.equals(actName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户注销，返回导航页面（首页）
     */
    public static void logout() {
        backHome();
    }
}