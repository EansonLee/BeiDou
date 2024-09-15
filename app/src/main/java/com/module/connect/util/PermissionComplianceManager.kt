package com.module.connect.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.PermissionUtils

object PermissionComplianceManager {

    val PERMISSION_STORAGE_GROUP = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val PERMISSION_PHONE = arrayOf(
        Manifest.permission.READ_PHONE_STATE
    )

    val PERMISSION_FINE_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun requestStoragePermission(
        context: Context,
        callback: SimpleCallbackProxy
    ) {
        if (allPermissionsGranted(context, PERMISSION_STORAGE_GROUP)) {
            callback.onGranted()
        } else {
            PermissionUtils.permission(*PERMISSION_STORAGE_GROUP)
                .callback(callback)
                .request()
        }
    }


    fun requestStoragePermissionHasTip(
        context: FragmentActivity,
        callback: SimpleCallbackProxy
    ) {
        if (allPermissionsGranted(context, PERMISSION_STORAGE_GROUP)) {
            callback.onGranted()
        } else {
            PermissionUtils.permission(*PERMISSION_STORAGE_GROUP)
                .callback(callback)
                .request()

        }
    }

    fun requestFineLocationPermissionHasTip(
        context: FragmentActivity,
        callback: SimpleCallbackProxy
    ) {
        if (allPermissionsGranted(context, PERMISSION_FINE_LOCATION)) {
            callback.onGranted()
        } else {
            PermissionUtils.permission(*PERMISSION_FINE_LOCATION)
                .callback(callback)
                .request()

        }
    }

    /**
     * 判断权限是否已申请
     */
    fun allPermissionsGranted(context: Context, permissions: Array<String>) =
        permissions.all {
            ContextCompat.checkSelfPermission(
                context, it
            ) == PackageManager.PERMISSION_GRANTED
        }

    abstract class SimpleCallbackProxy : PermissionUtils.SimpleCallback {
        override fun onGranted() {
        }

        override fun onDenied() {

        }
    }
}