package com.tekkr.flypigeonsample.ui.views.flights

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import java.io.Serializable

@AndroidEntryPoint
class FlightsListActivity : BaseActivity() {

    var flightSearchType = Constants.oneWay

    private val flightsSearchViewModel: FlightSearchViewModel by viewModels()
    private val oneWayFlightSearchAdapter by lazy { OneWayFlightsListAdapter(::onOneWayFlightItemClicked) }
    private val roundTripDepFlightSearchAdapter by lazy { RoundTripFlightsListAdapter() }
    private val roundTripArrFlightSearchAdapter by lazy { RoundTripFlightsListAdapter() }

    //some important params
    var flightSrcAndDest = ""
    var selectedClass = ""
    var adultsCount = ""
    var childrenCount = ""
    var infantsCount = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flights_list)

        intent?.let {
            flightSearchType =
                (it.getStringExtra(Constants.FlightSearchQueryParams.JourneyType.param)
                    ?: "")
            selectedClass = (it.getStringExtra(Constants.FlightSearchQueryParams.FlightClass.param)
                ?: "")
            adultsCount = it.getIntExtra(
                Constants.FlightSearchQueryParams.AdultsCount.param,
                0
            ).toString()
            childrenCount = it.getIntExtra(
                Constants.FlightSearchQueryParams.ChildrenCount.param, 0
            ).toString()
            infantsCount = it.getIntExtra(
                Constants.FlightSearchQueryParams.InfantsCount.param,
                0
            ).toString()
            val flightSearchParams = hashMapOf(
                Constants.FlightSearchQueryParams.JourneyType.param to flightSearchType,
                Constants.FlightSearchQueryParams.SrcAirPortCode.param to (it.getStringExtra(
                    Constants.FlightSearchQueryParams.SrcAirPortCode.param
                ) ?: ""),
                Constants.FlightSearchQueryParams.DestAirPortCode.param to (it.getStringExtra(
                    Constants.FlightSearchQueryParams.DestAirPortCode.param
                ) ?: ""),
                Constants.FlightSearchQueryParams.DepDate.param to (it.getStringExtra(Constants.FlightSearchQueryParams.DepDate.param)
                    ?: ""),
                Constants.FlightSearchQueryParams.ReturnDate.param to (it.getStringExtra(Constants.FlightSearchQueryParams.ReturnDate.param)
                    ?: ""),
                Constants.FlightSearchQueryParams.AdultsCount.param to adultsCount,
                Constants.FlightSearchQueryParams.ChildrenCount.param to childrenCount,
                Constants.FlightSearchQueryParams.InfantsCount.param to infantsCount,
                Constants.FlightSearchQueryParams.FlightClass.param to selectedClass
            )

            flightSrcAndDest =
                "${it.getStringExtra(Constants.FlightSearchQueryParams.SrcCity.param)} to ${
                    it.getStringExtra(Constants.FlightSearchQueryParams.DestCity.param)
                }"
            tv_toolbar_title.text = flightSrcAndDest


            tv_departure_date.text =
                it.getStringExtra(Constants.FlightSearchQueryParams.FormattedDepDate.param) ?: ""

            //get api call
            if (flightSearchType == Constants.oneWay) {
                flightsSearchViewModel.getOneWayFlightSearchResults(flightSearchParams)
                    .observe(this, Observer {
                        handleApiCall(it) { searchResult ->
                            oneWayFlightSearchAdapter.submitList(searchResult.AirSearchResponse.AirSearchResult.FareItineraries)
                            rv_one_way_flights.visibility = VISIBLE
                            rv_one_way_flights.adapter = oneWayFlightSearchAdapter
                        }
                    })
            } else {
                flightsSearchViewModel.getRoundTripFlightSearchResults(flightSearchParams)
                    .observe(this, Observer {
                        handleApiCall(it) { searchResult ->
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
        val intent = Intent(this, FlightBookingFlowActivity::class.java)
        with(intent) {
            putExtra(
                Constants.fareItinerary,
                airFareItinerary.fareItinerary
            )
            putExtra(Constants.FlightSearchQueryParams.FlightClass.param, selectedClass)
            putExtra(Constants.sourceAnDestCity, flightSrcAndDest)
            putExtra(Constants.FlightSearchQueryParams.ChildrenCount.param, childrenCount.toInt())
            putExtra(Constants.FlightSearchQueryParams.AdultsCount.param, adultsCount.toInt())
            putExtra(Constants.FlightSearchQueryParams.InfantsCount.param, infantsCount.toInt())

        }


        startActivity(intent)
    }
}