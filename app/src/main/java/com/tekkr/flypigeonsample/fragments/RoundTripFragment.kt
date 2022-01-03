package com.tekkr.flypigeonsample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tekkr.flypigeonsample.Constants
import com.tekkr.flypigeonsample.R
import kotlinx.android.synthetic.main.fragment_one_way.*
import kotlinx.android.synthetic.main.fragment_one_way.rl_from
import kotlinx.android.synthetic.main.fragment_one_way.rl_to
import kotlinx.android.synthetic.main.fragment_one_way.tv_dep_date
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
            launchSearchAirportsActivity(Constants.origin){r1,r2 ->
                tv_from_airport_title_round_trip.text = r1
                tv_from_airport_desc_round_trip.text= r2
            }
        }
        rl_to.setOnClickListener {
            launchSearchAirportsActivity(Constants.origin){r1, r2 ->
                tv_to_airport_title_round_trip.text = r1
                tv_to_airport_desc_round_trip.text= r2
            }
        }
    }

}