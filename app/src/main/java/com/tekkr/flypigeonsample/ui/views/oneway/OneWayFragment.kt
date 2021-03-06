package com.tekkr.flypigeonsample.ui.views.oneway

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.tekkr.flypigeonsample.*
import com.tekkr.flypigeonsample.ui.BaseFragment
import com.tekkr.flypigeonsample.ui.viewmodels.FlightSearchViewModel
import com.tekkr.flypigeonsample.ui.views.flights.FlightsListActivity
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.utils.checkIfStringsEmpty
import com.tekkr.flypigeonsample.utils.convertMillsToDate
import com.tekkr.flypigeonsample.utils.showToast
import kotlinx.android.synthetic.main.fragment_one_way.*
import kotlinx.android.synthetic.main.fragment_one_way.rl_class
import kotlinx.android.synthetic.main.fragment_one_way.rl_dep_date_picker
import kotlinx.android.synthetic.main.fragment_one_way.rl_from
import kotlinx.android.synthetic.main.fragment_one_way.rl_to
import kotlinx.android.synthetic.main.fragment_one_way.rl_travellers
import kotlinx.android.synthetic.main.fragment_one_way.tv_dep_date
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class OneWayFragment : BaseFragment() {

    var dateInMills = 0L
    var adultTravellers = 1
    var childTravellers = 0
    var infantTravellers = 0
    var sourceType = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_one_way, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        rl_from.setOnClickListener {
            sourceType = Constants.origin
            launchSearchAirportsActivity(sourceType, oneWayAirportSearchActvLauncher)
        }
        rl_to.setOnClickListener {
            sourceType = Constants.destination
            launchSearchAirportsActivity(sourceType, oneWayAirportSearchActvLauncher)
        }

        //set default travellers count from shared prefs
//        lifecycleScope.launchWhenStarted {
//            dataStoreManager.getString("TRAVELLERS_COUNT").collectLatest {
//                tv_travellers_count.text = it ?: "1 Travellers"
//            }
//        }

        //create instance of calendar for bounds
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val currentDate = calendar.timeInMillis

        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        val jan = calendar.timeInMillis

        calendar.set(Calendar.MONTH, Calendar.DECEMBER)
        val dec = calendar.timeInMillis


        val calConstraintsBuilder = CalendarConstraints.Builder()
        calConstraintsBuilder.apply {
            setStart(jan)
            setEnd(dec)
            setValidator(DateValidatorPointForward.now())
        }

        //init and launch date picker
        val depDatePicker =
            MaterialDatePicker.Builder.datePicker().setTitleText("Select a Departure Date")
                .setCalendarConstraints(calConstraintsBuilder.build())
                .setSelection(currentDate)
                .build()

        rl_dep_date_picker.setOnClickListener {
            depDatePicker.show(parentFragmentManager, "DEP_DATE_PICKER")
        }

        depDatePicker.addOnPositiveButtonClickListener {
            tv_dep_date.text = depDatePicker.headerText
            dateInMills = it
        }

        rl_travellers.setOnClickListener {
            showTravellersSelectionBottomSheet() { adults, children, infants ->
                adultTravellers = adults
                childTravellers = children
                infantTravellers = infants
                tv_travellers_count.text = "${adults + children + infants} Travellers"
            }
        }

        rl_class.setOnClickListener {
            sourceType
            showClassPopupMenu(tv_class)
        }

        iv_pointing_arrow.setOnClickListener {
            interChangeSrcAndDest(
                iv_pointing_arrow,
                tv_one_way_src_airport_code,
                tv_one_way_src_city,
                tv_one_way_dest_airport_code,
                tv_one_way_dest_city
            )
        }

        btn_search_flights.setOnClickListener {

            val srcAirportCode = tv_one_way_src_airport_code.text.toString()
            val destAirportCode = tv_one_way_dest_airport_code.text.toString()

            checkIfStringsEmpty(listOf(srcAirportCode, destAirportCode)) {
                if (!it) {
                    launchFlightSearchActivity(
                        flightsSearchResultsActivityLauncher,
                        Constants.FlightJourneyParams.OneWay.param,
                        srcAirportCode,
                        destAirportCode,
                        dateInMills.convertMillsToDate(),
                        "",
                        adultTravellers,
                        childTravellers,
                        infantTravellers,
                        tv_class.text.toString(),
                        tv_one_way_src_city.text.toString(),
                        tv_one_way_dest_city.text.toString(),
                        tv_dep_date.text.toString()
                    )

                } else {
                    requireContext().showToast("All fields are mandatory..")
                }

            }


        }

    }

    private val oneWayAirportSearchActvLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    if (sourceType == Constants.origin) {
                        tv_one_way_src_airport_code.text =
                            it.getStringExtra(Constants.FlightJourneyParams.SrcAirPortCode.param)
                                .toString()
                        tv_one_way_src_city.text =
                            it.getStringExtra(Constants.FlightJourneyParams.SrcCity.param)
                                .toString()
                    } else {
                        tv_one_way_dest_airport_code.text =
                            it.getStringExtra(Constants.FlightJourneyParams.DestAirPortCode.param)
                                .toString()
                        tv_one_way_dest_city.text =
                            it.getStringExtra(Constants.FlightJourneyParams.DestCity.param)
                                .toString()
                    }
                }
            }
        }

    private val flightsSearchResultsActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}


}
