package com.autoforce.push;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.umeng.message.UmengNotifyClickActivity;
import com.umeng.message.lib.R;
import org.android.agoo.common.AgooConstants;

/**
 * Created by xlh on 2019/3/18.
 * description:
 */
public class MipushTestActivity extends UmengNotifyClickActivity {

    private static String TAG = MipushTestActivity.class.getName();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_mipush);
    }

    @Override
    public void onMessage(Intent intent) {
        super.onMessage(intent);  //此方法必须调用，否则无法统计打开数
        String body = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        Log.i(TAG, "onMessage -> " + body);

        IPushCallback callback = PushManager.getCallback();
        if (callback != null) {
            callback.onMessageReceived(body);
        }
//        UMessage uMessage = GsonProvider.gson().fromJson(body, UMessage.class);
//        new PushCallbackImpl(this).handlePushMessage(App.getInstance(), uMessage.custom, uMessage.extra);

        finish();
    }
}
