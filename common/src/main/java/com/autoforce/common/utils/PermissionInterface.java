package com.autoforce.common.utils;

/*
* 权限请求接口
* @autor  fox
* creat at 2018/11/30 14:16
*
*/
public interface PermissionInterface {

    /*
    * 获取权限请求码
    */
    public int getPermissionRequestCode();

    /*
    * 设置需要请求的权限
    */

    public String[] getPermission();

    /*
    * 权限请求成功的回调
    *
    */
    public void requestPermissionSuccess();

    /*
    * 权限请求失败的回调
    *
    */
    public void requestPermissionFaile();


}
