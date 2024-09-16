package com.module.connect.util

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothSocket
import android.util.Log
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

    // 发送不需要回复的指令
    fun sendCommand(socket: BluetoothSocket, command: String) {
        try {
            val outputStream = socket.outputStream
            outputStream.write("$command\r\n".toByteArray())  // 发送指令加上回车换行
            outputStream.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 发送需要回复的指令并返回响应
    fun sendCommandWithResponse(socket: BluetoothSocket, command: String): String? {
        return try {
            val outputStream = socket.outputStream
            val inputStream = socket.inputStream

            // 发送指令
            outputStream.write("$command\r\n".toByteArray())
            outputStream.flush()

            // 接收设备回复
            val buffer = ByteArray(1024)
            val bytes = inputStream.read(buffer)
            String(buffer, 0, bytes).trim() // 去掉多余的空格或换行符
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    private const val TAG = "BLECommandUtil"

    /**
     * 发送不需要回复的命令
     */
    fun sendCommand(gatt: BluetoothGatt, serviceUUID: String, characteristicUUID: String, command: String) {
        val service = gatt.getService(java.util.UUID.fromString(serviceUUID))
        val characteristic = service?.getCharacteristic(java.util.UUID.fromString(characteristicUUID))

        if (characteristic != null) {
            characteristic.value = "$command\r\n".toByteArray() // 发送命令
            val success = gatt.writeCharacteristic(characteristic)
            Log.d(TAG, "发送指令: $command, 发送结果: $success")
        } else {
            Log.e(TAG, "找不到指定的特性: $characteristicUUID")
        }
    }

    /**
     * 发送需要回复的命令并读取响应
     */
    fun sendCommandWithResponse(
        gatt: BluetoothGatt,
        serviceUUID: String,
        characteristicUUID: String,
        command: String
    ): Boolean {
        val service = gatt.getService(java.util.UUID.fromString(serviceUUID))
        val characteristic = service?.getCharacteristic(java.util.UUID.fromString(characteristicUUID))

        if (characteristic != null) {
            characteristic.value = "$command\r\n".toByteArray()
            val success = gatt.writeCharacteristic(characteristic)
            Log.d(TAG, "发送指令: $command, 发送结果: $success")
            return success
        } else {
            Log.e(TAG, "找不到指定的特性: $characteristicUUID")
            return false
        }
    }

    /**
     * 读取设备的回复
     */
    fun readResponse(
        gatt: BluetoothGatt,
        serviceUUID: String,
        characteristicUUID: String,
        onResponse: (response: String?) -> Unit
    ) {
        val service = gatt.getService(java.util.UUID.fromString(serviceUUID))
        val characteristic = service?.getCharacteristic(java.util.UUID.fromString(characteristicUUID))

        if (characteristic != null) {
            gatt.readCharacteristic(characteristic)
            val response = characteristic.value?.toString(Charsets.UTF_8)?.trim()
            onResponse(response)
            Log.d(TAG, "收到回复: $response")
        } else {
            Log.e(TAG, "找不到指定的特性: $characteristicUUID")
            onResponse(null)
        }
    }
}