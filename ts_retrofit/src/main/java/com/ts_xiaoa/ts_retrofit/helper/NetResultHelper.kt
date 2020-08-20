package com.ts_xiaoa.ts_retrofit.helper

import com.ts_xiaoa.ts_retrofit.TsNetConfig
import com.ts_xiaoa.ts_retrofit.bean.IHttpResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import retrofit2.HttpException
import java.io.EOFException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * create by ts_xiaoA on 2020-08-20 11:31
 * email：443502578@qq.com
 * desc：
 */

//获取网络数据
suspend fun <T> Deferred<T>.awaitNet(
    isCancel: Boolean = true,
    customErrorHandle: ((code: Int, message: String?) -> Unit)? = null
): T? {
    return try {
        val result = this.await()
        if (result is IHttpResult<*>) {
            //先处理需要拦截的特殊code
            TsNetConfig.instance.resultInterceptorList?.map {
                if (it.getCode().contains(result.getCode())) {
                    if (it.dispatchResult()) {
                        throw CancellationException()
                    }
                }
            }
            if (result.getCode() == TsNetConfig.instance.successCode) {
                result
            } else {
                throw ServerException(result.getCode(), result.getMessage())
            }
        } else {
            result
        }
    } catch (ex: Exception) {
        errorHandle(ex, customErrorHandle)
        if (isCancel) {
            throw CancellationException()
        }
        null
    }
}

//处理异常的方法
fun errorHandle(e: Throwable, customErrorHandle: ((code: Int, msg: String?) -> Unit)? = null) {
    val errorHandler = customErrorHandle ?: TsNetConfig.instance.netErrorHandle
    when (e) {
        is ServerException -> {
            errorHandler(e.code, e.message ?: "未知错误")
        }
        is HttpException -> {
            errorHandler(e.code(), e.message())
        }
        is UnknownHostException -> {
            errorHandler(400, "无法连接到服务器")
        }
        is SocketTimeoutException -> {
            errorHandler(400, "链接超时")
        }
        is ConnectException -> {
            errorHandler(500, "链接失败")
        }
        is SocketException -> {
            errorHandler(500, "服务器无响应")//链接关闭
        }
        is EOFException -> {
            errorHandler(500, "服务器无响应")//链接关闭
        }
        is IllegalArgumentException -> {
            errorHandler(400, "参数错误")
        }
        is SSLException -> {
            errorHandler(500, "证书错误")
        }
        is NullPointerException -> {
            errorHandler(500, "数据为空")
        }
        else -> {
            errorHandler(500, "未知错误:${e.message}")
        }
    }
}