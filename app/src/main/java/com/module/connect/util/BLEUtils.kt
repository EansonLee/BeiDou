package com.module.connect.util

import android.bluetooth.BluetoothGattCharacteristic
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import cn.com.heaton.blelibrary.ble.Ble
import cn.com.heaton.blelibrary.ble.callback.BleReadCallback
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback
import cn.com.heaton.blelibrary.ble.model.BleDevice
import com.blankj.utilcode.util.Utils
import com.module.connect.consts.IConsts

object BLEUtils {

    var CURRENT_DEVICE: BleDevice? = null
    var manager: FragmentManager? = null
    var isConnected = false
    var bleAddress = ""
    var bleName = ""
    var isSuccess = false
    var currentCommand = ""

    @JvmStatic
    fun sendCommand(command: String, callBack:()->Unit) {
        if (!isConnected || CURRENT_DEVICE == null) {
            ToastUtils.show("未连接，请先连接设备")
            return
        }
        currentCommand = command
        CURRENT_DEVICE?.let {
            Ble.getInstance<BleDevice>().write(it, stringToByteArray(command), object :
                BleWriteCallback<BleDevice>() {
                override fun onWriteSuccess(
                    device: BleDevice?,
                    characteristic: BluetoothGattCharacteristic?
                ) {
                    isSuccess = true
                    callBack()
                }

                override fun onWriteFailed(device: BleDevice?, failedCode: Int) {
                    super.onWriteFailed(device, failedCode)
                    Toast.makeText(Utils.getApp(), "发送失败，请重试", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    @JvmStatic
    fun byteArrayToAsciiString(byteArray: ByteArray): String {
        return byteArray.toString(Charsets.US_ASCII)
    }

    private fun stringToByteArray(input: String, charset: String = "UTF-8"): ByteArray {
        return input.toByteArray(charset(charset))
    }

}