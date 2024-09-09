package com.module.connect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.R

class AddApter : RecyclerView.Adapter<AddApter.AddHolder>() {

    private val mData: ArrayList<String> = arrayListOf()


    inner class AddHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAdd = itemView.findViewById<TextView>(R.id.tv_add_item)
    }

    override fun onBindViewHolder(holder: AddHolder, position: Int) {
        holder.tvAdd.text = mData[position]
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_rv, parent, false)
        return AddHolder(view)
    }

    fun setData(list: MutableList<String>) {
        mData.clear()
        mData.addAll(list)
        notifyDataSetChanged()
    }
}