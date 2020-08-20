package com.ts_xiaoa.ts_retrofit.interceptor

import com.ts_xiaoa.ts_retrofit.bean.INetResult

/**
 * create by ts_xiaoA on 2020-08-20 11:32
 * email：443502578@qq.com
 * desc：网络结果拦截期初
 */
interface ResultCodeInterceptor {
    //需要拦截的code
    fun getCode(): Int

    //拦截网络结果
    fun interceptResult(result: INetResult<*>): Boolean
}