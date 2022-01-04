package com.tekkr.flypigeonsample.ui.views.airportssearch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tekkr.flypigeonsample.R
import kotlinx.android.synthetic.main.activity_search_airports.*
import kotlinx.android.synthetic.main.standarad_toolbar.*

class SearchAirportsActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_airports)

        initUi()

    }

    private fun initUi() {

        layout_toolbar.setBackgroundColor(Color.BLACK)
        tv_toolbar_title.typeface = Typeface.SANS_SERIF

        intent?.let {
            tv_toolbar_title.text = "Select ${it.getStringExtra("source_type")} City"
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