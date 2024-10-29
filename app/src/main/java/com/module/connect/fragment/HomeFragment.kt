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
import androidx.recyclerview.widget.LinearLayoutManager
import cn.com.heaton.blelibrary.ble.Ble
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback
import cn.com.heaton.blelibrary.ble.model.BleDevice
import com.module.connect.adapter.BlueToothAdapter
import com.module.connect.adapter.StackAdapter
import com.module.connect.bean.StackBean
import com.module.connect.consts.IConsts
import com.module.connect.databinding.FragmentHomeBinding
import com.module.connect.dialog.BlueToothListDialog
import com.module.connect.dialog.ResultDialog
import com.module.connect.util.BLEUtils
import com.module.connect.util.InCludeUtils
import com.module.connect.util.LiveDataBus
import com.module.connect.util.PermissionComplianceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var cummand = ""
    private var mStackAdapter: StackAdapter? = null
    private var mStackList = mutableListOf<StackBean>()

    private var isSearchUart = false

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
        mStackAdapter = StackAdapter()
        binding.rvStack.adapter = mStackAdapter
        binding.rvStack.layoutManager = LinearLayoutManager(context)
        mStackAdapter?.setData(mStackList)

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


        // 版本号
        binding.llInclude.tvVersion.setOnClickListener {
            cummand = "AT+VERSION=?"
            BLEUtils.sendCommand("AT+VERSION=?\r\n") {
            }
        }

        // 查询MEMS数据
        binding.llInclude.tvMems.setOnClickListener {
            cummand = "AT+MEMS=?"
            BLEUtils.sendCommand("AT+MEMS=?\r\n") {
            }
        }
        // 查询SIM卡ICCID
        binding.llInclude.tvIccid.setOnClickListener {
            cummand = "AT+ICCID=?"
            BLEUtils.sendCommand("AT+ICCID=?\r\n") {
            }
        }
        //波特率
        binding.llInclude.tvUart.setOnClickListener {
            cummand = "AT+UART=?"
            BLEUtils.sendCommand("AT+UART=?\r\n") {
            }
        }
        // 连接状态
        binding.llInclude.tvStatus.setOnClickListener {
            cummand = "AT+STATUS=?"
            BLEUtils.sendCommand("AT+STATUS=?\r\n") {
            }
        }
        // 工作状态
        binding.llInclude.tvState.setOnClickListener {
            cummand = "AT+STATE=?"
            BLEUtils.sendCommand("AT+STATE=?\r\n") {
            }
        }
        // 数据上报频率
        binding.llInclude.tvMemsfre.setOnClickListener {
            cummand = "AT+MEMS_FRE=?"
            BLEUtils.sendCommand("AT+MEMS_FRE=?\r\n") {
            }
        }
        // 电源使能
        binding.llInclude.tvPower.setOnClickListener {
            cummand = "AT+POWER=?"
            BLEUtils.sendCommand("AT+POWER=?\r\n") {
            }
        }
        // 工作模式
        binding.llInclude.tvMode.setOnClickListener {
            cummand = "AT+MODE=?"
            BLEUtils.sendCommand("AT+MODE=?\r\n") {
            }
        }
        // 工作模式
        binding.llInclude.tvMode.setOnClickListener {
            cummand = "AT+MODE=?"
            BLEUtils.sendCommand("AT+MODE=?\r\n") {
            }
        }
        // 查询时间和日期
        binding.llInclude.tvCclk.setOnClickListener {
            cummand = "AT+CCLK=?"
            BLEUtils.sendCommand("AT+CCLK=?\r\n") {
            }
        }
        // 4G网络信号强度
        binding.llInclude.tvCclk.setOnClickListener {
            cummand = "AT+CSQ/4G=?"
            BLEUtils.sendCommand("AT+CSQ/4G=?\r\n") {
            }
        }
        // 4G TCP服务器参数
        binding.llInclude.tvSock4.setOnClickListener {
            cummand = "AT+SOCK/4G=?"
            BLEUtils.sendCommand("AT+SOCK/4G=?\r\n") {
            }
        }
        // 4G前段解算模式开启状态
        binding.llInclude.tvFeslo.setOnClickListener {
            cummand = "AT+FESLO/4G=?"
            BLEUtils.sendCommand("AT+FESLO/4G=?\r\n") {
            }
        }
        // 4G MQTT服务器参数
        binding.llInclude.tvMqtt.setOnClickListener {
            cummand = "AT+MQTTSVR/4G=?"
            BLEUtils.sendCommand("AT+MQTTSVR/4G=?\r\n") {
            }
        }
        // 4G MQTT订阅主题
        binding.llInclude.tvMqttTheme.setOnClickListener {
            cummand = "AT+MQTT_SUB/4G=?"
            BLEUtils.sendCommand("AT+MQTT_SUB/4G=?\r\n") {
            }
        }
        // 4G MQTT发布主题
        binding.llInclude.tvSendtheme.setOnClickListener {
            cummand = "AT+MQTT_PUB/4G=?"
            BLEUtils.sendCommand("AT+MQTT_PUB/4G=?\r\n") {
            }
        }
        // 4G MQTT 串口模式
        binding.llInclude.tvSerial.setOnClickListener {
            cummand = "AT+MQTT_SERIAL_MODE/4G=?"
            BLEUtils.sendCommand("AT+MQTT_SERIAL_MODE/4G=?\r\n") {
            }
        }
        // 4G NTRIP服务器参数
        binding.llInclude.tvNtrip.setOnClickListener {
            cummand = "AT+NTRIPSVR/4G=?"
            BLEUtils.sendCommand("AT+NTRIPSVR/4G=?\r\n") {
            }
        }
        // 4G FTP服务器参数
        binding.llInclude.tvFtp.setOnClickListener {
            cummand = "AT+FTPSVR/4G=?"
            BLEUtils.sendCommand("AT+FTPSVR/4G=?\r\n") {
            }
        }
        // 以太网本地的IP参数
        binding.llInclude.tvDhcp.setOnClickListener {
            cummand = "AT+DHCP/NET=?"
            BLEUtils.sendCommand("AT+DHCP/NET=?\r\n") {
            }
        }
        // 以太网DHCP工作模式
        binding.llInclude.tvDhcp.setOnClickListener {
            cummand = "AT+DHCP/NET=?"
            BLEUtils.sendCommand("AT+DHCP/NET=?\r\n") {
            }
        }
        // 以太网本地的IP参数
        binding.llInclude.tvIp.setOnClickListener {
            cummand = "AT+LOCALIP/NET=?"
            BLEUtils.sendCommand("AT+LOCALIP/NET=?\r\n") {
            }
        }
        // NET TCP/UDP服务器参数
        binding.llInclude.tvNet.setOnClickListener {
            cummand = "AT+SERVERIP/NET=?"
            BLEUtils.sendCommand("AT+SERVERIP/NET=?\r\n") {
            }
        }
        // 以太网 NTRIP服务器参数
        binding.llInclude.tvServerntrip.setOnClickListener {
            cummand = "AT+NTRIPSVR/NET=?"
            BLEUtils.sendCommand("AT+NTRIPSVR/NET=?\r\n") {
            }
        }
        // 重置WIFI设置
        binding.llInclude.tvResetWifi.setOnClickListener {
            cummand = "AT+RST/WIFI"
            BLEUtils.sendCommand("AT+RST/WIFI\r\n") {
                LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
            }
        }
        // WIFI热点的名称和密码
        binding.llInclude.tvResetWifi.setOnClickListener {
            cummand = "AT+AP/WIFI=?"
            BLEUtils.sendCommand("AT+AP/WIFI=?\r\n") {
            }
        }
        // WIFI热点的名称和密码
        binding.llInclude.tvResetWifi.setOnClickListener {
            cummand = "AT+AP/WIFI=?"
            BLEUtils.sendCommand("AT+AP/WIFI=?\r\n") {
            }
        }
        // WIFI连接热点的名称和密码
        binding.llInclude.tvConnectwifi.setOnClickListener {
            cummand = "AT+STA/WIFI=?"
            BLEUtils.sendCommand("AT+STA/WIFI=?\r\n") {
            }
        }
        // WIFI TCP/UDP服务器参数
        binding.llInclude.tvUdp.setOnClickListener {
            cummand = "AT+SOCK/WIFI=?"
            BLEUtils.sendCommand("AT+SOCK/WIFI=?\r\n") {
            }
        }
        // WIFI MQTT服务器参数
        binding.llInclude.tvWifimqtt.setOnClickListener {
            cummand = "AT+MQTTSVR/WIFI=?"
            BLEUtils.sendCommand("AT+MQTTSVR/WIFI=?\r\n") {
            }
        }
        // 34 WIFI MQTT订阅与发布的主题名
        binding.llInclude.tvMqttwifitheme.setOnClickListener {
            cummand = "AT+MQTT_SUB_PUB/WIFI=?"
            BLEUtils.sendCommand("AT+MQTT_SUB_PUB/WIFI=?\r\n") {
            }
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
//            res?.let {
//                if (BLEUtils.isSuccess) {
//                    ResultDialog.newInstance(childFragmentManager, it, "")
//                }
//            }
            res?.let {
                val stack = StackBean(cummand, it)
                mStackAdapter?.addData(stack)
                BLEUtils.isSuccess = false

                when (cummand) {
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