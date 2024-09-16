package com.module.connect.util

import android.bluetooth.*
import android.content.Context
import android.util.Log
import com.module.connect.consts.IConsts
import java.util.*

object BluetoothLEUtil {
    private const val TAG = "BluetoothLEUtil"

    // 1. 连接设备并获取 BluetoothGatt 对象
    fun connectToDevice(context: Context, device: BluetoothDevice, gattCallback: BluetoothGattCallback): BluetoothGatt? {
        return device.connectGatt(context, false, gattCallback)
    }

    // 2. 发送指令并启用特性通知
    fun sendCommandWithNotification(
        gatt: BluetoothGatt,
        serviceUUID: String,
        characteristicUUID: String,
        command: String
    ): Boolean {
        val service = gatt.getService(UUID.fromString(serviceUUID))
        val characteristic = service?.getCharacteristic(UUID.fromString(characteristicUUID))

        return if (characteristic != null) {
            characteristic.value = "$command\r\n".toByteArray()
            val success = gatt.writeCharacteristic(characteristic)
            Log.d(TAG, "发送指令: $command, 发送结果: $success")
            // 启用通知接收响应
            enableCharacteristicNotification(gatt, serviceUUID, characteristicUUID)
            success
        } else {
            Log.e(TAG, "找不到指定的特性: $characteristicUUID")
            false
        }
    }

    // 3. 启用特性通知
    fun enableCharacteristicNotification(
        gatt: BluetoothGatt,
        serviceUUID: String,
        characteristicUUID: String
    ): Boolean {
        val service = gatt.getService(UUID.fromString(serviceUUID))
        val characteristic = service?.getCharacteristic(UUID.fromString(characteristicUUID))

        return if (characteristic != null) {
            gatt.setCharacteristicNotification(characteristic, true)
            
            val descriptorUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
            val descriptor = characteristic.getDescriptor(descriptorUUID)
            descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            gatt.writeDescriptor(descriptor)
            true
        } else {
            Log.e(TAG, "找不到指定的特性: $characteristicUUID")
            false
        }
    }

    // 4. 处理设备回复，通过 BluetoothGattCallback 的 onCharacteristicChanged
    val gattCallback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.d(TAG, "连接成功，开始服务发现")
                gatt.discoverServices() // 连接成功后开始发现服务
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "连接断开")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "服务发现成功，可以开始通信")
                val services = gatt.services
                for (service in services) {
                    Log.d("BluetoothGatt", "发现服务: ${service.uuid}")
                    KeyValueUtils.setString(IConsts.KEY_CURRENT_WRITE_UUID, service.uuid.toString())
                    // 获取服务中的所有特性
                    val characteristics = service.characteristics
                    for (characteristic in characteristics) {
                        val characteristicUUID = characteristic.uuid
                        Log.d("BluetoothGatt", "发现特性: $characteristicUUID")
                        KeyValueUtils.setString(IConsts.KEY_CURRENT_WRITE_CHARACTERISTICS, characteristicUUID.toString())


                        // 检查该特性是否支持写入
                        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) {
                            Log.d("BluetoothGatt", "特性支持写入: $characteristicUUID")
                        }

                        // 检查该特性是否支持读取
                        if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
                            Log.d("BluetoothGatt", "特性支持读取: $characteristicUUID")
                        }
                    }
                }
            } else {
                Log.e(TAG, "服务发现失败，状态码: $status")
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            // 当设备的特性发生变化时接收通知
            val response = characteristic.value?.toString(Charsets.UTF_8)?.trim()
            Log.d(TAG, "收到设备回复: $response")
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "指令发送成功")
            } else {
                Log.e(TAG, "指令发送失败，状态码: $status")
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val response = characteristic?.value?.toString(Charsets.UTF_8)?.trim()
                Log.d(TAG, "读取到的设备回复: $response")
            } else {
                Log.e(TAG, "读取设备特性失败，状态码: $status")
            }
        }
    }
}
