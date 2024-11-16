package com.module.connect.fragment

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.DRAWING_CACHE_QUALITY_HIGH
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.module.connect.dialog.ResultDialog
import com.module.connect.model.CommandModel
import com.module.connect.util.BLEUtils
import com.module.connect.util.LiveDataBus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var mSetAdapter: SettingAdapter
    private val mList = mutableListOf<CommandBean>()

    private val viewModel: CommandModel by viewModels()
    private var progressDialog: ProgressDialog? = null


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
            mSetAdapter = SettingAdapter { _, res ->
                Log.e("------", "set-command：${res}")
                HomeFragment.SEND_COMMAND = res
                progressDialog = ProgressDialog.show(requireContext(), "指令发送中", "")
                BLEUtils.sendCommand(res) {
                    LiveDataBus.postString(IConsts.KEY_COMMEND_RES, HomeFragment.SEND_COMMAND)
                    lifecycleScope.launch {
                        delay(1000)
                        progressDialog?.dismiss()
                    }
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
        viewModel.settingList.observe(viewLifecycleOwner) {
            mList.addAll(it)
            mSetAdapter.submitList(mList)
        }
        viewModel.getSettingCommand()

        LiveDataBus.observeString(IConsts.KEY_COMMEND_FINAL, viewLifecycleOwner) { res ->

            res?.let {
                BLEUtils.isSuccess = false
                val homCommand = HomeFragment.cummand
                Log.e("------", "getHome：$homCommand")
                val index = getItemIndexByName(HomeFragment.cummand)
//                Log.e("setlist", "index：$index，setList：${mList}")
                if (index != -1) {
                    progressDialog?.dismiss()
//                    mSetAdapter.submitList(mList)
                    if (res == "OK\r\n") {
                        ResultDialog.newInstance(childFragmentManager, "设置成功", "请重新查询获得最新结果")
                    } else {
                        mList[index].res = it
                        mSetAdapter.notifyItemChanged(index)
                    }
                }
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