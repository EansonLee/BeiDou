package com.module.connect.util

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.module.connect.bean.BlueToothBean
import java.io.IOException

object ConnectUtil {
    private const val GNSS_UUID: String = "00001101-0000-1000-8000-00805F9B34FB" // 通用串行设备服务 UUID

    var CURRENT_ADDRESS = ""

    var CURRENT_BLUE_SOCKET: BluetoothSocket? = null

    var CURRENT_DEVICE: BluetoothDevice? = null
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
//            val uuid = UUID.fromString(GNSS_UUID)
            val uuid = device.uuids[0].uuid
            val bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            bluetoothSocket.connect()
            return bluetoothSocket
        } catch (e: IOException) {
            e.printStackTrace()
            LogUtils.e(e.message)
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

    fun disconnectGnssDevice(device: BluetoothDevice?) {
        // 尝试断开连接
        try {
            val bluetoothSocket = device?.createRfcommSocketToServiceRecord(device.uuids[0].uuid)
            if (bluetoothSocket != null) {
                if (bluetoothSocket.isConnected) {
                    bluetoothSocket.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun scanForBluetoothDevices(context: Context, onDevicesFound: (List<BlueToothBean>) -> Unit) {
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            // 蓝牙不可用或未启用
            onDevicesFound(emptyList())
            return
        }
        val foundDevices = mutableListOf<BlueToothBean>()
        // 创建广播接收器以接收发现的蓝牙设备
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action: String? = intent?.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    // 获取发现的蓝牙设备
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        val deviceInfo = BlueToothBean(name = it.name, address = it.address, it)
                        foundDevices.add(deviceInfo)
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                    // 当设备扫描完成时，停止扫描并返回设备列表
                    context?.unregisterReceiver(this)
                    bluetoothAdapter.cancelDiscovery()
                    onDevicesFound(foundDevices)
                }
            }
        }

        // 注册广播接收器
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        context.registerReceiver(receiver, filter)

        // 开始蓝牙设备扫描
        bluetoothAdapter.startDiscovery()
    }

    //停止扫描
    fun stopBluetoothScan(bluetoothAdapter: BluetoothAdapter) {
        if (bluetoothAdapter.isDiscovering) {
            bluetoothAdapter.cancelDiscovery()
        }
    }

    fun pairBluetoothDevice(context: Context, device: BluetoothDevice) {
        try {
            if (device.bondState == BluetoothDevice.BOND_NONE) {
                // 尝试与设备配对
                val method = device.javaClass.getMethod("createBond")
                method.invoke(device)

                // 注册广播监听器以跟踪配对状态
                val bondReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        val action = intent?.action
                        if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                            val state = intent.getIntExtra(
                                BluetoothDevice.EXTRA_BOND_STATE,
                                BluetoothDevice.BOND_NONE
                            )
                            if (state == BluetoothDevice.BOND_BONDED) {
                                Log.d("BluetoothPair", "设备配对成功: ${device.name}")
                                // 可以在此继续执行连接等其他操作
                                CURRENT_BLUE_SOCKET =  connectBluetoothDevice(device)

                            } else if (state == BluetoothDevice.BOND_NONE) {
                                Log.d("BluetoothPair", "设备配对失败: ${device.name}")
                            }
                            context?.unregisterReceiver(this)
                        }
                    }
                }
                val filter = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
                context.registerReceiver(bondReceiver, filter)
            } else {
                Log.e("BluetoothPair", "蓝牙已配对")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BluetoothPair", "蓝牙配对发生错误")
        }
    }

    fun connectBluetoothDevice(device: BluetoothDevice): BluetoothSocket? {
        var bluetoothSocket: BluetoothSocket? = null
        try {
            // 确保设备已配对
            if (device.bondState == BluetoothDevice.BOND_BONDED) {
                val uuid = device.uuids[0].uuid  // 获取设备的UUID
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)

                // 取消蓝牙适配器的设备扫描，避免影响连接速度
                val bluetoothAdapter = device.bluetoothClass
                bluetoothSocket.connect()
                Log.d("BluetoothConnect", "设备连接成功: ${device.name}")
            } else {
                Log.d("BluetoothConnect", "设备未配对，无法连接")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("BluetoothConnect", "连接失败: ${e.message}")
            try {
                bluetoothSocket?.close()
            } catch (closeException: IOException) {
                closeException.printStackTrace()
            }
        }
        return bluetoothSocket
    }

    fun unpairBluetoothDevice(device: BluetoothDevice): Boolean {
        return try {
            // 使用反射获取 removeBond 方法
            val method = device.javaClass.getMethod("removeBond")
            method.invoke(device)
            Log.d("BluetoothUnpair", "设备解除配对请求已发送: ${device.name}")
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("BluetoothUnpair", "解除配对时发生错误: ${e.message}")
            false
        }
    }
}