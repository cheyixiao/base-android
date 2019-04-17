package com.autoforce.common.view.refresh;

import java.util.List;

/**
 * Created by xlh on 2019/4/17.
 * description:  A callback for transferring data.
 */
public interface DataRequestCallback<T extends StatusTypeInterface> {

    /**
     *
     * @param data  A list for show.
     * @param json If you don't want to cache data on disk, you can pass null, otherwise please pass the valid json.
     */
    void onDataGot(List<T> data, String json);

    void onDataError();
}
