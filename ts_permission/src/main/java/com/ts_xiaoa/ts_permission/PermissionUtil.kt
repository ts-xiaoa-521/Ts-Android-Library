package com.ts_xiaoa.ts_permission

import android.content.pm.PackageManager
import androidx.fragment.app.FragmentActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * create by ts_xiaoA on 2020-06-12 17:51
 * email：443502578@qq.com
 * desc：
 */

suspend fun FragmentActivity.requestPermissionsForResult(vararg permissions: String): Boolean {
    return suspendCoroutine {
        val permissionFragment =
            RequestPermissionFragment.newInstance(
                { _, _, grantResults ->
                    var granted = true
                    for (grantResult in grantResults) {
                        if (grantResult == PackageManager.PERMISSION_DENIED) {
                            granted = false
                            break
                        }
                    }
                    it.resume(granted)
                },
                *permissions
            )
        this.supportFragmentManager.beginTransaction()
            .add(permissionFragment, "request_permission")
            .commitAllowingStateLoss()
    }
}

