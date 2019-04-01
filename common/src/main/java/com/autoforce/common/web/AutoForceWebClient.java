package com.autoforce.common.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import com.orhanobut.logger.Logger;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by xlh on 2019/3/21.
 * description:
 */
public class AutoForceWebClient extends WebViewClient {

    private WebCacheStrategy mWebCacheStrategy;
    private boolean isInterceptRequest;

    public AutoForceWebClient(WebCacheStrategy strategy, boolean isInterceptRequest) {
        this.mWebCacheStrategy = strategy;
        this.isInterceptRequest = isInterceptRequest;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        return doOverrideUrlLoading(webView, s);
    }

    @Override
    public void onPageStarted(WebView webView, String url, Bitmap bitmap) {

        Logger.e("onPageStarted...");
        if (isInterceptRequest) {

            boolean isPageHtmlExist = mWebCacheStrategy.isHtmlExist(url);

            if (!isPageHtmlExist) {
                mWebCacheStrategy.downloadHtml(url);
            }
        }

        super.onPageStarted(webView, url, bitmap);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest request) {

        if (isInterceptRequest) {
            //通过url检索本地数据，并用本地数据创建生成WebResourceResponse
            WebResourceResponse response = mWebCacheStrategy.getLocalWebResourceResponse(request);
            if (response != null) {
                return response;
            }
        }

        return super.shouldInterceptRequest(webView, request);
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        sslErrorHandler.proceed();
    }

    @Override
    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);

    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
        Logger.e("onPageFinished...");
    }

    private boolean doOverrideUrlLoading(WebView webView, String s) {

        // 应对web端跳转应用市场
        if (s != null && s.startsWith("market://")) {
            Uri uri = Uri.parse(s);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            webView.getContext().startActivity(intent);
        }else {
            webView.loadUrl(s);
        }

        return true;
    }

}
