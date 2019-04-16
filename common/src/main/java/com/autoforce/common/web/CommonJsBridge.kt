package com.autoforce.common.web

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentActivity
import android.webkit.JavascriptInterface
import com.autoforce.common.utils.GsonProvider
import com.autoforce.common.utils.JSONUtil
import com.autoforce.common.utils.ToastUtil
import com.autoforce.common.web.bean.CallMessage
import com.orhanobut.logger.Logger
import org.json.JSONObject
import java.lang.ref.WeakReference

/**
 * Created by xlh on 2019/3/21.
 * description:
 */
open class CommonJsBridge(activity: FragmentActivity) {

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

        Logger.e("CommonJsBridge...")
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
                call(msg.param.phoneNumber)
            }
        }

    }

    @JavascriptInterface
    fun call(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNumber")
        intent.data = data
        mActivity.get()?.startActivity(intent)
    }

    @JavascriptInterface
    fun toast(msg: String) {
        mActivity.get()?.runOnUiThread {
            ToastUtil.showToast(msg)
        }
    }

    protected open fun handleWebMessage(json: String) = false

    fun getActivity(): Activity? {
        return mActivity.get()
    }

}
