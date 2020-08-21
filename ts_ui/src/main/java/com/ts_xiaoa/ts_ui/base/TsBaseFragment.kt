package com.ts_xiaoa.ts_ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ts_xiaoa.ts_ktx.view_model.SimpleViewModel
import com.ts_xiaoa.ts_retrofit.helper.errorHandle
import com.ts_xiaoa.ts_ui.dialog.LoadingDialog
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * create by ts_xiaoA on 2020-06-28 16:56
 * email：443502578@qq.com
 * desc：fragment基类
 */
abstract class TsBaseFragment : Fragment() {

    protected open val viewModelStoreKeySet by lazy { HashSet<String>() }

    //DataBinding对象
    protected open var rootBinding: ViewDataBinding? = null

    //--------------加载中dialog---------------------
    protected open val loadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }

    protected open val loadingHandler: Handler by lazy {
        @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                showLoadingDialog()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootBinding = DataBindingUtil.inflate(
            inflater,
            getLayoutId(),
            container,
            false
        )
        rootBinding?.lifecycleOwner = this
        initView(savedInstanceState)
        initEvent(savedInstanceState)
        return rootBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onInit(savedInstanceState)
    }

    //获取布局文件id
    @LayoutRes
    abstract fun getLayoutId(): Int

    protected open fun initView(savedInstanceState: Bundle?) {}
    protected open fun initEvent(savedInstanceState: Bundle?) {}

    open fun onInit(savedInstanceState: Bundle?) {

    }

    //获取一个正在加载的pop
    open fun showLoadingDialog() {
        loadingDialog.show(childFragmentManager)
    }

    /**
     * 指定时间间隔后 显示一个dialog
     *
     * @param delayMillis 时间间隔
     */
    @SuppressLint("HandlerLeak")
    open fun showLoadingDialog(delayMillis: Long) {
        loadingHandler.sendEmptyMessageDelayed(0, delayMillis)
    }

    //取消正在加载的提示
    open fun dismissLoading() {
        loadingHandler.removeCallbacksAndMessages(null)
        if (loadingDialog.host != null && loadingDialog.isAdded && loadingDialog.dialog?.isShowing != false) {
            loadingDialog.dismiss()
        }
    }

    //启动一个协程
    fun request(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        onStart: () -> Unit = { onRequestStart() },
        onError: (handler: Throwable) -> Unit = { onRequestError(it) },
        onCompletion: (handler: Throwable?) -> Unit = { onRequestCompletion(it) },
        block: suspend CoroutineScope.() -> Unit
    ): DisposableHandle {
        onStart()
        return lifecycleScope.launch(context + CoroutineExceptionHandler { _, handler ->
            onError(handler)
        }, start, block).invokeOnCompletion {
            onCompletion(it)
        }

    }

    //创建ViewModel
    protected open fun <T : ViewModel?> createViewMode(modelClass: Class<T>): T {
        return ViewModelProvider(this).get(modelClass)
    }

    //创建ViewModel
    protected open fun <T : ViewModel?> createViewModeByActivity(modelClass: Class<T>): T {
        return ViewModelProvider(activity!!).get(modelClass)
    }


    //创建ViewModel
    protected inline fun <reified T> createViewMode(exitKey: String? = null): SimpleViewModel<T> {
        val key: String = if (exitKey == null) {
            "ts_view_model_key_default_${viewModelStoreKeySet.size}.${T::class.java}"
        } else {
            "ts_view_model_key_customer_${exitKey}.${T::class.java}"
        }
        if (!viewModelStoreKeySet.contains(key)) {
            viewModelStoreKeySet.add(key)
        }
        return ViewModelProvider(this, SimpleViewModel.TsViewModelFactory<T>())
            .get(SimpleViewModel::class.java) as SimpleViewModel<T>
    }

    //创建ViewModel
    protected inline fun <reified T> createViewModeByActivity(exitKey: String? = null): SimpleViewModel<T> {
        val key: String = if (exitKey == null) {
            "ts_view_model_key_default_${viewModelStoreKeySet.size}.${T::class.java}"
        } else {
            "ts_view_model_key_customer_${exitKey}.${T::class.java}"
        }
        if (!viewModelStoreKeySet.contains(key)) {
            viewModelStoreKeySet.add(key)
        }
        return ViewModelProvider(activity!!, SimpleViewModel.TsViewModelFactory<T>())
            .get(SimpleViewModel::class.java) as SimpleViewModel<T>
    }


    //协程启动时的回调方法
    open fun onRequestStart() {
        showLoadingDialog()
    }

    //协程结束时的回调
    open fun onRequestCompletion(handler: Throwable?) {
        dismissLoading()
    }

    //协程异常统一处理
    open fun onRequestError(handler: Throwable) {
        errorHandle(handler)
    }

}