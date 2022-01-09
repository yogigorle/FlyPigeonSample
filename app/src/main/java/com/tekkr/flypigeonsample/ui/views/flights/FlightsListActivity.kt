package com.tekkr.flypigeonsample.ui.views.flights

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirFareItinerary
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.ui.viewmodels.FlightSearchViewModel
import com.tekkr.flypigeonsample.ui.views.bookingFlow.FlightBookingFlowActivity
import com.tekkr.flypigeonsample.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_flights_list.*
import kotlinx.android.synthetic.main.progress_bar_layout.view.*
import kotlin.math.log

@AndroidEntryPoint
class FlightsListActivity : BaseActivity() {

    var flightSearchType = Constants.oneWay

    private val flightsSearchViewModel: FlightSearchViewModel by viewModels()
    private val oneWayFlightSearchAdapter by lazy { OneWayFlightsListAdapter(::onOneWayFlightItemClicked) }
    private val roundTripDepFlightSearchAdapter by lazy { RoundTripFlightsListAdapter() }
    private val roundTripArrFlightSearchAdapter by lazy { RoundTripFlightsListAdapter() }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flights_list)

        intent?.let {
            flightSearchType =
                (it.getStringExtra(Constants.FlightSearchQueryParams.journeyType.param)
                    ?: "")
            val flightSearchParams = hashMapOf(
                Constants.FlightSearchQueryParams.journeyType.param to flightSearchType,
                Constants.FlightSearchQueryParams.srcAirPortCode.param to (it.getStringExtra(
                    Constants.FlightSearchQueryParams.srcAirPortCode.param
                ) ?: ""),
                Constants.FlightSearchQueryParams.destAirPortCode.param to (it.getStringExtra(
                    Constants.FlightSearchQueryParams.destAirPortCode.param
                ) ?: ""),
                Constants.FlightSearchQueryParams.depDate.param to (it.getStringExtra(Constants.FlightSearchQueryParams.depDate.param)
                    ?: ""),
                Constants.FlightSearchQueryParams.returnDate.param to (it.getStringExtra(Constants.FlightSearchQueryParams.returnDate.param)
                    ?: ""),
                Constants.FlightSearchQueryParams.adultsCount.param to it.getIntExtra(
                    Constants.FlightSearchQueryParams.adultsCount.param,
                    0
                ).toString(),
                Constants.FlightSearchQueryParams.childrenCount.param to it.getIntExtra(
                    Constants.FlightSearchQueryParams.childrenCount.param, 0
                ).toString(),
                Constants.FlightSearchQueryParams.infantsCount.param to it.getIntExtra(
                    Constants.FlightSearchQueryParams.infantsCount.param,
                    0
                ).toString(),
                Constants.FlightSearchQueryParams.flightClass.param to (it.getStringExtra(Constants.FlightSearchQueryParams.flightClass.param)
                    ?: "")
            )

            tv_toolbar_title.text =
                "${it.getStringExtra(Constants.FlightSearchQueryParams.srcCity.param)} to ${
                    it.getStringExtra(Constants.FlightSearchQueryParams.destCity.param)
                }"
            tv_departure_date.text =
                it.getStringExtra(Constants.FlightSearchQueryParams.formattedDepDate.param)

            //get api call

            if (flightSearchType == Constants.oneWay) {
                flightsSearchViewModel.getOneWayFlightSearchResults(flightSearchParams)
                    .observe(this, Observer {
//                        progress_bar_layout.progress_bar_view.visibility = VISIBLE
                        handleApiCall(it) { searchResult ->
//                            progress_bar_layout.progress_bar_view.visibility = GONE
                            oneWayFlightSearchAdapter.submitList(searchResult.AirSearchResponse.AirSearchResult.FareItineraries)
                            rv_one_way_flights.visibility = VISIBLE
                            rv_one_way_flights.adapter = oneWayFlightSearchAdapter
                        }
                    })
            } else {
                flightsSearchViewModel.getRoundTripFlightSearchResults(flightSearchParams)
                    .observe(this, Observer {
//                        progress_bar_layout.progress_bar_view.visibility = VISIBLE
                        handleApiCall(it) { searchResult ->
//                            progress_bar_layout.progress_bar_view.visibility = GONE
                            roundTripDepFlightSearchAdapter.submitList(searchResult.AirSearchResponse.AirSearchResult.FareItineraries[0])
                            roundTripArrFlightSearchAdapter.submitList(searchResult.AirSearchResponse.AirSearchResult.FareItineraries[1])
                            ll_round_trip_flights.visibility = VISIBLE
                            rv_first_route_flights.adapter = roundTripDepFlightSearchAdapter
                            rv_second_route_flights.adapter = roundTripArrFlightSearchAdapter
                        }
                    })
            }

            fl_back.setOnClickListener {
                finish()
            }

        }
    }

    private fun onOneWayFlightItemClicked(airFareItinerary: AirFareItinerary) {
        //call revalidate api and launch booking flow
        Log.e("fareSourceCode", airFareItinerary.fareItinerary.airItineraryFareInfo.FareSourceCode)
//        val intent = Intent(this, FlightBookingFlowActivity::class.java)
//        startActivity(intent)
    }
}