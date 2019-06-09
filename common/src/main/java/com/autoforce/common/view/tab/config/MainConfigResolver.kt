package com.autoforce.common.view.tab.config

import android.content.Context
import com.autoforce.common.data.layout.ILayoutDataFetcher
import com.autoforce.common.data.layout.LayoutDataFetcherImpl
import com.google.gson.Gson

/**
 * Created by xlh on 2019/3/18.
 * description: 主页配置信息解析类
 */
class MainConfigResolver(private val mCallback: OnMainConfigCallback?, private val configName: String) : MainConfigResolveInterface {

    private var fetcher: ILayoutDataFetcher? = null

    override fun loadTabsInfo(context: Context, url: String?) {

        if (fetcher == null) {
            fetcher = object : LayoutDataFetcherImpl(context) {
                override fun filenameInAssets() = configName
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
