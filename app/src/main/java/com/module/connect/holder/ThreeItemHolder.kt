package com.module.connect.holder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemThreeBinding

class ThreeItemHolder(private val binding: ItemThreeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean) -> Unit) {
        binding.tvName.text = bean.name
        if (!TextUtils.isEmpty(bean.res)) {
            val res = bean.res.substringAfter("=").split(",")
            binding.et1.setText(res[0])
            binding.et2.setText(res[1])
            binding.et3.setText(res[2])
        } else {
            binding.et1.hint = bean.tip1
            binding.et2.hint = bean.tip2
            binding.et3.hint = bean.tip3
        }
        binding.tvBtn.setOnClickListener {
            itemClick.invoke(bean)
        }
    }
}