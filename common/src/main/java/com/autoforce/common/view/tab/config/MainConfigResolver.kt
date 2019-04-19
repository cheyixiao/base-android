package com.autoforce.common.view.tab.config

import android.content.Context
import android.text.TextUtils
import com.autoforce.common.utils.AssetFileUtils
import com.google.gson.Gson

/**
 * Created by xlh on 2019/3/18.
 * description: 主页配置信息解析类
 */
class MainConfigResolver(private val mCallback: OnMainConfigCallback?) : MainConfigResolveInterface {

    companion object {
        private const val CONFIG_NAME = "main_config.json"
    }

    override fun loadTabsInfo(context: Context, url: String?) {

        if (TextUtils.isEmpty(url)) {
            loadFromAssets(context)
        } else {
            loadFromNet(url!!)
        }
    }

    private fun loadFromNet(url: String) {

        // 读取策略：当前显示的数据是上一次请求缓存的数据，并且每次都读取网络数据更新缓存
        // 1. 是否存在缓存：没有则loadFromAssets()
        // 2. 有缓存，则加载缓存
        // 3. 无论是第1还是第2步，都需要去请求网络json更新缓存


    }

    private fun loadFromAssets(context: Context) {
        val json = AssetFileUtils.getFile(context, CONFIG_NAME)
        processJson(json)
    }

    private fun processJson(json: String) {

        val config =
            Gson().fromJson(json, MainConfigResult::class.java)

        mCallback?.onConfigDataGot(config)
    }
}
