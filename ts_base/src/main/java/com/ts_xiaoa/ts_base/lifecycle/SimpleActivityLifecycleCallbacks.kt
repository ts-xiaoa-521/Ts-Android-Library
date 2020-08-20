package com.ts_xiaoa.ts_base.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * create by ts_xiaoA on 2020-07-18 10:22
 * email：443502578@qq.com
 * desc：activity 生命周期回调实现类
 */
open class SimpleActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }


}