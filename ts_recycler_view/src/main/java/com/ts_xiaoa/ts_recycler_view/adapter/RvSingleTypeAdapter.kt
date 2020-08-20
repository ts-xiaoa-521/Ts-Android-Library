package com.ts_xiaoa.ts_recycler_view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ts_xiaoa.ts_recycler_view.BaseViewHolder

/**
 * create by ts_xiaoA on 2020-06-29 09:49
 * email：443502578@qq.com
 * desc：recyclerView adapter 基类
 */
abstract class RvSingleTypeAdapter<Data>(dataList: MutableList<Data?>? = null) :

    RecyclerView.Adapter<BaseViewHolder>() {

    //上下文对象
    protected var context: Context? = null

    //数据集合
    private var dataList: MutableList<Data?>

    //item点击监听
    var onItemClickListener: ((view: View, position: Int, data: Data?) -> Unit)? = null
    var onItemChildClickListener: ((view: View, position: Int, data: Data?) -> Unit)? = null

    //主构造函数
    init {
        if (dataList != null) {
            this.dataList = dataList
        } else {
            this.dataList = mutableListOf()
        }
    }

    //绑定视图时 获取context对象
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (this.context == null)
            this.context = recyclerView.context
    }


    //创建布局文件
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val rootBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            getLayoutResId(viewType),
            parent,
            false
        )
        return BaseViewHolder(rootBinding.root)
    }


    //获取item count
    override fun getItemCount(): Int {
        return getData().size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val rootBinding = DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)
        onItemClickListener?.let { listener ->
            rootBinding?.root?.setOnClickListener {
                listener.invoke(it, position, getData()[position])
            }
        }
        if (position < getData().size)
            onBindItem(holder, rootBinding, getData()[position])
        else
            onBindNullData(holder, rootBinding)
    }


    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val rootBinding = DataBindingUtil.getBinding<ViewDataBinding>(holder.itemView)
            onBindItemPayLoad(holder, rootBinding, dataList[position])
        }
    }



    //获取布局文件
    @LayoutRes
    protected abstract fun getLayoutResId(viewType: Int): Int

    //绑定布局
    protected abstract fun onBindItem(
        holder: BaseViewHolder,
        rootBinding: ViewDataBinding?,
        item: Data?
    )

    //绑定布局
    protected open fun onBindNullData(
        holder: BaseViewHolder,
        rootBinding: ViewDataBinding?
    ) {
    }

    //绑定布局
    protected open fun onBindItemPayLoad(
        holder: BaseViewHolder,
        rootBinding: ViewDataBinding?,
        item: Data?
    ) {

    }

    //获取数据集合
    open fun getData(): MutableList<Data?> {
        return this.dataList
    }

    open fun notifyItemChangeByPayload(position: Int) {
        notifyItemChanged(position, 0)
    }
}