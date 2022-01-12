package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.ui.viewmodels.FlightBookingViewModel
import com.tekkr.flypigeonsample.utils.Constants
import kotlinx.android.synthetic.main.activity_extra_flight_details.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import kotlinx.android.synthetic.main.standarad_toolbar.*
import kotlinx.android.synthetic.main.standarad_toolbar.view.*

class ExtraFlightDetailsActivity : BaseActivity() {

    private val flightBookingViewModel: FlightBookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_flight_details)

        uiRelatedOps()

        intent?.let {
            val fareSourceCode = it.getStringExtra(Constants.fareSourceCode) ?: ""
            flightBookingViewModel.getFareRules(fareSourceCode).observe(this, Observer {
                progress_bar_view.visibility = VISIBLE
                handleApiCall(it) {
                    progress_bar_view.visibility = GONE
                    tv_fare_rules.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(
                            it.FareRules1_1Response.FareRules1_1Result.FareRules[0].fareRule.Rules,
                            Html.FROM_HTML_MODE_LEGACY
                        )
                    } else {
                        Html.fromHtml(it.FareRules1_1Response.FareRules1_1Result.FareRules[0].fareRule.Rules)
                    }
                }

            })
        }
    }

    private fun uiRelatedOps() {
        tv_toolbar_title.text = "Fare Rules"
        fl_back.setOnClickListener {
            finish()
        }
    }

}