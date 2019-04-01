package com.autoforce.common.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DiskLruCacheUtils {

    private static final String TAG = "DiskLruCacheUtils";

    public static ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();


    /**
     * 初始化DiskLruCache
     *
     * @return 成功初始化则返回DiskLruCache实例，否则返回null
     */
    public static DiskLruCache getDiskLruCache(Context context) {
        DiskLruCache diskLruCache = null;
        try {
            //判断SDCard是否挂载
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                //String cachePath = getExternalCacheDir().getPath();
                String cachePath = context.getExternalFilesDir("myCache").getPath();
                diskLruCache = DiskLruCache.open(new File(cachePath), 1, 1, 1024 * 1024 * 1024);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return diskLruCache;
    }

    /**
     * DiskLruCache是否已经存储该文件到本地
     *
     * @param diskLruCache DiskLruCache实例
     * @param fileKey      文件存储标识Key值
     * @return 已经保存返回true否则返回false
     */
    public static boolean isFileSaved(DiskLruCache diskLruCache, String fileKey) {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(fileKey);
            if (snapshot == null) {

                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * DiskLruCache是否已经存储该文件到本地
     *
     * @param diskLruCache DiskLruCache实例
     * @param fileKey      文件存储标识Key值
     * @return 已经保存返回true否则返回false
     */
    public static boolean isFileSaved(DiskLruCache diskLruCache, String fileKey, String fileUrl) {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(fileKey);
            if (snapshot == null) {
                if (fileKey.equals("a138383fb67c6ebfb604862209a82f2c"))
                    Log.e(TAG, "文件不存在, fileKey:" + fileKey + "===fileUrl:" + fileUrl);
                return false;
            } else {
                if (fileKey.equals("a138383fb67c6ebfb604862209a82f2c"))
                    Log.e(TAG, "文件  存在, fileKey:" + fileKey + "===fileUrl:" + fileUrl);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fileKey.equals("a138383fb67c6ebfb604862209a82f2c"))
            Log.e(TAG, "出错");
        return false;
    }

    /**
     * 获取通过DiskLruCache保存到本地文件的输入流
     *
     * @param diskLruCache DiskLruCache实例
     * @param fileKey      文件存储标识Key值
     * @return fileKey对应文件的输入流
     */
    public static InputStream getSavedFileInputStream(DiskLruCache diskLruCache, String fileKey) {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(fileKey);
            if (snapshot != null) {

                return snapshot.getInputStream(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取通过DiskLruCache保存到本地文件的输入流
     *
     * @param diskLruCache DiskLruCache实例
     * @param fileKey      文件存储标识Key值
     * @return fileKey对应文件的输入流
     */
    public static InputStream getSavedFileInputStream(DiskLruCache diskLruCache, String fileKey, String fileUrl) {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(fileKey);
            if (snapshot != null) {
                //Log.e(TAG, "获取文件输入流成功, fileKey:" + fileKey + "===fileUrl:" + fileUrl);
                return snapshot.getInputStream(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Log.e(TAG, "获取文件输入流失败, fileKey:" + fileKey + "===fileUrl:" + fileUrl);
        return null;
    }

    public static String getSavedFileContentStr(DiskLruCache diskLruCache, String fileKey) {
        StringBuffer fileContentSbf = new StringBuffer("");
        InputStream savedFileInputStream = getSavedFileInputStream(diskLruCache, fileKey);
        BufferedReader reader = null;
        try {
            if (savedFileInputStream != null) {
                reader = new BufferedReader(new InputStreamReader(savedFileInputStream), 8 * 1024);
                String line = "";
                while ((line = reader.readLine()) != null) {
                    fileContentSbf.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (savedFileInputStream != null) {
                    savedFileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContentSbf.toString();
    }

    /**
     * 下载fileUrl对应文件到本地存储
     *
     * @param fileUrl 请求文件的url
     * @param fileKey 请求文件url对应的md5值
     */
    public static void downLoadFile(DiskLruCache diskLruCache, String fileUrl, String fileKey) {

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DiskLruCache.Editor editor = diskLruCache.edit(fileKey);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        boolean isDownloadSuccess = false;
                        /*if (fileUrl.contains(".html")) {
                            Log.e(TAG, "Html下载, fileKey:" + fileKey + ", fileUrl:" + fileUrl);
                        } else {
                            Log.e(TAG, "文件下载, fileKey:" + fileKey + ", fileUrl:" + fileUrl);
                        }*/
                        isDownloadSuccess = HttpUtils.downloadFile(fileUrl, outputStream, fileKey);
                        if (isDownloadSuccess) {
                            editor.commit();
                            //Log.e(TAG, "文件存储成功, fileKey:" + fileKey + "===fileUrl:" + fileUrl);
                        } else {
                            editor.abort();
                            //Log.e(TAG, "文件存储失败, fileKey:" + fileKey + "===fileUrl:" + fileUrl);
                        }
                    }
                    diskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void saveFile(DiskLruCache diskLruCache, String fileKey, String fileContent) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(fileKey);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                byte[] fileContentBytes = fileContent.getBytes();
                outputStream.write(fileContentBytes);
                editor.commit();
            }
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cutFile(DiskLruCache diskLruCache, String fileKey, File currentFile) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(fileKey);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                InputStream inputStream = new FileInputStream(currentFile);
                int size;
                byte[] content = new byte[1024];
                while ((size = inputStream.read(content)) != -1) {
                    outputStream.write(content, 0, size);
                }
                editor.commit();
                currentFile.delete();
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            }
            diskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(DiskLruCache diskLruCache, String fileKey) {
        try {
            if (isFileSaved(diskLruCache, fileKey)) {
                diskLruCache.remove(fileKey);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
