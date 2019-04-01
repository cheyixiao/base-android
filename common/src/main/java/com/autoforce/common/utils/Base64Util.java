package com.autoforce.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Base64Util {

    /*base64编码文件*/
    public static String encodeBase64File(String filePath){
        try {
            File file = new File(filePath);
            FileInputStream inputFile = null;
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            return Base64.encodeToString(buffer, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*base64解码文件*/
    public static File descodBase64File(String str){
        try {
            File file = new File(Environment.getExternalStorageDirectory(), str.substring(0, 5));
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] decode = Base64.decode(str, Base64.DEFAULT);
            fileOutputStream.write(decode);
            fileOutputStream.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /*base64编码bitmap*/
    public static String encodeBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes , Base64.DEFAULT);
    }

    /*base64解码bitmap*/
    public static Bitmap descideBitmap(String bitmapStr){
        byte[] decode = Base64.decode(bitmapStr, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }

}
