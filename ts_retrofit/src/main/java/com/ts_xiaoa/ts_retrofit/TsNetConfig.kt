package com.ts_xiaoa.ts_retrofit

import android.util.Log
import com.ts_xiaoa.ts_retrofit.interceptor.ResultCodeInterceptor

/**
 * create by ts_xiaoA on 2020-08-20 11:29
 * email：443502578@qq.com
 * desc：网络相关配置
 */
class TsNetConfig private constructor() {

    //默认成功的mode
    var successCode = 200

    //对网络返回数据code拦截
    var resultInterceptorList: MutableList<ResultCodeInterceptor>? = null

    //默认网络错误处理对象
    var netErrorHandle: ((code: Int, msg: String?) -> Unit) = { code, msg ->
        Log.e("Net errorMessage", "$code\t$msg")
    }

    companion object {
        //单例
        val instance: TsNetConfig by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { TsNetConfig() }
    }
}