package com.tekkr.flypigeonsample.ui.views.bookingFlow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.BookingDetails
import com.tekkr.flypigeonsample.ui.viewmodels.FlightBookingViewModel
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.showToast
import com.tekkr.flypigeonsample.utils.toJson
import kotlinx.android.synthetic.main.standarad_toolbar.*

class TripDetailsShowCaseActivity : AppCompatActivity() {

    private val flightBookingViewModel: FlightBookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        intent?.let {
            val bookingUniqueId = it.getStringExtra(Constants.bookingUniqueId)
            bookingUniqueId?.let {
                flightBookingViewModel.getTripDetails(it).observe(this, Observer {

                })
            }
        }

        uiRelatedOps()
    }

    private fun uiRelatedOps() {
        tv_toolbar_title.text = "Flight Trip Details"
        iv_back.setOnClickListener {
            finish()
        }
    }
}