package com.tekkr.flypigeonsample.ui.views.bookingFlow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tekkr.flypigeonsample.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FlightBookingFlowActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_booking_flow)

        intent?.let {

        }
    }
}