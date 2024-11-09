package com.module.connect.holder

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.module.connect.bean.CommandBean
import com.module.connect.databinding.ItemOneSetBinding
import com.module.connect.util.InCludeUtils
import com.module.connect.util.ToastUtils
import com.module.connect.util.singleClick

class OneItemSetHolder(private val binding: ItemOneSetBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun render(bean: CommandBean, itemClick: (CommandBean, String) -> Unit) {
        binding.tvName.text = bean.name
        binding.tvBtn.text = "设置"

        if (bean.command == "AT+OTA_UPDATE" || bean.command == "AT+OTA/4G") {
            binding.etRes.visibility = View.GONE
        }else {
            binding.etRes.visibility = View.VISIBLE
        }
        if (!TextUtils.isEmpty(bean.res)) {
            binding.etRes.setText("")
            val res = bean.res.substringAfter("=", null.toString())
            binding.etRes.setText(res)
        }

        binding.tvBtn.singleClick({
            if (bean.command != "AT+OTA_UPDATE" && bean.command != "AT+OTA/4G" && InCludeUtils.areAllEditTextsNotNullOrEmpty(
                    binding.etRes
                ).not()
            ) {
                ToastUtils.show("设置指令不能为空")
                return@singleClick
            }
            val res = bean.command + binding.etRes.text
            itemClick.invoke(bean, res)
        })
    }
}