package com.module.connect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.BlueToothBean
import com.module.connect.databinding.ItemBlueToothBinding

class BlueToothAdapter(private val onItemClick: (BlueToothBean) -> Unit) :
    RecyclerView.Adapter<BlueToothAdapter.BlueToothHolder>() {

    private val mData: ArrayList<BlueToothBean> = arrayListOf()


    inner class BlueToothHolder(private val mBinding: ItemBlueToothBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun render(bean: BlueToothBean, onItemClick: (BlueToothBean) -> Unit) {
            mBinding.tvTitle.text = bean.name
            mBinding.tvAddress.text = bean.address
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

    fun setData(list: List<BlueToothBean>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }
}