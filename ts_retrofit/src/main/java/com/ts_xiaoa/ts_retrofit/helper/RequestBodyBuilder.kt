package com.ts_xiaoa.ts_retrofit.helper

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * create by ts_xiaoA on 2019-12-23 11:24
 * email：443502578@qq.com
 * desc：requestBody Builder 简单生成RequestBody对象
 */
class RequestBodyBuilder private constructor() {

    private var jsonType = 0
    private var jsonElement: JsonElement? = null

    fun add(property: String?, value: String?): RequestBodyBuilder {
        if (value != null)
            if (value.isNotEmpty()) jsonObject!!.addProperty(property, value)
        return this
    }

    fun add(property: String?, value: CharSequence?): RequestBodyBuilder {
        return add(property, value.toString())
    }

    fun addNullable(property: String?, value: String?): RequestBodyBuilder {
        jsonObject!!.addProperty(property, value)
        return this
    }

    fun addNullable(property: String?, value: CharSequence?): RequestBodyBuilder {
        return addNullable(property, value.toString())
    }

    fun add(property: String?, value: Number?): RequestBodyBuilder {
        if (value != null) jsonObject!!.addProperty(property, value)
        return this
    }

    fun add(property: String?, value: Boolean?): RequestBodyBuilder {
        if (value != null) jsonObject!!.addProperty(property, value)
        return this
    }

    fun add(property: String?, value: Char?): RequestBodyBuilder {
        if (value != null) jsonObject!!.addProperty(property, value)
        return this
    }

    fun add(property: String?, value: JsonElement?): RequestBodyBuilder {
        if (value != null) jsonObject!!.add(property, value)
        return this
    }

    fun add(property: String?, vararg values: String?): RequestBodyBuilder {
        if (values.isNotEmpty()) {
            val jsonArray = JsonArray()
            for (value in values) {
                jsonArray.add(value)
            }
            add(property, jsonArray)
        }
        return this
    }

    fun add(
        property: String?,
        values: List<String?>
    ): RequestBodyBuilder {
        if (values.size > 0) {
            val jsonArray = JsonArray()
            for (value in values) {
                jsonArray.add(value)
            }
            add(property, jsonArray)
        }
        return this
    }

    fun add(value: String?): RequestBodyBuilder {
        if (value != null) jsonArray!!.add(value)
        return this
    }

    fun add(value: Number?): RequestBodyBuilder {
        if (value != null) jsonArray!!.add(value)
        return this
    }

    fun add(value: Boolean?): RequestBodyBuilder {
        if (value != null) jsonArray!!.add(value)
        return this
    }

    fun add(value: Char?): RequestBodyBuilder {
        if (value != null) jsonArray!!.add(value)
        return this
    }

    fun add(value: JsonElement?): RequestBodyBuilder {
        if (value != null) jsonArray!!.add(value)
        return this
    }

    fun add(value: JsonArray?): RequestBodyBuilder {
        if (value != null) jsonArray!!.addAll(value)
        return this
    }

    private val jsonObject: JsonObject?
        get() = jsonElement as JsonObject?

    private val jsonArray: JsonArray?
        get() {
            require(jsonElement !is JsonObject) { "if you want to json array ,please use RequestBodyBuilder.create(RequestBodyBuilder.JSON_TYPE_ARRAY)" }
            return jsonElement as JsonArray?
        }

    fun build(): RequestBody {
        return jsonElement.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

    companion object {
        const val JSON_TYPE_OBJECT = 1
        const val JSON_TYPE_ARRAY = 2
        var requestBodyBuilder: RequestBodyBuilder? = null

        @JvmOverloads
        fun create(jsonType: Int = JSON_TYPE_OBJECT): RequestBodyBuilder {
            if (requestBodyBuilder == null) {
                requestBodyBuilder =
                    RequestBodyBuilder()
            }
            requestBodyBuilder!!.jsonElement = null
            requestBodyBuilder!!.jsonType = jsonType
            if (jsonType == JSON_TYPE_OBJECT) {
                requestBodyBuilder!!.jsonElement = JsonObject()
            } else {
                requestBodyBuilder!!.jsonElement = JsonArray()
            }
            return requestBodyBuilder!!
        }
    }
}