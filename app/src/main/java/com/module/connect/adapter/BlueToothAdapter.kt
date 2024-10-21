package com.module.connect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.com.heaton.blelibrary.ble.model.BleDevice
import com.module.connect.databinding.ItemBlueToothBinding

class BlueToothAdapter(private val onItemClick: (BleDevice) -> Unit) :
    RecyclerView.Adapter<BlueToothAdapter.BlueToothHolder>() {

    private val mData: ArrayList<BleDevice> = arrayListOf()


    inner class BlueToothHolder(private val mBinding: ItemBlueToothBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun render(bean: BleDevice, onItemClick: (BleDevice) -> Unit) {
            mBinding.tvTitle.text = bean.bleName
            mBinding.tvAddress.text = bean.bleAddress
            mBinding.root.setOnClickListener {
                onItemClick(bean)
            }
        }
    }

    override fun onBindViewHolder(holder: BlueToothHolder, position: Int) {
        holder.render(bean = mData[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlueToothHolder {
        return BlueToothHolder(
            ItemBlueToothBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setData(list: List<BleDevice>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun setData(device: BleDevice) {
        mData.add(device)
        notifyDataSetChanged()
    }
}