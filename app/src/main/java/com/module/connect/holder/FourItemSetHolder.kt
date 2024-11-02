package com.module.connect.holder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemFourSetBinding
import com.module.connect.util.InCludeUtils
import com.module.connect.util.ToastUtils

class FourItemSetHolder(private val binding: ItemFourSetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean) -> Unit) {
        binding.tvName.text = bean.name
        binding.tvBtn.text = "设置"
        val resList = if (!TextUtils.isEmpty(bean.res)) {
            bean.res.substringAfter("=").split(",")
        } else {
            emptyList()
        }
        binding.et1.setText(resList.getOrNull(0) ?: "")
        binding.et2.setText(resList.getOrNull(1) ?: "")
        binding.et3.setText(resList.getOrNull(2) ?: "")
        binding.et4.setText(resList.getOrNull(3) ?: "")

        binding.et1.hint = bean.tip1
        binding.et2.hint = bean.tip2
        binding.et3.hint = bean.tip3
        binding.et4.hint = bean.tip4
        binding.tvBtn.setOnClickListener {
            if (InCludeUtils.areAllEditTextsNotNullOrEmpty(
                    binding.et1,
                    binding.et2,
                    binding.et3,
                    binding.et4
                )
                    .not()
            ) {
                ToastUtils.show("设置指令不能为空")
                return@setOnClickListener
            }
            bean.command =
                "${bean.command + binding.et1.text},${binding.et2.text},${binding.et3.text},${binding.et4.text}"
            itemClick.invoke(bean)
        }
    }
}