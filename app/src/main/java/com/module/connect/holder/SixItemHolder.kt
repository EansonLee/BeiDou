package com.module.connect.holder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemSixBinding

class SixItemHolder(private val binding: ItemSixBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean) -> Unit) {
        binding.tvName.text = bean.name
        if (!TextUtils.isEmpty(bean.res)) {
            val res = bean.res.substringAfter("=").split(",")
            binding.et1.setText(res[0])
            binding.et2.setText(res[1])
            binding.et3.setText(res[2])
            binding.et4.setText(res[3])
            binding.et5.setText(res[4])
            binding.et6.setText(res[5])
        } else {
            binding.et1.hint = bean.tip1
            binding.et2.hint = bean.tip2
            binding.et3.hint = bean.tip3
            binding.et4.hint = bean.tip4
            binding.et5.hint = bean.tip5
            binding.et6.hint = bean.tip6
        }
        binding.tvBtn.setOnClickListener {
            itemClick.invoke(bean)
        }
    }
}