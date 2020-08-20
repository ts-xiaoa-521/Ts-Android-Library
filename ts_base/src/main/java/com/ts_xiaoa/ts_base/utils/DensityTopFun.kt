package com.ts_xiaoa.ts_base.utils

import android.content.Context
import android.content.res.Configuration
import android.util.TypedValue
import com.ts_xiaoa.ts_base.TsBaseConfig
import com.ts_xiaoa.ts_base.provider.ApplicationProvider

/**
 * create by ts_xiaoA on 2020-06-10 15:18
 * email：443502578@qq.com
 * desc：设置屏幕适配宽度  单位：dp  即 ui上设计图的dp宽度
 */
fun Context.setDensityWidth(designWidthDP: Int) {
    TsBaseConfig.instance.densityWidthDp = designWidthDP
    val context = this
    val config = Configuration()
    config.setToDefaults()
    context.createConfigurationContext(config)
//        resources.updateConfiguration(config, resources.displayMetrics)
    //获取手机显示相关信息
    val displayMetrics = context.resources.displayMetrics
    //获取屏幕的实际宽度
    val screenWidth = displayMetrics.widthPixels
    //计算当前设备应该设定的density值（px = dp * density  ==>density = px / dp）
    val screenDensity = 1.0f * screenWidth / designWidthDP
    //计算当前设备应该设定的ScaleDensity值（通过比例计算）
    val screenScaleDensity =
        displayMetrics.scaledDensity * (screenDensity / displayMetrics.density)
    //重新设置density值
    displayMetrics.density = screenDensity
    displayMetrics.scaledDensity = screenScaleDensity
    //density值变化 所以屏幕的dpi也发生了变化，重新计算dpi
    displayMetrics.densityDpi = (160 * screenDensity).toInt()
}

val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        ApplicationProvider.application.resources.displayMetrics
    ).toInt()


val Int.dp: Int
    get() = this.toFloat().dp