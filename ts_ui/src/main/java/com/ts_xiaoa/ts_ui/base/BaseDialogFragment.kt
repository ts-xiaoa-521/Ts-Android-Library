package com.ts_xiaoa.ts_ui.base

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.FloatRange
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.ts_xiaoa.ts_retrofit.helper.errorHandle
import com.ts_xiaoa.ts_ui.dialog.LoadingDialog
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * create by ts_xiaoA on 2020-06-05 11:45
 * email：443502578@qq.com
 * desc：
 */
abstract class BaseDialogFragment : DialogFragment() {

    //该类对象的唯一标识
    protected open val TAG = this.toString()

    //dataBinding对象
    protected open var rootView: View? = null
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
    //--------------加载中dialog---------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null != rootView) {
            val parent = rootView!!.parent as ViewGroup
            parent.removeView(rootView)
        } else {
            rootBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            rootView = rootBinding!!.root
            initView(savedInstanceState)
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dialog = dialog
        if (dialog != null && dialog.window != null) {
            val window = dialog.window
            window!!.setBackgroundDrawable(ColorDrawable(0x00000000))
            window.setDimAmount(getDimAmount())
            val params = window.attributes
            params.gravity = Gravity.CENTER
            window.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        initEvent(savedInstanceState)
        init(savedInstanceState)
    }

    /**
     * 设置布局文件
     *
     * @return
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * 初始化View
     *
     * @param savedInstanceState savedInstanceState
     */
    protected open fun initView(savedInstanceState: Bundle?) {

    }

    /**
     * 初始化事件
     *
     * @param savedInstanceState savedInstanceState
     */
    protected open fun initEvent(savedInstanceState: Bundle?) {

    }

    /**
     * 初始化
     *
     * @param savedInstanceState savedInstanceState
     */
    protected open fun init(savedInstanceState: Bundle?) {

    }

    /**
     * 获取一个正在加载的pop
     *
     * @return
     */
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

    /**
     * 取消正在加载的提示
     */
    open fun dismissLoading() {
        loadingHandler.removeCallbacksAndMessages(null)
        loadingDialog.dismissAllowingStateLoss()
    }

    /**
     * 背景黑暗度
     */
    @FloatRange(from = 0.0, to = 1.0)
    protected open fun getDimAmount(): Float {
        return 0.6f
    }

    /**
     * 显示dialog
     *
     * @param manager FragmentManager
     */
    open fun show(manager: FragmentManager) {
        try {
            val mDismissed =
                DialogFragment::class.java.getDeclaredField("mDismissed")
            val mShownByMe =
                DialogFragment::class.java.getDeclaredField("mShownByMe")
            mDismissed.isAccessible = true
            mDismissed[this] = false
            mShownByMe.isAccessible = true
            mShownByMe[this] = true
            val fragmentTransaction =
                manager.beginTransaction()
            if (!isAdded) {
                fragmentTransaction.add(this, TAG)
            } else {
                fragmentTransaction.show(this)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
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