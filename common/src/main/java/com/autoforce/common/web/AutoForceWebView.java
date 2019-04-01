package com.autoforce.common.web;

import android.content.Context;
import android.util.AttributeSet;
import com.autoforce.common.utils.AppMessageUtils;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.sdk.WebSettings;

/**
 * Created by xialihao on 2018/11/15.
 * 自定义WebView ,便于后期替换
 */
public class AutoForceWebView extends com.tencent.smtt.sdk.WebView {


    public AutoForceWebView(Context context) {
        super(context);
        init(context);
    }

    public AutoForceWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        WebSettings webSetting = getSettings();

        // chrome调试打开，debug包打开，release包关闭
//        if (BuildConfig.DEBUG) {
//            WebView.setWebContentsDebuggingEnabled(true);
//        }

        webSetting.setJavaScriptEnabled(true);//js交互允许

        //自适应屏幕
        webSetting.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSetting.setLoadWithOverviewMode(true);//缩放至屏幕的大小

        //缩放
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);

        //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点。
        requestFocusFromTouch();

        //访问文件
        webSetting.setAllowFileAccess(true);

        //多窗口
        webSetting.setSupportMultipleWindows(true);
        //支持js打开新窗口
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);

        webSetting.setLoadsImagesAutomatically(true);  //支持自动加载图片
        //支持内容重新布局
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        //h5缓存
        webSetting.setAppCacheEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        //Log.e("CachePath", getExternalFilesDir("myCache").getPath());
        //webSetting.setAppCachePath(getExternalFilesDir("myCache").getPath());
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);

        webSetting.setDomStorageEnabled(true);//当网页需要保存数时据
        webSetting.setGeolocationEnabled(true);//启用还H5的地理定位服务
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);

        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setLoadWithOverviewMode(true);

        String userAgentString = webSetting.getUserAgentString();
        String newUserAgent = userAgentString + " " +
                "cheyixiao/" +
                AppMessageUtils.getAppVersionName(context);
        Logger.e("userAgent: " + newUserAgent);
        webSetting.setUserAgentString(newUserAgent);

    }
}
