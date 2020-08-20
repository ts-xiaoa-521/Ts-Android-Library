package com.ts_xiaoa.ts_base.provider

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.pm.ProviderInfo
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ts_xiaoa.ts_base.TsBaseConfig
import com.ts_xiaoa.ts_base.lifecycle.SimpleActivityLifecycleCallbacks
import com.ts_xiaoa.ts_base.utils.setDensityWidth
import java.lang.ref.WeakReference


/**
 * create by ts_xiaoA on 2020-07-17 11:07
 * email：443502578@qq.com
 * desc：全局Application对象
 */
class ApplicationProvider : ContentProvider() {

    companion object {
        //全局application对象
        lateinit var application: Application

        //toast对象
        lateinit var toast: Toast

        //当前显示的activity对象
        var currentActivityWeakRef: WeakReference<AppCompatActivity>? = null
    }

    @SuppressLint("ShowToast")
    override fun onCreate(): Boolean {
        application = context!!.applicationContext as Application
        toast = Toast.makeText(application, null, Toast.LENGTH_SHORT)
        //注册activity初始化生命周期回调
        application.registerActivityLifecycleCallbacks(object : SimpleActivityLifecycleCallbacks() {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                //屏幕适配
                activity.setDensityWidth(TsBaseConfig.instance.densityWidthDp)
            }

            override fun onActivityResumed(activity: Activity) {
                //保存当前显示的activity
                if (currentActivityWeakRef != null) {
                    currentActivityWeakRef?.clear()
                    currentActivityWeakRef = null
                }
                currentActivityWeakRef = WeakReference(activity as AppCompatActivity)
            }
        })
        return true
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}
