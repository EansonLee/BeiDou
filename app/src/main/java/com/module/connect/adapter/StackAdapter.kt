package com.module.connect.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.StackBean
import com.module.connect.databinding.ItemStackBinding
import com.module.connect.util.gone
import com.module.connect.util.visible

class StackAdapter : RecyclerView.Adapter<StackAdapter.StackHolder>() {

    private val mData: ArrayList<StackBean> = arrayListOf()

    inner class StackHolder(private val mBinding: ItemStackBinding) :
        RecyclerView.ViewHolder(mBinding.root) {
        fun render(bean: StackBean) {
            mBinding.tvSend.text = bean.send
            if (!TextUtils.isEmpty(bean.res)) {
                mBinding.tvRes.text = bean.res
//                mBinding.tvRes.gone()
            }
//            mBinding.root.setOnClickListener {
//                onItemClick(bean)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StackHolder {
        return StackHolder(
            ItemStackBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: StackHolder, position: Int) {
        holder.render(bean = mData[position])
    }

    fun setData(list: List<StackBean>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(stack: StackBean) {
        mData.add(stack)
        notifyDataSetChanged()
    }
}