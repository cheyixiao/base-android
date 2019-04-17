package com.autoforce.common.view.refresh;

import java.util.List;

/**
 * Created by xlh on 2019/4/17.
 * description:
 */
public interface DataRequestCallback<T extends StatusTypeInterface> {

    void onDataGot(List<T> data);

    void onDataError();
}
