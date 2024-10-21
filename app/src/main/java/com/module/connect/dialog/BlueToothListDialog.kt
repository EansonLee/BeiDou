package com.module.connect.dialog

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.com.heaton.blelibrary.ble.Ble
import cn.com.heaton.blelibrary.ble.BleLog
import cn.com.heaton.blelibrary.ble.callback.BleConnectCallback
import cn.com.heaton.blelibrary.ble.callback.BleNotifyCallback
import cn.com.heaton.blelibrary.ble.model.BleDevice
import cn.com.heaton.blelibrary.ble.utils.ByteUtils
import com.module.connect.adapter.BlueToothAdapter
import com.module.connect.databinding.DialogBlueToothListBinding
import com.module.connect.ext.makeArguments
import com.module.connect.ext.params
import com.module.connect.fragment.HomeFragment


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


    override fun getBinding(
        inflate: LayoutInflater,
        container: ViewGroup?
    ): DialogBlueToothListBinding {
        return DialogBlueToothListBinding.inflate(inflate)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBluetoothAdapter = BlueToothAdapter {
//            connectingProgressDialog = ProgressDialog(requireContext()).apply {
//                setMessage("连接中...")
//                setCancelable(false)
//                show()
//            }
//            ConnectUtil.CURRENT_ADDRESS = it.address
//            ConnectUtil.CURRENT_DEVICE = it.device
//            KeyValueUtils.setString(IConsts.KEY_CURRENT_ADDRESS, it.address)
//            ConnectUtil.CURRENT_BLUE_SOCKET = ConnectUtil.connectToGnssDevice(it.address)
//            ConnectUtil.pairBluetoothDevice(requireContext(), it.device)
//            if (BluetoothHelper.isDevicePaired(it.device)) {
//                Toast.makeText(context,"此设备已配对", Toast.LENGTH_SHORT).show()
//            } else {
//                BluetoothHelper.connectDevice(requireContext(), it.device, {
//                    Toast.makeText(context,"连接成功", Toast.LENGTH_SHORT).show()
//                    Log.e("---", "已连接")
//                }, {
//                    Toast.makeText(context,"连接失败", Toast.LENGTH_SHORT).show()
//                    Log.e("---", "连接失败")
//                })
//            }
            Ble.getInstance<BleDevice>().connect(it, object : BleConnectCallback<BleDevice>() {
                override fun onConnectionChanged(device: BleDevice?) {
                    Toast.makeText(requireContext(), "蓝牙已连接", Toast.LENGTH_SHORT).show()
                }

                override fun onReady(device: BleDevice) {
                    super.onReady(device)
                    Ble.getInstance<BleDevice>().enableNotify(device, true, object : BleNotifyCallback<BleDevice>(){
                        override fun onChanged(
                            device: BleDevice?,
                            characteristic: BluetoothGattCharacteristic
                        ) {
                            val uuid = characteristic.uuid
                            BleLog.e("-------", "onChanged==uuid:$uuid")
                            BleLog.e(
                                "-------",
                                "onChanged==data:" + ByteUtils.toHexString(characteristic.value)
                            )
                        }

                        override fun onNotifySuccess(device: BleDevice) {
                            super.onNotifySuccess(device)
                            BleLog.e("-------", "onNotifySuccess: "+ device.bleName)
                        }
                    })
                }

                override fun onServicesDiscovered(device: BleDevice?, gatt: BluetoothGatt?) {
                    super.onServicesDiscovered(device, gatt)
                    Toast.makeText(requireContext(), "发现服务", Toast.LENGTH_SHORT).show()
                }

                override fun onConnectCancel(device: BleDevice?) {
                    super.onConnectCancel(device)
                    Toast.makeText(requireContext(), "取消连接", Toast.LENGTH_SHORT).show()
                }

                override fun onConnectFailed(device: BleDevice?, errorCode: Int) {
                    super.onConnectFailed(device, errorCode)
                    Toast.makeText(requireContext(), "连接失败", Toast.LENGTH_SHORT).show()
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