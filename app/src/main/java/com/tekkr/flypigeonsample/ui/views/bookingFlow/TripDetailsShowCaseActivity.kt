package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.BookingDetails
import com.tekkr.flypigeonsample.data.models.FlightTripDetails
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.ui.viewmodels.FlightBookingViewModel
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.showToast
import com.tekkr.flypigeonsample.utils.toJson
import kotlinx.android.synthetic.main.activity_trip_details.*
import kotlinx.android.synthetic.main.flight_ticket_item.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import kotlinx.android.synthetic.main.standarad_toolbar.*
import java.lang.Exception

class TripDetailsShowCaseActivity : BaseActivity() {

    private val flightBookingViewModel: FlightBookingViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_details)

        intent?.let {
            val bookingUniqueId = it.getStringExtra(Constants.bookingUniqueId)
            val returnFlightBookingId = it.getStringExtra(Constants.returnBookingUniqueId)
            bookingUniqueId?.let {

                getTripDetails(it) {
                    if (returnFlightBookingId?.isNotEmpty()!!) {
                        getTripDetails(returnFlightBookingId) {
                            tv_return_tickets.text =
                                "Tickets for route ${it.tripDetailsResponse.tripDetailsResult.travelItinerary.Origin} to ${it.tripDetailsResponse.tripDetailsResult.travelItinerary.Destination}"
                            ll_return_tickets.visibility = VISIBLE
                            drawTicketDetails(
                                ll_return_tickets,
                                it.tripDetailsResponse.tripDetailsResult.travelItinerary.itineraryInfo.CustomerInfos
                            )
                        }

                    }
                    tv_one_way_tickets.text =
                        "Tickets for route ${it.tripDetailsResponse.tripDetailsResult.travelItinerary.Origin} to ${it.tripDetailsResponse.tripDetailsResult.travelItinerary.Destination}"
                    drawTicketDetails(
                        ll_tickets,
                        it.tripDetailsResponse.tripDetailsResult.travelItinerary.itineraryInfo.CustomerInfos
                    )
                }


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

    private fun drawTicketDetails(
        linearLayout: LinearLayout,
        customerDetails: List<FlightTripDetails.TripDetailsResponse.TripDetailsResult.TravelItinerary.ItineraryInfo.CustomerInfo>
    ) {
        linearLayout.removeAllViews()
        for (customer in customerDetails) {
            val ticketLayout = LayoutInflater.from(this)
                .inflate(R.layout.flight_ticket_item, ll_tickets, false)
            with(ticketLayout) {
                tv_traveller_full_name.text =
                    "${customer.customerInfo.PassengerTitle} ${customer.customerInfo.PassengerFirstName} ${customer.customerInfo.PassengerLastName}"
                tv_traveller_gender.text = customer.customerInfo.Gender
                tv_traveller_dob.text = customer.customerInfo.formattedDob
                tv_traveller_ticket_no.text = customer.customerInfo.eTicketNumber
            }

            linearLayout.addView(ticketLayout)

        }
    }

    private fun getTripDetails(bookingId: String, onSuccess: (FlightTripDetails) -> Unit) {

        try {
            flightBookingViewModel.getTripDetails(bookingId).observe(this, Observer {
                progress_bar_view.visibility = VISIBLE
                handleApiCall(it) {
                    progress_bar_view.visibility = GONE

                    if (it.tripDetailsResponse.tripDetailsResult.Success.equals("true", true)) {
                        onSuccess(it)
                    } else {
                        showToast("Something went wrong please try again...")
                    }

//                    onSuccess(it)
                }
            })
        }catch (e: Exception){
            showToast("Sorry Something Went Wrong please try again, later..")
        }


    }
}