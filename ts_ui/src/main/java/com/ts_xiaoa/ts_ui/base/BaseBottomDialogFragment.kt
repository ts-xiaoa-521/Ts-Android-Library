package com.ts_xiaoa.ts_ui.base

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ts_xiaoa.ts_ui.R

/**
 * create by ts_xiaoA on 2020-07-30 10:52
 * email：443502578@qq.com
 * desc：
 */
abstract class BaseBottomDialogFragment : BaseDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dialog = dialog
        if (dialog != null && dialog.window != null) {
            val window = dialog.window
            val params = window!!.attributes
            params.gravity = Gravity.BOTTOM
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }
}