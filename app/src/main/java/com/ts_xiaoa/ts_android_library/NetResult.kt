package com.ts_xiaoa.ts_android_library

import com.ts_xiaoa.ts_retrofit.bean.INetResult

/**
 * create by ts_xiaoA on 2020-08-20 14:11
 * email：443502578@qq.com
 * desc：
 */
class NetResult<T> : INetResult<T> {
    var code: Int = 0
    var data: T? = null
    var message: String? = null

    override fun getResultCode(): Int {
        return code
    }

    override fun getResultMessage(): String {
        return message ?: ""
    }

    override fun getResultData(): T? {
        return data
    }
}