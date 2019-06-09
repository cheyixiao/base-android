package com.autoforce.common.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xialihao on 2018/11/22.
 */
public class FormatUtils {

    private static final DecimalFormat DECIMAL_FORMAT_WITH_DECIMALS = new DecimalFormat("#0.00");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    public static String formatWithDecimals(@Nullable Double value, boolean isUp) {

        if (value == null) {
            return "0";
        }

        if (isUp) {
            DECIMAL_FORMAT_WITH_DECIMALS.setRoundingMode(RoundingMode.UP);
        } else {
            DECIMAL_FORMAT_WITH_DECIMALS.setRoundingMode(RoundingMode.DOWN);
        }

        return subZero(DECIMAL_FORMAT_WITH_DECIMALS.format(value));
    }

    public static String subZero(String s) {
        if (s.indexOf(".") > 0) {
            //去掉多余的0
            s = s.replaceAll("0+?$", "");
            //如最后一位是.则去掉
            s = s.replaceAll("[.]$", "");
        }
        return s;
    }

    public static String encryptMobile(String phone) {

        if (phone == null) {
            return "";
        }

        if (phone.length() < 11) {
            return phone;
        }

        return encryptText(phone, 3, 7, '*');
    }

    public static String encryptMobile3(String phone) {

        if (phone == null) {
            return "";
        }

        if (phone.length() < 11) {
            return phone;
        }

        return encryptText(phone, 3, phone.length(), '*');
    }

    public static String encryptText(String text, int startIndex, int endIndex, char replace) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }

        if (startIndex < 0 || endIndex < startIndex) {
            return text;
        }

        char[] chars = text.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (i >= startIndex && i < endIndex) {
                chars[i] = replace;
            }
        }

        return new String(chars);

    }

    public static String encryptName(String name) {
        return encryptText(name, 1, name.length(), '*');
    }

    public static String formatTime(String timeStamp) {

        try {
            long value = Long.parseLong(timeStamp);
            return DATE_FORMAT.format(new Date(value));
        } catch (NumberFormatException e) {
            return timeStamp;
        }
    }
}
