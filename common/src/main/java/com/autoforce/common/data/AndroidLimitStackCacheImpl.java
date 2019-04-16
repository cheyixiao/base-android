package com.autoforce.common.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by xlh on 2019/4/16.
 * description:
 */
public class AndroidLimitStackCacheImpl<T> implements LimitStackCacheInterface<T> {

    private final LimitStack<T> mStack;
    private final SharedPreferences mSp;
    private final String mKey;
    private StackDataConverter<T> mDataConverter;
    private final static String SP_NAME = "limitStack";

    public AndroidLimitStackCacheImpl(Context context, String key, int size, StackDataConverter<T> converter) {
        mStack = new LimitStack<>(size);
        mKey = key;
        mSp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        this.mDataConverter = converter;
        loadData();
    }

    @Override
    public void cache(T t) {

        mStack.add(t);
        saveToFile();
    }


    @Override
    public void cacheAll(List<T> list, boolean isReverse) {

        if (list != null && !list.isEmpty()) {
            mStack.addAll(list, isReverse);
            saveToFile();
        }
    }

    @Override
    public List<? extends T> getData() {
        return mStack.getAll();
    }

    protected void loadData() {

        String cacheData = mSp.getString(mKey, "");

        if (mDataConverter != null) {
            List<T> data = mDataConverter.deserialize(cacheData);
            mStack.addAll(data, false);
        }
    }

    private void saveToFile() {

        List<T> objects = mStack.getAll();
        if (mDataConverter != null) {
            String cacheData = mDataConverter.serialize(objects);
            mSp.edit().putString(mKey, cacheData).apply();
        }
    }
}
