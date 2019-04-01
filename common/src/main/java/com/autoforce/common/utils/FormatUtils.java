package com.autoforce.common.utils;

import android.support.annotation.Nullable;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by xialihao on 2018/11/22.
 */
public class FormatUtils {

    private static final DecimalFormat DECIMAL_FORMAT_WITH_DECIMALS = new DecimalFormat("#0.00");

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
}
