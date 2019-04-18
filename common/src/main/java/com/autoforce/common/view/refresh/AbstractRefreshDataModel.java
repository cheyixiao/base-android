package com.autoforce.common.view.refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.autoforce.common.data.file.FileCacheImpl;
import com.autoforce.common.data.file.FileCacheInterface;

import java.util.List;

/**
 * Created by xlh on 2019/4/17.
 * description:
 * A data model for refresh. When you want to cache data on disk, please override such methods:<br/>
 * - getKey(): return data identifier <br>
 * - processCacheJson(): to process cache json<br>
 *
 * Also, in the subclass of AutoForceRecyclerView, you should override isCache() to make it return true.
 */
public abstract class AbstractRefreshDataModel<T extends StatusTypeInterface> implements IRefreshDataModel {

    private final int pageStart;
    private int page;
    private OnDataLoadCallback<T> callback;
    private FileCacheInterface mFileCache;

    public AbstractRefreshDataModel(Context context, int pageStart, boolean isCache, OnDataLoadCallback<T> callback) {
        this.pageStart = pageStart;
        this.callback = callback;

        if (isCache) {
            this.mFileCache = getFileCache(context);
        }

    }

    @Override
    public void loadData(boolean isLoadMore) {

        if (isLoadMore) {
            page++;
        } else {
            page = pageStart;
        }

        doRequest(page, new DataRequestCallback<T>() {

            @Override
            public void onDataGot(List<T> data, String json) {

                if (!TextUtils.isEmpty(json)) {

                    checkKey();

                    if (mFileCache != null) {
                        mFileCache.saveJson(getKey(), json);
                    }
                }


                if (callback != null) {
                    callback.onDataGot(data, isLoadMore);
                }
            }

            @Override
            public void onDataError() {

                if (callback != null) {
                    callback.onDataError(isLoadMore);
                }
            }
        });
    }

    private void checkKey() {

        String key = getKey();
        if (TextUtils.isEmpty(key) && mFileCache != null) {
            throw new RuntimeException("You should override the getKey() method to make it not return null " +
                    "when using file cache!");
        }

    }

    @Override
    public void loadCacheData() {

        checkKey();

        if (mFileCache != null && callback != null) {
            String cacheJson = mFileCache.getCacheJson(getKey());

            if (TextUtils.isEmpty(cacheJson)) {
                callback.onCacheDataGot(null);
                return;
            }

            // clear cached json when an error occurs.
            try {
                List<T> cacheData = processCacheJson(cacheJson);
                if (cacheData != null) {
                    callback.onCacheDataGot(cacheData);
                }
            } catch (Exception e) {
                mFileCache.saveJson(getKey(), "");
                callback.onCacheDataGot(null);
            }

        }
    }

    /**
     * @param context
     * @return
     */
    @NonNull
    protected FileCacheImpl getFileCache(Context context) {
        return new FileCacheImpl(context, "common", "json");
    }

    protected String getKey() {
        return null;
    }

    /**
     * Do http request and transform data by callback.
     *
     * @param page     request page number
     * @param callback for data transfer
     */
    public abstract void doRequest(int page, DataRequestCallback callback);

    protected List<T> processCacheJson(String cacheJson) {
        return null;
    }


}
