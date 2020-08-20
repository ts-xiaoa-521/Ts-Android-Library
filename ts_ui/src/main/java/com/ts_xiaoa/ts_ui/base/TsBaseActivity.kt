package com.ts_xiaoa.ts_ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ts_xiaoa.ts_base.utils.StatusBarUtil
import com.ts_xiaoa.ts_ui.R
import com.ts_xiaoa.ts_ui.TsUIConfig
import com.ts_xiaoa.ts_ui.databinding.TsActivityBaseBinding
import com.ts_xiaoa.ts_ui.helper.LayoutConfig

/**
 * create by ts_xiaoA on 2020-08-20 17:53
 * email：443502578@qq.com
 * desc：activity 基类
 */
abstract class TsBaseActivity : AppCompatActivity() {

    //rootBinding
    protected open lateinit var rootBinding: ViewDataBinding

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
        init(savedInstanceState)
    }

    //appbar深色布局
    protected open fun setAppbarDarkLayout(parent: ViewGroup?) {}

    //appbar高亮布局
    protected open fun setAppbarLightLayout(parent: ViewGroup?) {}

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
    protected open fun init(savedInstanceState: Bundle?) {

    }
}