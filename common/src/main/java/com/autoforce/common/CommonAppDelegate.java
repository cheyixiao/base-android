package com.autoforce.common;

import android.app.Application;
import android.content.Context;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.smtt.sdk.QbSdk;


/**
 * Created by xlh on 2019/3/19.
 * description:
 */
public class CommonAppDelegate {

    private static Application sContext;

    public static void init(Application appContext) {
        sContext = appContext;
        initX5WebView(appContext);
        initLogger(BuildConfig.DEBUG);
    }

    private static void initX5WebView(Context appContext) {
        QbSdk.setDownloadWithoutWifi(true);//非wifi条件下允许下载X5内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Logger.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(appContext, cb);
    }

    private static void initLogger(final boolean loggable) {

        PrettyFormatStrategy strategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(0)
                .methodCount(2)
                .tag("cheyixiao")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(strategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return loggable;
            }
        });

    }

    public static Context getContext() {
        return sContext;
    }
}
