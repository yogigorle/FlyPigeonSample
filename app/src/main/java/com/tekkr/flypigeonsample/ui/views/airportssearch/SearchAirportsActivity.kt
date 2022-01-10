package com.tekkr.flypigeonsample.ui.views.airportssearch

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.datastore.dataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.data.models.AirportsData
import com.tekkr.flypigeonsample.ui.BaseActivity
import com.tekkr.flypigeonsample.ui.viewmodels.FlightSearchViewModel
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.DataStoreManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_search_airports.*
import kotlinx.android.synthetic.main.progress_bar_layout.*
import kotlinx.android.synthetic.main.standarad_toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchAirportsActivity : BaseActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private val flightSearchViewModel: FlightSearchViewModel by viewModels()
    private val flightsListAdapter by lazy { AirportsListAdapter(::onAirportItemClicked) }

    var sourceType = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_airports)
        initUi()
    }

    private fun initUi() {

        layout_toolbar.setBackgroundColor(Color.BLACK)
        tv_toolbar_title.typeface = Typeface.SANS_SERIF

        intent?.let {
            sourceType = it.getStringExtra("source_type") ?: ""
            tv_toolbar_title.text = "Select $sourceType City"
        }

        airports_search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                flightSearchViewModel.getAirportsList(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                flightSearchViewModel.getAirportsList(query)
                return false
            }
        })

        flightSearchViewModel.filteredAirportsData.observe(this, Observer {
            progress_bar_view.visibility = VISIBLE
            handleApiCall(it) { airportsList ->
                progress_bar_view.visibility = GONE
                flightsListAdapter.submitList(airportsList)
                rv_airports_list.adapter = flightsListAdapter
            }
        })

        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun onAirportItemClicked(airportData: AirportsData) {
        setResult(Activity.RESULT_OK, Intent().apply {
            if (sourceType == Constants.origin) {
                putExtra(
                    Constants.FlightJourneyParams.SrcAirPortCode.param,
                    airportData.airportcode
                )
                putExtra(Constants.FlightJourneyParams.SrcCity.param, airportData.city)
                lifecycleScope.launch {
                    dataStoreManager.putString(Constants.FlightJourneyParams.SrcAirportName.param,airportData.airportname)
                }

            } else {
                putExtra(
                    Constants.FlightJourneyParams.DestAirPortCode.param,
                    airportData.airportcode
                )
                putExtra(Constants.FlightJourneyParams.DestCity.param, airportData.city)
                lifecycleScope.launch {
                    dataStoreManager.putString(Constants.FlightJourneyParams.DestAirportName.param,airportData.airportname)
                }
            }
        })
        finish()
    }

}