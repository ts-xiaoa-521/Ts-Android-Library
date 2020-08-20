package com.ts_xiaoa.ts_android_library

import android.app.Application
import com.ts_xiaoa.ts_base.utils.setDensityWidth

/**
 * create by ts_xiaoA on 2020-08-20 14:47
 * email：443502578@qq.com
 * desc：
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //屏幕适配
        setDensityWidth(375)
    }
}