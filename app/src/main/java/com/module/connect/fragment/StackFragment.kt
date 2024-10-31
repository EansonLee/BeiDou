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
import com.module.connect.util.LiveDataBus

class StackFragment : Fragment() {

    private var _binding: FragmentStackBinding? = null
    private val binding get() = _binding!!

    private var mStackAdapter: StackAdapter? = null
    private var mStackList = mutableListOf<StackBean>()

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
    }

    private fun initData() {
        LiveDataBus.observeString(IConsts.KEY_COMMEND_RES, viewLifecycleOwner) { res->
            res?.let {
                val stack = StackBean(HomeFragment.cummand, it)
                mStackAdapter?.addData(stack)
            }
        }
    }
}