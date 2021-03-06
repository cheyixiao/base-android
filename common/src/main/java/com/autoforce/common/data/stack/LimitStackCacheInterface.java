package com.autoforce.common.data.stack;

import java.util.List;

public interface LimitStackCacheInterface<T> {

    void cache(T t);

    void cacheAll(List<T> list, boolean isReverse);

    List<? extends T> getData();
}
