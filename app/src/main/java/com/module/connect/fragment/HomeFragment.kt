package com.module.connect.fragment

import BluetoothHelper
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import cn.com.heaton.blelibrary.ble.Ble
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback
import cn.com.heaton.blelibrary.ble.model.BleDevice
import com.blankj.utilcode.util.ToastUtils
import com.module.connect.bean.BlueToothBean
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

    companion object {
        val deviceList = mutableListOf<BleDevice>()
        val devices = mutableListOf<BleDevice>()
    }

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
        PermissionComplianceManager.requestBlueToothPermissionHasTip(requireActivity(), object :
            PermissionComplianceManager.SimpleCallbackProxy() {

            override fun onGranted() {
                super.onGranted()
                BluetoothHelper.init(requireContext())
            }

            override fun onDenied() {
                super.onDenied()
                Toast.makeText(context,"请授予蓝牙权限", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        })

        binding.ll1.setOnClickListener {
            val uuid = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_UUID)
            val char = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_CHARACTERISTICS)
            Log.e("---", "uuid：$uuid")
            Log.e("---", "char：$char")
            BluetoothHelper.sendCommandAndWaitForResponse("AT+VERSION=?\r\n")
//            BluetoothLEUtil.sendCommandWithNotification(
//                BluetoothHelper.getCurrentGate()!!,
//                uuid,
//                char,
//                "AT+VERSION=?\r\n"
//            )
//            CommandUtil.readResponse(BluetoothHelper.getCurrentGate()!!, uuid, char) {
//                Log.e("---", "resp：$it")
//            }
        }

        binding.ll2.setOnClickListener {
//            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                CommandUtil.sendCommand(it, "AT+REBOOT")
//                ResultDialog.newInstance(childFragmentManager, "成功", "")
//            }
            val uuid = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_UUID)
            val char = KeyValueUtils.getString(IConsts.KEY_CURRENT_WRITE_CHARACTERISTICS)
            CommandUtil.sendCommand(ConnectUtil.CURRENT_GATE!!, uuid, char, "AT+REBOOT\r")
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
                    @SuppressLint("MissingPermission")
                    override fun onGranted() {
                        BlueToothListDialog.newInstance(childFragmentManager, devices)
                        Ble.getInstance<BleDevice>().startScan(object :
                            BleScanCallback<BleDevice>() {
                            override fun onLeScan(device: BleDevice, rssi: Int, scanRecord: ByteArray?) {
                                if (!deviceList.contains(device) && !TextUtils.isEmpty(device.bleName)) {
                                    deviceList.add(device)
                                    BlueToothListDialog.notify(device)
                                }
                            }
                        })
                    }
                })
        }

        binding.tvDisconnect.setOnClickListener {
            BluetoothHelper.disconnect()
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
        if (BluetoothHelper.isAnyDeviceConnected(requireContext())) {
            binding.tvStatus.text = "蓝牙已连接"
            binding.tvConnect.visibility = View.GONE
            binding.tvDisconnect.visibility = View.VISIBLE
//            ConnectUtil.getCurrentSocket()
//            if (bluetoothHelper.isAnyDeviceConnected(requireContext())) {
//                binding.tvLink.visibility = View.GONE
//            } else {
//                binding.tvLink.visibility = View.VISIBLE
//            }
        } else {
            binding.tvStatus.text = "蓝牙未连接"
            binding.tvConnect.visibility = View.VISIBLE
            binding.tvDisconnect.visibility = View.GONE
            binding.tvLink.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        BluetoothHelper.disconnect()
    }
}