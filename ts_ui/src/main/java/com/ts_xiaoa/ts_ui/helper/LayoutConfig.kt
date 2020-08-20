package com.ts_xiaoa.ts_ui.helper

import androidx.annotation.LayoutRes
import com.ts_xiaoa.ts_ui.TsUIConfig

/**
 * create by ts_xiaoA on 2020-07-22 09:22
 * email：443502578@qq.com
 * desc：布局配置
 */
class LayoutConfig constructor(
    //布局文件id
    @LayoutRes val layoutId: Int = 0,
    //是否有默认的appbar布局
    val hasAppbarLayout: Boolean = TsUIConfig.instant.hasAppBarLayout,
    //当前界面模式(深色、高亮)
    @TsUIConfig.AppMode val appMode: Int =  TsUIConfig.instant.appMode
)