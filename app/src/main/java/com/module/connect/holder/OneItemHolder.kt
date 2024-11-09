package com.module.connect.holder

import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemOneBinding
import com.module.connect.util.singleClick

class OneItemHolder(private val binding: ItemOneBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean) -> Unit) {
        binding.tvName.text = bean.name
        if (!TextUtils.isEmpty(bean.res)) {
            binding.etRes.text = ""
            val res = bean.res.substringAfter("=", null.toString())
            binding.etRes.text = res
        }
        binding.tvBtn.singleClick({
            itemClick.invoke(bean)
        })
    }
}