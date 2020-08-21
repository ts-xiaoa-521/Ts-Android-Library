package com.ts_xiaoa.ts_android_library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ts_xiaoa.ts_android_library.databinding.LayoutAppbarBinding
import com.ts_xiaoa.ts_base.utils.StatusBarUtil
import com.ts_xiaoa.ts_ui.base.TsBaseActivity

/**
 * create by ts_xiaoA on 2020-08-21 14:53
 * email：443502578@qq.com
 * desc：
 */
abstract class BaseActivity : TsBaseActivity() {
    protected open var appbarBinding: LayoutAppbarBinding? = null

    override fun createAppbarLayout(parent: ViewGroup?, appMode: Int) {
        appbarBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.layout_appbar,
            parent,
            true
        )
        StatusBarUtil.setImmersionView(appbarBinding?.root)
        appbarBinding?.tvBack?.setOnClickListener { onBackPressed() }
        configAppbarLayout(appbarBinding)
    }

    protected open fun configAppbarLayout(appbarBinding: LayoutAppbarBinding?) {

    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        appbarBinding?.tvTitle?.text = title
    }
}