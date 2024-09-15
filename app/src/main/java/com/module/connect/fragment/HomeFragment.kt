package com.module.connect.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.module.connect.databinding.FragmentHomeBinding
import com.module.connect.dialog.BlueToothListDialog
import com.module.connect.util.ConnectUtil
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
        binding.llSend.setOnClickListener {
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
            ConnectUtil.unpairBluetoothDevice(ConnectUtil.CURRENT_DEVICE!!)
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
            binding.tvStatus.text = "蓝牙已连接"
            binding.tvConnect.visibility = View.GONE
            binding.tvDisconnect.visibility = View.VISIBLE
        } else {
            binding.tvStatus.text = "蓝牙未连接"
            binding.tvConnect.visibility = View.VISIBLE
            binding.tvDisconnect.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}