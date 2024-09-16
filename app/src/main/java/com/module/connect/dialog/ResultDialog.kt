package com.module.connect.dialog

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.module.connect.databinding.DialogResultBinding
import com.module.connect.ext.makeArguments
import com.module.connect.ext.params

class ResultDialog : BaseFragmentDialog<DialogResultBinding>() {

    companion object {
        fun newInstance(
            manager: FragmentManager,
            res: String,
            tip: String,
        ) {
            ResultDialog().apply {
                makeArguments("key_res" to res, "key_tip" to tip)
                show(manager, "ResultDialog")
            }
        }
    }

    private val res: String by params("key_res") { "" }
    private val tip: String? by params("key_tip") { "" }

    override fun getBinding(
        inflate: LayoutInflater,
        container: ViewGroup?
    ): DialogResultBinding {
        return DialogResultBinding.inflate(inflate)
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (TextUtils.isEmpty(tip)) {
            binding.tv2.visibility = View.GONE
            binding.tvTips.visibility = View.GONE
        } else {
            binding.tv2.visibility = View.VISIBLE
            binding.tvTips.visibility = View.VISIBLE
            binding.tvTips.text = tip
        }
        binding.tvResult.text = res
    }

    override fun initData() {
    }

}