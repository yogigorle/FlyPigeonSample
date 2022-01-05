package com.tekkr.flypigeonsample.ui.views.flights

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.ui.viewmodels.FlightSearchViewModel
import com.tekkr.flypigeonsample.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_flights_list.*
import kotlinx.android.synthetic.main.activity_flights_list.view.*
import kotlinx.android.synthetic.main.progress_bar_layout.view.*

@AndroidEntryPoint
class FlightsListActivity : BaseActivity() {

    private val flightsSearchViewModel: FlightSearchViewModel by viewModels()
    private val flightSearchAdapter by lazy { FlightsListAdapter() }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flights_list)

        intent?.let {
            val flightSearchParams = hashMapOf(
                Constants.FlightSearchQueryParams.journeyType.param to (it.getStringExtra(Constants.FlightSearchQueryParams.journeyType.param)
                    ?: ""),
                Constants.FlightSearchQueryParams.srcAirPortCode.param to (it.getStringExtra(
                    Constants.FlightSearchQueryParams.srcAirPortCode.param
                ) ?: ""),
                Constants.FlightSearchQueryParams.destAirPortCode.param to (it.getStringExtra(
                    Constants.FlightSearchQueryParams.destAirPortCode.param
                ) ?: ""),
                Constants.FlightSearchQueryParams.depDate.param to (it.getStringExtra(Constants.FlightSearchQueryParams.depDate.param)
                    ?: ""),
                Constants.FlightSearchQueryParams.returnDate.param to "",
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
            flightsSearchViewModel.getFlightSearchResults(flightSearchParams)
                .observe(this, Observer {
                    progress_bar_layout.progress_bar_view.visibility = VISIBLE
                    handleApiCall(it) { searchResult ->
                        progress_bar_layout.progress_bar_view.visibility = GONE
                        flightSearchAdapter.submitList(searchResult.AirSearchResponse.AirSearchResult.FareItineraries)
                        rv_one_way_flights.adapter = flightSearchAdapter
                    }
                })

            iv_back.setOnClickListener {
                finish()
            }

        }
    }
}