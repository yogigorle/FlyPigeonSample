package com.tekkr.flypigeonsample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tekkr.flypigeonsample.fragments.MultiCityFragment
import com.tekkr.flypigeonsample.fragments.OneWayFragment
import com.tekkr.flypigeonsample.fragments.RoundTripFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OneWayFragment()
            1 -> RoundTripFragment()
            2 -> MultiCityFragment()
            else -> OneWayFragment()
        }
    }

}