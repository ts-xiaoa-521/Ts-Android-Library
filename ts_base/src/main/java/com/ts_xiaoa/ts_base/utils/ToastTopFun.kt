package com.ts_xiaoa.ts_base.utils

import com.ts_xiaoa.ts_base.provider.ApplicationProvider

/**
 * create by ts_xiaoA on 2020-07-21 16:58
 * email：443502578@qq.com
 * desc：Toast
 */

fun showToast(msg: String?) {
    ApplicationProvider.toast.setText(msg)
    ApplicationProvider.toast.show()
}

