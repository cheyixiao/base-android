package com.autoforce.common.view.tab.config

import android.content.Context
import com.autoforce.common.data.layout.ILayoutDataFetcher
import com.autoforce.common.data.layout.LayoutDataFetcherImpl
import com.google.gson.Gson

/**
 * Created by xlh on 2019/3/18.
 * description: 主页配置信息解析类
 */
class MainConfigResolver(private val mCallback: OnMainConfigCallback?) : MainConfigResolveInterface {

    private var fetcher: ILayoutDataFetcher? = null

    companion object {
        private const val CONFIG_NAME = "main_config.json"
    }


    override fun loadTabsInfo(context: Context, url: String?) {

        if (fetcher == null) {
            fetcher = object : LayoutDataFetcherImpl(context) {
                override fun filenameInAssets() = CONFIG_NAME
            }
        }

        processJson(fetcher?.getLayoutData(context, url))

    }


    private fun processJson(json: String?) {

        val config =
            Gson().fromJson(json, MainConfigResult::class.java)

        mCallback?.onConfigDataGot(config)
    }
}
