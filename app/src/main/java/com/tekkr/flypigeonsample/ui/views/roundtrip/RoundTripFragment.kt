package com.tekkr.flypigeonsample.ui.views.roundtrip

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.ui.BaseFragment
import com.tekkr.flypigeonsample.utils.convertMillisToReadableDate
import com.tekkr.flypigeonsample.utils.convertMillsToDate
import kotlinx.android.synthetic.main.fragment_one_way.*
import kotlinx.android.synthetic.main.fragment_round_trip.*
import kotlinx.android.synthetic.main.fragment_round_trip.tv_travellers_count
import kotlinx.coroutines.flow.collectLatest
import java.util.*


class RoundTripFragment : BaseFragment() {

    var depDateInMillis = 0L
    var returnDateInMills = 0L
    var adultTravellers = 0
    var childTravellers = 0
    var infantTravellers = 0
    var depAndReturnDate = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_round_trip, container, false)

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rl_from_round_trip.setOnClickListener {
            launchSearchAirportsActivity(Constants.origin, roundTripAirportSearchActvLauncher)
        }
        rl_to_round_trip.setOnClickListener {
            launchSearchAirportsActivity(Constants.origin, roundTripAirportSearchActvLauncher)
        }

        //set default travellers count
        lifecycleScope.launchWhenStarted {
            dataStoreManager.getString("TRAVELLERS_COUNT").collectLatest {
                tv_travellers_count.text = it ?: "1 Travellers"
            }
        }


        iv_pointing_arrow_round_trip.setOnClickListener {
            interChangeSrcAndDest(
                iv_pointing_arrow_round_trip,
                tv_round_trip_src_airport_code,
                tv_round_trip_src_city,
                tv_round_trip_dest_airport_code,
                tv_round_trip_dest_city
            )
        }

        rl_travellers_round_trip.setOnClickListener {
            showTravellersSelectionBottomSheet() { adults, children, infants ->
                adultTravellers = adults
                childTravellers = children
                infantTravellers = infants
                tv_travellers_count.text = "${adults + children + infants} Travellers"
            }
        }

        ll_dep_return_date_picker.setOnClickListener {
            launchRangeDatePicker(parentFragmentManager) { depDate, returnDate, headerText ->
                depDateInMillis = depDate
                returnDateInMills = returnDate
                depAndReturnDate = headerText
                tv_dep_date_round_trip.text = depDateInMillis.convertMillisToReadableDate()
                tv_return_date_round_trip.text = returnDateInMills.convertMillisToReadableDate()
            }

        }

        rl_class_round_trip.setOnClickListener {
            showClassPopupMenu(tv_class_round_trip)
        }

        btn_search_flights_round_trip.setOnClickListener {
            launchFlightSearchActivity(
                roundTripSearchResultsLauncher,
                Constants.roundTrip,
                tv_round_trip_src_airport_code.text.toString(),
                tv_round_trip_dest_airport_code.text.toString(),
                depDateInMillis.convertMillsToDate(),
                returnDateInMills.convertMillsToDate(),
                adultTravellers,
                childTravellers,
                infantTravellers,
                tv_class_round_trip.text.toString(),
                tv_round_trip_src_city.text.toString(),
                tv_round_trip_dest_city.text.toString(),
                depAndReturnDate
            )
        }

    }

    private val roundTripAirportSearchActvLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.let {
                    tv_round_trip_src_airport_code.text =
                        it.getStringExtra("source_city_code_name").toString()
                    tv_round_trip_src_city.text =
                        it.getStringExtra("source_city_full_name").toString()
                    tv_round_trip_dest_airport_code.text =
                        it.getStringExtra("dest_city_code_name").toString()
                    tv_round_trip_dest_city.text =
                        it.getStringExtra("dest_city_full_name").toString()

                }
            }
        }

    private fun launchRangeDatePicker(
        fragmentManager: FragmentManager,
        result: (Long, Long, String) -> Unit
    ) {
        //create instance of calendar for bounds
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

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

        val rangeDatePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Departure and Return Date")
            .setCalendarConstraints(calConstraintsBuilder.build())
            .build()

        with(rangeDatePicker) {
            show(fragmentManager, "RANGE_DATE_PICKER")
            addOnPositiveButtonClickListener {
                it?.let {
                    result(it.first, it.second, rangeDatePicker.headerText)
                }
            }
        }
    }

    private val roundTripSearchResultsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {}

}