package com.ts_xiaoa.ts_permission

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * create by ts_xiaoA on 2020-06-11 17:44
 * email：443502578@qq.com
 * desc：申请权限的fragment
 */
open class RequestPermissionFragment : Fragment() {

    //请求的权限
    private var permissions: Array<String>? = null
    private val requestCode = 521
    private var listener: ((
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) -> Unit)? = null

    companion object {
        private const val permissionKey = "request_permissions"
        fun newInstance(
            listener: (requestCode: Int, permissions: Array<out String>, grantResults: IntArray) -> Unit,
            vararg permissions: String
        ): RequestPermissionFragment {
            val fragment = RequestPermissionFragment()
            fragment.listener = listener
            val bundle = Bundle()
            bundle.putStringArray(permissionKey, permissions)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            permissions = it.getStringArray(permissionKey) as Array<String>
        }
    }

    override fun onResume() {
        super.onResume()
        if (permissions != null) {
            requestPermissions(permissions!!, requestCode)
        } else {
            removeFragment()
        }
    }

    private fun removeFragment() {
        fragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        listener?.invoke(requestCode, permissions, grantResults)
        removeFragment()
    }
}