package com.tekkr.flypigeonsample.ui.views.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tekkr.flypigeonsample.ui.views.oneway.OneWayFragment
import com.tekkr.flypigeonsample.ui.views.roundtrip.RoundTripFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OneWayFragment()
            1 -> RoundTripFragment()
            else -> OneWayFragment()
        }
    }

}