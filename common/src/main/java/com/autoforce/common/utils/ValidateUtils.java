package com.autoforce.common.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by xialihao on 2018/12/4.
 */
public class ValidateUtils {
    /**
     * 手机号码:
     * 13[0-9], 14[5,7], 15[0, 1, 2, 3, 5, 6, 7, 8, 9], 17[0, 1, 6, 7, 8], 18[0-9]
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,152,155,156,170,171,176,185,186
     * 电信号段: 133,134,153,170,177,180,181,189
     */
    public static boolean isMobileNO(String mobiles) {

        Pattern pattern = Pattern.compile("^1[3|4|5|7|8]\\d{9}$");
        return !TextUtils.isEmpty(mobiles) && pattern.matcher(mobiles).matches() && mobiles.length() == 11;
    }

    public static boolean isMobileFirstLetterValid(String mobile) {
        return !TextUtils.isEmpty(mobile) && mobile.startsWith("1") && mobile.length() == 11;
    }

    public static boolean isMobileLengthValid(String mobile) {
        return !TextUtils.isEmpty(mobile) && mobile.length() == 11;
    }

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        return !TextUtils.isEmpty(email) && pattern.matcher(email).matches();
    }

    public static boolean isPhone(String phoneNo) {
        Pattern pattern = Pattern.compile("^((\\d{3,4}-)?\\d{7,8})$|^(0[0-9][0-9]\\d{8})$");
        return !TextUtils.isEmpty(phoneNo) && pattern.matcher(phoneNo).matches();

    }

    public static boolean isBankCard(String bankCard) {

        Pattern pattern = Pattern.compile(" ^(\\d{16}|\\d{19})$");
        return !TextUtils.isEmpty(bankCard) && pattern.matcher(bankCard).matches();
    }

    public static boolean isPostNo(String postNo) {
        Pattern pattern = Pattern.compile("[0-9]\\d{5}(?!\\d)");
        return !TextUtils.isEmpty(postNo) && pattern.matcher(postNo).matches();

    }

    public static boolean isPassword(String password) {

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{6,16}$");
        return !TextUtils.isEmpty(password) && pattern.matcher(password).matches();

    }
}

