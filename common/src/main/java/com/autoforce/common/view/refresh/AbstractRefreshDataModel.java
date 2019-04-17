package com.autoforce.common.view.refresh;

import java.util.List;

/**
 * Created by xlh on 2019/4/17.
 * description:
 */
public abstract class AbstractRefreshDataModel<T> implements IRefreshDataModel {

    private final int pageStart;
    private int page;
    private DataRequestCallback<T> callback;

    public AbstractRefreshDataModel(int pageStart, DataRequestCallback<T> callback) {
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
            public void onDataGot(List<T> data, boolean isLoadMore) {

                if (callback != null) {
                    callback.onDataGot(data, isLoadMore);
                }
            }

            @Override
            public void onDataError(boolean isLoadMore) {

                if (callback != null) {
                    callback.onDataError(isLoadMore);
                }
            }
        });
    }

    public abstract void doRequest(int page, DataRequestCallback callback);
}
