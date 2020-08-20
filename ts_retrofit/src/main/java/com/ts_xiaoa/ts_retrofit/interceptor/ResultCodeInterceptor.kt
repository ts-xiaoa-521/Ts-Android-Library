package com.ts_xiaoa.ts_retrofit.interceptor

/**
 * create by ts_xiaoA on 2020-08-20 11:32
 * email：443502578@qq.com
 * desc：
 */
interface ResultCodeInterceptor {
    //需要拦截的code
    fun getCode(): List<Int>

    //分发网络结果
    fun dispatchResult(): Boolean
}