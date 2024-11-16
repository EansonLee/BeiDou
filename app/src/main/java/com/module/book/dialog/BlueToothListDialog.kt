package com.module.book.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.module.book.adapter.BlueToothAdapter
import com.module.book.bean.BlueToothBean
import com.module.book.databinding.DialogBlueToothListBinding
import com.module.book.ext.makeArguments
import com.module.book.ext.params
import com.module.book.fragment.HomeFragment

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