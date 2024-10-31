package com.module.connect.holder

import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemOneBinding

class OneItemHolder(private val binding: ItemOneBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean) -> Unit) {
        binding.tvName.text = bean.name
        binding.etRes.text = bean.res
        binding.tvBtn.setOnClickListener {
            itemClick.invoke(bean)
        }
    }
}