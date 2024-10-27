package com.module.connect.util

import android.widget.Toast
import com.blankj.utilcode.util.Utils

object ToastUtils {

    @JvmStatic
    fun show(str: String) {
        Toast.makeText(Utils.getApp(), str, Toast.LENGTH_SHORT).show()
    }
}