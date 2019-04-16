package com.autoforce.common.data;

import java.util.List;

/**
 * Created by xlh on 2019/4/16.
 * description:
 */
public interface StackDataConverter<T> {

    List<T> deserialize(String cacheData);

    String serialize(List<T> data);
}
