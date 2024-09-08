package com.module.connect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.module.connect.R
import com.module.connect.ext.init
import com.module.connect.ext.initMain
import com.module.connect.ext.interceptLongClick

class MainFragment : Fragment() {

    private var mainViewpager: ViewPager2? = null
    private var mainBottom: BottomNavigationViewEx? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
    }

    private fun initView(root: View) {
        mainViewpager = root.findViewById(R.id.mainViewpager)
        mainBottom = root.findViewById(R.id.mainBottom)

        //初始化viewpager2
        mainViewpager?.initMain(this)
        //初始化 bottomBar
        mainBottom?.init {
            when (it) {
                R.id.menu_main -> mainViewpager?.setCurrentItem(0, false)
            }
        }
        mainBottom?.interceptLongClick(R.id.menu_main)
    }
}