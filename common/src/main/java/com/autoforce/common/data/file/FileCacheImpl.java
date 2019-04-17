package com.autoforce.common.data.file;

import android.content.Context;
import com.autoforce.common.utils.AppMessageUtils;
import com.autoforce.common.utils.MD5Util;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by xlh on 2019/3/8.
 * description:
 */
public class FileCacheImpl implements FileCacheInterface {

    private DiskLruCache mJsonCache;

    public FileCacheImpl(Context context, String dir, String childDir) {

        File directory = new File(context.getExternalFilesDir(dir), childDir);
        try {
            mJsonCache = DiskLruCache.open(directory, Integer.parseInt(AppMessageUtils.getAppVersionCode(context)),
                    1,
                    50 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getCacheJson(String key) {

        checkNullable();

        try {
            DiskLruCache.Snapshot snapshot = mJsonCache.get(MD5Util.md5(key));
            if (snapshot != null) {
                return snapshot.getString(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private void checkNullable() {

        if (mJsonCache == null) {
            throw new RuntimeException("DiskLruCache == null");
        }

    }

    @Override
    public void saveJson(String key, String json) {

        checkNullable();

        try {
            DiskLruCache.Editor editor = mJsonCache.edit(MD5Util.md5(key));
            editor.set(0, json);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
