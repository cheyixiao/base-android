package com.autoforce.common.data.layout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import com.autoforce.common.data.file.FileCacheImpl;
import com.autoforce.common.data.file.FileCacheInterface;
import com.autoforce.common.utils.AssetFileUtils;
import com.autoforce.common.utils.HttpUtils;
import com.autoforce.common.utils.JSONUtil;
import com.autoforce.common.utils.ScheduleCompat;
import io.reactivex.Flowable;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Created by xlh on 2019/4/22.<br/>
 * description:
 */
public abstract class LayoutDataFetcherImpl implements ILayoutDataFetcher {

    private FileCacheInterface mFileCache;
    private DisposableSubscriber<String> subscriber;

    public LayoutDataFetcherImpl(Context context) {

        this.mFileCache = new FileCacheImpl(context, getCacheDir(), getCacheChildDir());
    }

    @Override
    public String getLayoutData(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            return loadFromAssets(context);
        }

        return loadFromNet(context, url);

    }

    private void dispose() {

        if (subscriber != null && !subscriber.isDisposed()) {
            subscriber.dispose();
            subscriber = null;
        }
    }

    private String loadFromAssets(Context context) {


        String filename = filenameInAssets();
        String json = AssetFileUtils.getFile(context, filename);

        if (TextUtils.isEmpty(json)) {
            throw new RuntimeException("There is no " + filename + " in assets or the content is empty.");
        }

        return json;

    }

    private String loadFromNet(Context context, String url) {

        /*
         * 1. If the cache files exist, just read it. Otherwise, read it from assets.
         * 2. In any case, do request to get server data to save .
         */
        String showJson;
        if (!isCached(url)) {
            showJson = loadFromAssets(context);
        } else {
            showJson = loadCache(url);
        }

        doRequest(url);

        return showJson;
    }

    private boolean isCached(String url) {
        return !TextUtils.isEmpty(mFileCache.getCacheJson(url));
    }

    private void doRequest(String url) {

        dispose();
        subscriber = Flowable.just(url)
                .map(s -> HttpUtils.downloadDataString(url))
                .compose(ScheduleCompat.apply())
                .subscribeWith(new DisposableSubscriber<String>() {
                    @Override
                    public void onNext(String json) {
                        if (JSONUtil.isJSONValid(json)) {
                            mFileCache.saveJson(url, json);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                        Log.e("TAG", t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @NonNull
    protected String getCacheDir() {
        return "common";
    }

    @NonNull
    protected String getCacheChildDir() {
        return "layout";
    }

    protected String loadCache(String url) {
        return mFileCache.getCacheJson(url);
    }


}
