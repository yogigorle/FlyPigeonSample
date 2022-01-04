package com.tekkr.flypigeonsample.ui.views.roundtrip

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.tekkr.flypigeonsample.utils.Constants
import com.tekkr.flypigeonsample.R
import com.tekkr.flypigeonsample.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_one_way.rl_from
import kotlinx.android.synthetic.main.fragment_one_way.rl_to
import kotlinx.android.synthetic.main.fragment_round_trip.*


class RoundTripFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_round_trip, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rl_from.setOnClickListener {
            launchSearchAirportsActivity(Constants.origin, roundTripAirportSearchActvLauncher)
        }
        rl_to.setOnClickListener {
            launchSearchAirportsActivity(Constants.origin, roundTripAirportSearchActvLauncher)
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

}