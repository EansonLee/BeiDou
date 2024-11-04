package com.module.connect.fragment

import BluetoothHelper
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.module.connect.bean.STYLE_FIVE
import com.module.connect.bean.STYLE_FOUR
import com.module.connect.bean.STYLE_ONE
import com.module.connect.bean.STYLE_SEVEN
import com.module.connect.bean.STYLE_SIX
import com.module.connect.bean.STYLE_THIRD
import com.module.connect.bean.STYLE_TWO
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
        var SEND_COMMAND = ""
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
                cummand = it.command
                SEND_COMMAND = "${it.command}?"
                Log.e("------", "home-command：${it.command}?")
                BLEUtils.sendCommand("${it.command}?\r\n") {
                }
            }
            itemAnimator = null
            animation = null
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mCommandAdapter
            setItemViewCacheSize(30)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            val pool = recycledViewPool
            pool.setMaxRecycledViews(STYLE_ONE, 30)
            pool.setMaxRecycledViews(STYLE_TWO, 30)
            pool.setMaxRecycledViews(STYLE_THIRD, 30)
            pool.setMaxRecycledViews(STYLE_FOUR, 30)
            pool.setMaxRecycledViews(STYLE_FIVE, 30)
            pool.setMaxRecycledViews(STYLE_SIX, 30)
            pool.setMaxRecycledViews(STYLE_SEVEN, 30)
            setRecycledViewPool(pool)
        }

        // 重启
        binding.llReboot.setOnClickListener {
            cummand = "AT+REBOOT"
            SEND_COMMAND = cummand
            BLEUtils.sendCommand("AT+REBOOT=?\r\n") {
                BLEUtils.isConnected = false
                LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
            }
        }
        // 恢复出厂
        binding.llClear.setOnClickListener {
            cummand = "AT+CLEAR"
            SEND_COMMAND = cummand
            BLEUtils.sendCommand("AT+CLEAR\r\n") {
                BLEUtils.isConnected = false
                LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
            }
        }

        // 保存
        binding.llSave.setOnClickListener {
            cummand = "AT+SAVE"
            SEND_COMMAND = cummand
            BLEUtils.sendCommand("AT+SAVE\r\n") {
                BLEUtils.isConnected = false
                LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
            }
        }

        binding.tvConnect.setOnClickListener {
            PermissionComplianceManager.requestFineLocationPermissionHasTip(
                requireActivity(),
                object : PermissionComplianceManager.SimpleCallbackProxy() {
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
            mCommandAdapter.submitList(mList)
        }
        viewModel.getAllCommand()


        LiveDataBus.observeString(IConsts.KEY_COMMEND_RES, viewLifecycleOwner) { res ->
            res?.let {
                BLEUtils.isSuccess = false
                val index = getItemIndexByName(cummand)
                Log.e("------", "Home index：$index")
                if (index != -1) {
                    if (res != "OK\r\n") {
                        mList[index].res = it.replace("\r", "").replace("\n", "")
                        mCommandAdapter.notifyItemChanged(index)
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

    private fun getItemIndexByName(command: String): Int {
        return mList.indexOfFirst { it.command == command }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}