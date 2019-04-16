package com.autoforce.common.view.popup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.*
import android.widget.*
import com.autoforce.common.R
import com.autoforce.common.utils.DeviceUtil
import com.autoforce.common.utils.KeyboardUtil

/**
 * Created by xialihao on 2018/11/23.
 */
class MaskPopupWindowManager(params: MaskPopupWindowManager.Params, private var mActivity: Activity?) {

    private var mPopupWindow: PopupWindow? = null
    private var mHandler: Handler? = null
    private var mOnDismissListener: OnDismissListener? = null

    init {
        initView(params)
    }

    private fun initView(configs: Params) {

        mOnDismissListener = configs.onDismissListener

        val anchorView = configs.anchorView ?: throw RuntimeException("Please set anchorView")

        // 没有设置ContentView，则使用默认的ListView作为contentView
        if (configs.contentView == null) {
            configs.contentView = getDefaultContentView(configs)
        }

        initPopup(configs, anchorView)
        show(anchorView)
    }

    private fun initPopup(
        configs: Params,
        anchorView: View
    ) {
        mPopupWindow =
                PopupWindow(
                    configs.contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
                )

        val wm = mActivity!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val height = wm.defaultDisplay.height
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)
        mPopupWindow?.isOutsideTouchable = true
        mPopupWindow?.height = height - location[1] - anchorView.bottom
        mPopupWindow?.width = ViewGroup.LayoutParams.MATCH_PARENT
        mPopupWindow?.contentView = configs.contentView
        mPopupWindow?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    mActivity!!,
                    R.color.half_transparent_05
                )
            )
        )

        mPopupWindow?.setOnDismissListener {
            mHandler?.removeCallbacksAndMessages(null)
            mHandler = null

            mOnDismissListener?.onDismiss()
        }
    }

    private fun getDefaultContentView(configs: Params): View {
        return PopupListView(mActivity, null, configs)
    }

    private fun show(anchorView: View?): MaskPopupWindowManager? {

        if (KeyboardUtil.isSoftInputShow(mActivity)) {
            KeyboardUtil.hideSoftInput(mActivity)
            mHandler = Handler()
            mHandler?.postDelayed({
                mPopupWindow?.showAsDropDown(anchorView)
            }, 100)
        } else {
            mPopupWindow?.showAsDropDown(anchorView)
        }

        return this
    }

    fun dismiss() {
        mPopupWindow?.dismiss()
    }


    interface OnDismissListener {
        fun onDismiss()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, string: String?)
    }

    class Builder {

        private var params: Params = Params()

        fun setOnDismissListener(listener: OnDismissListener): Builder {
            params.onDismissListener = listener
            return this
        }

        fun setOnItemClickListener(listener: OnItemClickListener): Builder {
            params.onItemClickListener = listener
            return this
        }

        fun setStringArray(strs: Array<String?>): Builder {
            params.stringArray = strs
            return this
        }

        fun setSelectedPos(position: Int): Builder {
            params.selectedPos = position
            return this
        }

        fun setAnchorView(view: View): Builder {
            params.anchorView = view
            return this
        }

        fun setDividerHeight(dp: Float): Builder {
            params.dividerHeight = dp
            return this
        }

        fun setShowItemCount(count: Int): Builder {
            params.showCount = count
            return this
        }

        fun setContentView(view: View): Builder {
            params.contentView = view
            return this
        }

        fun addFooterView(footerView: View): Builder {
            params.footerView = footerView
            return this
        }

        fun show(activity: Activity): MaskPopupWindowManager {
            return MaskPopupWindowManager(params, activity)
        }

    }

    class Params {
        var onDismissListener: OnDismissListener? = null
        var onItemClickListener: OnItemClickListener? = null
        var stringArray: Array<String?>? = null
        var selectedPos: Int = 0
        var anchorView: View? = null
        var contentView: View? = null
        var footerView: View? = null
        var dividerHeight: Float = 0.5f
        var showCount: Int = 4
    }

    @SuppressLint("ViewConstructor")
    inner class PopupListView(context: Context?, attrs: AttributeSet?, configs: MaskPopupWindowManager.Params) :
        LinearLayout(context, attrs) {

        init {
            initView(context, configs)
        }

        private fun initView(
            context: Context?,
            configs: MaskPopupWindowManager.Params
        ) {

            orientation = VERTICAL
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.pop_filter_list, this)

            val listView: ListView = findViewById(R.id.list_view)
            listView.dividerHeight = DeviceUtil.dip2px(context, configs.dividerHeight)

            val params = listView.layoutParams
            val footHeight = if (configs.footerView == null) {
                0
            } else {
                configs.footerView?.measure(0, 0)
                configs.footerView!!.measuredHeight
            }
            // 这些写死的写死也可改成外部传入参数进行配置
            params.height = DeviceUtil.dip2px(context, 40f) * configs.showCount + (configs.showCount - 1) *
                    DeviceUtil.dip2px(context, configs.dividerHeight) + footHeight
            listView.layoutParams = params
            listView.setPadding(
                DeviceUtil.dip2px(context, 16f),
                listView.paddingTop,
                listView.paddingRight,
                listView.paddingBottom
            )

            listView.adapter =
                    object : ArrayAdapter<String>(context, R.layout.array_adapter_text_view, configs.stringArray) {

                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view = super.getView(position, convertView, parent)

                            val textView: TextView = view.findViewById(R.id.text1)
                            textView.setBackgroundResource(R.drawable.list_mask_popup_bg)
                            textView.gravity = Gravity.CENTER_VERTICAL
                            textView.height = DeviceUtil.dip2px(context, 40f)
                            val paddingRight = DeviceUtil.dip2px(context, 16f)
                            textView.setPadding(
                                textView.paddingLeft,
                                textView.paddingTop,
                                paddingRight,
                                textView.paddingBottom
                            )

                            if (configs.selectedPos == position) {
                                textView.setTextColor(ContextCompat.getColor(context, R.color.redD5))
                                val drawable: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_list_check)!!
                                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
                                textView.setCompoundDrawables(null, null, drawable, null)
                            } else {
                                textView.setTextColor(ContextCompat.getColor(context, R.color.black26))
                                textView.setCompoundDrawables(null, null, null, null)
                            }

                            return view
                        }
                    }

            configs.footerView?.let {
                listView.addFooterView(configs.footerView)
            }

            listView.setOnItemClickListener { _, _, position, _ ->
                if (configs.selectedPos != position && configs.stringArray != null) {
                    try {
                        configs.onItemClickListener?.onItemClick(position, configs.stringArray!![position])
                    } catch (e: ArrayIndexOutOfBoundsException) {

                    }
                }

                mPopupWindow?.dismiss()
            }

            setOnClickListener {
                mPopupWindow?.dismiss()
            }
        }
    }

}
