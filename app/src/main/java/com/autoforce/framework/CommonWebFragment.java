package com.autoforce.framework;

import android.os.Bundle;
import com.autoforce.common.utils.MD5Util;
import com.autoforce.common.web.AbstractWebFragment;
import com.autoforce.common.web.LruWebCacheImpl;
import com.autoforce.common.web.WebCacheStrategy;
import org.jetbrains.annotations.NotNull;

/**
 * Created by xlh on 2019/3/19.
 * description:
 */
public class CommonWebFragment extends AbstractWebFragment {

    private static final String URL_KEY = "url";
    private static final String SUB_PREFIX = "ixiao/";

    public static CommonWebFragment newInstance(String url) {
        CommonWebFragment fragment = new CommonWebFragment();

        Bundle args = new Bundle();
        args.putString(URL_KEY, url);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected void webSet() {
        super.webSet();
    }

    @Override
    protected String getUrl() {
        return getArguments().getString(URL_KEY);
    }

    @Override
    protected boolean isInterceptRequest() {
        return true;
    }

    @Override
    protected WebCacheStrategy getCacheStrategy() {

        return new LruWebCacheImpl(getArguments().getString(URL_KEY), getActivity()) {
            @NotNull
            @Override
            public String getFileKey(@NotNull String url) {

                int postIndex = url.indexOf("?");
                int preIndex = url.indexOf(SUB_PREFIX);

                if (preIndex != -1) {
                    preIndex = SUB_PREFIX.length() + preIndex;
                } else {
                    preIndex = 0;
                }

                String subStr = url.substring(preIndex, postIndex);
                return MD5Util.md5(subStr);
            }
        };
    }
}
