package com.autoforce.push;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import org.android.agoo.huawei.HuaWeiRegister;
import org.android.agoo.xiaomi.MiPushRegistar;

/**
 * Created by xlh on 2018/12/27.
 */
public class PushManager {

    //    private static final String MI_ID = "2882303761517874723";
//    private static final String MI_KEY = "5761787428723";
    private static IPushCallback mCallback;
    private static PushAgent sPushAgent;
    private static final String SP_NAME = "PushManager";
    private static final String IS_FIRST_ENTER = "is_first_enter";

    public static void initUmengPush(final Application context, String pushSecret, String miId, String miKey, IPushCallback callback) {

        UMengStatistics.init(context, null);
        UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, pushSecret);
        mCallback = callback;

        //华为push初始化
        HuaWeiRegister.register(context);

        //mi push 初始化
        MiPushRegistar.register(context, miId, miKey);


        sPushAgent = PushAgent.getInstance(context);
        //注册推送服务，每次调用register方法都会回调该接口
        sPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
//                Logger.e("deviceToken: %s ", deviceToken);

//                dealWithDeviceToken(deviceToken);
                if (mCallback != null) {
                    mCallback.onRegisterSuccess(deviceToken);
                }
            }

            @Override
            public void onFailure(String s, String s1) {

                if (mCallback != null) {
                    mCallback.onRegisterFailure(s, s1);
                }
            }
        });


//        UmengMessageHandler umengMessageHandler = new UmengMessageHandler() {
//
//            /**
//             * 处理以自定义方式发送消息时回调的方法
//             * @param context
//             * @param uMessage
//             */
//            @Override
//            public void dealWithCustomMessage(Context context, UMessage uMessage) {
//                Logger.e("dealWithCustomMessage -> " + uMessage.extra.toString());
//                super.dealWithNotificationMessage(context, uMessage);
//            }
//
//
//            /**
//             * 处理以通知的方式发送消息时的回调方法（通知送达时会回调）
//             * @param context
//             * @param uMessage
//             */
//            @Override
//            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
////                Logger.e("dealWithNotificationMessage -> " + uMessage.extra.toString());
////                handlePushMessage(context, uMessage);
//                super.dealWithNotificationMessage(context, uMessage);
//            }
//
//        };

//        mPushAgent.setMessageHandler(umengMessageHandler);

        UmengNotificationClickHandler umengNotificationClickHandler = new UmengNotificationClickHandler() {

//            @Override
//            public void handleMessage(Context context, UMessage uMessage) {
//                super.handleMessage(context, uMessage);
//
//
//                handlePushMessage(context, uMessage);
//            }

            @Override
            public void dealWithCustomAction(Context context, UMessage uMessage) {
                super.dealWithCustomAction(context, uMessage);

//                handlePushMessage(context, uMessage);
                if (mCallback != null) {
                    mCallback.onMessageReceived(uMessage.custom, uMessage.extra);
                }

            }
        };

        sPushAgent.setNotificationClickHandler(umengNotificationClickHandler);


        // 第一次进入app时启动Push
        if (isFirstEnter(context)) {
            sPushAgent.enable(new IUmengCallback() {
                @Override
                public void onSuccess() {
//                    LocalRepository.getInstance().changeToNotFirstEnter();
                    changeToNotFirstEnter(context);
                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
//        if (LocalRepository.getInstance().isFirstEnter()) {
//            sPushAgent.enable(new IUmengCallback() {
//                @Override
//                public void onSuccess() {
//                    LocalRepository.getInstance().changeToNotFirstEnter();
//                }
//
//                @Override
//                public void onFailure(String s, String s1) {
//
//                }
//            });
//
//        }


    }

    private static boolean isFirstEnter(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(IS_FIRST_ENTER, true);
    }

    private static void changeToNotFirstEnter(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_FIRST_ENTER, false).apply();
    }


//    public void handlePushMessage(Context context, UMessage uMessage) {
//
//        String token = LocalRepository.getInstance().getToken();
//
//        if (token == null || token.equals("")) {
//            LoginActivity.start(context);
//            return;
//        }
//
//        String custom = uMessage.custom;
//
//        Logger.e("PushManager =》 " + custom);
//
//        if (custom == null || custom.equals("")) {
//            MainActivity.start(context);
//            return;
//        }
//
//        Intent intent;
//        String url;
//        数字对应的跳转
//        1: '购车签约',
//                2: '定金担保',
//                3: '我发布的车源被预定',
//                4: '我发布的寻车已收到报价',
//                5: '提现',
//                6: '认证',
//                7: '驳回'
//        switch (custom) {
//            case "1":
//            case "2":
//            case "3":
//                url = CommonConstants.BASE_PAGE_URL + MineGloab.OPTION_ORDERS;
//                intent = new Intent(context, CommonX5WebViewInterceptActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("url", url);
//                context.startActivity(intent);
//                break;
////
////                intent = new Intent(context, MyCarSourceAct.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                context.startActivity(intent);
////                break;
//            case "4":
//                MainActivity.start(context, CommonConstants.CAR_FRAGMENT);
//                break;
//            case "5":
//                url = CommonConstants.BASE_PAGE_URL + MineGloab.MY_BALANUCE;
//                intent = new Intent(context, CommonX5WebViewInterceptActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("url", url);
//                context.startActivity(intent);
//                break;
//            case "7":
//                InfoCertificateAct.Companion.start(context, true);
//                break;
//            case "8":
//                if (uMessage.extra != null) {
//                    String jumpUrl = uMessage.extra.get("jumpUrl");
//                    Logger.e("jumpUrl " + jumpUrl);
//                    if (!TextUtils.isEmpty(jumpUrl)) {
//                        if (jumpUrl.contains("user_id")) {
//                            jumpUrl = jumpUrl.replace("user_id", "userid");
//                            CommonX5WebViewInterceptActivity.startFromApplication(context, CommonConstants.BASE_PAGE_URL + jumpUrl);
//                        }
//                    }
//                }
//                break;
//            case "9":
//                MainActivity.start(context, CommonConstants.FRAGMENT_CHECK_CAR_SOURCE);
//                break;
//            case "6":
//            default:
//                MainActivity.start(context, CommonConstants.HOME_FRAGMENT);
//                break;
//
//        }
//        if (custom.equals("驳回")) {
//            InfoCertificateAct.Companion.start(context, true);
//        } else if (custom.equals("购车签约") || custom.equals("定金担保")) {
//            String url = CommonConstants.BASE_PAGE_URL + MineGloab.OPTION_ORDERS;
//            Intent intent = new Intent(context, CommonX5WebViewInterceptActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("url", url);
//            context.startActivity(intent);
//        } else if (custom.equals("我发布的车源被预定")) {
//            Intent intent = new Intent(context, MyCarSourceAct.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        } else if (custom.equals("我发布的寻车已收到报价")) {
//            MainActivity.start(context, CommonConstants.CAR_FRAGMENT);
//        } else if (custom.equals("购车订单余额提现") || custom.equals("车源订单余额提现")) {
//            String url = CommonConstants.BASE_PAGE_URL + MineGloab.MY_BALANUCE;
//            Intent intent = new Intent(context, CommonX5WebViewInterceptActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("url", url);
//            context.startActivity(intent);
//        } else {
//            MainActivity.start(context, CommonConstants.HOME_FRAGMENT);
//        }
//    }


//    private void dealWithDeviceToken(String deviceToken) {
//
//        // 此时用户未登录
//        LocalRepository.getInstance().saveLocalDeviceToken(deviceToken);
//        if (TextUtils.isEmpty(deviceToken) || TextUtils.isEmpty(LocalRepository.getInstance().getSalerId())) {
//            return;
//        }
//
//        String serverDeviceToken = LocalRepository.getInstance().getDeviceToken();
//
//        // 本地有deviceToken
//        if (!TextUtils.isEmpty(serverDeviceToken)) {
//            // 当前获取的和服务器保存的不一致
//            if (!deviceToken.equals(serverDeviceToken)) {
//                // 通知服务器更新deviceToken -> 上传   更新本地保存
//                updateDeviceToken(deviceToken);
//            }
//        } else {
//            // 通知服务器更新deviceToken
//            updateDeviceToken(deviceToken);
//        }
//
//    }

//    @SuppressLint("CheckResult")
//    public static void updateDeviceToken(String deviceToken) {
//        Logger.e("deviceToken update to server");
//
//        if (TextUtils.isEmpty(deviceToken)) {
//            return;
//        }
//
//        CommonRepository.getInstance().updateDeviceToken(deviceToken)
//                .subscribeWith(new DefaultDisposableSubscriber<SimpleResult>() {
//                    @Override
//                    protected void success(SimpleResult data) {
//                        LocalRepository.getInstance().saveDeviceToken(deviceToken);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Logger.e("updateDeviceToken error -> " + t.getMessage());
//                    }
//                });
//
//    }

    /**
     * 推送关闭与打开
     *
     * @param isEnable
     */
    public static void enablePush(boolean isEnable) {
        if (sPushAgent == null) {
            return;
        }

        if (isEnable) {
            sPushAgent.enable(new IUmengCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        } else {
            sPushAgent.disable(new IUmengCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
    }

    public static void onAppStart(Activity activity) {
        PushAgent.getInstance(activity).onAppStart();
    }

    public static IPushCallback getCallback() {
        return mCallback;
    }
}
