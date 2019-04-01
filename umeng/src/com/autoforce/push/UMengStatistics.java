package com.autoforce.push;

import android.content.Context;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.Map;

/**
 * 友盟统计封装类
 * Create by AiYaChao on 2018/11/29
 *
 * 账号：zhh@autoforce.net
 * 密码：cheyouxuan678
 */
public class UMengStatistics {

    private static final String UM_APP_KEY = "5bdbb93ab465f50cff0001d9";
    private static final String UM_SECRET_KEY = "";
    private static final String UM_PUSH_SECRET = "";

    private static Context con; //上下文，保存当前application实例

    public static void init(Context context, String channel) {
        con = context;
        init(context, channel, 3000);
    }

    public static void init(Context context, String channel, long sessionInterval) {
        con = context;
        init(context, channel, sessionInterval, false);
    }

    public static void init(Context context, String channel, long sessionInterval, boolean isOpenActivityAutoStatistic) {
        con = context;
        init(context, channel, sessionInterval, isOpenActivityAutoStatistic, true);
    }

    /**
     * 初始化方法
     *
     * @param channel                     渠道标识
     * @param sessionInterval             友盟统计Session判定最大间隔时间（单位毫秒）
     * @param isOpenActivityAutoStatistic 是否自动统计Activity页面跳转
     * @param isCatchUncaughtException    是否自动上传捕获异常
     */
    public static void init(Context context, String channel, long sessionInterval, boolean isOpenActivityAutoStatistic, boolean isCatchUncaughtException) {
        con = context;
        //友盟统计初始化
        UMConfigure.init(context, UM_APP_KEY, channel, UMConfigure.DEVICE_TYPE_PHONE, UM_PUSH_SECRET);

        //友盟统计场景类型设置
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //友盟统计SecretKey设置
        MobclickAgent.setSecret(context, UM_SECRET_KEY);

        //友盟统计Session判定最大间隔时间设置
        MobclickAgent.setSessionContinueMillis(sessionInterval);

        //友盟统计自动统计Activity页面跳转统计开关设置
        MobclickAgent.openActivityDurationTrack(isOpenActivityAutoStatistic);

        //友盟统计自动上传捕获异常功能开启设置
        MobclickAgent.setCatchUncaughtExceptions(isCatchUncaughtException);
    }

    /**
     * 调用kill或者exit之类的方法杀死进程时调用，用于保存统计数据
     */
    public static void onAppKill() {
        MobclickAgent.onKillProcess(con);
    }

    /**
     * Session启动、App使用时长等基础数据统计接口
     * 在被统计activity的onResume()生命周期方法中调用
     *
     * @param activity 被统计activity
     */
    public static void onResume(Context activity) {
        MobclickAgent.onResume(activity);
    }

    /**
     * Session启动、App使用时长等基础数据统计接口
     * 在被统计activity的onPause()生命周期方法中调用
     *
     * @param activity 被统计activity
     */
    public static void onPause(Context activity) {
        MobclickAgent.onPause(activity);
    }

    /**
     * 统计用户登录
     *
     * @param userId 用户登录账号
     */
    public static void loginIn(String userId) {
        MobclickAgent.onProfileSignIn(userId);
    }

    /**
     * 统计用户通过第三方账号登录
     *
     * @param userId    用户第三方账号
     * @param threePart 用户第三方账号平台标识（如新浪微博登录时，"WB"）
     */
    public static void loginInWithThreePart(String userId, String threePart) {
        MobclickAgent.onProfileSignIn(userId, threePart);
    }

    /**
     * 统计用户退出登出账号
     */
    public static void loginOut() {
        MobclickAgent.onProfileSignOff();
    }

    /**
     * 手动统计页面跳转
     * 页面可以是activity, fragment, view
     *
     * @param pageName 跳转页面名称标识
     */
    public static void onPageStart(String pageName) {
        MobclickAgent.onPageStart(pageName);
    }

    /**
     * 手动统计页面跳转
     * 页面可以是activity, fragment, view
     *
     * @param pageName 跳转页面名称标识
     */
    public static void onPageEnd(String pageName) {
        MobclickAgent.onPageEnd(pageName);
    }

    /**
     * 事件统计（统计事件发生次数，属计数事件统计）
     *
     * @param eventId 当前统计的事件Id
     */
    public static void statisticEventNumber(String eventId) {
        MobclickAgent.onEvent(con, eventId);
    }

    /**
     * 事件统计（统计事件发生次数，属计数事件统计）
     *
     * @param eventId 当前统计的事件Id
     * @param label   统计事件的标签属性
     */
    public static void statisticEventNumber(String eventId, String label) {
        MobclickAgent.onEvent(con, eventId, label);
    }

    /**
     * 事件统计（统计事件发生次数，属计数事件统计）
     *
     * @param eventId      当前统计的事件Id
     * @param attributeMap 当前统计事件的属性和取值（Key-Value键值对）。
     */


    public static void statisticEventNumber(String eventId, Map<String, String> attributeMap) {
        MobclickAgent.onEvent(con, eventId, attributeMap);
    }

    /**
     * 事件统计（统计事件发生相关数值型变量的值的分布，属计算事件统计）
     * 如统计事件持续时间、每次付款金额等
     *
     * @param eventId      当前统计的事件Id
     * @param attributeMap 当前统计事件的属性和取值（Key-Value键值对）。
     * @param eventValue   事件发生相关数值
     */
    public static void statisticEventCalculation(String eventId, Map<String, String> attributeMap, int eventValue) {
        MobclickAgent.onEventValue(con, eventId, attributeMap, eventValue);
    }

    /**
     * 统计SDK调试模式开启设置
     *
     * @param isEnabled 是否开启SDK调试模式
     */
    public static void setLogEnabled(boolean isEnabled) {
        UMConfigure.setLogEnabled(isEnabled);
    }

}