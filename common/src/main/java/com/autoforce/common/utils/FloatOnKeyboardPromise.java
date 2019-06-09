package com.autoforce.common.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by xlh on 2019/4/29.<br/>
 * description:
 */
public class FloatOnKeyboardPromise {

    private static int keyboardHeight = 0;
    private final WeakReference<Activity> mActivity;
    private boolean isVisibleForLast = false;

    public FloatOnKeyboardPromise(Activity activity) {
        this.mActivity = new WeakReference<>(activity);
    }

    public void bind(ViewGroup viewGroup, View targetView) {

        if (mActivity.get() == null) {
            return;
        }

        final View rootView = mActivity.get().getWindow().getDecorView().findViewById(android.R.id.content);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {

            if (mActivity.get() == null) {
                return;
            }

            boolean mKeyboardUp = DeviceUtil.isKeyboardShown(rootView);
            if (mKeyboardUp) {
                //键盘弹出
                int loginHeight = getViewHeight(targetView);
                int distanceY = loginHeight - keyboardHeight;
                int defaultDistance = DeviceUtil.dip2px(mActivity.get(), 25);
                Logger.e("distanceY = " + distanceY + ", defaultDistance = " + defaultDistance);
                if (distanceY < defaultDistance) {
                    viewGroup.animate().translationY(distanceY - defaultDistance).start();
                }
            } else {
                //键盘收起
                viewGroup.animate().translationY(0).start();


            }
        });

        addOnSoftKeyBoardVisibleListener();

    }

    private int getViewHeight(View view) {
        if (view != null) {
            int bottom = view.getBottom();
            int screenHeight = DeviceUtil.getScreenHeight(mActivity.get());
            return screenHeight - bottom - DeviceUtil.getStatusBarHeight(mActivity.get());
        }

        return 0;
    }

    private void addOnSoftKeyBoardVisibleListener() {
        if (keyboardHeight > 0) {
            return;
        }

        if (mActivity.get() == null) {
            return;
        }

        final View decorView = mActivity.get().getWindow().getDecorView();
        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = () -> {

            if (mActivity.get() == null) {
                return;
            }

            Rect rect = new Rect();
            decorView.getWindowVisibleDisplayFrame(rect);
            //计算出可见屏幕的高度
            int displayHeight = rect.bottom - rect.top;
            //获得屏幕整体的高度
            int height = decorView.getHeight();
            boolean visible = (double) displayHeight / height < 0.8;
//            int navigatorHeight = 0;
//            try {
//                navigatorHeight = DeviceUtil.getNavigationBarHeight(mActivity.get());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            if (visible && !isVisibleForLast) {

                //获得键盘高度
                keyboardHeight = height - displayHeight;
                Logger.i("keyboardHeight==1213==" + keyboardHeight);

            }
            isVisibleForLast = visible;
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void onResume(ViewGroup viewGroup) {
        if (viewGroup != null) {
            viewGroup.animate().translationY(0).start();
        }
    }

}
