package com.module.connect.util

import android.bluetooth.BluetoothSocket
import com.blankj.utilcode.util.ToastUtils
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object CommandUtil {

    //发送AT+VERSION=?指令
    fun sendVersionCommand(bluetoothSocket: BluetoothSocket): String? {
        if (ConnectUtil.isBluetoothConnected().not()) {
            ToastUtils.showShort("当前设备未连接")
            return null
        }

        val outputStream: OutputStream?
        val inputStream: InputStream?

        try {
            outputStream = bluetoothSocket.outputStream
            inputStream = bluetoothSocket.inputStream
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        // 发送 AT+VERSION=? 指令
        try {
            val command = "AT+VERSION=?\r\n" // GNSS设备一般需要以 \r\n 作为结束符
            outputStream.write(command.toByteArray())
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        // 读取设备的回复
        return try {
            val buffer = ByteArray(1024)
            val bytesRead = inputStream.read(buffer)
            String(buffer, 0, bytesRead).trim() // 将接收到的字节转换为字符串
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}