package com.tekkr.flypigeonsample.ui.views.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        vp_flights.adapter = adapter

        TabLayoutMediator(tl_flights, vp_flights) { tab, position ->
            when (position) {
                0 -> tab.text = "One Way"
                1 -> tab.text = "Round Trip"
            }
        }.attach()

    }
}