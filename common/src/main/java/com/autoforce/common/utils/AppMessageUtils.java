package com.autoforce.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * create by AiYaChao on 2018/11/29
 * App自身信息获取工具类
 */
public class AppMessageUtils {

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        String appName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            int labelRes = pi.applicationInfo.labelRes;
            appName = context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appName;
    }

    /**
     * 返回当前程序版本号
     */
    public static synchronized String getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {

        }
        return versionCode + "";
    }

    /**
     * 返回当前程序版本名
     */
    public static synchronized String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 返回当前程序包名
     */
    public static synchronized String getAppPackageName(Context context) {
        String packageName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            packageName =  pi.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageName;
    }

    /**
     * 获取图标bitmap
     */
    public static synchronized Bitmap getAppLauncherIcon(Context context) {
        PackageManager pm = null;
        ApplicationInfo ai = null;
        try {
            pm = context.getApplicationContext().getPackageManager();
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            ai = null;
        }
        Drawable d = pm.getApplicationIcon(ai);
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }

    /**
     * 获取Manifest中注册标识信息，例如获取渠道号
     */
    public static String getAppMetaData(Context context, String key) {
        if (context == null || StringUtils.isEmpty(key)) {
            return null;
        }
        String channel = null;
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (ai != null) {
                    if (ai.metaData != null) {
                        channel = ai.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

}
