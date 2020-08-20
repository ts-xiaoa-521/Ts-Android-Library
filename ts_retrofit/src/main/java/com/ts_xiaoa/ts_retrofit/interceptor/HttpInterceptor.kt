package com.ts_xiaoa.ts_retrofit.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit


/**
 * create by ts_xiaoA on 2020-08-10 10:56
 * email：443502578@qq.com
 * desc：日志拦截器
 */
class HttpInterceptor : Interceptor {

    private val UTF8: Charset = Charset.forName("UTF-8")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        var body: String? = null

        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)!!
            }
            body = buffer.readString(charset)
        }

        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs =
            TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body
        var rBody: String? = null

        if (responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()
            var charset = UTF8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8)!!
                } catch (e: UnsupportedCharsetException) {
                    e.printStackTrace()
                }
            }
            rBody = buffer.clone().readString(charset)
        }
        Log.e("请求Url：", "${request.method}\t${request.url}")
        Log.e("请求参数：", "${request.headers}$body")
        Log.e("响应数据：", response.code.toString() + rBody)
        Log.e("请求耗时：", "${tookMs}ms")
        return response
    }
}