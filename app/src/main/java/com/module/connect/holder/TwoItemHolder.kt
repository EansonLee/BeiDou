package com.module.connect.holder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemTwoBinding
import com.module.connect.util.singleClick

class TwoItemHolder(private val binding: ItemTwoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean) -> Unit) {
        binding.tvName.text = bean.name
        val resList = if (!TextUtils.isEmpty(bean.res)) {
            bean.res.substringAfter("=").split(",")
        } else {
            emptyList()
        }
        binding.et1.text = resList.getOrNull(0) ?: ""
        binding.et2.text = resList.getOrNull(1) ?: ""

        binding.et1.hint = bean.tip1
        binding.et2.hint = bean.tip2
        binding.tvBtn.singleClick( {
            itemClick.invoke(bean)
        })
    }
}