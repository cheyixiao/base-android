package com.autoforce.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;

/**
 * Created by liusilong on 2018/7/2.
 * version:1.0
 * Describe:
 */

public class DeviceUtil {
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            @SuppressLint({"MissingPermission", "HardwareIds"})
            String deviceId = telephonyManager.getDeviceId();
            if (deviceId != null) {
                return deviceId;
            }
        }
        return null;
    }



    /**
     * 得到屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
//        context.getWindowManager().getDefaultDisplay().getMetrics(metric);

        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {

        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取顶部statusBar高度
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }


    /**
     * 获取虚拟功能键高度
     *
     * @param context
     * @return
     */
    private static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    public static int getNavigationBarHeight(Context context) {
        if (checkDeviceHasNavigationBar(context)) {
            return getVirtualBarHeight(context);
        } else {
            return 0;
        }
    }

    /**
     * 获取是否存在NavigationBar
     *
     * @param context
     * @return
     */
    private static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        Logger.e("11111111111111 -> id = " + id);
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        Logger.e("222222   -> hasNavigationBar " + hasNavigationBar);

        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            Logger.e("navBarOverride " + navBarOverride);
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
                Logger.e("333333   -> hasNavigationBar " + hasNavigationBar);
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
                Logger.e("444444   -> hasNavigationBar " + hasNavigationBar);
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
        return hasNavigationBar;
    }

    public static boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

}
