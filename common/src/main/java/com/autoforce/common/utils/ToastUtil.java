package com.autoforce.common.utils;

import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;
import com.autoforce.common.CommonAppDelegate;

public class ToastUtil {


    private static Toast toast;

    public static void showToast(String msg) {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            if (toast == null) {
                toast = Toast.makeText(CommonAppDelegate.getContext(), msg, Toast.LENGTH_SHORT);
            } else {
                toast.setText(msg);
            }

            toast.show();
        }

    }

    public static void showToast(@StringRes int resId) {
        showToast(CommonAppDelegate.getContext().getString(resId));
    }

    public static void reset() {
        toast = null;
    }
}
