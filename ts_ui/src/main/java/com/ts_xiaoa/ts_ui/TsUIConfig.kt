package com.ts_xiaoa.ts_ui

import androidx.annotation.IntDef
import com.ts_xiaoa.ts_base.TsBaseConfig
import com.ts_xiaoa.ts_glide.TsGlideConfig
import com.ts_xiaoa.ts_retrofit.TsNetConfig

/**
 * create by ts_xiaoA on 2020-08-20 17:41
 * email：443502578@qq.com
 * desc：ts_ui库相关配置
 */
class TsUIConfig private constructor() {

    val tsBaseConfig = TsBaseConfig.instance
    val tsNetConfig = TsNetConfig.instance
    val tsGlideConfig = TsGlideConfig.instance

    //app模式(深色、高亮)
    var appMode = AppMode.LIGHT

    //默认是否有appbar
    var hasAppBarLayout = true

    //默认分页
    var page = 1
    var pageSize = 20


    companion object {
        val instant: TsUIConfig by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { TsUIConfig() }
    }

    @IntDef(AppMode.DARK, AppMode.LIGHT)
    annotation class AppMode {
        companion object {
            //深色模式 白色字体
            const val DARK = 1

            //高亮模式 黑色字体
            const val LIGHT = 2
        }
    }
}