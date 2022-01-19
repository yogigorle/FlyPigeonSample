package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.BookingDetails
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.ui.viewmodels.FlightBookingViewModel
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.showToast
import com.tekkr.flypigeonsample.utils.toJson
import kotlinx.android.synthetic.main.activity_trip_details.*
import kotlinx.android.synthetic.main.flight_ticket_item.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import kotlinx.android.synthetic.main.standarad_toolbar.*

class TripDetailsShowCaseActivity : BaseActivity() {

    private val flightBookingViewModel: FlightBookingViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        intent?.let {
            val bookingUniqueId = it.getStringExtra(Constants.bookingUniqueId)
            bookingUniqueId?.let {
                flightBookingViewModel.getTripDetails(it).observe(this, Observer {
                    progress_bar_view.visibility = VISIBLE
                    handleApiCall(it) {
                        if (it.tripDetailsResponse.tripDetailsResult.Success.equals("true", true)) {
                            ll_tickets.removeAllViews()
                            for (customerDetails in it.tripDetailsResponse.tripDetailsResult.travelItinerary.itineraryInfo.CustomerInfos) {
                                val ticketLayout = LayoutInflater.from(this)
                                    .inflate(R.layout.flight_ticket_item, null, false)
                                with(ticketLayout) {
                                    tv_name.text =
                                        "${customerDetails.customerInfo.PassengerTitle} ${customerDetails.customerInfo.PassengerFirstName} ${customerDetails.customerInfo.PassengerLastName}"
                                    tv_gender.text = customerDetails.customerInfo.Gender
                                    tv_dob.text = customerDetails.customerInfo.DateOfBirth
                                    tv_e_ticket.text = customerDetails.customerInfo.eTicketNumber
                                }

                                ll_tickets.addView(ticketLayout)

                            }
                        }
                    }

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