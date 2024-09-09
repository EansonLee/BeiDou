package com.module.connect.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.module.connect.adapter.BlueToothAdapter
import com.module.connect.databinding.DialogBlueToothListBinding

class BlueToothListDialog : BaseFragmentDialog<DialogBlueToothListBinding>() {

    private var mBluetoothAdapter: BlueToothAdapter? = null


    override fun getBinding(
        inflate: LayoutInflater,
        container: ViewGroup?
    ): DialogBlueToothListBinding {
        return DialogBlueToothListBinding.inflate(inflate)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBluetoothAdapter = BlueToothAdapter()
        binding.rvBlueTooth.adapter = mBluetoothAdapter


    }

    override fun initData() {
    }
}