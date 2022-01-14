package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.FlightDetailsForReview
import com.tekkr.flypigeonsample.data.models.RevalidateFlightResult
import com.tekkr.flypigeonsample.databinding.ActivityFlightBookingFlowBinding
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_flight_booking_flow.*
import kotlinx.android.synthetic.main.fragment_one_way.*
import kotlinx.android.synthetic.main.one_way_flight_info_item.*

@AndroidEntryPoint
class FlightBookingFlowActivity : BaseActivity(), PaymentResultWithDataListener {

    lateinit var flightBookingFlowDatBinding: ActivityFlightBookingFlowBinding

    private var srcAndDest = ""
    private var journeyOverview = ""
    private var adultCount = 0
    private var childCount = 0
    private var infantCount = 0
    private var isPassportRequired = false
    private var fareSourceCode = ""

    private var paymentStatusListener: PaymentStatusListener? = null
    private var returnFlightDetails: FlightDetailsForReview? = null

    @SuppressLint("SetTextIn")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flightBookingFlowDatBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_flight_booking_flow)

        val navController = findNavController(R.id.flight_booking_flow_nav_host_fragment)

        intent?.let { intent ->

            adultCount =
                intent.getIntExtra(Constants.FlightJourneyParams.AdultsCount.param, 0)
            childCount =
                intent.getIntExtra(Constants.FlightJourneyParams.ChildrenCount.param, 0)
            infantCount =
                intent.getIntExtra(Constants.FlightJourneyParams.InfantsCount.param, 0)


            val oneWayRevalidatedFlightDetails =
                intent.getParcelableExtra<FlightDetailsForReview>(
                    Constants.FlightTypes.OneWay.type
                ) as FlightDetailsForReview

            if (intent.hasExtra(Constants.FlightTypes.Return.type)) {
                returnFlightDetails =
                    intent.getParcelableExtra<FlightDetailsForReview>(Constants.FlightTypes.Return.type) as FlightDetailsForReview

                tv_total_price.text = intent.getStringExtra(Constants.roundTripTotalFare)
            }

            with(flightBookingFlowDatBinding) {
                oneWayFlightDetails = oneWayRevalidatedFlightDetails

                returnFlightDetails?.let {
                    returnFlightDetails = it

                }
                isPassportRequired =
                    oneWayRevalidatedFlightDetails.revalidatedFareItinerary.IsPassportMandatory
                fareSourceCode =
                    oneWayRevalidatedFlightDetails.revalidatedFareItinerary.airItineraryFareInfo.FareSourceCode

                //get src and dest airport city names
                with(baseViewModel) {
                    srcAirportName.observe(this@FlightBookingFlowActivity, Observer {
                        it?.let {
                            tv_oneway_src_airport_name.text = it
                            tv_return_dest_airport_name.text = it
                        }
                    })
                    destAirportName.observe(this@FlightBookingFlowActivity, Observer {
                        it?.let {
                            tv_oneway_dest_airport_name.text = it
                            tv_return_src_airport_name.text = it
                        }
                    })
                }

                btnContinueBooking.setOnClickListener {
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


                btn_oneway_fare_rules.setOnClickListener {
                    val launchIntent = Intent(
                        this@FlightBookingFlowActivity,
                        ExtraFlightDetailsActivity::class.java
                    )
                    launchIntent.putExtra(Constants.fareSourceCode, fareSourceCode)
                    startActivity(launchIntent)
                }

            }
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