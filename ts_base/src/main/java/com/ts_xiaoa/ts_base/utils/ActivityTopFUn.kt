package com.ts_xiaoa.ts_base.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import java.io.Serializable
import java.lang.IllegalArgumentException

/**
 * create by ts_xiaoA on 2020-07-22 13:44
 * email：443502578@qq.com
 * desc：activity相关方法
 */
/**
 * 简单启动Activity
 */
inline fun <reified C : Activity> Activity.startActivityOrForResult(
    vararg params: Pair<String, Any?>,
    requestCode: Int = -1
) {
    val intent = Intent(this, C::class.java)
    params.forEach {
        when (val value = it.second) {
            null -> intent.putExtra(it.first, null as Serializable?)
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
    if (requestCode == -1) {
        this.startActivity(intent)
    } else {
        this.startActivityForResult(intent, requestCode)
    }
}

/**
 * 简单启动Activity
 */
inline fun <reified C : Activity> Fragment.startActivityOrForResult(
    vararg params: Pair<String, Any?>,
    requestCode: Int = -1
) {
    val intent = Intent(context, C::class.java)
    params.forEach {
        when (val value = it.second) {
            null -> intent.putExtra(it.first, null as Serializable?)
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
    if (requestCode == -1) {
        this.startActivity(intent)
    } else {
        this.startActivityForResult(intent, requestCode)
    }
}

//简单启动Activity
inline fun <reified C : Activity> Context.startActivityOrForResult(
    vararg params: Pair<String, Any?>, requestCode: Int = -1
) {
    val intent = Intent(this, C::class.java)
    params.forEach {
        when (val value = it.second) {
            null -> intent.putExtra(it.first, null as Serializable?)
            is Int -> intent.putExtra(it.first, value)
            is Long -> intent.putExtra(it.first, value)
            is CharSequence -> intent.putExtra(it.first, value)
            is String -> intent.putExtra(it.first, value)
            is Float -> intent.putExtra(it.first, value)
            is Double -> intent.putExtra(it.first, value)
            is Char -> intent.putExtra(it.first, value)
            is Short -> intent.putExtra(it.first, value)
            is Boolean -> intent.putExtra(it.first, value)
            is Serializable -> intent.putExtra(it.first, value)
            is Bundle -> intent.putExtra(it.first, value)
            is Parcelable -> intent.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
                value.isArrayOf<String>() -> intent.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
                else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(it.first, value)
            is LongArray -> intent.putExtra(it.first, value)
            is FloatArray -> intent.putExtra(it.first, value)
            is DoubleArray -> intent.putExtra(it.first, value)
            is CharArray -> intent.putExtra(it.first, value)
            is ShortArray -> intent.putExtra(it.first, value)
            is BooleanArray -> intent.putExtra(it.first, value)
            else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
    if (this is Activity) {
        this.startActivityForResult(intent, requestCode)
    } else {
        this.startActivity(intent)
    }
}

//获取本地视频封面图 本地视频id
fun Context.getThumbnailPath(fileId: Long): String? {
    var thumbCursor: Cursor? = null
    try {
        thumbCursor = this.contentResolver.query(
            MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Video.Thumbnails.DATA), MediaStore.Video.Thumbnails.VIDEO_ID + " = "
                    + fileId, null, null
        )
        thumbCursor?.let {
            if (thumbCursor.moveToFirst()) {
                return thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA))
            }
        }
    } finally {
        thumbCursor?.close()
    }
    return null
}


