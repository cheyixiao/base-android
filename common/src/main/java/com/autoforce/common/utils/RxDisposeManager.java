package com.autoforce.common.utils;

import android.support.v4.util.ArrayMap;
import io.reactivex.disposables.Disposable;

import java.util.Set;

/**
 * Created by xialihao on 2018/12/3.
 */
public class RxDisposeManager {
    private volatile static RxDisposeManager sInstance = null;

    private ArrayMap<Object, Disposable> maps;

    public static RxDisposeManager get() {

        if (sInstance == null) {
            synchronized (RxDisposeManager.class) {
                if (sInstance == null) {
                    sInstance = new RxDisposeManager();
                }
            }
        }
        return sInstance;
    }

    private RxDisposeManager() {
        maps = new ArrayMap<>();
    }


    public void add(Object tag, Disposable disposable) {
        cancel(tag);
        maps.put(tag, disposable);
    }


    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            maps.remove(tag);
        }
    }

    public void removeAll() {
        if (!maps.isEmpty()) {
            maps.clear();
        }
    }


    public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isDisposed()) {
            maps.get(tag).dispose();
            maps.remove(tag);
        }
    }

    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}
