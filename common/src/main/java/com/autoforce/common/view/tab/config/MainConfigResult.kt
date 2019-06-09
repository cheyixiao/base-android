package com.autoforce.common.view.tab.config

import android.support.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Created by xlh on 2019/3/18.
 * description:
 */
@Keep
class MainConfigResult {
    @SerializedName("layout_height")
    var layoutHeight: Float? = null
    var defaultCheck: Int = 0

    var tabs: List<TabInfoBean>? = null

    @Keep
    class TabInfoBean {
        var tabName: String? = null
        var iconUnchecked: String? = null
        var iconChecked: String? = null
        var textColorChecked: String? = null
        var textColorUnchecked: String? = null
        var textSize: Float? = null
        var drawablePadding: Float? = null
        var iconPosition: String? = null
        var webUrl: String? = null
        var imageRatio: Float? = null
        var pageName: String? = null
    }
}