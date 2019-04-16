package com.autoforce.common.view.popup

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.autoforce.common.R
import com.autoforce.common.utils.DeviceUtil

/**
 * Created by xlh on 2019/2/19.
 * description:
 */
class FilterGridView(context: Context, configs: MaskPopupWindowManager.Params) :
    RecyclerView(context) {

    private val mAdapter: FilterGridAdapter =
        FilterGridAdapter(configs.spanCount, configs.selectedPos)

    init {

        adapter = mAdapter
        layoutManager = GridLayoutManager(
            context,
            configs.spanCount
        )

        setBackgroundColor(
            ContextCompat.getColor(
                context, R.color.white
            )
        )

        // 设置Grid的高度 可使用builder对外暴露配置
        post {
            val params = layoutParams
            params.height = DeviceUtil.dip2px(context, 96f)
            layoutParams = params
        }

        // 设置grid的内边距，可对外暴露配置
        val horizontalPadding = DeviceUtil.dip2px(context, 16f)
        val verticalPadding = DeviceUtil.dip2px(context, 15f)

        setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding)

        setData(configs.stringArray?.toList())

    }

    fun setData(data: List<String?>?) {
        mAdapter.setData(data)
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener?) {
        mAdapter.setOnItemClickListener(itemClickListener)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, text: String?)
    }
}

class FilterGridAdapter(
    private val spanCount: Int,
    private val mSelectedPos: Int = 0
) : RecyclerView.Adapter<FilterGridAdapter.ViewHolder>() {

    private val mData: MutableList<String?> = mutableListOf()
    private var onItemClickListener: FilterGridView.OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_buy_filter, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val textView: TextView = holder.itemView as TextView

        val params = textView.layoutParams as RecyclerView.LayoutParams
        if ((position + 1) % spanCount != 0) {
            params.rightMargin = DeviceUtil.dip2px(holder.itemView.context, 10f)
        } else {
            params.rightMargin = 0
        }

        if (position >= spanCount) {
            params.topMargin = DeviceUtil.dip2px(holder.itemView.context, 10f)
        }

        textView.layoutParams = params

        textView.text = mData[position]

        textView.setTextColor(
            if (position == mSelectedPos) {
                ContextCompat.getColor(textView.context, R.color.redD5)
            } else {
                ContextCompat.getColor(textView.context, R.color.black3)
            }
        )

        textView.setOnClickListener {
            onItemClickListener?.onItemClick(position, mData[position])
        }
    }

    fun setData(data: List<String?>?) {
        if (data != null && data.isNotEmpty()) {
            mData.clear()
            mData.addAll(data)
            notifyDataSetChanged()
        }
    }

    fun setOnItemClickListener(itemClickListener: FilterGridView.OnItemClickListener?) {
        this.onItemClickListener = itemClickListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}