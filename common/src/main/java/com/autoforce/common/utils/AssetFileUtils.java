package com.autoforce.common.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Create by xlh on 2018/10/29.
 */
public class AssetFileUtils {

    /**
     * 判断assets目录下是否存在某个文件
     * @param context
     * @param fileName
     * @return true/false
     */
    public static boolean isFileInAssets(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            assetManager.open(fileName);
            return true;
        } catch (IOException e) {
            Log.e("AssetFileUtils", "assets中不存在: " + fileName);
            return false;
        }
    }

    /**
     * 获取assets目录下的json文件
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toString();
    }

}
