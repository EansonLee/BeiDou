package com.module.connect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.module.connect.consts.IConsts
import com.module.connect.databinding.FragmentSettingBinding
import com.module.connect.util.LiveDataBus

class SettingFragment : Fragment(){

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView(){
        binding.tvSend.setOnClickListener {
            HomeFragment.cummand = "AT_BRTTT"
            LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "great")
        }
    }

    private fun initData() {

    }
}