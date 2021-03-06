package com.ts_xiaoa.ts_ui.base

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ts_xiaoa.ts_recycler_view.adapter.RvSingleTypeAdapter
import com.ts_xiaoa.ts_ui.R
import com.ts_xiaoa.ts_ui.TsUIConfig
import com.ts_xiaoa.ts_ui.databinding.TsLayoutRecyclerListBinding
import com.ts_xiaoa.ts_ui.helper.LayoutConfig

/**
 * create by ts_xiaoA on 2020-08-21 09:46
 * email：443502578@qq.com
 * desc：简单列表基类
 */
abstract class TsRvListActivity<T> : TsBaseActivity() {

    protected open val binding by lazy { rootBinding as TsLayoutRecyclerListBinding }
    protected open lateinit var mLayoutManager: RecyclerView.LayoutManager
    protected open lateinit var mAdapter: RvSingleTypeAdapter<T>
    protected open var page = TsUIConfig.instant.page
    protected open var pageSize = TsUIConfig.instant.pageSize

    override fun getLayoutConfig(): LayoutConfig {
        return LayoutConfig(R.layout.ts_layout_recycler_list)
    }


    override fun initView(savedInstanceState: Bundle?) {
        mLayoutManager = getLayoutManager()
        mAdapter = getAdapter()
        binding.rvData.layoutManager = mLayoutManager
        binding.rvData.adapter = mAdapter


    }

    override fun initEvent(savedInstanceState: Bundle?) {
        binding.tsSmartRefreshLayout.setOnRefreshListener { refresh(false) }
        binding.tsSmartRefreshLayout.setOnLoadMoreListener { loadMore() }
    }

    override fun onInit(savedInstanceState: Bundle?) {
        refresh(true)
    }

    //刷新
    protected open fun refresh(defaultStartEnable: Boolean = false) {
        request(onStart = {
            if (defaultStartEnable) onRequestStart()
        }, onCompletion = {
            binding.tsSmartRefreshLayout.finishRefresh()
            dismissLoading()
        }) {
            page = 1
            val result = getDataSourceAsync()
            result?.let {
                mAdapter.getData().clear()
                mAdapter.getData().addAll(it)
                mAdapter.notifyDataSetChanged()
                if (it.size < pageSize) {
                    binding.tsSmartRefreshLayout.finishRefreshWithNoMoreData()
                }
                binding.tsLlNothing.isVisible = mAdapter.getData().size == 0
            }
        }
    }

    //加载更多
    protected open fun loadMore() {
        request(onStart = {}, onCompletion = {
            binding.tsSmartRefreshLayout.finishLoadMore()
        }) {
            page++
            val result = getDataSourceAsync()
            result?.let {
                mAdapter.getData().addAll(it)
                mAdapter.notifyDataSetChanged()
                if (it.size < pageSize) {
                    binding.tsSmartRefreshLayout.finishLoadMoreWithNoMoreData()
                }
                binding.tsLlNothing.isVisible = mAdapter.getData().size == 0
            }
        }
    }

    //layoutManager
    protected open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

    //数据源
    abstract suspend fun getDataSourceAsync(): List<T>?

    //adapter
    abstract fun getAdapter(): RvSingleTypeAdapter<T>
}