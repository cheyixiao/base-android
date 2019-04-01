package com.autoforce.common.web

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.webkit.JavascriptInterface
import com.autoforce.common.utils.GsonProvider
import com.autoforce.common.utils.JSONUtil
import com.autoforce.common.utils.ToastUtil
import com.autoforce.common.web.bean.CallMessage
import org.json.JSONObject
import java.lang.ref.WeakReference

/**
 * Created by xlh on 2019/3/21.
 * description:
 */
class CommonJsBridge(activity: Activity) {

    private val mActivity = WeakReference(activity)

    companion object {
        const val METHOD = "method"
        const val PARAM = "param"
        const val METHOD_CALL = "callPhone"
    }

    /**
     * json
     * 例如：拨打电话
     * { "method": "callPhone", }
     */
    @JavascriptInterface
    fun postMessage(json: String) {

        if (handleWebMessage(json)) {
            return
        }

        if (!JSONUtil.isJSONValid(json)) {
            return
        }

        val obj = JSONObject(json)

        when (obj.getString(METHOD)) {
            METHOD_CALL -> {
                val msg = GsonProvider.gson().fromJson<CallMessage>(json, CallMessage::class.java)
                doCall(msg.param.phoneNumber)
            }
        }


    }

    @JavascriptInterface
    fun call(phoneNumber: String) {
        doCall(phoneNumber)
    }

    @JavascriptInterface
    fun toast(msg: String) {
        mActivity.get()?.runOnUiThread {
            ToastUtil.showToast(msg)
        }
    }

    open fun doCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNumber")
        intent.data = data
        mActivity.get()?.startActivity(intent)
    }

    open fun handleWebMessage(json: String) = false

}
