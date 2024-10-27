package com.module.connect.fragment

import BluetoothHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
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
import com.module.connect.consts.IConsts
import com.module.connect.databinding.FragmentHomeBinding
import com.module.connect.dialog.BlueToothListDialog
import com.module.connect.dialog.ResultDialog
import com.module.connect.util.BLEUtils
import com.module.connect.util.ConnectUtil
import com.module.connect.util.KeyValueUtils
import com.module.connect.util.LiveDataBus
import com.module.connect.util.PermissionComplianceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.security.auth.callback.Callback

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
        if (!Ble.getInstance<BleDevice>().isBleEnable && Ble.getInstance<BleDevice>()
                .isSupportBle(requireContext())
        ) {
            Ble.getInstance<BleDevice>().turnOnBlueTooth(requireActivity())
        }
        initView()
        initData()
    }

    private fun initView() {
        BLEUtils.manager = childFragmentManager
        PermissionComplianceManager.requestBlueToothPermissionHasTip(requireActivity(), object :
            PermissionComplianceManager.SimpleCallbackProxy() {
            override fun onDenied() {
                super.onDenied()
                Toast.makeText(context, "请授予蓝牙权限", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        })

        binding.ll1.setOnClickListener {
            BLEUtils.sendCommand("AT+VERSION=?\r\n") {
            }
        }

        binding.ll2.setOnClickListener {
            BLEUtils.sendCommand("AT+REBOOT=?\r\n") {
                ResultDialog.newInstance(childFragmentManager, "发送成功", "")
                BLEUtils.isConnected = false
                BLEUtils.isSuccess = false
            }
        }

        binding.ll3.setOnClickListener {
            BLEUtils.sendCommand("AT+CLEAR\r\n") {
                BLEUtils.isConnected = false
                BLEUtils.isSuccess = false
            }
        }


        binding.ll4.setOnClickListener {
            BLEUtils.sendCommand("AT+SAVE\r\n") {
                BLEUtils.isConnected = false
                BLEUtils.isSuccess = false
            }
        }

        binding.ll5.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                val res = CommandUtil.sendCommandWithResponse(it, "AT+STATUS=?")
//                if (TextUtils.isEmpty(res)) {
//                    ResultDialog.newInstance(childFragmentManager, "", "")
//                } else {
//                    ResultDialog.newInstance(childFragmentManager, res!!, "")
//                }
            }
        }

        binding.ll6.setOnClickListener {
            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                val res = CommandUtil.sendCommandWithResponse(it, "AT+STATE=?")
//                if (TextUtils.isEmpty(res)) {
//                    ResultDialog.newInstance(childFragmentManager, "", "")
//                } else {
//                    ResultDialog.newInstance(childFragmentManager, res!!, "")
//                }
            }
        }

        binding.ll7.setOnClickListener {
//            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                val res = CommandUtil.sendCommandWithResponse(it, "AT+MEMS=?")
//                if (TextUtils.isEmpty(res)) {
//                    ResultDialog.newInstance(childFragmentManager, "", "")
//                } else {
//                    ResultDialog.newInstance(childFragmentManager, res!!, "")
//                }
//            }
        }

        binding.ll8.setOnClickListener {
//            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                val res = CommandUtil.sendCommandWithResponse(it, "AT+ICCID=?")
//                if (TextUtils.isEmpty(res)) {
//                    ResultDialog.newInstance(childFragmentManager, "", "")
//                } else {
//                    ResultDialog.newInstance(childFragmentManager, res!!, "")
//                }
//            }
        }

        binding.ll9.setOnClickListener {
//            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                val res = CommandUtil.sendCommandWithResponse(it, "AT+CCLK=?")
//                if (TextUtils.isEmpty(res)) {
//                    ResultDialog.newInstance(childFragmentManager, "", "")
//                } else {
//                    ResultDialog.newInstance(childFragmentManager, res!!, "")
//                }
//            }
        }

        binding.ll10.setOnClickListener {
//            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                val res = CommandUtil.sendCommandWithResponse(it, "AT+CSQ/4G=?")
//                if (TextUtils.isEmpty(res)) {
//                    ResultDialog.newInstance(childFragmentManager, "", "")
//                } else {
//                    ResultDialog.newInstance(childFragmentManager, res!!, "")
//                }
//            }
        }

        binding.ll11.setOnClickListener {
//            ConnectUtil.CURRENT_BLUE_SOCKET?.let {
//                CommandUtil.sendCommand(it, "AT+RST/WIFI")
//                ResultDialog.newInstance(childFragmentManager, "成功", "")
//            }
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
                            override fun onLeScan(
                                device: BleDevice,
                                rssi: Int,
                                scanRecord: ByteArray?
                            ) {
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
            Ble.getInstance<BleDevice>().disconnectAll()
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

        LiveDataBus.observeString(IConsts.KEY_COMMEND_RES, viewLifecycleOwner) { res ->
            res?.let {
                if (BLEUtils.isSuccess) {
                    ResultDialog.newInstance(childFragmentManager, it, "")
                    BLEUtils.isSuccess = false
                }
            }
        }
    }


    private fun refreshPageBlueToothState() {
        if (BluetoothHelper.isAnyDeviceConnected(requireContext())) {
            binding.tvStatus.text = "蓝牙已连接"
            binding.tvConnect.visibility = View.GONE
            binding.tvDisconnect.visibility = View.VISIBLE
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
    }
}