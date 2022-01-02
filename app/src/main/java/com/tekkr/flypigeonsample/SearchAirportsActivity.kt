package com.tekkr.flypigeonsample

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_search_airports.*

class SearchAirportsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_airports)

        intent?.let {
            tv_page_title.text = "Select ${it.getStringExtra("source_type")} City"
        }

        iv_back.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("source_city_code_name", "vtz")
                putExtra("source_city_full_name", "Vizagpatnam")
                putExtra("dest_city_code_name", "HYD")
                putExtra("dest_city_full_name", "Hyderabad")
            })
            finish()
        }

    }

}