package com.autoforce.common.data.file;

/**
 * Created by xlh on 2019/3/8.
 * description:
 */
public interface FileCacheInterface {


    String getCacheJson(String key);

    void saveJson(String key, String json);
}
