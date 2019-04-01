package com.autoforce.common.utils;

import com.tencent.smtt.sdk.MimeTypeMap;

public class MimeTypeUtils {

    /**
     * 通过请求url获取对应文件的MimeType字符串
     * @param url 请求的url
     * @return url对应文件的MimeType字符串
     */
    public static String getMimeTypeFromUrl(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (!StringUtils.isEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
