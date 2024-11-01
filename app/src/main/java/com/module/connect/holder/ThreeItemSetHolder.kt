package com.module.connect.holder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemThreeBinding
import com.module.connect.util.InCludeUtils
import com.module.connect.util.ToastUtils

class ThreeItemSetHolder(private val binding: ItemThreeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean) -> Unit) {
        binding.tvName.text = bean.name
        binding.tvBtn.text = "设置"
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
            if (InCludeUtils.areAllEditTextsNotNullOrEmpty(binding.et1, binding.et2, binding.et3)
                    .not()
            ) {
                ToastUtils.show("设置指令不能为空")
                return@setOnClickListener
            }
            bean.command =
                "${bean.command + binding.et1.text},${binding.et2.text},${binding.et3.text}"
            itemClick.invoke(bean)
        }
    }
}