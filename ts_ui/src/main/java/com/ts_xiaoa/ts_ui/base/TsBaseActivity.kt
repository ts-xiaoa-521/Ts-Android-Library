package com.ts_xiaoa.ts_ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ts_xiaoa.ts_base.utils.StatusBarUtil
import com.ts_xiaoa.ts_ktx.view_model.SimpleViewModel
import com.ts_xiaoa.ts_retrofit.helper.errorHandle
import com.ts_xiaoa.ts_ui.R
import com.ts_xiaoa.ts_ui.TsUIConfig
import com.ts_xiaoa.ts_ui.databinding.TsActivityBaseBinding
import com.ts_xiaoa.ts_ui.dialog.LoadingDialog
import com.ts_xiaoa.ts_ui.helper.LayoutConfig
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * create by ts_xiaoA on 2020-08-20 17:53
 * email：443502578@qq.com
 * desc：activity 基类
 */
abstract class TsBaseActivity : AppCompatActivity() {

    //rootBinding
    protected open lateinit var rootBinding: ViewDataBinding

    protected open val viewModelStoreKeySet by lazy { HashSet<String>() }

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
    //--------------加载中dialog---------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置全屏模式
        StatusBarUtil.fullScreen(this)
        val layoutConfig = getLayoutConfig()


        if (layoutConfig.hasAppbarLayout) {
            val binding: TsActivityBaseBinding =
                DataBindingUtil.setContentView(this, R.layout.ts_activity_base)
            createAppbarLayout(binding.tsFlAppbar, layoutConfig.appMode)
            rootBinding = if (layoutConfig.layoutId != 0) {
                DataBindingUtil.inflate(
                    LayoutInflater.from(this),
                    layoutConfig.layoutId,
                    binding.tsFlContent,
                    true
                )
            } else {
                binding
            }
        } else {
            rootBinding = if (layoutConfig.layoutId != 0) {
                DataBindingUtil.setContentView(this, layoutConfig.layoutId)
            } else {
                DataBindingUtil.setContentView(this, R.layout.ts_activity_base)
            }
        }
        if (layoutConfig.appMode == TsUIConfig.AppMode.DARK) {
            StatusBarUtil.setDarkMode(this)
        } else {
            StatusBarUtil.setLightMode(this)
        }
        StatusBarUtil.AndroidBug5497Workaround.assistActivity(this)
        rootBinding.lifecycleOwner = this
        initView(savedInstanceState)
        initEvent(savedInstanceState)
        onInit(savedInstanceState)
    }

    //创建AppbarLayout布局
    protected open fun createAppbarLayout(parent: ViewGroup?, @TsUIConfig.AppMode appMode: Int) {}

    //获取布局配置
    abstract fun getLayoutConfig(): LayoutConfig

    //初始化view
    protected open fun initView(savedInstanceState: Bundle?) {

    }

    //初始化事件
    protected open fun initEvent(savedInstanceState: Bundle?) {

    }

    //初始化
    protected open fun onInit(savedInstanceState: Bundle?) {

    }

    /**
     * 获取一个正在加载的pop
     *
     * @return
     */
    open fun showLoadingDialog() {
        loadingDialog.show(supportFragmentManager)
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

    /**
     * 取消正在加载的提示
     */
    open fun dismissLoading() {
        loadingHandler.removeCallbacksAndMessages(null)
        loadingDialog.dismissAllowingStateLoss()
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