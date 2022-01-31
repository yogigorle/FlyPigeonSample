package com.tekkr.flypigeonsample.ui.views.flights

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirFareItinerary
import com.tekkr.flypigeonsample.data.models.FlightDetailsForReview
import com.tekkr.flypigeonsample.data.models.RoundTripAirFareItinerary
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.ui.viewmodels.FlightSearchViewModel
import com.tekkr.flypigeonsample.ui.views.bookingFlow.FlightBookingFlowActivity
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.formatMoney
import com.tekkr.flypigeonsample.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_flights_list.*
import kotlinx.android.synthetic.main.progress_bar_layout.*

@AndroidEntryPoint
class FlightsListActivity : BaseActivity() {

    private var flightSearchType = Constants.FlightJourneyParams.OneWay.param

    private val flightsSearchViewModel: FlightSearchViewModel by viewModels()
    private val oneWayFlightSearchAdapter by lazy { OneWayFlightsListAdapter(::onOneWayFlightItemClicked) }
    private val roundTripDepFlightSearchAdapter by lazy {
        RoundTripFlightsListAdapter(
            Constants.FlightTypes.Departure.type,
            ::onRoundTripFlightsSelected
        )
    }
    private val roundTripArrFlightSearchAdapter by lazy {
        RoundTripFlightsListAdapter(
            Constants.FlightTypes.Arrival.type,
            ::onRoundTripFlightsSelected
        )
    }

    //some important params
    private var flightSrcAndDest = ""
    private var returnFlightSrcAndDest = ""
    private var selectedClass = ""
    private var adultsCount = ""
    private var childrenCount = ""
    private var infantsCount = ""
    private var srcAirportCode = ""
    private var destAirPortCode = ""

    //params to store dep and arr roundtrip flight info
    private var roundTripDepFlightInfo: RoundTripAirFareItinerary? = null
    private var roundTripArrFlightInfo: RoundTripAirFareItinerary? = null

    private var roundTripDepFlightFare = 0
    private var roundTripArrFlightFare = 0


    private var roundTripTotalFareFormatted = ""

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
                1
            ).toString()
            childrenCount = it.getIntExtra(
                Constants.FlightJourneyParams.ChildrenCount.param, 0
            ).toString()
            infantsCount = it.getIntExtra(
                Constants.FlightJourneyParams.InfantsCount.param,
                0
            ).toString()

            srcAirportCode = (it.getStringExtra(
                Constants.FlightJourneyParams.SrcAirPortCode.param
            ) ?: "")

            destAirPortCode = (it.getStringExtra(
                Constants.FlightJourneyParams.DestAirPortCode.param
            ) ?: "")


            val flightSearchParams = hashMapOf(
                Constants.FlightJourneyParams.JourneyType.param to flightSearchType,
                Constants.FlightJourneyParams.SrcAirPortCode.param to srcAirportCode,
                Constants.FlightJourneyParams.DestAirPortCode.param to destAirPortCode,
                Constants.FlightJourneyParams.DepDate.param to (it.getStringExtra(Constants.FlightJourneyParams.DepDate.param)
                    ?: ""),
                Constants.FlightJourneyParams.ReturnDate.param to (it.getStringExtra(Constants.FlightJourneyParams.ReturnDate.param)
                    ?: ""),
                Constants.FlightJourneyParams.AdultsCount.param to adultsCount,
                Constants.FlightJourneyParams.ChildrenCount.param to childrenCount,
                Constants.FlightJourneyParams.InfantsCount.param to infantsCount,
                Constants.FlightJourneyParams.FlightClass.param to selectedClass
            )

            val flightSrc = it.getStringExtra(Constants.FlightJourneyParams.SrcCity.param)
            val flightDest = it.getStringExtra(Constants.FlightJourneyParams.DestCity.param)

            flightSrcAndDest =
                "$flightSrc to $flightDest"
            tv_toolbar_title.text = flightSrcAndDest

            returnFlightSrcAndDest = "$flightDest to $flightSrc"


            tv_departure_date.text =
                it.getStringExtra(Constants.FlightJourneyParams.FormattedDepDate.param) ?: ""

            //get api call
            if (flightSearchType == Constants.FlightJourneyParams.OneWay.param) {
                flightsSearchViewModel.getOneWayFlightSearchResults(flightSearchParams)
                    .observe(this, Observer {
                        progress_bar_view.visibility = VISIBLE
                        handleApiCall(it) { searchResult ->
                            progress_bar_view.visibility = GONE
                            oneWayFlightSearchAdapter.submitList(searchResult.airSearchResponse.airSearchResult.fareItineraries)
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
                            ll_round_trip_bottom_strip.visibility = VISIBLE
                            rv_first_route_flights.adapter = roundTripDepFlightSearchAdapter
                            rv_second_route_flights.adapter = roundTripArrFlightSearchAdapter

                            tv_first_route.text = "$srcAirportCode - $destAirPortCode"
                            tv_second_route.text = "$destAirPortCode - $srcAirportCode"

                        }
                    })
            }

            fl_back.setOnClickListener {
                finish()
            }

        }

        btn_book_round_trip_flights.setOnClickListener {
            roundTripDepFlightInfo?.let { depFareItin ->
                roundTripArrFlightInfo?.let { arrFareItin ->
                    flightsSearchViewModel.revalidateFlight(depFareItin.fareItinerary.airItineraryFareInfo.FareSourceCode)
                        .observe(this,
                            Observer {
                                it?.let {
                                    progress_bar_view.visibility = VISIBLE
                                    handleApiCall(it) { depRevalidatedResult ->
                                        progress_bar_view.visibility = GONE
                                        if (depRevalidatedResult.searchData.airRevalidateResponse.airRevalidateResult.isValid.toBooleanStrict()) {
                                            flightsSearchViewModel.revalidateFlight(arrFareItin.fareItinerary.airItineraryFareInfo.FareSourceCode)
                                                .observe(this,
                                                    Observer { arrRevalidatedResult ->
                                                        arrRevalidatedResult?.let {
                                                            progress_bar_view.visibility = VISIBLE
                                                            handleApiCall(it) { arrRevalidatedResult ->
                                                                progress_bar_view.visibility = GONE
                                                                if (arrRevalidatedResult.searchData.airRevalidateResponse.airRevalidateResult.isValid.toBooleanStrict()) {
                                                                    val intent =
                                                                        Intent(
                                                                            this,
                                                                            FlightBookingFlowActivity::class.java
                                                                        )
                                                                    with(intent) {
                                                                        putExtra(
                                                                            Constants.FlightTypes.OneWay.type,
                                                                            FlightDetailsForReview(
                                                                                flightSrcAndDest,
                                                                                selectedClass,
                                                                                depRevalidatedResult
                                                                            )
                                                                        )
                                                                        putExtra(
                                                                            Constants.FlightTypes.Return.type,
                                                                            FlightDetailsForReview(
                                                                                returnFlightSrcAndDest,
                                                                                selectedClass,
                                                                                arrRevalidatedResult
                                                                            )
                                                                        )
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

                                                                        putExtra(
                                                                            Constants.roundTripTotalFare,
                                                                            roundTripTotalFareFormatted
                                                                        )
                                                                    }
                                                                    startActivity(intent)
                                                                }
                                                            }
                                                        }

                                                    })


                                        } else {
                                            showToast("Sorry Something went wrong please try again...")
                                        }
                                    }

                                }
                            })
                }
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
                            if (it.searchData.airRevalidateResponse.airRevalidateResult.isValid.toBooleanStrict()) {
                                val intent = Intent(this, FlightBookingFlowActivity::class.java)
                                with(intent) {
                                    putExtra(
                                        Constants.FlightTypes.OneWay.type,
                                        FlightDetailsForReview(
                                            flightSrcAndDest,
                                            selectedClass,
                                            it
                                        )
                                    )
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

                            } else {
                                showToast("Sorry Something went wrong please try again...")
                            }
                        }
                    }
                })
    }

    @SuppressLint("SetTextI18n")
    private fun onRoundTripFlightsSelected(
        roundTripFareItinerary: RoundTripAirFareItinerary,
        flightType: String
    ) {


        if (flightType == Constants.FlightTypes.Departure.type) {
            roundTripDepFlightInfo = roundTripFareItinerary
            //set flight details in bottom strip
            roundTripDepFlightInfo?.let {
                roundTripDepFlightFare =
                    it.fareItinerary.airItineraryFareInfo.fareBreakdown.passengerFare.totalFare.Amount
                tv_dep_flight_details.text =
                    "${it.ValidatingAirlineCode} ${it.fareItinerary.airItineraryFareInfo.fareBreakdown.passengerFare.totalFare.formattedTotalFare}"
            }

        } else {
            roundTripArrFlightInfo = roundTripFareItinerary
            roundTripArrFlightInfo?.let {
                roundTripArrFlightFare =
                    it.fareItinerary.airItineraryFareInfo.fareBreakdown.passengerFare.totalFare.Amount
                tv_arr_flight_details.text =
                    "${it.ValidatingAirlineCode} ${it.fareItinerary.airItineraryFareInfo.fareBreakdown.passengerFare.totalFare.formattedTotalFare}"
            }
        }

        roundTripTotalFareFormatted =
            (roundTripDepFlightFare + roundTripArrFlightFare).formatMoney()

        tv_total_price.text =
            "Total $roundTripTotalFareFormatted"

    }
}