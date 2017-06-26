-optimizationpasses 5
-dontusemixedcaseclassnames
-dontpreverify
-ignorewarnings
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

#指定不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 
#两个命令配合删除类的路径
-allowaccessmodification
-repackageclasses ''

#确定统一的混淆类的成员名称来增加混淆
-useuniqueclassmembernames

#weixin
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

#zuche
#-libraryjars libs/commons-codec-1.7.jar
-dontwarn com.szzc.ucar.models.entry.** 
-dontwarn org.jboss.**
-keep class org.jboss.** { *; }
-keep class com.szzc.ucar.models.entry.** { *; }


#Samsung
#-libraryjars libs/multiwindow-v1.2.3.jar
#-libraryjars libs/sdk-v1.0.0.jar
-dontwarn com.samsung.android.**
-keep class com.samsung.android.** { *; }

#amap
-keep class com.amap.api.**  {*;}      
-keep class com.autonavi.**  {*;}
-keep class com.a.a.**  {*;}
-dontwarn com.amap.api.maps.**

#baidu
#-libraryjars libs/baidumapapi_v3_0_0.jar
#-libraryjars libs/bd_locSDK_4.1.jar

#-keep class com.baidu.** {*;}
-keep class com.sinovoice.** {*;}
#-keep class com.baidu.nplatform.* {*;}
#-keep class com.baidu.nplatform.** {*;}

#-keep class com.baidu.mapapi.* {*; }
#-keep class com.baidu.mapapi.** {*; }
#-keep class com.baidu.mapapi.search {*; }
#-keep class com.baidu.mapapi.search.* {*; }

#-keep class com.baidu.platform.** {*; }

#-keep class com.baidu.location {*; }
#-keep class com.baidu.location.* {*; }
#-keep class com.baidu.location.** {*; }

#-keep class com.baidu.vi.** {*; }
-keep class vi.com.gdi.bgl.android.** {*;}

#-dontwarn com.github.stuxuhai.jpinyin.**
#-dontwarn demo.**
#-libraryjars libs/jpinyin-1.0.jar
#-keep class com.github.stuxuhai.jpinyin.** { *;}
#-keep class demo.** { *;}

#jpush
#-libraryjars libs/jpush-sdk-release1.6.1.jar
#-dontwarn cn.jpush.**
#-keep class cn.jpush.** { *; }

#umeng
#-libraryjars libs/umeng-analytics-v5.2.4.jar
-keep class com.umeng.**{*;}
-dontwarn com.umeng.**

 -keep public class com.szzc.R$*{
     public static final int *;
 }
 -keep public class com.feedback.ui.ThreadView { }

#iflytek
-dontwarn com.iflytek.**
-keepattributes Signature
-keep class com.iflytek.**{*;} 

#maa
-keep class com.mato.** { *; } 
-dontwarn com.mato.** 

#yuntongxun
#gson
-keep class com.google.gson.stream.** { *; }
-dontwarn org.dom4j.**

#zxing
-dontwarn com.google.zxing.** 
-keep class com.google.zxing.** {*; }

#easemob
-keep class com.easemob.** {*;}
-keep class org.jivesoftware.** {*;}
-keep class org.apache.** {*;}
-keep class com.szzc.ucar.view.easemob.chatinputmenu.EaseSmileUtils {*;}
-dontwarn  com.easemob.**

#apache
-keep class org.apache.**{*;}
-dontwarn org.apache.commons.**

-dontwarn android.support.**
-dontwarn android.**
-dontwarn sina.**
-dontwarn weibo.**
-dontwarn oauth.**
-dontwarn com.google.**
-dontwarn com.tencent.**

-keep class android.support.**{*;}
-keep class android.** { *; }

-keep class sina.** { *; }
-keep class weibo.** { *; }
-keep class oauth.** { *; }

-keep class com.google.** { *; }
-keep class com.tencent.**{*;}

-dontwarn android.support.v4.**
-dontwarn android.support.v13.**
-dontwarn android.webkit.WebViewClient
-dontwarn com.tencent.mm.sdk.**


#-dontwarn com.baidu.navisdk.**
#-dontwarn com.baidu.location.**
-dontwarn com.sinovoice.hcicloudsdk.**
-dontwarn org.bouncycastle.jce.**
-dontwarn org.bouncycastle.util.**
-dontwarn org.bouncycastle.x509.**
-dontwarn com.tencent.weibo.sdk.**

-keep public class com.sinovoice.hcicloudsdk.**
-keep public class org.bouncycastle.jce.**
-keep public class org.bouncycastle.util.**
-keep public class org.bouncycastle.x509.**
-keep public class com.tencent.weibo.sdk.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.v4.** { *; }
-keep class android.support.v13.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v13.**
-keep public class * extends android.app.Fragment
-keep public class * extends android.os.IInterface


-keep class com.tencent.mm.sdk.**
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class com.szzc.ucar.utils.ViewInject
-keepclassmembers class com.szzc.ucar.** {
	@com.szzc.ucar.utils.ViewInject *;
}
-keep class com.szzc.ucar.view.WebView {
	public void goback();
}

-keep public class com.szzc.ucar.driver.R$*{
public static final int *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

