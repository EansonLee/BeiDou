package com.module.connect.fragment

import android.app.ProgressDialog
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothProfile
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.module.connect.consts.IConsts
import com.module.connect.databinding.FragmentHomeBinding
import com.module.connect.dialog.BlueToothListDialog
import com.module.connect.dialog.ResultDialog
import com.module.connect.util.BluetoothLEUtil
import com.module.connect.util.CommandUtil
import com.module.connect.util.ConnectUtil
import com.module.connect.util.KeyValueUtils
import com.module.connect.util.PermissionComplianceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var scanningProgressDialog: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        binding.ll1.setOnClickListener {
            val uuid = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_UUID)
            val char = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_CHARACTERISTICS)
//            val uuid = "00112233-4455-6677-8899-aabbccddeeff"
//            val char = "00112433-4455-6677-8899-aabbccddeeff"

//            CommandUtil.sendCommandWithResponse(ConnectUtil.CURRENT_GATE!!, uuid, char, "AT+VERSION=?")
//
            BluetoothLEUtil.sendCommandWithNotification(
                ConnectUtil.CURRENT_GATE!!,
                uuid,
                char,
                "AT+VERSION=?"
            )
            CommandUtil.readResponse(ConnectUtil.CURRENT_GATE!!, uuid, char) {
                Log.e("---", "resp：$it")
            }
        }

        binding.ll2.setOnClickListener {
//            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                CommandUtil.sendCommand(it, "AT+REBOOT")
//                ResultDialog.newInstance(childFragmentManager, "成功", "")
//            }
            val uuid = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_UUID)
            val char = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_CHARACTERISTICS)
            CommandUtil.sendCommand(ConnectUtil.CURRENT_GATE!!, uuid, char, "AT+REBOOT")
        }

        binding.ll3.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                CommandUtil.sendCommand(it, "AT+CLEAR")
                ResultDialog.newInstance(childFragmentManager, "成功", "")
            }
        }


        binding.ll4.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                CommandUtil.sendCommand(it, "AT+SAVE")
                ResultDialog.newInstance(childFragmentManager, "成功", "")
            }
        }

        binding.ll5.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                val res = CommandUtil.sendCommandWithResponse(it, "AT+STATUS=?")
                if (TextUtils.isEmpty(res)) {
                    ResultDialog.newInstance(childFragmentManager, "", "")
                } else {
                    ResultDialog.newInstance(childFragmentManager, res!!, "")
                }
            }
        }

        binding.ll6.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                val res = CommandUtil.sendCommandWithResponse(it, "AT+STATE=?")
                if (TextUtils.isEmpty(res)) {
                    ResultDialog.newInstance(childFragmentManager, "", "")
                } else {
                    ResultDialog.newInstance(childFragmentManager, res!!, "")
                }
            }
        }

        binding.ll7.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                val res = CommandUtil.sendCommandWithResponse(it, "AT+MEMS=?")
                if (TextUtils.isEmpty(res)) {
                    ResultDialog.newInstance(childFragmentManager, "", "")
                } else {
                    ResultDialog.newInstance(childFragmentManager, res!!, "")
                }
            }
        }

        binding.ll8.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                val res = CommandUtil.sendCommandWithResponse(it, "AT+ICCID=?")
                if (TextUtils.isEmpty(res)) {
                    ResultDialog.newInstance(childFragmentManager, "", "")
                } else {
                    ResultDialog.newInstance(childFragmentManager, res!!, "")
                }
            }
        }

        binding.ll9.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                val res = CommandUtil.sendCommandWithResponse(it, "AT+CCLK=?")
                if (TextUtils.isEmpty(res)) {
                    ResultDialog.newInstance(childFragmentManager, "", "")
                } else {
                    ResultDialog.newInstance(childFragmentManager, res!!, "")
                }
            }
        }

        binding.ll10.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                val res = CommandUtil.sendCommandWithResponse(it, "AT+CSQ/4G=?")
                if (TextUtils.isEmpty(res)) {
                    ResultDialog.newInstance(childFragmentManager, "", "")
                } else {
                    ResultDialog.newInstance(childFragmentManager, res!!, "")
                }
            }
        }

        binding.ll11.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
                CommandUtil.sendCommand(it, "AT+RST/WIFI")
                ResultDialog.newInstance(childFragmentManager, "成功", "")
            }
        }


        binding.tvConnect.setOnClickListener {
            PermissionComplianceManager.requestFineLocationPermissionHasTip(
                requireActivity(),
                object : PermissionComplianceManager.SimpleCallbackProxy() {
                    override fun onGranted() {
                        scanningProgressDialog = ProgressDialog(requireContext()).apply {
                            setMessage("扫描中...")
                            setCancelable(false)
                            show()
                        }
                        ConnectUtil.scanForBluetoothDevices(requireContext()) { devices ->
                            scanningProgressDialog?.dismiss()
                            BlueToothListDialog.newInstance(childFragmentManager, devices)
                        }
                    }
                })
        }

        binding.tvDisconnect.setOnClickListener {
            ConnectUtil.unpairBluetoothDevice(ConnectUtil.CURRENT_DEVICE)
        }
        binding.tvLink.setOnClickListener {
/*            ConnectUtil.connectBLEDevice(requireContext(), ConnectUtil.getCurrentDevice()!!,object : BluetoothGattCallback() {
                override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
                    when (newState) {
                        BluetoothProfile.STATE_CONNECTED -> {
                            Log.d("BLEConnection", "设备已连接")
                            gatt.discoverServices()  // 开始发现服务
                            binding.tvLink.visibility = View.GONE
                        }
                        BluetoothProfile.STATE_DISCONNECTED -> {
                            Log.d("BLEConnection", "设备已断开连接")
                        }
                    }
                }

                override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        Log.d("BLEConnection", "服务发现成功")
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
                        Log.d("BLEConnection", "服务发现失败: $status")
                    }
                }
            })*/
            ConnectUtil.CURRENT_GATE = BluetoothLEUtil.connectToDevice(
                requireContext(),
                ConnectUtil.getCurrentDevice()!!,
                BluetoothLEUtil.gattCallback
            )
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            while (true) {
                delay(1000)
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    refreshPageBlueToothState()
                }
            }
        }
    }


    private fun refreshPageBlueToothState() {
        if (ConnectUtil.isBluetoothConnected()) {
            binding.tvStatus.text = "蓝牙已配对"
            binding.tvConnect.visibility = View.GONE
            binding.tvDisconnect.visibility = View.VISIBLE
            ConnectUtil.getCurrentSocket()
            if (ConnectUtil.isBluetoothLinked()) {
                binding.tvLink.visibility = View.GONE
            } else {
                binding.tvLink.visibility = View.VISIBLE
            }
        } else {
            binding.tvStatus.text = "蓝牙未配对"
            binding.tvConnect.visibility = View.VISIBLE
            binding.tvDisconnect.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}