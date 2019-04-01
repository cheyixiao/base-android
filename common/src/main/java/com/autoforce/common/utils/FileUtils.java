package com.autoforce.common.utils;

import java.io.*;

/**
 * Created by AiYaChao on 2018/12/10.
 * version:1.0
 * Describe:
 */

public class FileUtils {

    /**
     * 判断指令路径下文件是否存在
     * @param parentPath
     * @param fileName
     */
    public static boolean isFileExist(File parentPath, String fileName) {
        File file = new File(parentPath, fileName);
        return file.exists();
    }

    /**
     * 删除指定路径下的文件
     * @param parentPath
     * @param fileName
     */
    public static void deleteFile(File parentPath, String fileName) {
        File file = new File(parentPath, fileName);
        if (file.exists())
            file.delete();
    }

    /**
     * 删除指定文件
     */
    public static boolean deleteFile(File file) {
        boolean isSuccess = false;
        if (file.exists())
            isSuccess = file.delete();
        return isSuccess;
    }

    /**
     * 保存字符串内容到指定文件
     * @param parentPath
     * @param fileName
     * @param fileContentStr
     */
    public static void saveFileContentStr(File parentPath, String fileName, String fileContentStr) {
        OutputStream out = null;
        try {
            File file = new File(parentPath, fileName);
            if (!file.exists())
                file.createNewFile();
            out = new FileOutputStream(file);
            out.write(fileContentStr.getBytes());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {e.printStackTrace();}
            }
        }

    }

    /**
     * 文件剪切
     * @param sourceFile
     * @param targetFile
     */
    public static boolean cutFile(File sourceFile, File targetFile) {
        boolean isCutSuccess = false;
        InputStream in = null;
        OutputStream out = null;
        try {
            if (!sourceFile.exists()) {
                throw new Exception("源文件不存在，剪切文件失败");
            }
            if (targetFile.exists()) {
                targetFile.delete();
            }
            targetFile.createNewFile();
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(targetFile);
            int size = 0;
            byte[] buff = new byte[1024];
            while ((size = in.read(buff)) > 0) {
                out.write(buff, 0, size);
            }
            out.flush();
            isCutSuccess = true;
            sourceFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return isCutSuccess;
        }
    }

    /**
     * 获取指定路径文件的内容字符串
     * @param parentPath
     * @param fileName
     * @return
     */
    public static String getFileContentStr(File parentPath, String fileName) {
        StringBuffer contentStrBuf = new StringBuffer("");
        BufferedReader fileReader = null;
        try {
            File file = new File(parentPath, fileName);
            if (!file.exists()) {file.createNewFile();}
            fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), 8 * 1024);
            String line = null;
            while ((line = fileReader.readLine()) != null)
                contentStrBuf.append(line);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {e.printStackTrace();}
            }
        }
        return contentStrBuf.toString();
    }

    /**
     * 获取文件输入流
     * @param parentPath
     * @param fileName
     * @return
     */
    public static InputStream getFileInputStream(File parentPath, String fileName) {
        InputStream inputStream = null;
        try {
            File file = new File(parentPath, fileName);
            if (!file.exists())
                return inputStream;
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 获取文件输入流
     * @param filePath
     * @return
     */
    public static InputStream getFileInputStream(String filePath) {
        InputStream inputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists())
                return inputStream;
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 递归删除多层空文件夹
     * @param fileDirectory
     */
    public static void deleteFilesDirectory(File fileDirectory) {
        File[] files = fileDirectory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteFilesDirectory(file);
            }
        }
        fileDirectory.delete();
    }
}
