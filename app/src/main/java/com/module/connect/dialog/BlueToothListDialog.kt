package com.module.connect.dialog

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.module.connect.adapter.BlueToothAdapter
import com.module.connect.bean.BlueToothBean
import com.module.connect.databinding.DialogBlueToothListBinding
import com.module.connect.ext.makeArguments
import com.module.connect.ext.params
import com.module.connect.fragment.HomeFragment

class BlueToothListDialog : BaseFragmentDialog<DialogBlueToothListBinding>() {

    companion object {

        private var mBluetoothAdapter: BlueToothAdapter? = null


        fun newInstance(
            manager: FragmentManager,
            list: List<BlueToothBean>,
        ) {
            BlueToothListDialog().apply {
                makeArguments("key_list" to list)
                show(manager, "LinkLocationDialog")
            }
        }

        fun notify(device: BlueToothBean) {
            mBluetoothAdapter?.setData(device)
        }
    }

    private val list: List<BlueToothBean> by params("key_list") { mutableListOf() }


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
            if (BluetoothHelper.isDevicePaired(it.device)) {
                Toast.makeText(context,"此设备已配对", Toast.LENGTH_SHORT).show()
            } else {
                BluetoothHelper.connectDevice(requireContext(), it.device, {
                    Toast.makeText(context,"连接成功", Toast.LENGTH_SHORT).show()
                    Log.e("---", "已连接")
                }, {
                    Toast.makeText(context,"连接失败", Toast.LENGTH_SHORT).show()
                    Log.e("---", "连接失败")
                })
            }

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