@file:Suppress("unused")

package com.ts_xiaoa.ts_base.utils

import android.content.Context
import android.content.SharedPreferences
import com.ts_xiaoa.ts_base.TsBaseConfig
import com.ts_xiaoa.ts_base.provider.ApplicationProvider

/**
 * create by ts_xiaoA on 2020-08-03 09:42
 * email：443502578@qq.com
 * desc：SharedPreferences数据库操作工具类
 */
object SharePreferenceUtil {

    private val sharedPreferences: SharedPreferences by lazy {
        ApplicationProvider.application.getSharedPreferences(
            TsBaseConfig.instance.shareDbName,
            Context.MODE_PRIVATE
        )
    }

    fun put(key: String?, values: String?) {
        sharedPreferences.edit().putString(key, values).apply()
    }

    fun put(key: String?, values: Double) {
        sharedPreferences.edit().putFloat(key, values.toFloat()).apply()
    }

    fun put(key: String?, values: Float) {
        sharedPreferences.edit().putFloat(key, values).apply()
    }


    fun put(key: String?, values: Int) {
        sharedPreferences.edit().putInt(key, values).apply()
    }

    fun put(key: String?, values: Long) {
        sharedPreferences.edit().putLong(key, values).apply()
    }

    fun put(key: String?, values: Set<String?>?) {
        sharedPreferences.edit().putStringSet(key, values).apply()
    }


    fun getString(key: String?): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun getString(key: String?, def: String?): String {
        return sharedPreferences.getString(key, def) ?: ""
    }

    fun getInt(key: String?): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun getInt(key: String?, defValue: Int): Int {
        return sharedPreferences.getInt(key, defValue)
    }

    fun getBoolean(key: String?): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getBoolean(key: String?, defaultVal: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultVal)
    }

    fun getFloat(key: String?, def: Float): Float {
        return sharedPreferences.getFloat(key, def)
    }

    fun getFloat(key: String?): Float {
        return sharedPreferences.getFloat(key, 0f)
    }

    fun getLong(key: String?): Long {
        return sharedPreferences.getLong(key, 0L)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}