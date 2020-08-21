package com.ts_xiaoa.ts_recycler_view.widget.wheel

import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.ts_xiaoa.ts_recycler_view.BaseViewHolder
import com.ts_xiaoa.ts_recycler_view.R
import com.ts_xiaoa.ts_recycler_view.databinding.TsRvWheelStringBinding

/**
 * create by ts_xiaoA on 2020-07-30 11:07
 * email：443502578@qq.com
 * desc：
 */
open class WheelStringAdapter<Data>(
    data: MutableList<Data>? = null,
    private var itemBinder: ((holder: BaseViewHolder, itemBinding: TsRvWheelStringBinding, item: Data) -> Unit)? = null
) : BaseWheelAdapter<Data>(data = data) {


    override fun getLayoutResId(viewType: Int): Int {
        return R.layout.ts_rv_wheel_string
    }

    final override fun onBindItem(holder: BaseViewHolder, rootBinding: ViewDataBinding?, item: Data) {
        val binding = rootBinding as TsRvWheelStringBinding
        if (itemBinder == null) {
            binding.tvText.text = item.toString()
        } else {
            itemBinder?.invoke(holder, binding, item)
        }

    }

    override fun configSelectedView(view: View) {
        val tvText = view as TextView
        tvText.setTextColor(context!!.resources.getColor(R.color.colorWheelSelected))
        tvText.textSize = 18f
        tvText.paint.isFakeBoldText = true
    }

    override fun configUnSelectedView(view: View) {
        val tvText = view as TextView
        tvText.setTextColor(context!!.resources.getColor(R.color.colorWheelNormal))
        tvText.textSize = 15f
        tvText.paint.isFakeBoldText = false
    }

}