package com.module.connect.dialog

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.com.heaton.blelibrary.ble.Ble
import cn.com.heaton.blelibrary.ble.callback.BleConnectCallback
import cn.com.heaton.blelibrary.ble.callback.BleNotifyCallback
import cn.com.heaton.blelibrary.ble.model.BleDevice
import com.blankj.utilcode.util.Utils
import com.module.connect.adapter.BlueToothAdapter
import com.module.connect.consts.IConsts
import com.module.connect.databinding.DialogBlueToothListBinding
import com.module.connect.ext.makeArguments
import com.module.connect.ext.params
import com.module.connect.fragment.HomeFragment
import com.module.connect.util.BLEUtils
import com.module.connect.util.KeyValueUtils
import com.module.connect.util.LiveDataBus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class BlueToothListDialog : BaseFragmentDialog<DialogBlueToothListBinding>() {

    companion object {

        private var mBluetoothAdapter: BlueToothAdapter? = null


        fun newInstance(
            manager: FragmentManager,
            list: List<BleDevice>,
        ) {
            BlueToothListDialog().apply {
                makeArguments("key_list" to list)
                show(manager, "LinkLocationDialog")
            }
        }

        fun notify(device: BleDevice) {
            mBluetoothAdapter?.setData(device)
        }
    }

    private val list: List<BleDevice> by params("key_list") { mutableListOf() }

    private val byteArrayOutputStream = ByteArrayOutputStream()
    private var isFirstDataReceived = true
    private var lastNotifyTime = System.currentTimeMillis()

    override fun getBinding(
        inflate: LayoutInflater,
        container: ViewGroup?
    ): DialogBlueToothListBinding {
        return DialogBlueToothListBinding.inflate(inflate)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBluetoothAdapter = BlueToothAdapter {
            Ble.getInstance<BleDevice>().connect(it, object : BleConnectCallback<BleDevice>() {
                override fun onConnectionChanged(device: BleDevice) {
                    Log.e("------", device.toString())
                    if (device.isConnected) {
                        BLEUtils.isConnected = true
                        BLEUtils.bleName = device.bleName
                        BLEUtils.bleAddress = device.bleAddress
                        KeyValueUtils.setString(IConsts.KEY_CURRENT_ADDRESS, device.bleAddress)
                        KeyValueUtils.setString(IConsts.KEY_CURRENT_DEVICE, device.bleName)
                        Toast.makeText(Utils.getApp(), "蓝牙已连接", Toast.LENGTH_SHORT).show()
                        Ble.getInstance<BleDevice>().stopScan()
                    }
                }

                override fun onReady(device: BleDevice) {
                    super.onReady(device)
                    BLEUtils.CURRENT_DEVICE = device
                    Ble.getInstance<BleDevice>()
                        .enableNotify(device, true, object : BleNotifyCallback<BleDevice>() {
                            override fun onChanged(
                                device: BleDevice?,
                                characteristic: BluetoothGattCharacteristic
                            ) {
                                val uuid = characteristic.uuid
                                val newData = characteristic.value
//                                Log.e("-------", "onChanged==uuid:$uuid")
//
//                                // 如果还没有接收到第一条数据，直接保存第一条结果
//                                if (!isFirstDataReceived) {
//                                    byteArrayOutputStream.write(newData)
//                                    isFirstDataReceived = true
//                                } else {
//                                    byteArrayOutputStream.write(newData)
//
//                                    // 获取当前时间
//                                    val currentTime = System.currentTimeMillis()
//
//                                    // 检查是否到达一定的时间间隔，比如1000ms
//                                    if (currentTime - lastNotifyTime >= 1000) {
//                                        val finalResult =BLEUtils.byteArrayToAsciiString(byteArrayOutputStream.toByteArray())
//
//                                        // 清空 StringBuilder 为下一轮累积做准备
//                                        BLEUtils.byteArrayToAsciiString(byteArrayOutputStream.toByteArray())
//                                        lastNotifyTime = currentTime
//
//                                        Log.e("-------", "Final result: $finalResult")
//                                        LiveDataBus.postString(IConsts.KEY_COMMEND_RES, finalResult)
//                                    }
//                                }
                                byteArrayOutputStream.write(newData)
                                if(!isFirstDataReceived) {
                                    return
                                }
                                isFirstDataReceived = false
                                lifecycleScope.launch {
                                    delay(300)
                                    val finalResult =BLEUtils.byteArrayToAsciiString(byteArrayOutputStream.toByteArray())
                                    Log.e("-------", "Final result: $finalResult")
                                    LiveDataBus.postString(IConsts.KEY_COMMEND_RES, finalResult)
                                    byteArrayOutputStream.reset()
                                    isFirstDataReceived = true
                                }
                            }

                            override fun onNotifySuccess(device: BleDevice) {
                                super.onNotifySuccess(device)
                                Log.e("-------", "onNotifySuccess: " + device.bleName)
                            }
                        })
                }

                override fun onServicesDiscovered(device: BleDevice, gatt: BluetoothGatt) {
                    super.onServicesDiscovered(device, gatt)
                    Log.e("------", "发现服务")
                    val services = gatt.services
                    for (service in services) {
                        val serviceUUID = service.uuid
                        Log.d("BLE", "Service UUID: $serviceUUID")

                        // 获取并打印该服务的所有特征 UUID
                        for (characteristic in service.characteristics) {
                            val characteristicUUID = characteristic.uuid
                            Log.d("BLE", "Characteristic UUID: $characteristicUUID")
                        }
                    }
                }

                override fun onConnectCancel(device: BleDevice?) {
                    super.onConnectCancel(device)
                    Toast.makeText(Utils.getApp(), "取消连接", Toast.LENGTH_SHORT).show()
                }

                override fun onConnectFailed(device: BleDevice?, errorCode: Int) {
                    super.onConnectFailed(device, errorCode)
                    Toast.makeText(Utils.getApp(), "连接失败", Toast.LENGTH_SHORT).show()
                }
            })
            dismissAllowingStateLoss()
        }
        binding.rvBlueTooth.adapter = mBluetoothAdapter
        binding.rvBlueTooth.layoutManager = LinearLayoutManager(requireContext())
        mBluetoothAdapter?.setData(list)
    }

    override fun initData() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        HomeFragment.deviceList.clear()
        HomeFragment.devices.clear()
    }
}