package com.autoforce.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    private static final String TAG = "HttpUtils";

    public static boolean downloadFile(String urlStr, OutputStream fileOutPutStream) {
        return downloadFile(urlStr, fileOutPutStream, "");
    }

    public static boolean downloadFile(String urlStr, OutputStream out, String urlKey) {
        HttpURLConnection conn = null;
        InputStream in = null;

        try {
            URL fileUrl = new URL(urlStr);
            conn = (HttpURLConnection) fileUrl.openConnection();
            conn.connect();
            int connectCode = conn.getResponseCode();
            if (connectCode == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                int size = 0;
                byte[] buff = new byte[1024];
                while ((size = in.read(buff)) != -1) {
                    out.write(buff, 0, size);
                }
                //Log.e(TAG, "文件:" + urlStr + "下载成功, fileKey:" + urlKey);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Log.e(TAG, "文件:" + urlStr + "下载失败, fileKey:" + urlKey);
        return false;
    }

    public static String downloadDataString(String dataUrl) {
        HttpURLConnection conn = null;
        StringBuffer sbf = new StringBuffer("");
        BufferedReader reader = null;
        try {
            URL fileUrl = new URL(dataUrl);
            conn = (HttpURLConnection) fileUrl.openConnection();
            conn.connect();
            int connectCode = conn.getResponseCode();
            if (connectCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()), 8 * 1024);
                String dataLine = null;
                while ((dataLine = reader.readLine()) != null) {
                    sbf.append(dataLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sbf.toString();
    }
}
