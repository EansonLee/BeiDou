package com.module.connect.ext

import android.os.Bundle
import android.os.Parcelable

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2022/1/10
 * @desc: Bundle 的一些扩展工具类
 */

fun Bundle.makeParams(it: Pair<String, Any>) {
    when (val value = it.second) {
        is Int -> putInt(it.first, value)
        is Long -> putLong(it.first, value)
        is CharSequence -> putCharSequence(it.first, value)
        is String -> putString(it.first, value)
        is Float -> putFloat(it.first, value)
        is Double -> putDouble(it.first, value)
        is Char -> putChar(it.first, value)
        is Short -> putShort(it.first, value)
        is Boolean -> putBoolean(it.first, value)
        is java.io.Serializable -> putSerializable(it.first, value)
        is Bundle -> putBundle(it.first, value)
        is Parcelable -> putParcelable(it.first, value)
        is IntArray -> putIntArray(it.first, value)
        is LongArray -> putLongArray(it.first, value)
        is FloatArray -> putFloatArray(it.first, value)
        is DoubleArray -> putDoubleArray(it.first, value)
        is CharArray -> putCharArray(it.first, value)
        is ShortArray -> putShortArray(it.first, value)
        is BooleanArray -> putBooleanArray(it.first, value)
        else -> throw IllegalArgumentException("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
    }
}