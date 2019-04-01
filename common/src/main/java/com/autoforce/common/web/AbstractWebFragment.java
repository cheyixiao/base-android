package com.autoforce.common.web;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ProgressBar;
import com.autoforce.common.R;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by xlh on 2019/3/19.
 * description:
 * 1. 提供onResume()、onPause()供web端使用
 * 2. 提供了自定义拦截WebView资源的缓存策略
 * 3. 支持web端通过scheme链接跳转App应用市场
 * 4. 为web端提供一些常用需原生支持的功能：例如拨打电话等
 */
public abstract class AbstractWebFragment extends Fragment {

    protected AutoForceWebView mWebView;
    protected ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_webview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bindViews();

        if (getActivity() != null) {
            getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);//防止网页中的视频，上屏幕的时候，可能出现闪烁的情况
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//避免输入法界面弹出后遮挡输入光标的问题
        }

        webSet();

    }

    private void bindViews() {
        mWebView = getView().findViewById(R.id.web_view);
        mProgressBar = getView().findViewById(R.id.progress_bar);
    }

    protected void webSet() {

        if (getContext() == null) {
            return;
        }

        if (isInterceptRequest())
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(getWebViewClient());

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                if (mProgressBar != null) {
                    if (newProgress != 100) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.setProgress(newProgress);
                    } else {
                        mProgressBar.setVisibility(View.GONE);
                    }
                }
            }
        });

        mWebView.addJavascriptInterface(getBridgeObject(), getBridgeName());

        mWebView.loadUrl(getUrl());

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.loadUrl("javascript:onResume()");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mWebView != null) {
            mWebView.loadUrl("javascript:onPause()");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.clearHistory();
            mWebView.getSettings().setJavaScriptEnabled(false);
            ViewParent parent = mWebView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.destroy();
            mWebView = null;
        }

    }

    /*是否拦截   默认不拦截*/
    protected boolean isInterceptRequest() {
        return false;
    }

    protected WebCacheStrategy getCacheStrategy() {
        return new LruWebCacheImpl(getUrl(), getActivity());
    }

    protected WebViewClient getWebViewClient() {
        return new AutoForceWebClient(getCacheStrategy(), isInterceptRequest());
    }

    protected String getBridgeName() {
        return "CYX_JSBridge";
    }

    protected Object getBridgeObject() {
        return new CommonJsBridge(getActivity());
    }


    /*h5网页链接*/
    protected abstract String getUrl();
}
