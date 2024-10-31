package com.module.connect.fragment

import BluetoothHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.com.heaton.blelibrary.ble.Ble
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback
import cn.com.heaton.blelibrary.ble.model.BleDevice
import com.module.connect.adapter.CommandAdapter
import com.module.connect.bean.CommandBean
import com.module.connect.consts.IConsts
import com.module.connect.databinding.FragmentHomeBinding
import com.module.connect.dialog.BlueToothListDialog
import com.module.connect.model.CommandModel
import com.module.connect.util.BLEUtils
import com.module.connect.util.InCludeUtils
import com.module.connect.util.LiveDataBus
import com.module.connect.util.PermissionComplianceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mCommandAdapter: CommandAdapter
    private val mList = mutableListOf<CommandBean>()

    private val viewModel: CommandModel by viewModels()

    companion object {
        val deviceList = mutableListOf<BleDevice>()
        val devices = mutableListOf<BleDevice>()
        var cummand = ""
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
        with(binding.rvCommand) {
            mCommandAdapter = CommandAdapter {
                when (it.command) {
                    "AT+VERSION=?" -> {
                        cummand = "AT+VERSION=?"
                        BLEUtils.sendCommand("AT+VERSION=?\r\n") {
                        }
                    }

                    "AT+MEMS=?" -> {
                        cummand = "AT+MEMS=?"
                        BLEUtils.sendCommand("AT+MEMS=?\r\n") {
                        }
                    }

                    "AT+ICCID=?" -> {
                        cummand = "AT+ICCID=?"
                        BLEUtils.sendCommand("AT+ICCID=?\r\n") {
                        }
                    }

                    "AT+UART=?" -> {
                        cummand = "AT+UART=?"
                        BLEUtils.sendCommand("AT+UART=?\r\n") {
                        }
                    }

                    "AT+STATUS=?" -> {
                        cummand = "AT+STATUS=?"
                        BLEUtils.sendCommand("AT+STATUS=?\r\n") {
                        }
                    }

                    "AT+STATE=?" -> {
                        cummand = "AT+STATE=?"
                        BLEUtils.sendCommand("AT+STATE=?\r\n") {
                        }
                    }

                    "AT+MEMS_FRE=?" -> {
                        cummand = "AT+MEMS_FRE=?"
                        BLEUtils.sendCommand("AT+MEMS_FRE=?\r\n") {
                        }
                    }

                    "AT+POWER=?" -> {
                        cummand = "AT+POWER=?"
                        BLEUtils.sendCommand("AT+POWER=?\r\n") {
                        }
                    }

                    "AT+MODE=?" -> {
                        cummand = "AT+MODE=?"
                        BLEUtils.sendCommand("AT+MODE=?\r\n") {
                        }
                    }

                    "AT+CCLK=?" -> {
                        cummand = "AT+CCLK=?"
                        BLEUtils.sendCommand("AT+CCLK=?\r\n") {
                        }
                    }

                    "AT+CSQ/4G=?" -> {
                        cummand = "AT+CSQ/4G=?"
                        BLEUtils.sendCommand("AT+CSQ/4G=?\r\n") {
                        }
                    }

                    "AT+SOCK/4G=?" -> {
                        cummand = "AT+SOCK/4G=?"
                        BLEUtils.sendCommand("AT+SOCK/4G=?\r\n") {
                        }
                    }

                    "AT+FESLO/4G=?" -> {
                        cummand = "AT+FESLO/4G=?"
                        BLEUtils.sendCommand("AT+FESLO/4G=?\r\n") {
                        }
                    }

                    "AT+MQTTSVR/4G=?" -> {
                        cummand = "AT+MQTTSVR/4G=?"
                        BLEUtils.sendCommand("AT+MQTTSVR/4G=?\r\n") {
                        }
                    }

                    "AT+MQTT_SUB/4G=?" -> {
                        cummand = "AT+MQTT_SUB/4G=?"
                        BLEUtils.sendCommand("AT+MQTT_SUB/4G=?\r\n") {
                        }
                    }

                    "AT+MQTT_PUB/4G=?" -> {
                        cummand = "AT+MQTT_PUB/4G=?"
                        BLEUtils.sendCommand("AT+MQTT_PUB/4G=?\r\n") {
                        }
                    }

                    "AT+MQTT_SERIAL_MODE/4G=?" -> {
                        cummand = "AT+MQTT_SERIAL_MODE/4G=?"
                        BLEUtils.sendCommand("AT+MQTT_SERIAL_MODE/4G=?\r\n") {
                        }
                    }

                    "AT+MQTT_NTRIPSVR/4G=?" -> {
                        cummand = "AT+NTRIPSVR/4G=?"
                        BLEUtils.sendCommand("AT+NTRIPSVR/4G=?\r\n") {
                        }
                    }

                    "AT+FTPSVR/4G=?" -> {
                        cummand = "AT+FTPSVR/4G=?"
                        BLEUtils.sendCommand("AT+FTPSVR/4G=?\r\n") {
                        }
                    }

                    "AT+DHCP/NET=?" -> {
                        cummand = "AT+DHCP/NET=?"
                        BLEUtils.sendCommand("AT+DHCP/NET=?\r\n") {
                        }
                    }

                    "AT+LOCALIP/NET=?" -> {
                        cummand = "AT+LOCALIP/NET=?"
                        BLEUtils.sendCommand("AT+LOCALIP/NET=?\r\n") {
                        }
                    }

                    "AT+SERVERIP/NET=?" -> {
                        cummand = "AT+SERVERIP/NET=?"
                        BLEUtils.sendCommand("AT+SERVERIP/NET=?\r\n") {
                        }
                    }

                    "AT+NTRIPSVR/NET=?" -> {
                        cummand = "AT+NTRIPSVR/NET=?"
                        BLEUtils.sendCommand("AT+NTRIPSVR/NET=?\r\n") {
                        }
                    }

//                    "AT+RST/WIFI" -> {
//                        InCludeUtils.setResetWifi(binding, it)
//                    }

                    "AT+AP/WIFI=?" -> {
                        cummand = "AT+AP/WIFI=?"
                        BLEUtils.sendCommand("AT+AP/WIFI=?\r\n") {
                        }
                    }

                    "AT+STA/WIFI=?" -> {
                        cummand = "AT+STA/WIFI=?"
                        BLEUtils.sendCommand("AT+STA/WIFI=?\r\n") {
                        }
                    }

                    "AT+SOCK/WIFI=?" -> {
                        cummand = "AT+SOCK/WIFI=?"
                        BLEUtils.sendCommand("AT+SOCK/WIFI=?\r\n") {
                        }
                    }

                    "AT+MQTTSVR/WIFI=?" -> {
                        cummand = "AT+MQTTSVR/WIFI=?"
                        BLEUtils.sendCommand("AT+MQTTSVR/WIFI=?\r\n") {
                        }
                    }

                    "AT+MQTT_SUB_PUB/WIFI=?" -> {
                        cummand = "AT+MQTT_SUB_PUB/WIFI=?"
                        BLEUtils.sendCommand("AT+MQTT_SUB_PUB/WIFI=?\r\n") {
                        }
                    }

                    "AT+NTRIPSVR/WIFI=?" -> {
                        cummand = "AT+NTRIPSVR/WIFI=?"
                        BLEUtils.sendCommand("AT+NTRIPSVR/WIFI=?\r\n") {
                        }
                    }

                    "AT+BLEMODE/WIFI=?" -> {
                        cummand = "AT+BLEMODE/WIFI=?"
                        BLEUtils.sendCommand("AT+BLEMODE/WIFI=?\r\n") {
                        }
                    }

                    "AT+BLENAME/WIFI=?" -> {
                        cummand = "AT+BLENAME/WIFI=?"
                        BLEUtils.sendCommand("AT+BLENAME/WIFI=?\r\n") {
                        }
                    }

                    "AT+CONFIG/DT?" -> {
                        cummand = "AT+CONFIG/DT=?"
                        BLEUtils.sendCommand("AT+CONFIG/DT=?\r\n") {
                        }
                    }

                    "AT+AIR_BAUD/DT?" -> {
                        cummand = "AT+AIR_BAUD/DT=?"
                        BLEUtils.sendCommand("AT+AIR_BAUD/DT=?\r\n") {
                        }
                    }

                    "AT+ID/DT?" -> {
                        cummand = "AT+ID/DT=?"
                        BLEUtils.sendCommand("AT+ID/DT=?\r\n") {
                        }
                    }
                }
            }
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mCommandAdapter
        }

        // 重启
        binding.llReboot.setOnClickListener {
            cummand = "AT+REBOOT=?"
            BLEUtils.sendCommand("AT+REBOOT=?\r\n") {
                BLEUtils.isConnected = false
                LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
            }
        }
        // 恢复出厂
        binding.llClear.setOnClickListener {
            cummand = "AT+CLEAR=?"
            BLEUtils.sendCommand("AT+CLEAR\r\n") {
                BLEUtils.isConnected = false
                LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
            }
        }

        // 保存
        binding.llSave.setOnClickListener {
            cummand = "AT+SAVE=?"
            BLEUtils.sendCommand("AT+SAVE\r\n") {
                BLEUtils.isConnected = false
                LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
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


        // 重置WIFI设置
//            cummand = "AT+RST/WIFI"
//            BLEUtils.sendCommand("AT+RST/WIFI\r\n") {
//                LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
//            }

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

        viewModel.commandList.observe(viewLifecycleOwner) {
            mList.addAll(it)
            mCommandAdapter.submitList(it)
        }
        viewModel.getAllCommand()


        LiveDataBus.observeString(IConsts.KEY_COMMEND_RES, viewLifecycleOwner) { res ->
//            res?.let {
//                if (BLEUtils.isSuccess) {
//                    ResultDialog.newInstance(childFragmentManager, it, "")
//                }
//            }
            res?.let {
                BLEUtils.isSuccess = false
                val index = getItemIndexByName(cummand)
                mList[index].res = it
                mCommandAdapter.submitList(mList)

                /*when (cummand) {
                    "AT+VERSION=?" -> {
                        InCludeUtils.seVersion(binding, it)
                    }

                    "AT+MEMS=?" -> {
                        InCludeUtils.seMems(binding, it)
                    }

                    "AT+ICCID=?" -> {
                        InCludeUtils.seICCID(binding, it)
                    }

                    "AT+UART=?" -> {
                        InCludeUtils.seUart(binding, it)
                    }

                    "AT+STATUS=?" -> {
                        InCludeUtils.seStatus(binding, it)
                    }

                    "AT+STATE=?" -> {
                        InCludeUtils.seState(binding, it)
                    }

                    "AT+MEMS_FRE=?" -> {
                        InCludeUtils.seMemsFre(binding, it)
                    }

                    "AT+POWER=?" -> {
                        InCludeUtils.sePower(binding, it)
                    }

                    "AT+MODE=?" -> {
                        InCludeUtils.seMode(binding, it)
                    }

                    "AT+CCLK=?" -> {
                        InCludeUtils.seCclk(binding, it)
                    }

                    "AT+CSQ/4G=?" -> {
                        InCludeUtils.seCsq(binding, it)
                    }

                    "AT+SOCK/4G=?" -> {
                        InCludeUtils.seSock4(binding, it)
                    }

                    "AT+FESLO/4G=?" -> {
                        InCludeUtils.seFeslo(binding, it)
                    }

                    "AT+MQTTSVR/4G=?" -> {
                        InCludeUtils.seMqtt(binding, it)
                    }

                    "AT+MQTT_SUB/4G=?" -> {
                        InCludeUtils.seMqttTheme(binding, it)
                    }

                    "AT+MQTT_PUB/4G=?" -> {
                        InCludeUtils.setPubTheme(binding, it)
                    }

                    "AT+MQTT_SERIAL_MODE/4G=?" -> {
                        InCludeUtils.seSerial(binding, it)
                    }

                    "AT+MQTT_NTRIPSVR/4G=?" -> {
                        InCludeUtils.setNtrip(binding, it)
                    }

                    "AT+FTPSVR/4G=?" -> {
                        InCludeUtils.setFtp(binding, it)
                    }

                    "AT+DHCP/NET=?" -> {
                        InCludeUtils.setDhcp(binding, it)
                    }

                    "AT+LOCALIP/NET=?" -> {
                        InCludeUtils.setIp(binding, it)
                    }

                    "AT+SERVERIP/NET=?" -> {
                        InCludeUtils.seNet(binding, it)
                    }

                    "AT+NTRIPSVR/NET=?" -> {
                        InCludeUtils.seServerntrip(binding, it)
                    }

                    "AT+RST/WIFI" -> {
                        InCludeUtils.setResetWifi(binding, it)
                    }

                    "AT+AP/WIFI=?" -> {
                        InCludeUtils.setApWifi(binding, it)
                    }

                    "AT+STA/WIFI=?" -> {
                        InCludeUtils.setStaWifi(binding, it)
                    }

                    "AT+SOCK/WIFI=?" -> {
                        InCludeUtils.setUdp(binding, it)
                    }

                    "AT+MQTTSVR/WIFI=?" -> {
                        InCludeUtils.setMqttWifi(binding, it)
                    }

                    "AT+MQTT_SUB_PUB/WIFI=?" -> {
                        InCludeUtils.setMqttPub(binding, it)
                    }

                    "AT+NTRIPSVR/WIFI=?" -> {
                        InCludeUtils.setWifiNtrip(binding, it)
                    }

                    "AT+BLEMODE/WIFI=?" -> {
                        InCludeUtils.setWifiMode(binding, it)
                    }

                    "AT+BLENAME/WIFI=?" -> {
                        InCludeUtils.setWifiName(binding, it)
                    }

                    "AT+CONFIG/DT?" -> {
                        InCludeUtils.setDtMode(binding, it)
                    }

                    "AT+AIR_BAUD/DT?" -> {
                        InCludeUtils.setSpeed(binding, it)
                    }

                    "AT+ID/DT?" -> {
                        InCludeUtils.setDtId(binding, it)
                    }
                }*/
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

    fun getItemIndexByName(cmmand: String): Int {
        return mList.indexOfFirst { it.command == cmmand }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}