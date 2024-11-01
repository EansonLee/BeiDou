package com.module.connect.holder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemFiveBinding

class FiveItemHolder(private val binding: ItemFiveBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean) -> Unit) {
        binding.tvName.text = bean.name
        // 提取res并更新EditText
        val resList = if (!TextUtils.isEmpty(bean.res)) {
            bean.res.substringAfter("=").split(",")
        } else {
            emptyList()
        }

        binding.et1.text = resList.getOrNull(0) ?: ""
        binding.et2.text = resList.getOrNull(1) ?: ""
        binding.et3.text = resList.getOrNull(2) ?: ""
        binding.et4.text = resList.getOrNull(3) ?: ""
        binding.et5.text = resList.getOrNull(4) ?: ""

        binding.et1.hint = bean.tip1
        binding.et2.hint = bean.tip2
        binding.et3.hint = bean.tip3
        binding.et4.hint = bean.tip4
        binding.et5.hint = bean.tip5
        binding.tvBtn.setOnClickListener {
            itemClick.invoke(bean)
        }
    }
}