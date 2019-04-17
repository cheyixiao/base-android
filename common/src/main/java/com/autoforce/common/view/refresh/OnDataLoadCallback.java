package com.autoforce.common.view.refresh;

import java.util.List;

/**
 * Created by xlh on 2019/4/17.
 * description:
 */
public interface OnDataLoadCallback<T extends StatusTypeInterface> {

    void onDataGot(List<T> data, boolean isLoadMore);

    void onDataError(boolean isLoadMore);

    void onCacheDataGot(List<T> data);
}
