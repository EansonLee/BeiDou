package com.module.connect.ext

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <reified T : Any> Fragment.params(
    key: String
) = lazy(LazyThreadSafetyMode.NONE) {
    arguments?.get(key) as T
}

inline fun <reified T : Any> Fragment.params(
    key: String,
    crossinline defaultValue: () -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    val value = arguments?.get(key)
    if (value is T) value else defaultValue()
}

fun Fragment.makeArguments(
    vararg params: Pair<String, Any>
): Fragment = apply {
    makeArguments {
        params
    }
}

inline fun Fragment.makeArguments(
    params: () -> Array<out Pair<String, Any>>
): Fragment = apply {
    arguments = Bundle().apply {
        for ((_, value) in params().withIndex()) {
            makeParams(value)
        }
    }
}

