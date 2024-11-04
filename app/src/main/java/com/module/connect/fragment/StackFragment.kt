package com.module.connect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.module.connect.adapter.StackAdapter
import com.module.connect.bean.StackBean
import com.module.connect.consts.IConsts
import com.module.connect.databinding.FragmentStackBinding
import com.module.connect.util.BLEUtils
import com.module.connect.util.InCludeUtils
import com.module.connect.util.LiveDataBus
import com.module.connect.util.ToastUtils

class StackFragment : Fragment() {

    private var _binding: FragmentStackBinding? = null
    private val binding get() = _binding!!

    private var mStackAdapter: StackAdapter? = null
    private var mStackList = mutableListOf<StackBean>()
    private var isCustom = false
    private var mCustomCommand = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStackBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        mStackAdapter = StackAdapter()
        binding.rvStack.adapter = mStackAdapter
        binding.rvStack.layoutManager = LinearLayoutManager(context)
        mStackAdapter?.setData(mStackList)


        binding.tvSend.setOnClickListener {
            if (InCludeUtils.areAllEditTextsNotNullOrEmpty(binding.etSend).not()) {
                ToastUtils.show("设置指令不能为空")
                return@setOnClickListener
            }
            mCustomCommand = binding.etSend.text.toString()
            if (!mCustomCommand.startsWith("AT")) {
                ToastUtils.show("请输入正确指令")
                return@setOnClickListener
            }
            isCustom = true
            BLEUtils.sendCommand("${mCustomCommand}\r\n") {
//                || mCustomCommand == "AT+OTA/4G"
//                || mCustomCommand == "AT+OTA_UPDATE"
                if (mCustomCommand == "AT+REBOOT" || mCustomCommand == "AT+CLEAR" || mCustomCommand == "AT+SAVE") {
                    BLEUtils.isConnected = false
                    LiveDataBus.postString(IConsts.KEY_COMMEND_RES, "发送成功")
                }
            }
        }
    }

    private fun initData() {
        LiveDataBus.observeString(IConsts.KEY_COMMEND_RES, viewLifecycleOwner) { res ->
            res?.let {
                var mSendCommand = ""
                if (isCustom) {
                    mSendCommand = mCustomCommand
                    isCustom = false
                    mCustomCommand = ""
                } else {
                    mSendCommand = HomeFragment.SEND_COMMAND
                }
                val stack = StackBean(mSendCommand, it)
                mStackAdapter?.addData(stack)

            }
        }
    }
}