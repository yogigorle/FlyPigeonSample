package com.tekkr.flypigeonsample.ui.views.bookingFlow

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirFareItinerary
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_flight_booking_flow.*

@AndroidEntryPoint
class FlightBookingFlowActivity : BaseActivity() {

    var fareItinerary: AirFareItinerary.FareItinerary? = null
    var src_and_dest = ""
    var journey_overview = ""
    var adultCount = 0
    var childCount = 0
    var infantCount = 0
    var isPassportRequired = false


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

            val fareItinerary =
                intent.getParcelableExtra<AirFareItinerary.FareItinerary>(Constants.fareItinerary) as AirFareItinerary.FareItinerary
            fareItinerary.let {
                val selectedClass =
                    intent.getStringExtra(Constants.FlightJourneyParams.FlightClass.param) ?: ""
                val stopInfo =
                    if (it.OriginDestinationOptions[0].TotalStops > 0) "${it.OriginDestinationOptions[0].TotalStops} stop" else "non-stop"
                val flightDuration =
                    it.OriginDestinationOptions[0].originDestinationOption[0].stopQuantityInfo.formattedFlightDuration
                val srcCity =
                    intent.getStringExtra(Constants.sourceAnDestCity)?.substringBefore("to")
                val destCity =
                    intent.getStringExtra(Constants.sourceAnDestCity)?.substringAfter("to")
                val srcAirportCode =
                    it.OriginDestinationOptions[0].originDestinationOption[0].flightSegment.DepartureAirportLocationCode
                val destAirportCode =
                    it.OriginDestinationOptions[0].originDestinationOption[0].flightSegment.ArrivalAirportLocationCode
                tv_flight_duration.text = flightDuration
                journey_overview = "$selectedClass | $stopInfo | $flightDuration"
                src_and_dest = "$srcCity to $destCity"
                tv_journey_overview.text = journey_overview
                tv_src_city.text = "$srcCity($srcAirportCode)"
                tv_dest_city.text = "$destCity($destAirportCode)"
                tv_src_and_dest.text = src_and_dest
                with(tv_refund_status) {
                    if (it.airItineraryFareInfo.IsRefundable) {
                        strokeColor = ColorStateList.valueOf(Color.GREEN)
                        setTextColor(Color.GREEN)
                        text = "Refundable"
                    } else {
                        strokeColor = ColorStateList.valueOf(Color.RED)
                        setTextColor(Color.RED)
                        text = "Non Refundable"
                    }
                }
                tv_dep_date.text =
                    it.OriginDestinationOptions[0].originDestinationOption[0].stopQuantityInfo.readableDepDate
                tv_arr_date.text =
                    it.OriginDestinationOptions[0].originDestinationOption[0].stopQuantityInfo.readableArrDate
                tv_dep_time.text =
                    it.OriginDestinationOptions[0].originDestinationOption[0].stopQuantityInfo.formattedDepTime
                tv_arrival_time.text =
                    it.OriginDestinationOptions[0].originDestinationOption[0].stopQuantityInfo.formattedArrTime
                tv_flight_name.text =
                    it.OriginDestinationOptions[0].originDestinationOption[0].flightSegment.operatingAirline.formattedFlightCode

                tv_total_price.text =
                    fareItinerary.airItineraryFareInfo.fareBreakdown[0].passengerFare.totalFare.formattedTotalFare

                isPassportRequired = fareItinerary.IsPassportMandatory
            }


        }

        btn_continue_booking.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.flightReviewFragment -> {
                    val action =
                        FlightReviewFragmentDirections.actionFlightReviewFragmentToTravellerDetailsFragment(
                            src_and_dest,
                            journey_overview,
                            adultCount,
                            childCount,
                            infantCount,
                            isPassportRequired
                        )

                    navController.navigate(action)
                }
            }
        }

        iv_back.setOnClickListener {
            if(navController.currentDestination?.id == R.id.flightReviewFragment){
                finish()
            }else{
                navController.popBackStack()
            }
        }
    }


}