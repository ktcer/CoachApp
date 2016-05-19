package com.cn.coachs.ui;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.cn.coachs.ui.chat.common.CCPAppManager;
import com.cn.coachs.ui.chat.common.utils.ECPreferenceSettings;
import com.cn.coachs.ui.chat.common.utils.ECPreferences;
import com.cn.coachs.ui.chat.common.utils.LogUtil;
import com.cn.coachs.util.AppDisk;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.InvalidClassException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * 应用程序
 *
 * @author kuangtiecheng
 */
public class AppMain extends Application {
    public BMapManager mBMapManager = null;
    String mStrKey = "BB4FEFE6EE3ADDA18993E36DC6A4F4827F30C604";//百度地图的key
    boolean m_bKeyRight = true; // 授权Key正确，验证通过

    public static boolean isAppRunning = false;
    /**
     * 旅游支付0，我的金币支付1
     */
    public static int kindsPay = 0;
    private CrashHandler crashHandler = CrashHandler.getInstance();

    /**
     * 对称算法的数据密钥
     */
    public static byte[] desKey = null;
    /** 用户是否登录 */
//	public static boolean isLogin = false; //调试模式（默认可在未登陆状态下访问功能页面）
    /**
     * 账号
     */
    public static String username = "android";
    /**
     * 密码
     */
    public static String password = "123456";
    /**
     * 设备信息，唯一识别号
     */
    public static String simSerial = "unknown";
    /**
     * 设备唯一的UUID
     */
    public static String uuid = "";
    /**
     * 自动登录标识
     */
//	public static boolean autoLogin = true;

    private static int weatherResource = 0; // 天气情况

    private static String weatherInfomation = "晴转多云\n25℃~33℃"; // 天气信息
    private Loger log = new Loger("[AppMain]");

    public static int screenWidths, screenHeight;


    /**
     * 来自AndroidManifest.xml配置文件
     */
    public static String version = "";

    /**
     * 来自AndroidManifest.xml配置文件
     */
    public static String versionName = "";

    /**
     * 来自AndroidManifest.xml配置文件
     */
    public static int verCode = 1;

    /**
     * 应用主程序
     */
    public static AppMain app;

    /**
     * 地图的IP地址
     */
    public static String mapIP = "http://118.26.142.171:8080";

    /**
     * 获取UUID值
     *
     * @return String
     */
    public static String getUuid() {
        if (null == app) {
            app = new AppMain();
        }
        return uuid;
    }

    /**
     * 获取版本号（1.3.0）
     *
     * @return String
     */
    public static String getVersion() {
        if (null == app) {
            app = new AppMain();
        }
        return version;
    }

    /**
     * 账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设备信息，唯一识别号
     **/
    public String getSimSerial() {
        if (null == simSerial) {
            simSerial = "unknown";
        }
        return simSerial;
    }

    /**
     * 获取对称算法的数据密钥
     *
     * @return byte[]
     */
    public static byte[] getDesKey() {
        if (null == app) {
            app = new AppMain();
        }
        return desKey;
    }

    /**
     * 设置对称算法的数据密钥
     *
     * @param data byte[]
     */
    public static void setDesKey(byte[] data) {
        if (null == app) {
            app = new AppMain();
        }
        desKey = data;
    }

    /**
     * 初始化应用接口
     *
     * @param act Activity
     */
    public static void initAppMain(ContextWrapper act) {
        if (null == app) {
            app = new AppMain();
        }
        app.dataInit(act);
    }

    //
//	/**
//	 * 数据初始化
//	 */
    private void dataInit(ContextWrapper context) {
        getUUID(context);
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    0);
            String versionName = info.versionName;
            version = versionName.split(" ")[0];
            TelephonyManager telManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            simSerial = telManager.getSimSerialNumber();// getSubscriberId
            Loger.print(" Application onCreate Game is Begin ....");

        } catch (NameNotFoundException e) {
            Loger.print("Application PackageInfo Read Error...", e);
        }
    }

    //
//	/**
//	 * 生成硬件的唯一码
//	 */
    private void getUUID(ContextWrapper context) {
        TelephonyManager tm = (TelephonyManager) context.getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = tm.getDeviceId();
        String androidId = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
        if (null == tmDevice) {
            Loger.print("[tmDevice]" + tmDevice);
            tmDevice = new String();
        }
        if (null == androidId) {
            Loger.print("[androidId]" + androidId);
            androidId = new String();
        }
        UUID deviceUuid = new UUID(androidId.hashCode(),
                (long) tmDevice.hashCode());

    }

    /** 是否清空内存 */
//	public boolean isCleanMemory = false;

    /**
     * 时间格式化（年-月-日 时-分-秒）
     */
    public static DateFormat timeFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 时间格式化（年-月-日）
     */
    public static DateFormat timeFormatSplit = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * 日期格式化（年月日）
     */
    public static DateFormat yearFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 定义公交模块中的刷新时间
     */
//	public static final int REFRESH_TIME = 30;


    /**
     * 单例，返回一个实例
     *
     * @return
     */
    public static AppMain getInstance() {
        if (app == null) {
            LogUtil.w("[AppMain] instance is null.");
        }
        return app;
    }


    /**
     * 保存当前的聊天界面所对应的联系人、方便来消息屏蔽通知
     */
    private void setChattingContactId() {
        try {
            ECPreferences.savePreference(ECPreferenceSettings.SETTING_CHATTING_CONTACTID, "", true);
        } catch (InvalidClassException e) {
            e.printStackTrace();
        }
    }

    private void initImageLoader() {
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), AppDisk.appName + "/imageCache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .threadPoolSize(1)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                        // .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(CCPAppManager.md5FileNameGenerator)
                        // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .diskCache(new UnlimitedDiskCache(cacheDir))

                .diskCache(new UnlimitedDiskCache(cacheDir, null, CCPAppManager.md5FileNameGenerator))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        // .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);
    }

    /**
     * 返回配置文件的日志开关
     *
     * @return
     */
    public boolean getLoggingSwitch() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
            boolean b = appInfo.metaData.getBoolean("LOGGING");
            LogUtil.w("[ECApplication - getLogging] logging is: " + b);
            return b;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean getAlphaSwitch() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(
                    getPackageName(), PackageManager.GET_META_DATA);
            boolean b = appInfo.metaData.getBoolean("ALPHA");
            LogUtil.w("[ECApplication - getAlpha] Alpha is: " + b);
            return b;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        crashHandler.init(this);
        Thread.currentThread().setUncaughtExceptionHandler(crashHandler);
//		Loger.openPrint();
//		Loger.print(" Application onCreate Game is Begin ....");
//		log.output("Application onCreate Game is Begin ....");
        CCPAppManager.setContext(app);
        setChattingContactId();
        initImageLoader();
        getUUID();
        readPackageInfo(); // 读取应用软件相关信息
        verCode = AppInfo.instance.getVersionCode();
        versionName = AppInfo.instance.getVersionName();
        version = versionName.split(" ")[0];
        simSerial = AppInfo.instance.getSimSerial();
//		initImageLoader(getApplicationContext());
        WindowManager wm = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        screenWidths = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();
        initEngineManager(this);
    }


    //	private void initImageLoader(Context context) {
//
//		//创建默认的ImageLoader配置参数 
//		ImageLoaderConfiguration configuration = ImageLoaderConfiguration .createDefault(this);
//		        
//		//Initialize ImageLoader with configuration.  
//		ImageLoader.getInstance().init(configuration);
//	}
//	
//	
    public static DisplayImageOptions initImageOptions(int drawable, boolean refresh) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawable) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(drawable)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawable)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                        //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                        //设置图片加入缓存前，对bitmap进行设置
                        //.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(refresh)//设置图片在下载前是否重置，复位
//    	.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少  
//    	.displayer(new FadeInBitmapDisplayer(refresh?100:0))//是否图片加载好后渐入的动画时间  

                .build();//构建完成

        return options;
    }

    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
//			Toast.makeText(MyApplication.getInstance().getApplicationContext(),
//					"BMapManager  初始化SUCSUC!", Toast.LENGTH_LONG).show();
        }

        if (!mBMapManager.init(mStrKey, new MyGeneralListener())) {
            Toast.makeText(app.getInstance().getApplicationContext(),
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 生成硬件的唯一码
     */
    private void getUUID() {
        TelephonyManager tm = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = tm.getDeviceId();
        String androidId = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);
        if (null == tmDevice) {
            Loger.print("[tmDevice]" + tmDevice);
            tmDevice = new String();
        }
        if (null == androidId) {
            Loger.print("[androidId]" + androidId);
            androidId = new String();
        }
        UUID deviceUuid = new UUID(androidId.hashCode(),
                (long) tmDevice.hashCode());
//		Encryption encry = new Encryption();
        uuid = deviceUuid.toString();
        Loger.print("[androidId] " + androidId + " [tmDevice] " + tmDevice
                + " [uuid] " + uuid);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Loger.print(" Application onTerminate Game is Over ....");
        LogerImp.getInstance().stopRun();
    }

    // 读取应用包相关信息
    private void readPackageInfo() {
        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            AppInfo.instance.setPackageName(info.packageName);
            AppInfo.instance.setVersionCode(info.versionCode);
            AppInfo.instance.setVersionName(info.versionName);

            TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String tel = telManager.getLine1Number(); // 手机号
            if (null != tel) {
                if (tel.startsWith("+86")) {
                    tel = tel.substring(3);
                }
                AppInfo.instance.setTel(tel);
            }

            // 国际移动用户识别码（IMSI） international mobile subscriber identity
            String subsriber = telManager.getSubscriberId();
            if (null == subsriber || subsriber.equals("")) {
                subsriber = "unknown";
            }
            AppInfo.instance.setSimSerial(subsriber);

            // 国际移动设备识别码（IMEI：International Mobile Equipment Identification
            // Number）
            String sim = telManager.getSimSerialNumber();
            if (null == sim || sim.equals("")) {
                sim = "unknown";
            }
            AppInfo.instance.setSimNum(sim);

            Loger.print(AppInfo.instance.toString());
        } catch (NameNotFoundException e) {
            Loger.print("Application PackageInfo Read Error...", e);
        }
    }

    private ActivityManager mActivityManager = null;

    /**
     * 获得系统可用内存信息
     *
     * @return String
     */
    public String getSystemAvaialbeMemorySize() {
        if (null == mActivityManager) {
            mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        }
        // 获得MemoryInfo对象
        MemoryInfo memoryInfo = new MemoryInfo();
        // 获得系统可用内存，保存在MemoryInfo对象上
        mActivityManager.getMemoryInfo(memoryInfo);

        // 字符类型转换
//		String availMemStr = Formatter
//				.formatFileSize(this, memoryInfo.availMem);
        return memoryInfo.availMem + "";
    }

    private int widthPixels; // 当前屏幕尺寸
    private int heightPixels; // 当前屏幕尺寸
//	private CommonUtil cu;

    /**
     * 设置当前屏幕的尺寸
     *
     * @param width  int 宽
     * @param height int 高
     */
    public void setPixels(int width, int height) {
        widthPixels = width;
        heightPixels = height;
    }

    /**
     * 屏幕宽
     *
     * @return int
     */
    public int getWidthPixels() {
        return widthPixels;
    }

    /**
     * 屏幕高
     *
     * @return int
     */
    public int getHeightPixels() {
        return heightPixels;
    }

    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {

        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(
                        app.getInstance().getApplicationContext(),
                        "您的网络出错啦！", Toast.LENGTH_LONG).show();
            } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(
                        app.getInstance().getApplicationContext(),
                        "输入正确的检索条件！", Toast.LENGTH_LONG).show();
            }
            // ...
        }

        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                // 授权Key错误：
                Toast.makeText(
                        app.getInstance().getApplicationContext(),
                        "请在Application.java文件输入正确的授权Key！",
                        Toast.LENGTH_LONG).show();
                app.getInstance().m_bKeyRight = false;
            }
        }

    }
}