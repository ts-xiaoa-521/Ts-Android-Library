package com.ts_xiaoa.ts_retrofit.helper

/**
 * create by ts_xiaoA on 2020-06-06 10:49
 * email：443502578@qq.com
 * desc：服务器正常返回的异常
 */
class ServerException constructor(val code: Int = -1, message: String = "") : Exception(message) {
    constructor(message: String) : this(-1, message)
}