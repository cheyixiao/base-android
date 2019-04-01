package com.autoforce.common.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by xlh on 2019/1/31.
 * 流关闭帮助类
 */
public class StreamUtil {

    public static void close(Closeable c) {

        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
