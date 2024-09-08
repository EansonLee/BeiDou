package com.module.connect.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import java.io.IOException
import java.util.UUID

object ConnectUtil {
    private const val GNSS_UUID: String = "00001101-0000-1000-8000-00805F9B34FB" // 通用串行设备服务 UUID

    //查询当前蓝牙是否已连接
    fun isBluetoothConnected(): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val connectedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices

        // 遍历所有已绑定的蓝牙设备，检查是否有已连接的设备
        connectedDevices?.forEach { device ->
            // 设备是否已连接
            val isConnected = device.bondState == BluetoothDevice.BOND_BONDED
            if (isConnected) {
                return true
            }
        }
        return false
    }

    //连接蓝牙设备
    fun connectToBluetoothDevice(deviceAddress: String): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)

        // 检查蓝牙设备是否存在
        if (device == null) {
            return false
        }

        // 尝试创建蓝牙连接
        try {
            val bluetoothSocket = device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
            bluetoothSocket.connect()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    //断开蓝牙设备连接
    fun disconnectBluetoothDevice(deviceAddress: String): Boolean {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)

        if (device == null) {
            return false
        }

        // 尝试断开连接
        try {
            val bluetoothSocket = device.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
            if (bluetoothSocket.isConnected) {
                bluetoothSocket.close()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //连接gnss设备
    fun connectToGnssDevice(deviceAddress: String): BluetoothSocket? {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        val device: BluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)

        if (device == null) {
            return null
        }

        try {
            val uuid = UUID.fromString(GNSS_UUID)
            val bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket.connect()
            return bluetoothSocket
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    //断开gnss设备
    fun disconnectGnssDevice(bluetoothSocket: BluetoothSocket?) {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun scanForBluetoothDevices(context: Context) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            // 蓝牙不可用或未启用
            return
        }

        // 创建广播接收器以接收发现的蓝牙设备
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action: String? = intent?.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    // 当发现一个设备时，获取设备对象
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        val deviceName = it.name
                        val deviceAddress = it.address // 获取设备的 MAC 地址
                        println("设备名称: $deviceName，设备地址: $deviceAddress")
                    }
                }
            }
        }

        // 注册广播接收器
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)

        // 开始扫描蓝牙设备
        bluetoothAdapter.startDiscovery()

        //停止扫描
//        bluetoothAdapter.cancelDiscovery()
    }

    //扫描蓝牙设备
    fun startBluetoothScan(context: Context) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            scanForBluetoothDevices(context)
        } else {
            println("蓝牙未启用或设备不支持蓝牙")
        }
    }

    //停止扫描
    fun stopBluetoothScan(bluetoothAdapter: BluetoothAdapter) {
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
    }
}