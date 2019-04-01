package com.autoforce.common.utils;

import android.content.Context;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by liusilong on 2018/3/29.
 * version:1.0
 * Describe:高德定位
 */

public class LocationUtil {
    private static final String TAG = "LocationUtil";
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationClientOption = null;
    private Context context;

    public LocationUtil(Context context) {
        this.context = context;
        initLocationClient();
    }

    private void initLocationClient() {
        locationClient = new AMapLocationClient(context);
        locationClientOption = new AMapLocationClientOption();
//        设置高精度模式：在这种定位模式下，将同时使用高德网络定位和GPS定位,优先返回精度高的定位
        locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        设置单次定位
        locationClientOption.setOnceLocation(true);
        locationClient.setLocationOption(locationClientOption);
    }

    /**
     * 开始定位
     */
    public void startLocation(AMapLocationListener aMapLocationListener) {
        if (aMapLocationListener != null) {
            locationClient.setLocationListener(aMapLocationListener);
            if (null == locationClient) {
                initLocationClient();
            }
            locationClient.startLocation();
        }
    }

    /**
     * 释放
     */
    public void clear() {
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
            locationClientOption = null;
        }
    }


}
