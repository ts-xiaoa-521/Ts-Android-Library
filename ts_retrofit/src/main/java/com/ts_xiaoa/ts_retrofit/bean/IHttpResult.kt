package com.ts_xiaoa.ts_retrofit.bean

/**
 * create by ts_xiaoA on 2020-08-20 11:25
 * email：443502578@qq.com
 * desc：网络请求数据接收类实现方法
 */
interface IHttpResult<T> {
    //code
    fun getCode(): Int

    //message
    fun getMessage(): String

    //data
    fun getData(): T?
}