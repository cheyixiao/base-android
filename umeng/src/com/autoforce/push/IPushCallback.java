package com.autoforce.push;

import java.util.Map;

/**
 * Created by xlh on 2019/3/18.
 * description:
 */
public interface IPushCallback {

    /**
     * 注册成功时回调此方法
     * @param deviceToken 设备的唯一标识
     */
    void onRegisterSuccess(String deviceToken);

    /**
     * 注册失败时回调此此方法
     * @param errCode 错误码
     * @param errMsg  错误信息
     */
    void onRegisterFailure(String errCode, String errMsg);

    /**
     * 接收到推送消息时回调此方法
     * @param custom  自定义行为字段
     * @param extras  自定义参数集合
     */
    void onMessageReceived(String custom, Map<String, String> extras);

    /**
     * 接收到推送消息时回调此方法 回调参数为整个json
     * @param jsonMessage
     */
    void onMessageReceived(String jsonMessage);
}
