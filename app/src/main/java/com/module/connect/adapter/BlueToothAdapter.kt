package com.module.connect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.R
import com.module.connect.bean.BlueToothBean

class BlueToothAdapter : RecyclerView.Adapter<BlueToothAdapter.BlueTooth>() {

    private val mData: ArrayList<BlueToothBean> = arrayListOf()


    inner class BlueTooth(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.findViewById<TextView>(R.id.tv_title)
        val tvAddress = itemView.findViewById<TextView>(R.id.tv_address)
    }

    override fun onBindViewHolder(holder: BlueTooth, position: Int) {
        holder.tvName.text = mData[position].name
        holder.tvAddress.text = mData[position].address
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlueTooth {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_blue_tooth, parent, false)
        return BlueTooth(view)
    }

    fun setData(list: MutableList<BlueToothBean>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }
}