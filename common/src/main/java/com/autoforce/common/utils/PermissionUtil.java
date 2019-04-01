package com.autoforce.common.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/*
* 权限请求工具类
* @autor  fox
* creat at 2018/11/30 14:27
*
*/
public class PermissionUtil {

    /*
    * 判断是否拥有某个权限
    * @partm permission 需要检查的权限
    */

    public static boolean hasPermission(Activity activity , String permission){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    /*
    * 请求权限
    * @partm permission 需要的权限数组
    * @partm request 权限请求码
    */

    public static void requestPermission(Activity activity ,String[] permissions , int requestCode){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            activity.requestPermissions(permissions , requestCode);
        }
    }

    /*
    * 获取没有允许的权限
    * return  返回null 表示所有权限通过
    * @partm
    */
    public static String[] getDenidPermissions(Activity activity , String[] permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            List<String> denidPermissionList = new ArrayList<>();
            for(String permission :permissions){
                if(activity.checkSelfPermission(permission)!=PackageManager.PERMISSION_GRANTED){
                    denidPermissionList.add(permission);
                }
            }
            if(denidPermissionList.size()>0){
                return denidPermissionList.toArray(new String[denidPermissionList.size()]);
            }
        }
        return null;
    }



}
