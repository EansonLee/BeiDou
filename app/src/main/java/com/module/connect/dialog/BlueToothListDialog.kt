package com.module.connect.dialog

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.module.connect.adapter.BlueToothAdapter
import com.module.connect.bean.BlueToothBean
import com.module.connect.databinding.DialogBlueToothListBinding
import com.module.connect.ext.makeArguments
import com.module.connect.ext.params
import com.module.connect.util.ConnectUtil

class BlueToothListDialog : BaseFragmentDialog<DialogBlueToothListBinding>() {

    private var connectingProgressDialog: ProgressDialog? = null

    companion object {
        fun newInstance(
            manager: FragmentManager,
            list: List<BlueToothBean>,
        ) {
            BlueToothListDialog().apply {
                makeArguments("key_list" to list)
                show(manager, "LinkLocationDialog")
            }
        }
    }

    private val list: List<BlueToothBean> by params("key_list") { mutableListOf() }


    private var mBluetoothAdapter: BlueToothAdapter? = null


    override fun getBinding(
        inflate: LayoutInflater,
        container: ViewGroup?
    ): DialogBlueToothListBinding {
        return DialogBlueToothListBinding.inflate(inflate)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBluetoothAdapter = BlueToothAdapter {
            connectingProgressDialog = ProgressDialog(requireContext()).apply {
                setMessage("连接中...")
                setCancelable(false)
                show()
            }
            ConnectUtil.CURRENT_ADDRESS = it.address
            ConnectUtil.CURRENT_DEVICE = it.device
//            ConnectUtil.CURRENT_BLUE_SOCKET = ConnectUtil.connectToGnssDevice(it.address)
            ConnectUtil.pairBluetoothDevice(requireContext(), it.device)
            connectingProgressDialog?.dismiss()
            if (ConnectUtil.CURRENT_BLUE_SOCKET != null) {
                ToastUtils.showShort("连接成功")
            } else {
                ToastUtils.showShort("连接失败")
            }
            dismissAllowingStateLoss()

        }
        binding.rvBlueTooth.adapter = mBluetoothAdapter
        binding.rvBlueTooth.layoutManager = LinearLayoutManager(requireContext())
        mBluetoothAdapter?.setData(list)
    }

    override fun initData() {
    }
}