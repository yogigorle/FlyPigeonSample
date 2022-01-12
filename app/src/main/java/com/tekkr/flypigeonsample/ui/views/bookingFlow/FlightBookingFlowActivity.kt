package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.tekkr.flypigeonsample.PaymentFragmentDirections
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirFareItinerary
import com.tekkr.flypigeonsample.data.models.RevalidateFlightResult
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_flight_booking_flow.*
import kotlinx.android.synthetic.main.activity_flight_booking_flow.tv_flight_name
import kotlinx.android.synthetic.main.one_way_flight_info_item.*
import javax.inject.Inject

@AndroidEntryPoint
class FlightBookingFlowActivity : BaseActivity(), PaymentResultWithDataListener {

    private var srcAndDest = ""
    private var journeyOverview = ""
    private var adultCount = 0
    private var childCount = 0
    private var infantCount = 0
    private var isPassportRequired = false
    private var fareSourceCode = ""

    private var paymentStatusListener: PaymentStatusListener? = null

    @SuppressLint("SetTextIn")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_booking_flow)

        val navController = findNavController(R.id.flight_booking_flow_nav_host_fragment)

        intent?.let { intent ->

            adultCount =
                intent.getIntExtra(Constants.FlightJourneyParams.AdultsCount.param, 0)
            childCount =
                intent.getIntExtra(Constants.FlightJourneyParams.ChildrenCount.param, 0)
            infantCount =
                intent.getIntExtra(Constants.FlightJourneyParams.InfantsCount.param, 0)

            val revalidateFlightResult =
                intent.getParcelableExtra<RevalidateFlightResult.AirRevalidateResponse.AirRevalidateResult>(
                    Constants.revalidatedFlightResult
                ) as RevalidateFlightResult.AirRevalidateResponse.AirRevalidateResult
            revalidateFlightResult.let {
                val fareItinerary = it.fareItineraries.fareItinerary
                val selectedClass =
                    intent.getStringExtra(Constants.FlightJourneyParams.FlightClass.param) ?: ""
                val stopInfo =
                    if (fareItinerary.originDestinationOptions[0].TotalStops > 0) "${fareItinerary.originDestinationOptions[0].TotalStops} stop" else "non-stop"

                with(fareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment) {
                    tv_dep_date.text = readableDepDate
                    tv_arr_date.text = readableArrDate
                    tv_dep_time.text = formattedDepTime
                    tv_arrival_time.text = formattedArrTime
                }
                val flightDuration =
                    fareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.formattedFlightDuration
                val srcCity =
                    intent.getStringExtra(Constants.sourceAnDestCity)?.substringBefore("to")
                val destCity =
                    intent.getStringExtra(Constants.sourceAnDestCity)?.substringAfter("to")
                val srcAirportCode =
                    fareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.DepartureAirportLocationCode
                val destAirportCode =
                    fareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.ArrivalAirportLocationCode
                tv_flight_duration.text = flightDuration
                journeyOverview = "$selectedClass | $stopInfo | $flightDuration"
                srcAndDest = "$srcCity to $destCity"
                tv_journey_overview.text = journeyOverview
                tv_src_city.text = "$srcCity($srcAirportCode)"
                tv_dest_city.text = "$destCity($destAirportCode)"
                tv_src_and_dest.text = srcAndDest
                with(tv_refund_status) {
                    if (fareItinerary.airItineraryFareInfo.IsRefundable) {
                        strokeColor = ColorStateList.valueOf(Color.GREEN)
                        setTextColor(Color.GREEN)
                        text = "Refundable"
                    } else {
                        strokeColor = ColorStateList.valueOf(Color.RED)
                        setTextColor(Color.RED)
                        text = "Non Refundable"
                    }
                }

                tv_flight_name.text =
                    fareItinerary.originDestinationOptions[0].originDestinationOption[0].flightSegment.operatingAirline.formattedFlightCode

                tv_total_price.text =
                    fareItinerary.airItineraryFareInfo.fareBreakdown[0].passengerFare.totalFare.formattedTotalFare

                isPassportRequired = fareItinerary.IsPassportMandatory
                fareSourceCode = fareItinerary.airItineraryFareInfo.FareSourceCode

                //get src and dest airport city names
                with(baseViewModel) {
                    srcAirportName.observe(this@FlightBookingFlowActivity, Observer {
                        it?.let {
                            tv_src_airport_name.text = it
                        }
                    })

                    destAirportName.observe(this@FlightBookingFlowActivity, Observer {
                        it?.let {
                            tv_dest_airport_name.text = it
                        }
                    })
                }


            }

        }


        btn_continue_booking.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.flightReviewFragment -> {
                    val action =
                        FlightReviewFragmentDirections.actionFlightReviewFragmentToTravellerDetailsFragment(
                            srcAndDest,
                            journeyOverview,
                            adultCount,
                            childCount,
                            infantCount,
                            isPassportRequired
                        )
                    navController.navigate(action)
                    btn_continue_booking.text = "Continue Payment"
                }
                R.id.travellerDetailsFragment -> {
                    val action =
                        TravellerDetailsFragmentDirections.actionTravellerDetailsFragmentToPaymentFragment(
                            "order_IiOmdLJMUcsabv"
                        )
                    navController.navigate(action)
                    //make some ui adjustments
                    btn_continue_booking.visibility = GONE
                    ll_grand_total.gravity = Gravity.CENTER
                }
            }
        }

        iv_back.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.flightReviewFragment -> {
                    finish()
                }
                R.id.travellerDetailsFragment -> {
                    navController.popBackStack()
                    btn_continue_booking.text = "Continue Booking"
                }
                R.id.paymentFragment -> {
                    navController.popBackStack()
                    btn_continue_booking.visibility = VISIBLE
                    btn_continue_booking.text = "Continue Payment"
                    ll_grand_total.gravity = Gravity.START
                }
            }

        }

        btn_fare_rules.setOnClickListener {
            val intent = Intent(this, ExtraFlightDetailsActivity::class.java)
            intent.putExtra(Constants.fareSourceCode, fareSourceCode)
            startActivity(intent)
        }
    }

    fun setPaymentStatusListener(listener: PaymentStatusListener) {
        paymentStatusListener = listener
    }

    interface PaymentStatusListener {
        fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?)
        fun onPaymentError(errorCode: Int, errorDescription: String?, paymentData: PaymentData?)
    }

    override fun onPaymentSuccess(rzpPaymentId: String?, paymentData: PaymentData?) {
        paymentStatusListener?.onPaymentSuccess(rzpPaymentId, paymentData)
    }

    override fun onPaymentError(
        errorCode: Int,
        errorDescription: String?,
        paymentData: PaymentData?
    ) {
        paymentStatusListener?.onPaymentError(errorCode, errorDescription, paymentData)
    }


}