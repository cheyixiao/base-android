package com.autoforce.common.view.refresh;

import java.util.List;

/**
 * Created by xlh on 2019/4/17.
 * description:
 */
public abstract class AbstractRefreshDataModel<T extends StatusTypeInterface> implements IRefreshDataModel {

    private final int pageStart;
    private int page;
    private OnDataLoadCallback<T> callback;

    public AbstractRefreshDataModel(int pageStart, OnDataLoadCallback<T> callback) {
        this.pageStart = pageStart;
        this.callback = callback;
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
            public void onDataGot(List<T> data) {

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

    /**
     * Do http request and transform data by callback.
     *
     * @param page
     * @param callback
     */
    public abstract void doRequest(int page, DataRequestCallback callback);
}
