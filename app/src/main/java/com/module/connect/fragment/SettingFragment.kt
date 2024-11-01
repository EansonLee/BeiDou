package com.module.connect.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.DRAWING_CACHE_QUALITY_HIGH
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.module.connect.adapter.SettingAdapter
import com.module.connect.bean.CommandBean
import com.module.connect.bean.STYLE_FIVE
import com.module.connect.bean.STYLE_FOUR
import com.module.connect.bean.STYLE_ONE
import com.module.connect.bean.STYLE_SEVEN
import com.module.connect.bean.STYLE_SIX
import com.module.connect.bean.STYLE_THIRD
import com.module.connect.bean.STYLE_TWO
import com.module.connect.consts.IConsts
import com.module.connect.databinding.FragmentSettingBinding
import com.module.connect.model.CommandModel
import com.module.connect.util.BLEUtils
import com.module.connect.util.LiveDataBus

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var mSetAdapter: SettingAdapter
    private val mList = mutableListOf<CommandBean>()

    private val viewModel: CommandModel by viewModels()


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

    private fun initView() {
        with(binding.rvRecv) {
            mSetAdapter = SettingAdapter {
                Log.e("------", "set-command：${it.command}")
                BLEUtils.sendCommand("${it.command}\r\n") {
                }
            }
            itemAnimator = null
            animation = null
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mSetAdapter
            setItemViewCacheSize(2)
            isDrawingCacheEnabled = true
            drawingCacheQuality = DRAWING_CACHE_QUALITY_HIGH
            val pool = recycledViewPool
            pool.setMaxRecycledViews(STYLE_ONE, 2)
            pool.setMaxRecycledViews(STYLE_TWO, 2)
            pool.setMaxRecycledViews(STYLE_THIRD, 2)
            pool.setMaxRecycledViews(STYLE_FOUR, 2)
            pool.setMaxRecycledViews(STYLE_FIVE, 2)
            pool.setMaxRecycledViews(STYLE_SIX, 2)
            pool.setMaxRecycledViews(STYLE_SEVEN, 2)
            setRecycledViewPool(pool)
        }
    }

    private fun initData() {
        viewModel.commandList.observe(viewLifecycleOwner) {
            mList.addAll(it)
            mSetAdapter.submitList(mList)
        }
        viewModel.getSettingCommand()

        LiveDataBus.observeString(IConsts.KEY_COMMEND_RES, viewLifecycleOwner) { res ->

            res?.let {
                BLEUtils.isSuccess = false
                val index = getItemIndexByName(HomeFragment.cummand)
                mList[index].res = it
                mSetAdapter.submitList(mList)
            }
        }
    }

    private fun getItemIndexByName(command: String): Int {
        return mList.indexOfFirst { it.command == command }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}