-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#-libraryjars libs/gson-2.2.3.jar
#-libraryjars libs/hellocharts-library-1.5.5.jar
#-libraryjars libs/nineoldandroids-library-2.4.0.jar
#-libraryjars libs/umeng-analytics-v5.4.2.jar
#-libraryjars libs/universal-image-loader-1.9.4-with-sources.jar
#-libraryjars libs/X-HealthLib.jar
#-libraryjars libs/armeabi-v7a/libqnUCSipSdk.so
#-libraryjars libs/alipaySDK-20150724.jar


-dontwarn android.support.v4.**
-dontwarn lecho.lib.hellocharts.** 
-dontwarn com.nineoldandroids.** 
-dontwarn com.umeng.**
-dontwarn com.nostra13.universalimageloader.**
-dontwarn com.ict.core.**
-dontwarn com.google.zxing.**

-keep class android.support.v4.** {*; }
-keep class lecho.lib.hellocharts.** {*; }
-keep class com.nineoldandroids.** {*; }
-keep class com.umeng.** {*; }
-keep class com.nostra13.universalimageloader.** {*; }
-keep class com.ict.core.** {*; }
-keep class com.google.zxing.** {*; }
-dontshrink
-dontoptimize
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keepattributes *Annotation*
 -keepattributes *JavascriptInterface*
-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}

-keep public class [your_pkg].R$*{
    public static final int *;
}


-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

-keep class com.tencent.mm.sdk.** {

   *;

}

# OrmLite uses reflection
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepattributes *Annotation*
-keep class com.cn.aihuexpert.database.** { *; }


-keep class com.cn.coachs.util.customgallery.model.*{*;}

-keep class com.yuntongxun.ecsdk.** {*; }

-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class * extends com.cn.coachs.ui.patient.main.healthdiary.datepicker.Labeler{
    public *;
}
-keep public class * extends android.widget.LinearLayout{
    public *;
}



-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep public class com.cn.coachs.R$* {
     public static final int *;
}

-keep public class com.feedback.ui.ThreadView {
}

-keep public class com.umeng.fb.ui.ThreadView {
} 
# Weibo API (sina&tencent)
-dontwarn com.tencent.weibo.**
-dontwarn com.weibo.net.**
-dontwarn android.webkit.**
-dontwarn android.net.http.**
-keep class com.tencent.weibo.**{*;}
-keep class com.weibo.net.**{*;}
-keep class android.webkit.**{*;}
-keep class android.net.http.**{*;}



-keep class com.google.**{*;}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#-libraryjars libs/baidumapapi_v2_1_2.jar 替换成自己所用版本的jar包

#-keep class com.baidu.** { *; }

#-keep class vi.com.gdi.bgl.android.**{*;}


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature  
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }  
# Application classes that will be serialized/deserialized over Gson
-keep class com.cn.coachs.model.** { *; }

##---------------End: proguard configuration for Gson  ----------

-ignorewarning