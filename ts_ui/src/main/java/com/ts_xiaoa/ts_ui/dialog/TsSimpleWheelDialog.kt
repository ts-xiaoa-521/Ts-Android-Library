package com.ts_xiaoa.ts_ui.dialog

import android.os.Bundle
import com.ts_xiaoa.ts_recycler_view.BaseViewHolder
import com.ts_xiaoa.ts_recycler_view.databinding.TsRvWheelStringBinding
import com.ts_xiaoa.ts_recycler_view.widget.wheel.WheelStringAdapter
import com.ts_xiaoa.ts_ui.R
import com.ts_xiaoa.ts_ui.base.BaseBottomDialogFragment
import com.ts_xiaoa.ts_ui.databinding.TsDialogSimpleWheelBinding

/**
 * create by ts_xiaoA on 2020-08-01 14:16
 * email：443502578@qq.com
 * desc：简单的wheel dialog
 */
class TsSimpleWheelDialog<T> constructor(
    //数据
    private val title: CharSequence = "",
    private val data: MutableList<T>,
    private val autoCancel: Boolean = true,
    itemBinder: ((holder: BaseViewHolder, itemBinding: TsRvWheelStringBinding, item: T) -> Unit)? = null,
    //选择完成后的监听回调
    private val listener: ((data: T) -> Unit)? = null
) : BaseBottomDialogFragment() {

    val binding by lazy { rootBinding as TsDialogSimpleWheelBinding }

    //显示adapter
    private var dataAdapter: WheelStringAdapter<T>? = null


    init {
        dataAdapter = WheelStringAdapter(data = data, itemBinder = itemBinder)
    }

    override fun getLayoutId(): Int {
        return R.layout.ts_dialog_simple_wheel
    }


    override fun initView(savedInstanceState: Bundle?) {
        binding.tvTitle.text = title
        binding.wrData.adapter = dataAdapter
    }


    override fun initEvent(savedInstanceState: Bundle?) {
        binding.tvCancel.setOnClickListener { dismiss() }
        binding.tvFinish.setOnClickListener {
            listener?.invoke(data[binding.wrData.selectPosition])
            if (autoCancel) dismiss()
        }
    }
}