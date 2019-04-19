package com.autoforce.common.web

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.autoforce.common.utils.DiskLruCacheUtils
import com.autoforce.common.utils.MD5Util
import com.autoforce.common.utils.MimeTypeUtils
import com.jakewharton.disklrucache.DiskLruCache
import com.orhanobut.logger.Logger
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import java.io.InputStream
import java.util.*

/**
 * Created by xlh on 2019/3/19.
 * description:
 */
open class LruWebCacheImpl(private val loadUrl: String, context: Context) : WebCacheStrategy {

    private val diskLruCache: DiskLruCache? = DiskLruCacheUtils.getDiskLruCache(context)

    override fun getLocalWebResourceResponse(request: WebResourceRequest): WebResourceResponse? {

        var response: WebResourceResponse? = null

        try {
            //请求的url
            val fileUrl = request.url.toString()

            if (isRestfulRequest(fileUrl)) {
                return null
            }

            //请求url的md5值，用作该文件在DiskLruCache操作的Key值
            var fileKey = getFileKey(fileUrl)

            Log.e("interceptUrl -> ", fileUrl)
            Log.e("interceptUrl -> ", fileKey)

            //如果DiskLruCache初始化成功，并且本地离线缓存包数据中不存在该文件则进行本地在线数据缓存处理
            if (response == null && diskLruCache != null) {
                if (!DiskLruCacheUtils.isFileSaved(diskLruCache, fileKey, fileUrl) && fileUrl != loadUrl) {
                    Logger.t("aaa").e("下载在线拦截数据:$fileUrl   md5:$fileKey")
                    DiskLruCacheUtils.downLoadFile(diskLruCache, fileUrl, fileKey)
                } else {
                    Logger.t("bbb").e("加载本地myCache数据:$fileUrl   md5:$fileKey")
                    if (fileUrl == loadUrl) {
                        fileKey = getFileKey(fileUrl)
                    }
                    val inputStream = DiskLruCacheUtils.getSavedFileInputStream(diskLruCache, fileKey, fileUrl)
                    response = createWebResourceResponse(fileUrl, inputStream)
                }
            }
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            return response
        }

    }


    override fun isRestfulRequest(url: String): Boolean {
        return false
    }

    override fun isHtmlExist(url: String): Boolean {
        val fileKey = getFileKey(url)
        return DiskLruCacheUtils.isFileSaved(diskLruCache, fileKey);
    }

    override fun downloadHtml(url: String) {

        val fileKey = getFileKey(url)
        if (diskLruCache != null && !DiskLruCacheUtils.isFileSaved(diskLruCache, fileKey)) {
            DiskLruCacheUtils.downLoadFile(diskLruCache, url, fileKey)
        }
    }

    private fun createWebResourceResponse(fileUrl: String, inputStream: InputStream?): WebResourceResponse? {
        var response: WebResourceResponse? = null
        val map = HashMap<String, String>()
        map["Access-Control-Allow-Origin"] = "*"
        map["Access-Control-Allow-Headers"] = "Content-Type"
        if (inputStream != null) {
            response = WebResourceResponse(
                MimeTypeUtils.getMimeTypeFromUrl(fileUrl),
                "utf-8",
                200,
                "localFile",
                map,
                inputStream
            )
        }

        return response
    }


    open fun getFileKey(url: String): String {

        val index = url.indexOf("?")
        return MD5Util.md5(
            if (index != -1) {
                url.substring(0, index)
            } else {
                url
            }
        )
    }

}