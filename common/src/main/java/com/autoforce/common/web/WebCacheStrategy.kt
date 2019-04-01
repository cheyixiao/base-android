package com.autoforce.common.web

import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse

/**
 * Created by xlh on 2019/3/19.
 * description:
 */
interface WebCacheStrategy {

    /**
     * 获取本地资源对应的webResource
     */
    fun getLocalWebResourceResponse(request: WebResourceRequest): WebResourceResponse?

    /**
     * 是否是资源请求，而不是webView中前端的接口请求
     */
    fun isRestfulRequest(url: String): Boolean

    /**
     * 下载html页面
     */
    fun downloadHtml(url: String)

    /**
     * 判断本地是否有对应html页面缓存
     */
    fun isHtmlExist(url: String): Boolean
}
