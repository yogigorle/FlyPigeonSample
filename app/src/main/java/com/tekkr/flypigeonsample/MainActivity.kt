package com.tekkr.flypigeonsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

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
                2 -> tab.text = "Multi City"
            }
        }.attach()

    }
}