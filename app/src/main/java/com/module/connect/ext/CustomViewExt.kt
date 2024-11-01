package com.module.connect.ext

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.Utils
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.module.connect.fragment.HomeFragment
import com.module.connect.fragment.SettingFragment
import com.module.connect.fragment.StackFragment
import com.module.connect.util.SettingUtil


fun ViewPager2.initMain(fragment: Fragment): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 3
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return HomeFragment()
                }

                1 -> {
                    return SettingFragment()
                }

                2 -> {
                    return StackFragment()
                }
//                3 -> {
//                    return PublicNumberFragment()
//                }
//                4 -> {
//                    return MeFragment()
//                }
                else -> {
                    return HomeFragment()
                }
            }
        }

        override fun getItemCount() = 3
    }
    return this
}

fun BottomNavigationViewEx.init(navigationItemSelectedAction: (Int) -> Unit): BottomNavigationViewEx {
    enableAnimation(true)
    enableShiftingMode(false)
    enableItemShiftingMode(true)
    itemIconTintList = SettingUtil.getColorStateList(SettingUtil.getColor(Utils.getApp()))
    itemTextColor = SettingUtil.getColorStateList(Utils.getApp())
    setTextSize(12F)
    setOnNavigationItemSelectedListener {
        navigationItemSelectedAction.invoke(it.itemId)
        true
    }
    return this
}

/**
 * 拦截BottomNavigation长按事件 防止长按时出现Toast ---- 追求完美的大屌群友提的bug
 * @receiver BottomNavigationViewEx
 * @param ids IntArray
 */
fun BottomNavigationViewEx.interceptLongClick(vararg ids: Int) {
    val bottomNavigationMenuView: ViewGroup = (this.getChildAt(0) as ViewGroup)
    for (index in ids.indices) {
        bottomNavigationMenuView.getChildAt(index).findViewById<View>(ids[index])
            .setOnLongClickListener {
                true
            }
    }
}
