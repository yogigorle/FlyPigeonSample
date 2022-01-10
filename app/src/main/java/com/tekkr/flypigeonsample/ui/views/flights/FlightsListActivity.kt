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
import com.tekkr.flypigeonsample.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_flights_list.*
import kotlinx.android.synthetic.main.progress_bar_layout.*

@AndroidEntryPoint
class FlightsListActivity : BaseActivity() {

    var flightSearchType = Constants.FlightJourneyParams.OneWay.param

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
                (it.getStringExtra(Constants.FlightJourneyParams.JourneyType.param)
                    ?: "")
            selectedClass = (it.getStringExtra(Constants.FlightJourneyParams.FlightClass.param)
                ?: "")
            adultsCount = it.getIntExtra(
                Constants.FlightJourneyParams.AdultsCount.param,
                0
            ).toString()
            childrenCount = it.getIntExtra(
                Constants.FlightJourneyParams.ChildrenCount.param, 0
            ).toString()
            infantsCount = it.getIntExtra(
                Constants.FlightJourneyParams.InfantsCount.param,
                0
            ).toString()
            val flightSearchParams = hashMapOf(
                Constants.FlightJourneyParams.JourneyType.param to flightSearchType,
                Constants.FlightJourneyParams.SrcAirPortCode.param to (it.getStringExtra(
                    Constants.FlightJourneyParams.SrcAirPortCode.param
                ) ?: ""),
                Constants.FlightJourneyParams.DestAirPortCode.param to (it.getStringExtra(
                    Constants.FlightJourneyParams.DestAirPortCode.param
                ) ?: ""),
                Constants.FlightJourneyParams.DepDate.param to (it.getStringExtra(Constants.FlightJourneyParams.DepDate.param)
                    ?: ""),
                Constants.FlightJourneyParams.ReturnDate.param to (it.getStringExtra(Constants.FlightJourneyParams.ReturnDate.param)
                    ?: ""),
                Constants.FlightJourneyParams.AdultsCount.param to adultsCount,
                Constants.FlightJourneyParams.ChildrenCount.param to childrenCount,
                Constants.FlightJourneyParams.InfantsCount.param to infantsCount,
                Constants.FlightJourneyParams.FlightClass.param to selectedClass
            )

            flightSrcAndDest =
                "${it.getStringExtra(Constants.FlightJourneyParams.SrcCity.param)} to ${
                    it.getStringExtra(Constants.FlightJourneyParams.DestCity.param)
                }"
            tv_toolbar_title.text = flightSrcAndDest


            tv_departure_date.text =
                it.getStringExtra(Constants.FlightJourneyParams.FormattedDepDate.param) ?: ""

            //get api call
            if (flightSearchType == Constants.FlightJourneyParams.OneWay.param) {
                flightsSearchViewModel.getOneWayFlightSearchResults(flightSearchParams)
                    .observe(this, Observer {
                        progress_bar_view.visibility = VISIBLE
                        handleApiCall(it) { searchResult ->
                            progress_bar_view.visibility = GONE
                            oneWayFlightSearchAdapter.submitList(searchResult.AirSearchResponse.AirSearchResult.FareItineraries)
                            rv_one_way_flights.visibility = VISIBLE
                            rv_one_way_flights.adapter = oneWayFlightSearchAdapter
                        }
                    })
            } else {
                flightsSearchViewModel.getRoundTripFlightSearchResults(flightSearchParams)
                    .observe(this, Observer {
                        progress_bar_view.visibility = VISIBLE
                        handleApiCall(it) { searchResult ->
                            progress_bar_view.visibility = GONE
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

        flightsSearchViewModel.revalidateFlight(airFareItinerary.fareItinerary.airItineraryFareInfo.FareSourceCode)
            .observe(this,
                Observer {
                    it?.let {
                        progress_bar_view.visibility = VISIBLE
                        handleApiCall(it) {
                            progress_bar_view.visibility = GONE
                            if (it.airRevalidateResponse.airRevalidateResult.isValid) {
                                val intent = Intent(this, FlightBookingFlowActivity::class.java)
                                with(intent) {
                                    putExtra(
                                        Constants.fareItinerary,
                                        it.airRevalidateResponse.airRevalidateResult.fareItineraries
                                    )
                                    putExtra(
                                        Constants.FlightJourneyParams.FlightClass.param,
                                        selectedClass
                                    )
                                    putExtra(Constants.sourceAnDestCity, flightSrcAndDest)
                                    putExtra(
                                        Constants.FlightJourneyParams.ChildrenCount.param,
                                        childrenCount.toInt()
                                    )
                                    putExtra(
                                        Constants.FlightJourneyParams.AdultsCount.param,
                                        adultsCount.toInt()
                                    )
                                    putExtra(
                                        Constants.FlightJourneyParams.InfantsCount.param,
                                        infantsCount.toInt()
                                    )
                                }
                                startActivity(intent)

                            }else{
                                showToast("Sorry Something went wrong please try again...")
                            }
                        }
                    }
                })
        Log.e("fareSourceCode", airFareItinerary.fareItinerary.airItineraryFareInfo.FareSourceCode)
    }
}