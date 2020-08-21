package com.ts_xiaoa.ts_recycler_view.widget.wheel

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ts_xiaoa.ts_recycler_view.BaseViewHolder
import com.ts_xiaoa.ts_recycler_view.adapter.RvSingleTypeAdapter

/**
 * create by ts_xiaoA on 2020-07-30 10:29
 * email：443502578@qq.com
 * desc：
 */
abstract class BaseWheelAdapter<Data>(
    var showItemCount: Int = 5,
    data: MutableList<Data>? = null
) : RvSingleTypeAdapter<Data>(data) {

    private val ITEM_TYPE_STANCE = 1230
    private val ITEM_TYPE_NOTHING = 1231

    var selectedPosition = 0

    //配置选中状态的view
    abstract fun configSelectedView(view: View)

    //配置未选中时的View
    abstract fun configUnSelectedView(view: View)

    override fun getItemViewType(position: Int): Int {
        val stanceItemCount = showItemCount / 2
        if (getData().size == 0 && position == showItemCount / 2) {
            return ITEM_TYPE_NOTHING
        }
        return if (position < stanceItemCount || position >= getData().size + stanceItemCount) {
            ITEM_TYPE_STANCE
        } else super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(holder.layoutPosition) == ITEM_TYPE_NOTHING) {
//            onBindViewNothing(holder, binding, null)
        } else if (getItemViewType(holder.layoutPosition) != ITEM_TYPE_STANCE) {
            val binding = DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)
            onBindItem(holder, binding, getData()[position - showItemCount / 2])
        }
        if (position == selectedPosition) {
            configSelectedView(holder.itemView)
        } else {
            configUnSelectedView(holder.itemView)
        }
    }

    override fun getItemCount(): Int {
        return if (super.getItemCount() == 0) {
            super.getItemCount() + showItemCount
        } else {
            super.getItemCount() + showItemCount - 1
        }
    }
}