package com.autoforce.common.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import com.autoforce.common.CommonAppDelegate;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static String getString(@StringRes int stringId) {
        return CommonAppDelegate.getContext().getString(stringId);
    }

    public static boolean isDecimal(String str) {

        if (TextUtils.isEmpty(str)) {
            return false;
        }

        boolean flag = true;
        for (int i = 0; i < str.length(); i++) {
            if (!(str.charAt(i) == '.') && (str.charAt(i) > '9' || str.charAt(i) < '0')) {
                flag = false;
                break;
            }
        }

        return flag;
    }

    public static String formatVersionName() {

        String versionName = AppMessageUtils.getAppVersionName(CommonAppDelegate.getContext());

        String[] split = versionName.split("\\.");

        StringBuilder sb = new StringBuilder();
        for (String str : split) {
            if (str.length() < 2) {
                sb.append("0");
            }
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 拆分链接的参数
     *
     * @param url 链接
     * @return queryMap
     */
    @Nullable
    public static Map<String, String> getQueryMap(String url) {

        if (TextUtils.isEmpty(url)) {
            return null;
        }

        int index = url.indexOf("?");
        if (index != -1) {
            String subStr = url.substring(index + 1);
            return splitToMap(subStr);
        }

        return null;
    }

    @NonNull
    public static Map<String, String> splitToMap(String subStr) {
        String[] params = subStr.split("&");

        Map<String, String> map = new HashMap<>();
        for (String param : params) {
            String[] values = param.split("=");
            // http://xxx?a=b&c=d，将参数以 “=” 拆分为数组
            if (values.length > 1) {
                if (null != values[0] && null != values[1]) {
                    map.put(values[0], values[1]);
                }
            }
        }
        return map;
    }

    public static String[] getStringArray(int arrayId){

        return CommonAppDelegate.getContext().getResources().getStringArray(arrayId);
    }

//    public static String formatPrice(String price) {
//¬
//        if (TextUtils.isEmpty(price)) {
//            return price;
//        }
//
//        if (price.length() > 2) {
//            return price.substring(0, 2) + "...";
//        } else {
//            return price;
//        }
//    }
}
